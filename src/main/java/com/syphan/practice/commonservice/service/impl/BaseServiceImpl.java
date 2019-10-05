package com.syphan.practice.commonservice.service.impl;

import com.syphan.practice.commonservice.dao.JpaQueryRepository;
import com.syphan.practice.commonservice.model.BaseEntity;
import com.syphan.practice.commonservice.model.wrapper.PageWrapper;
import com.syphan.practice.commonservice.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable> implements BaseService<T, ID> {
    private JpaQueryRepository<T, ID> repository;

    public BaseServiceImpl(JpaQueryRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T getById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<T> getAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public PageWrapper<T> list(Pageable pageable) {
        Page<T> page = repository.findAll(pageable);
        return new PageWrapper<>(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public List<T> listAll(Sort sort) {
        return null;
    }
}
