package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Min(0)
    private Integer stock;
    @NotNull
    @Min(0)
    private Integer price;
    @NotNull
    private Boolean isAvailable;
    private Date lastReStock;
}
