package com.syphan.practice.commonservice.dao;

import com.syphan.practice.commonservice.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface JpaQueryRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {
}
