package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequest {
    @NotNull(message = "Date must be filled")
    private Date date;
    @NotNull(message = "Quantity must be filled")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;
}
