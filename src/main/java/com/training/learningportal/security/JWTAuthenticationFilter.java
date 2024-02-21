package com.training.learningportal.security;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);

            try{
                username = this.jwtToken.getUsernameFromToken(token);
            } catch(IllegalArgumentException e){
                e.printStackTrace();
            } catch(ExpiredJwtException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
           logger.error("Invalid header information provided");
        }

        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
            //invalid credentials

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validToken = this.jwtToken.validateToken(token,userDetails);
            if(validToken){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } else {
                 logger.error("validation fails");
            }
        }

        filterChain.doFilter(request,response);
    }
}
