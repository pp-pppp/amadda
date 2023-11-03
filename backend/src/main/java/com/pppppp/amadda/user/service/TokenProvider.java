package com.pppppp.amadda.user.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TokenProvider {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.jwt.access-length}")
    private Long accessLength;
    @Value("${spring.jwt.refresh-length}")
    private Long refreshLength;

    public List<String> createTokens(Long userSeq) {
        return List.of(
                generateAccessToken(userSeq),
                generateRefreshToken(userSeq),
                generateRefreshAccessKey(userSeq)
        );
    }

    public boolean verifyToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    private String generateAccessToken(Long userSeq) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userSeq", userSeq)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessLength))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Long userSeq) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userSeq", userSeq)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshLength))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshAccessKey(Long userSeq) {
        return Base64.encodeBase64String(userSeq.toString().getBytes());
    }

    public Long decodeRefreshAccessKey(String rak) {
        return Long.parseLong(
                new String(Base64.decodeBase64(rak))
        );
    }

    public Long parseUserSeq(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // key는 토큰을 생성할 때 사용한 키와 동일해야 함
                .build()
                .parseClaimsJws(token)
                .getBody();

        // userSeq 값을 추출
        Long userSeq = claims.get("userSeq", Long.class);
        return userSeq;
    }
}
