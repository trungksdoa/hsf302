package com.product.server.hsf_301.payment.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.product.server.hsf_301.user.model.AppUser;
import com.product.server.hsf_301.blindBox.service.UserService;
import com.product.server.hsf_301.payment.TopUpService;
import com.product.server.hsf_301.payment.model.TopUpHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/pay")
public class PayPalController {

    @Autowired
    private APIContext apiContext;

    @Autowired
    private TopUpService topUpService;

    @Autowired
    private UserService userService;

    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;



    @GetMapping("/create")
    @ResponseBody
    public ResponseEntity<Void> pay(@RequestParam("value") String money) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(money);

        Transaction transaction = new Transaction();
        transaction.setDescription("Test Payment");
        transaction.setAmount(amount);

        List<Transaction> transactions = Collections.singletonList(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment created = payment.create(apiContext);
            String approvalUrl = created.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(Links::getHref)
                    .orElse("No approval_url found");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(approvalUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);

        } catch (PayPalRESTException e) {
            throw new RuntimeException("Payment creation failed", e);
        }
    }


    @GetMapping("/success")
    public String success(@RequestParam String paymentId, @RequestParam String PayerID, RedirectAttributes redirectAttributes) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(PayerID);
        Payment executedPayment = payment.execute(apiContext, execution);

        // Lấy transaction amount
        String amount = "0";
        if (executedPayment.getTransactions() != null && !executedPayment.getTransactions().isEmpty()) {
            amount = executedPayment.getTransactions().get(0).getAmount().getTotal();
        }


        // Convert string to BigDecimal
        BigDecimal paymentAmount = new BigDecimal(amount);


        //get current user from spring security


        //update to balance
        AppUser curr = userService.getCurrentUser();

        BigDecimal currentBalance = curr.getBalance() != null ? curr.getBalance() : new BigDecimal("0");
        curr.setBalance(currentBalance.add(paymentAmount));
        userService.updateProfile(curr); // Đảm bảo save user

        TopUpHistory topUpHistory = new TopUpHistory();
        topUpHistory.setAmount(amount);
        topUpHistory.setUser(curr);
        topUpHistory.setCreated_at(LocalDate.now() + "");
        topUpHistory.setTransaction_id(executedPayment.getId());
        topUpHistory.setStatus(executedPayment.getState());
        topUpService.save(topUpHistory);

        redirectAttributes.addFlashAttribute("message", "Nạp tiền thành công " + paymentAmount);
        return "redirect:/";
    }


    @GetMapping("/cancel")
    public String cancel() {
        return "Payment cancelled";
    }


}
