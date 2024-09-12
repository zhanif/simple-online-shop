package com.zein.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    private List<?> data;
    private PagedModel.PageMetadata meta;
}
