package dev.ruka.api_tarefas.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTTokenService {

    @Value("api.jwt.key")
    String secretKey;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("api-tarefas-ruka")
                    .withSubject(user.getUsername())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e){
            throw new BusinessException(e.getClass().getName(), e.getMessage());
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String userName = JWT.require(algorithm)
                    .withIssuer("api-tarefas-ruka")
                    .build()
                    .verify(token)
                    .getSubject();

            return userName;
        } catch (JWTVerificationException e){
            throw new BusinessException(e.getClass().getName(), e.getMessage());
        }
    }
}
