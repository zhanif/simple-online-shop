package com.zein.online_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

public class BaseServiceImpl {
    protected final List<Sort.Order> getSortOrder(List<String> sorts) throws RuntimeException {
        List<Sort.Order> orders = new ArrayList<>();

        try {
            if (sorts.get(0).contains(",")) {
                for (String sort : sorts) {
                    String attribute = sort.split(",")[0];
                    String direction = sort.split(",")[1];

                    orders.add(new Sort.Order(getSortDirection(direction), attribute));
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return orders;
    }

    protected final PagedModel.PageMetadata getPageMetadata(Page<?> result) {
        return new PagedModel.PageMetadata(
            result.getSize(),
            result.getNumber(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }

    private final Sort.Direction getSortDirection(String direction) {
        return (direction.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
