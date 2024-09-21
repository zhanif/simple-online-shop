package com.zein.online_shop.service;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.CustomerResponse;
import com.zein.online_shop.dto.response.SearchOptionResponse;

import java.util.List;

public interface CustomerService {
    ListResponse getAll(int page, int size, List<String> sort, String search);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Integer id, CustomerRequest request);
    void delete(Integer id);
    CustomerResponse get(Integer id);

    List<SearchOptionResponse> search(String search);
}
