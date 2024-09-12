package com.zein.online_shop.service;

import com.zein.online_shop.dto.request.CreateCustomerRequest;
import com.zein.online_shop.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> getAll(int page, int size, List<String> sort);
    CustomerResponse create(CreateCustomerRequest request);
}
