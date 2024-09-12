package com.zein.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    private String name;
    private String address;
    private String phone;
    private Boolean isActive;
    private String pic;
}
