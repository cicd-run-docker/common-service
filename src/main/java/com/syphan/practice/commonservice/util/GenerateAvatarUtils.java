package com.syphan.practice.commonservice.util;

import org.apache.commons.codec.digest.DigestUtils;

public class GenerateAvatarUtils {
    private static final String DEFAULT_EMAIL_HASH = "00000000000000000000000000000000";
    private static final String GRAVATAR_URL_FORMAT = "https://www.gravatar.com/avatar/%s?d=identicon";

    public static String generate(String email) {
        String emailHash = (email == null || email.equals("")) ? DEFAULT_EMAIL_HASH : DigestUtils.md5Hex(email.toLowerCase().trim());
        return String.format(GRAVATAR_URL_FORMAT, emailHash);
    }
}
