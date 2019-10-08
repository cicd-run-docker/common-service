package com.syphan.practice.commonservice.util;

import com.syphan.practice.commonservice.exception.BIZException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class EntityValidationUtils {
    public static void processBindingResults(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
            throw BIZException.buildBindingResultException(errors);
        }
    }
}