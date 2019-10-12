package com.syphan.practice.commonservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    @JsonIgnore
    private Integer version;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(name = "create_at", nullable = false)
    private Timestamp createAt = new Timestamp(System.currentTimeMillis());

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(name = "update_at", nullable = false)
    private Timestamp updateAt = new Timestamp(System.currentTimeMillis());
}
