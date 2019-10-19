package com.syphan.practice.commonservice.handler;

import com.syphan.practice.commonservice.exception.BIZException;
import com.syphan.practice.commonservice.util.response.BindingResultError;
import com.syphan.practice.commonservice.util.response.BindingResultErrorResponse;
import com.syphan.practice.commonservice.util.response.OpenApiBaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private Logger logger = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    @ExceptionHandler(BIZException.class)
    public ResponseEntity<Object> handleBusinessException(BIZException ex, WebRequest request) {
        logger.debug(String.format("%s %s", "Occurred BIZException. Message -", ex.getMessage()), ex);
        HttpStatus httpStatus;
        switch (ex.getErrType()) {
            case CONSTRAINT:
            case BAD_REQUEST: {
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            }
            default: {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        OpenApiBaseResponse response = null;
        BindingResultErrorResponse bindingResultErrorResponse = null;
        if (ex.getErrCode() != null) {
            response = new OpenApiBaseResponse();
            response.setHttpStatusCode(httpStatus.value());
            response.setCode(ex.getErrCode());
            try {
                response.setMessage(messageSource.getMessage(ex.getErrCode(), null, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                response.setMessage(ex.getErrMessage() == null ? "" : ex.getErrMessage());
            }
        } else if (!ex.getErrCodes().isEmpty()) {
            bindingResultErrorResponse = new BindingResultErrorResponse();
            bindingResultErrorResponse.setHttpStatusCode(httpStatus.value());
            List<BindingResultError> errors = new ArrayList<>();
            try {
                ex.getErrCodes().forEach(code -> errors.add(new BindingResultError(
                        code,
                        messageSource.getMessage(code, null, LocaleContextHolder.getLocale())
                )));
            } catch (Exception e) {
                ex.getErrCodes().forEach(code -> errors.add(new BindingResultError(
                        code
                )));
            }
            bindingResultErrorResponse.setErrors(errors);
        }

        return handleExceptionInternal(
                ex, response != null ? response : bindingResultErrorResponse, new HttpHeaders(), httpStatus, request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String requestURI = request instanceof ServletWebRequest ? ((ServletWebRequest) request).getRequest().getRequestURI() : "";
        logger.debug(String.format("%s%s%s%s", "Access denied when accessing '", requestURI, "' resources. Message - ", ex.getMessage()), ex);
        String errCode = "Forbidden";
        OpenApiBaseResponse response = new OpenApiBaseResponse();
        response.setHttpStatusCode(HttpStatus.FORBIDDEN.value());
        response.setCode(errCode);
        try {
            response.setMessage(messageSource.getMessage(errCode, null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            response.setMessage(errCode);
        }
        return handleExceptionInternal(
                ex, response, new HttpHeaders(), HttpStatus.valueOf(response.getHttpStatusCode()), request
        );
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.debug(String.format("%s%s%s%s", "An BindException occurred: ", ex.getMessage(), ", httpStatus: ", status));
        return handleExceptionInternal(ex, genValidationResponseBody(status, ex.getBindingResult().getFieldErrors(),
                ex.getBindingResult().getGlobalErrors()), headers, status, request);
    }

    //    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers,
//                                                                  HttpStatus status,
//                                                                  WebRequest request) {
//        logger.debug(String.format("%s %s %s %s", "An MethodArgumentNotValidException occurred:", ex.getMessage(),
//                "httpStatus:", status), ex);
//        return handleExceptionInternal(ex, genValidationResponseBode(status, ex.getBindingResult().getFieldErrors(),
//                ex.getBindingResult().getGlobalErrors()), headers, status, request);
//    }

    private OpenApiBaseResponse genValidationResponseBody(HttpStatus status, List<FieldError> fieldErrors,
                                                          List<ObjectError> globalErrors) {
        String fileName = fieldErrors.isEmpty() ? (globalErrors.isEmpty() ? "" : globalErrors.get(0).getObjectName()) : fieldErrors.get(0).getField();
        String code = fieldErrors.isEmpty() ? (globalErrors.isEmpty() ? "" : globalErrors.get(0).getDefaultMessage()) : fieldErrors.get(0).getDefaultMessage();
        OpenApiBaseResponse response = new OpenApiBaseResponse();
        response.setHttpStatusCode(status.value());
        response.setCode(String.format("%s%s", "Invalid.", fileName));
        try {
            response.setMessage(messageSource.getMessage(code, null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            response.setMessage(code);
        }
        return response;
    }
}
