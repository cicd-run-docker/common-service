package com.syphan.practice.commonservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BindingResultErrorResponse {
    private int httpStatusCode = HttpStatus.OK.value();
    private List<BindingResultError> errors;
    private Long timestamp = System.currentTimeMillis();
}
