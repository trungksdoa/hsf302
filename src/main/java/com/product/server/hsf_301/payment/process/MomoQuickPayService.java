package com.product.server.hsf_301.payment.process;// MomoQuickPayService.java
import com.product.server.hsf_301.payment.model.QuickPayRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class MomoQuickPayService {

    public void createPayment() throws Exception {
        String accessKey = "F8BBA842ECF85";
        String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

        String uuid = UUID.randomUUID().toString();

        QuickPayRequest request = new QuickPayRequest();
        request.setPartnerCode("MOMO");
        request.setPartnerName("MoMo Payment");
        request.setStoreId("Test Store");
        request.setRequestId(uuid);
        request.setOrderId(uuid);
        request.setOrderInfo("Nạp tiền blindBox");
        request.setAmount(5000);
        request.setRedirectUrl(""); // Nếu có thể bỏ trống
        request.setIpnUrl("https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b");
        request.setLang("vi");
        request.setAutoCapture(true);
        request.setExtraData("");
        request.setOrderGroupId("");
        request.setRequestType("captureWallet");
        request.setPaymentCode(null); // Để null giống C# nếu không có

        String rawSignature = "accessKey=" + accessKey +
                "&amount=" + request.getAmount() +
                "&extraData=" + request.getExtraData() +
                "&ipnUrl=" + request.getIpnUrl() +
                "&orderId=" + request.getOrderId() +
                "&orderInfo=" + request.getOrderInfo() +
                "&partnerCode=" + request.getPartnerCode() +
                "&redirectUrl=" + request.getRedirectUrl() +
                "&requestId=" + request.getRequestId() +
                "&requestType=" + request.getRequestType();


        // Tạo chữ ký SHA256
        request.setSignature(hmacSHA256(secretKey, rawSignature));

        // Gửi HTTP POST
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<QuickPayRequest> entity = new HttpEntity<>(request, headers);
        String momoUrl = "https://test-payment.momo.vn/v2/gateway/api/create";

        ResponseEntity<String> response = restTemplate.postForEntity(momoUrl, entity, String.class);

        System.out.println("Response: " + response.getBody());
    }

    public static String hmacSHA256(String key, String data) throws Exception {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKey);
        byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
