package com.zein.online_shop.dto.response;

import com.zein.online_shop.model.Customer;
import com.zein.online_shop.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer id;
    private Integer customerId;
    private Integer itemId;
    private String code;
    private Integer totalPrice;
    private Integer quantity;
    private Date date;
    private CustomerResponse customer;
    private ItemResponse item;
}
