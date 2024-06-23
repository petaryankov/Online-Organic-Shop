package com.organicshop.web.rest;

import com.organicshop.domain.dto.view.OrderViewDto;
import com.organicshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderDetailsController {

    private final OrderService orderService;

    public OrderDetailsController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/order/details/{id}")
    public ResponseEntity<OrderViewDto> getOrderById(@PathVariable("id") Long id) {

        OrderViewDto orderDetail = orderService.getOrderById(id);

        return ResponseEntity.ok(orderDetail);
    }

}
