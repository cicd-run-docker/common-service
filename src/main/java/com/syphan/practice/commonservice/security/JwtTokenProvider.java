package com.syphan.practice.commonservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Date;

public abstract class JwtTokenProvider {

    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private JwtTokenProperties jwtTokenProperties;

    Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtTokenProperties.getSecret()).parseClaimsJwt(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex);
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex);
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("Missing JWT required argument", ex);
        }
        return false;
    }

    Integer getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtTokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject() != null ? Integer.parseInt(claims.getSubject()) : null;
    }

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTokenProperties.getAvlPeriod().toMillis());
        return Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtTokenProperties.getSecret())
                .compact();
    }
}
