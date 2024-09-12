package com.zein.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Integer id;
    private String name;
    private String address;
    private String code;
    private String phone;
    private Boolean isActive;
    private Date lastOrderDate;
    private String pic;
    private Date createdTime;
    private Date lastModifiedTime;
}
