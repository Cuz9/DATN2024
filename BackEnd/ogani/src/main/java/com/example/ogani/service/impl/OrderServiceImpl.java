package com.example.ogani.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.ogani.entity.Product;
import com.example.ogani.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ogani.entity.Order;
import com.example.ogani.entity.OrderDetail;
import com.example.ogani.entity.User;
import com.example.ogani.exception.NotFoundException;
import com.example.ogani.model.request.CreateOrderDetailRequest;
import com.example.ogani.model.request.CreateOrderRequest;
import com.example.ogani.model.request.CreatePaymentLinkRequestBody;
import com.example.ogani.repository.OrderDetailRepository;
import com.example.ogani.repository.OrderRepository;
import com.example.ogani.repository.UserRepository;
import com.example.ogani.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void placeOrder(CreateOrderRequest request) {
        Order order = new Order();
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException("Not Found User With Username:" + request.getUsername()));
        order.setFirstname(request.getFirstname());
        order.setLastname(request.getLastname());
        order.setCountry(request.getCountry());
        order.setAddress(request.getAddress());
        order.setTown(request.getTown());
        order.setState(request.getState());
        order.setPostCode(request.getPostCode());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setNote(request.getNote());   
        orderRepository.save(order);
        long totalPrice = 0;
        for(CreateOrderDetailRequest rq: request.getOrderDetails()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(rq.getName());
            orderDetail.setPrice(rq.getPrice());
            orderDetail.setQuantity(rq.getQuantity());
            orderDetail.setSubTotal(rq.getPrice()* rq.getQuantity());
            orderDetail.setOrder(order);
            totalPrice += orderDetail.getSubTotal();
            orderDetailRepository.save(orderDetail);
            Optional<Product> optional = productRepository.findById(rq.getId());
            if (optional.isPresent()) {
                Product product = optional.get();
                product.setQuantity(product.getQuantity() - rq.getQuantity());
                productRepository.save(product);
            }
        }
        order.setTotalPrice(totalPrice);
        order.setUser(user);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getList() {
        return orderRepository.findAll(Sort.by("id").descending());
    }

    @Override
    public List<Order> getOrderByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Not Found User With Username:" + username));

        List<Order> orders = orderRepository.getOrderByUser(user.getId());
        return orders;  
    }

    @Override
    public void placeOrderPayos(CreatePaymentLinkRequestBody request) {
       
    }

    // @Override
    // public void placeOrderPayos(CreatePaymentLinkRequestBody request) {
    //     try {
    //         final String productName = request.getProductName();
    //         final String description = request.getDescription();
    //         final String returnUrl = request.getReturnUrl();
    //         final String cancelUrl = request.getCancelUrl();
    //         final int price = request.getPrice();

    //         // Generate order code
    //         String currentTimeString = String.valueOf(new Date().getTime());
    //         int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

    //         // Construct payment data
    //         ItemData item = new ItemData("Mì tôm hảo hảo Ly", 1, 1000);
    //         List<ItemData> itemList = new ArrayList<ItemData>();
    //         itemList.add(item);

    //         PaymentData paymentData = new PaymentData(orderCode, price, description, itemList, cancelUrl, returnUrl);

    //         // Create payment link using PayOS
    //         JsonNode data = payOS.createPaymentLink(paymentData);

    //         // You can log success or do any additional actions here
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         // You can log the exception or handle it in any appropriate way
    //     }
    // }

    

}
