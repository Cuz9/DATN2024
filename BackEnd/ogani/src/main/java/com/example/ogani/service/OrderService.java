package com.example.ogani.service;

import java.util.List;

import com.example.ogani.entity.Order;
import com.example.ogani.model.request.CreateOrderRequest;
import com.example.ogani.model.request.CreatePaymentLinkRequestBody;

public interface OrderService {
    
    void placeOrder(CreateOrderRequest request);

    void placeOrderPayos(CreatePaymentLinkRequestBody request);

    List<Order> getList();
    
    List<Order> getOrderByUser(String username);
}
