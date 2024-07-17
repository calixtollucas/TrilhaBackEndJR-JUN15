package dev.ruka.api_tarefas.infra.security;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.services.AuthUserService;
import dev.ruka.api_tarefas.services.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    AuthUserService authUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recupera o token, caso exista
        String token = this.getToken(request);
        if(token != null){
            //valida o token
            String username = tokenService.validateToken(token);

            //recupera o usuário
            User user = authUserService.findByUsername(username);

            //se o usuário existir, autentica ele, se não, lança exceção
            if(user == null) throw new BusinessException(BusinessException.class.getName(), "the user does not exists");

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    "",
                    user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authentication");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
