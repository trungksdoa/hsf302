package com.product.server.hsf_301.payment.model;

import lombok.Data;

import java.util.List;

@Data
public class MomoPaymentCallback {

    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private String storeId;
    private String orderInfo;
    private String partnerUserId;
    private String orderType;
    private Long transId;
    private Integer resultCode;
    private String message;
    private String payType;
    private Long responseTime;
    private String extraData;
    private String signature;
    private String paymentOption;
    private Long userFee;
    private List<PromotionInfo> promotionInfo;

    @Data
    public static class PromotionInfo {
        private String promotionCode;
        private String promotionName;
        private Long amount;
    }
}