package dev.ruka.api_tarefas.infra.security;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usernameFound = repository.findByUsername(username);

        if(usernameFound.isPresent()){
            return (UserDetails) usernameFound.get();
        }
        throw new BusinessException(BusinessException.class.getName() ,"User does not exists");
    }
}
