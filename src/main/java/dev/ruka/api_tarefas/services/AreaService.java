package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.AreaRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AreaService {

    @Autowired
    AreaRepository repository;

    public Area save(Area area){
        //procura se a area ja existe
        Optional<Area> foundArea = repository.findByTitle(area.getTitle());
        //se nao existir, salva no banco
        if(foundArea.isEmpty()){
            repository.save(area);
            return area;
        }
        //se existir, retorna a mesma
        return foundArea.get();
    }

    public Set<Area> getAllAreasFromUser(UUID userId) {
        Optional<Set<Area>> areas = repository.findByUserId(userId);
        return areas.orElse(null);
    }

    public void addUserToArea(User user, Area area){
        area.getUsers().add(user);
        repository.save(area);
    }

    public void deleteAreaFromUser(UUID areaId, User user){
        //procura se a área existe
        Optional<Area> areaFound = repository.findById(areaId);

        if(areaFound.isEmpty()){
            throw new BusinessException(BadRequestException.class.getName(), "The area does not exists");
        }
        Area rawArea = areaFound.get();
        //se área existir, verifica se possui outros usuários vinculados além do passado
        if(this.hasOtherUsersThan(rawArea, user)){
         //se possuir, apenas desvincula
            rawArea.getUsers().remove(user);
            //atualiza
            repository.save(rawArea);
            return;
        }
        //se não possuir, deleta a área em si
        repository.delete(rawArea);
    }

    private boolean hasOtherUsersThan(Area area, User user) {
        HashSet<User> areaUsers = new HashSet<>(area.getUsers());
        if(areaUsers.contains(user)){
            areaUsers.remove(user);
            if(areaUsers.isEmpty()){
                return false;
            } else {
                return true;
            }
        } else {
            throw new BusinessException(BadRequestException.class.getName(), "the area does not contains the user");
        }
    }
}
