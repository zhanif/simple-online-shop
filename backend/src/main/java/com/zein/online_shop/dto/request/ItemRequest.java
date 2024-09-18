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
    @NotBlank(message = "Name must be filed")
    private String name;
    @NotNull(message = "Stock must be filled")
    @Min(value = 0, message = "Stock must not be negative")
    private Integer stock;
    @NotNull(message = "Price must be filled")
    @Min(value = 0, message = "Price must not be negative")
    private Integer price;
    @NotNull(message = "Availability status must be provided")
    private Boolean isAvailable;
    private Date lastReStock;
}
