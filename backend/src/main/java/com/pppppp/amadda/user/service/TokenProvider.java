package com.pppppp.amadda.user.service;

import com.pppppp.amadda.user.dto.request.UserJwtRequest;
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

    public List<String> createTokens(UserJwtRequest request) {
        return List.of(
                generateAccessToken(request.userSeq()),
                generateRefreshToken(request.userSeq()),
                generateRefreshAccessKey(request.userSeq())
        );
    }

    public boolean verifyToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        try {
            // JWT 토큰 파싱
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error(">>> 잘못된 JWT 서명입니다.");
            // TODO 각 exception에 대한 처리 추후 추가 예정

        } catch (ExpiredJwtException e) {
            log.error(">>> 만료된 JWT 토큰입니다.");

        } catch (UnsupportedJwtException e) {
            log.error(">>> 지원되지 않는 JWT 토큰입니다.");

        } catch (IllegalArgumentException e) {
            log.error(">>> JWT 토큰이 잘못되었습니다.");

        }
        return false;
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

    private String generateRefreshAccessKey(Long userSeq) {
        return Base64.encodeBase64String(userSeq.toString().getBytes());
    }

    public Long decodeRefreshAccessKey(String rak) {
        return Long.parseLong(
                new String(Base64.decodeBase64(rak))
        );
    }
}