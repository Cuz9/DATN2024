package com.example.ogani.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.example.ogani.entity.OrderDetail;
import com.example.ogani.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ogani.entity.Order;

import com.example.ogani.model.request.CreateOrderRequest;
import com.example.ogani.model.request.CreatePaymentLinkRequestBody;
import com.example.ogani.model.response.MessageResponse;
import com.example.ogani.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*",maxAge = 3600)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/")
    @Operation(summary="Lấy ra danh sách đặt hàng")
    public ResponseEntity<List<Order>> getList(){
        List<Order> list = orderService.getList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user")
    @Operation(summary="Lấy ra danh sách đặt hàng của người dùng bằng username")
    public ResponseEntity<List<Order>> getListByUser(@RequestParam("username") String username){
        List<Order> list = orderService.getOrderByUser(username);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all-detail")
    @Operation(summary="Lấy ra danh sách đặt hàng của người dùng bằng username")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetail(){
        List<OrderDetail> list = orderDetailRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    @Operation(summary="Đặt hàng sản phẩm")
    public ResponseEntity<?> placeOrder(@RequestBody CreateOrderRequest request){
        orderService.placeOrder(request);
        return ResponseEntity.ok(new MessageResponse("Order Placed Successfully!"));
    }

    @PostMapping("/create-payos")
    public ResponseEntity<?> createPaymentLink(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        try {
            // Call the service method to perform the logic
            orderService.placeOrderPayos(requestBody); // Giả sử phương thức placeOrderPayos trả về đối tượng Order
            
            // Return a ResponseEntity with HTTP status 200 OK
            return ResponseEntity.ok(new MessageResponse("Banking thành công!"));
        } catch (Exception e) {
            e.printStackTrace();
            // If an exception occurs, return ResponseEntity with HTTP status 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
