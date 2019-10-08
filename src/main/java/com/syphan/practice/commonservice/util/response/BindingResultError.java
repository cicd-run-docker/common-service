package com.syphan.practice.commonservice.util.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindingResultError {
    private String errCode;
    private String errMessage;

    public BindingResultError(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public BindingResultError(String errCode) {
        this.errCode = errCode;
    }
}
