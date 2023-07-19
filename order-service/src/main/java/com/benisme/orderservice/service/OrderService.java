package com.benisme.orderservice.service;

import com.benisme.orderservice.dto.OrderRequest;

public interface OrderService {
    String placeOrder(OrderRequest orderRequest);
}
