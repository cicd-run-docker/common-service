package com.syphan.practice.commonservice.exception;

import com.syphan.practice.commonservice.model.enumclass.ErrType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BIZException extends RuntimeException {
    private ErrType errType;
    private String errCode;
    private String errMessage;
    private List<String> errCodes;

    public BIZException(List<String> errCodes) {
        this.errType = ErrType.BAD_REQUEST;
        this.errCodes = errCodes;
    }

    public BIZException(ErrType errType, String errCode, String errMessage) {
        this.errType = errType;
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public static BIZException buildBIZException(ErrType errType, String errCode, String errMessage) {
        return new BIZException(errType, errCode, errMessage);
    }

    public static BIZException buildBindingResultException(List<String> errCodes) {
        return new BIZException(errCodes);
    }
}
