package com.product.server.hsf_301.payment.controller;

import com.product.server.hsf_301.payment.TopUpService;
import com.product.server.hsf_301.payment.model.TopUpHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/topup")
public class TopUpController {

    @Autowired
    private TopUpService topUpService;

    @GetMapping
    public String topUpPage(Model model) {
        // Lấy lịch sử top-up (có thể filter theo user sau)
        List<TopUpHistory> topUpHistory = topUpService.getAllTopUp();
        model.addAttribute("topUpHistory", topUpHistory);

        model.addAttribute("content", "view/topup");

        return "view/layout";
    }

    @PostMapping("/create-payment")
    @ResponseBody
    public String createPayment(@RequestParam("amount") String amount) {
        // Redirect đến PayPal API
        return "/pay/create?value=" + amount;
    }

    @GetMapping("/history")
    public String topUpHistory(Model model) {
        List<TopUpHistory> history = topUpService.getAllTopUp();
        model.addAttribute("history", history);
        return "view/topup-history";
    }
}
