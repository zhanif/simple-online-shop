package com.zein.online_shop.service;

import com.zein.online_shop.dto.request.ItemRequest;
import com.zein.online_shop.dto.response.ItemResponse;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.SearchOptionResponse;

import java.util.List;

public interface ItemService {
    ListResponse getAll(int page, int size, List<String> sort, String search);
    ItemResponse create(ItemRequest request);
    ItemResponse update(Integer id, ItemRequest request);
    void delete(Integer id);
    ItemResponse get(Integer id);

    List<SearchOptionResponse> search(String search);
}
