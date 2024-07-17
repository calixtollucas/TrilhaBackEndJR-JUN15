package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.model.user.UserRequestPayload;
import dev.ruka.api_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    public void save(UserRequestPayload payload){
        Optional<User> userFound = repository.findByUsername(payload.username());
        if(userFound.isEmpty()){

            String encryptedPassword = new BCryptPasswordEncoder().encode(payload.password());

            User newUser = new User(payload.username(), encryptedPassword);
            repository.save(newUser);
            return;
        }

        throw new BusinessException(BusinessException.class.getName() ,"the user already exists");
    }

    public User findByUsername(String username){
        Optional<User> userFound = repository.findByUsername(username);
        return userFound.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usernameFound = repository.findByUsername(username);

        if(usernameFound.isPresent()){
            return usernameFound.get();
        }
        throw new BusinessException(BusinessException.class.getName() ,"User does not exists");
    }
}
