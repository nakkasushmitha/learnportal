package com.training.learningportal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component //creates object for the class and stores in application context.
public class JwtToken {

    private String secret = "Bearer";

    public String getUsernameFromToken(String token){
          return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        Claims claims;
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e){
              claims = null;
        }
        return claims;
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }


    /*
    it contains ur details, when it is created, when it is going to expire, security algorithm, roles
     */

    //token creation
    public String generateToken(UserDetails userDetails){

        Map<String,Object> claims = new HashMap<String,Object>();
        return doGenerateToken(claims, userDetails.getUsername());

    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(
                new Date(System.currentTimeMillis())
                ).setExpiration(new Date(System.currentTimeMillis()+3600000))
                        .signWith(SignatureAlgorithm.HS512,"Bearer").compact();
    }

    //token validation
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    //based on token get user details

    //token expiration

}
