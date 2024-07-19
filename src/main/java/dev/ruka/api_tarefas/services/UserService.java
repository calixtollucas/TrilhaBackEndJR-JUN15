package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.model.user.UserRequestPayload;
import dev.ruka.api_tarefas.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

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

    public User findById(UUID userId){
        Optional<User> userFound = repository.findById(userId);
        return userFound.orElse(null);
    }


    public User addAreaToUser(UUID userId, Area area) {
        //recupera o usuário
        Optional<User> userFound = repository.findById(userId);
        //se o usuário não existir, exceção
        if (userFound.isEmpty()) throw new BusinessException(BadRequestException.class.getName(), "the user does not exists");
        //se existir, adiciona a área a sua lista de áreas vinculadas
        userFound.get().getAreas().add(area);
        //atualiza o usuário
        repository.save(userFound.get());
        return userFound.get();
    }
}
