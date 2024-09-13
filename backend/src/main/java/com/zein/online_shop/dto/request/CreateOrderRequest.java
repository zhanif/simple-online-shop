package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Integer customerId;
    @NotBlank
    private Integer itemId;
    @NotBlank
    @Min(1)
    private Integer quantity;
    @NotNull
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date date;
}
