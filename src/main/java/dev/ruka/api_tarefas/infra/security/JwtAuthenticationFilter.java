package dev.ruka.api_tarefas.infra.security;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.services.UserService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recupera o token, caso exista
        String token = this.getToken(request);
        if(token != null){
            //valida o token
            String userId = tokenService.validateToken(token);
            //recupera o usuário
            UserDetails user = userService.findById(UUID.fromString(userId));

            //se o usuário existir, autentica ele, se não, lança exceção
            if(user == null) throw new BusinessException(BusinessException.class.getName(), "the user does not exists");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
