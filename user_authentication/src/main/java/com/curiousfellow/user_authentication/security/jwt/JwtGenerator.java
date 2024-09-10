package com.curiousfellow.user_authentication.security.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.curiousfellow.user_authentication.domain.AppUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtGenerator {

    // create the secretKey
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstants.JWT_SECRET.getBytes());

    // token generation logic
    public String generateToken(Authentication authObj) {
        // get the authorities from the authObj
        Collection<? extends GrantedAuthority> authList = authObj.getAuthorities();
        // convert List of authorities to comma-delimited string value
        String role = authList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        // since our principal is a UserDetails instance
        AppUser principal = (AppUser) authObj.getPrincipal();

        // Build jwt
        Long nowMillis = System.currentTimeMillis();
        Long expiryMillis = nowMillis + (60 * 60 * 60 * 24 * 1000);
        Date currentTime = new Date(nowMillis);
        Date expiryTime = new Date(expiryMillis);

        String jwt = Jwts.builder().expiration(expiryTime).issuedAt(currentTime).claim("email", principal.getEmail())
                .claim("authorities", role).signWith(key, SIG.HS256).compact();

        return jwt;
    }
}
