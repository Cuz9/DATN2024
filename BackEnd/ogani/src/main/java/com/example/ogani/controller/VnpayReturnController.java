package com.example.ogani.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.ogani.config.VNPayConfig;
import com.example.ogani.config.VNPayService;
import com.example.ogani.model.response.VNpayPaymentResponse;

@Controller
@RequestMapping("/api/vnpay-return")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VnpayReturnController {
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/vnpay-payment")
    public RedirectView vnpayPayment(@RequestParam("vnp_Amount") int orderTotal,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_SecureHash") String vnp_SecureHash,
            @RequestParam("vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam("vnp_PayDate") String vnp_PayDate,
            @RequestParam("vnp_TransactionNo") String vnp_TransactionNo, Model model) {
        int paymentStatus = vnPayService.orderReturn(orderTotal, orderInfo, vnp_SecureHash, vnp_TransactionStatus);

        String orderInfoField = orderInfo;
        String paymentTime = vnp_PayDate;
        String transactionId = vnp_TransactionNo;
        String totalPrice = String.valueOf(orderTotal);

        // // Create response object
        // VNpayPaymentResponse response = new VNpayPaymentResponse();
        // response.setOrderId(orderInfoField);
        // response.setTotalPrice(totalPrice);
        // response.setPaymentTime(paymentTime);
        // response.setTransactionId(transactionId);
        // response.setFilename(paymentStatus == 1 ? "banking-success.component.html" : "banking-fail.component.html");

        // Determine the redirect URL
        String redirectUrl = paymentStatus == 1 ? "http://localhost:4200/banking-success?orderId=" + orderInfoField + "&totalPrice=" + totalPrice + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId
                : "http://localhost:4200/banking-fail?orderId=" + orderInfoField + "&totalPrice=" + totalPrice + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId;

        // Perform the redirection
        return new RedirectView(redirectUrl, true);
    }
}