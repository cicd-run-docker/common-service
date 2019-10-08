package com.syphan.practice.commonservice.util.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class OpenApiBaseResponse {
    private int httpStatusCode = HttpStatus.OK.value();

    private String code = HttpStatus.OK.getReasonPhrase();

    private String message = HttpStatus.OK.getReasonPhrase();

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Long timestamp = System.currentTimeMillis();
}
