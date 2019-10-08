package com.syphan.practice.commonservice.model.enumclass;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrType {
    CONSTRAINT,
    NOT_FOUND,
    CONFLICT,
    UNSUPPORTED_OPERATION,
    BAD_REQUEST
}
