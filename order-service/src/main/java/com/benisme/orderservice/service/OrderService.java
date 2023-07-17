package com.benisme.orderservice.service;

import com.benisme.orderservice.dto.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
