package com.zein.online_shop.service;

import com.zein.online_shop.dto.request.CreateOrderRequest;
import com.zein.online_shop.dto.request.UpdateOrderRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    ListResponse getAll(int page, int size, List<String> sort);
    OrderResponse get(Integer id);
    OrderResponse create(CreateOrderRequest request);
    OrderResponse update(Integer id, UpdateOrderRequest request);
    void delete(Integer id);
    byte[] export(int page, int size, List<String> sort);
}
