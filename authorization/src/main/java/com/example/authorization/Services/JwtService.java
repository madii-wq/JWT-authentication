package com.example.authorization.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSecretKey())
                .compact();
    }
    private SecretKey getSecretKey(){
        String key = "v7f2h95hjh1028v0we112qugrhu456yue90cyiyfgu767";
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }
    private Claims extractAllClaims(String token){
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
    private <T> T parseClaims(String token, Function<Claims, T> claimsTResolver){
        final Claims claims = extractAllClaims(token);
        return claimsTResolver.apply(claims);
    }
    public String getUsername(String token){
        return parseClaims(token, Claims::getSubject);
    }
    public String extractUsername(String token){
        return parseClaims(token, Claims::getSubject);
    }
    private Date getExpirationDate(String token){
        return parseClaims(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }
}
