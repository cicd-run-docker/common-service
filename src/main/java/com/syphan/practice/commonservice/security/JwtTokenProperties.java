package com.syphan.practice.commonservice.security;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public abstract class JwtTokenProperties {
    private String secret;

    private Duration avlPeriod;

    private String header;

    private String tokenPrefix;
}
