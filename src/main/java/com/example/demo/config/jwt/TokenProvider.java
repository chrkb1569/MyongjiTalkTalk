package com.example.demo.config.jwt;

import com.example.demo.dto.token.RefreshTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component // 토큰의 생성, 유효성 확인, 토큰으로부터 인증 객체를 가져오는 작업 수행
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORIZATION_KEY = "auth";

    private String secret;

    private Long tokenValidationTime;
    private Long tokenRefreshTime;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] secret_key = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(secret_key);
    }

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.validationTime}") Long tokenValidationTime) {
        this.secret = secret;
        this.tokenValidationTime = tokenValidationTime * 1000;
        this.tokenRefreshTime = tokenValidationTime * 1000 * 2;
    }

    public RefreshTokenDto createToken(Authentication authentication) {
        String authorities = authentication.
                getAuthorities().stream().map(s -> s.getAuthority()).
                collect(Collectors.joining(","));

        Date nowDate = new Date();
        long now = nowDate.getTime();

        Date expirationTime = new Date(now + tokenValidationTime);
        Date refreshValidationTime = new Date(now + tokenRefreshTime);

        String originToken = Jwts.builder()
                .setExpiration(expirationTime)
                .setSubject(authentication.getName())
                .claim(AUTHORIZATION_KEY, authorities)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshValidationTime)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        return RefreshTokenDto.builder()
                .originToken(originToken)
                .refreshToken(refreshToken)
                .expirationTime(expirationTime.getTime())
                .grantedType("Bearer")
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(claims.get(AUTHORIZATION_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
