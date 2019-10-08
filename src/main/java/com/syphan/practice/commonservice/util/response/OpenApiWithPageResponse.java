package com.syphan.practice.commonservice.util.response;

import com.syphan.practice.commonservice.model.wrapper.PageWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OpenApiWithPageResponse<T extends Serializable> extends OpenApiWithDataResponse {
    private Long totalElements;

    private int totalPages;

    private int number;

    private int size;

    public OpenApiWithPageResponse(PageWrapper<T> page) {
        this.data = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
    }
}
