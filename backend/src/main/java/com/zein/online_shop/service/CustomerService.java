package com.zein.online_shop.service;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> getAll(int page, int size, List<String> sort);
    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(Integer id, CustomerRequest request);

    void delete(Integer id);
}
