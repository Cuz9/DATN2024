package com.example.ogani.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.view.RedirectView;

import com.example.ogani.config.VNPayService;
import com.example.ogani.model.response.VNpayPaymentResponse;

@RestController
@RequestMapping("/api/vnpay")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VnPayController {
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("")
    public String home() {
        return "index";
    }

    @PostMapping("/submit-order-vnpay")
    public ResponseEntity<String> submitOrder(@RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo) {
        String baseUrl = "http://localhost:8080";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return ResponseEntity.ok(vnpayUrl);
    }
}
