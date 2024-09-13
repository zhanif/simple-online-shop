package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @NotBlank
    private String name;
    @NotBlank
    private Integer stock;
    @NotBlank
    private Integer price;
    @NotBlank
    private Boolean isAvailable;
    @NotBlank
    private Date lastReStock;
}
