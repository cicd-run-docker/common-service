package com.syphan.practice.commonservice.model.wrapper;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class PageWrapper<T extends Serializable> extends PageImpl<T> implements Serializable {

    public PageWrapper(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageWrapper(List<T> content) {
        super(content);
    }
}
