package com.project.fitness.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    //@Autowired
    //UserDetailsService userDetailsService;s

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter Called...");
        try{
            String jwt=parseJwt(request);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String userId=jwtUtils.getUserIdFromToken(jwt);

                Claims claims=jwtUtils.getAllClaims(jwt);
                List<String> roles=claims.get("roles",List.class);
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role->(GrantedAuthority) new SimpleGrantedAuthority(role))
                        .toList();

                UsernamePasswordAuthenticationToken authentication=
                        new UsernamePasswordAuthenticationToken(userId,
                                null,
                                authorities);

                //saving the request information also in authentication object...
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //saving the authentication details into SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request,response);
    }

    //extract jwt token from header and return it.
    public String parseJwt(HttpServletRequest request){
        String jwt=jwtUtils.getJwtFromHeader(request);
        return jwt;
    }
}
