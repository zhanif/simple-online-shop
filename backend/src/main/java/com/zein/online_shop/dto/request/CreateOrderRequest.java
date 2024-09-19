package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Customer must be specified")
    private Integer customerId;
    @NotNull(message = "Item must be specified")
    private Integer itemId;
    @NotNull(message = "Quantity must be filled")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;
    @NotNull(message = "Date must be filled")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date date;
}
