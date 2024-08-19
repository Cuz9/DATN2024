package com.example.ogani.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNpayPaymentResponse {
    private String orderId;
    private String totalPrice;
    private String paymentTime;
    private String transactionId;
    private String filename;
}
