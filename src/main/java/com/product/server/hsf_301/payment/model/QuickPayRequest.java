package com.product.server.hsf_301.payment.model;

import lombok.Data;

@Data
public class QuickPayRequest {
    private String partnerCode;
    private String partnerName;
    private String storeId;
    private String requestId;
    private String orderId;
    private String orderInfo;
    private int amount;
    private String redirectUrl;
    private String ipnUrl;
    private String lang;
    private boolean autoCapture;
    private String extraData;
    private String orderGroupId;
    private String requestType;
    private String paymentCode;
    private String signature;
}