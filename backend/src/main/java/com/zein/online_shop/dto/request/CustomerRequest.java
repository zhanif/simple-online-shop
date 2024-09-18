package com.zein.online_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    @NotBlank(message = "Name must be filled")
    private String name;
    @NotBlank(message = "Address must be filled")
    private String address;
    @NotBlank(message = "Phone number must be filled")
    private String phone;
    @NotNull(message = "Active status must be provided")
    private Boolean isActive;
    private MultipartFile pic;
}
