package com.product.server.hsf_301.payment.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.product.server.hsf_301.payment.process.MomoQuickPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class PaymentController {

    @Autowired
    private MomoQuickPayService momoQuickPayService;

    @GetMapping("/create")
    public String createMomoPayment() {
        try {
            momoQuickPayService.createPayment();
            return "Request sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
