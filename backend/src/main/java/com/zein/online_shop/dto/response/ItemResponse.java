package com.zein.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Integer id;
    private String name;
    private String code;
    private Integer stock;
    private Integer price;
    private Boolean isAvailable;
    private Date lastReStock;
    private Date createdTime;
    private Date lastModifiedTime;
}
