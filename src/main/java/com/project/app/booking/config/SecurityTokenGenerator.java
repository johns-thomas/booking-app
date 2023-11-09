package com.project.app.booking.config;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.security.Key;

import java.util.Date;

@Component
public class SecurityTokenGenerator {

    private static final String SECRET ="CPPPROJECT45TSGFBA586SFSFSGRSGSZFD413DFDGH2B4B464647DFHHDB5970";
    public String generateToken(Authentication authentication){

        Date expDate= new Date(new Date().getTime()+7000L);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith( SignatureAlgorithm.HS256,getSignInKey())
                .compact();

    }

    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)) return false;
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Invalid token");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
