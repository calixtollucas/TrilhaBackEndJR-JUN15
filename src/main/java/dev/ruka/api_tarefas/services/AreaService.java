package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.exceptions.BusinessException;
import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaRequestPayload;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.AreaRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AreaService {

    @Autowired
    AreaRepository repository;

    public Area save(Area area) {
        Area saved = repository.save(area);
        return saved;
    }

    public Area findAreaById(UUID areaId) {
        Optional<Area> foundArea = repository.findById(areaId);
        return foundArea.orElseThrow(
                () -> new BusinessException(
                BadRequestException.class.getName(),
                "The area does not exists")
        );

    }

    public Set<Area> getAllAreasFromUser(User user) {
        Optional<Set<Area>> userFound = repository.findByUser(user);
        return userFound.orElseThrow(
                () -> new BusinessException(
                        BadRequestException.class.getName(),
                        "The user does not exists"
                )
        );
    }

    public Set<Area> findAllByUser(User user) {
        Optional<Set<Area>> areasFound = repository.findAllByUser(user);
        return areasFound.orElseThrow(
                ()-> new BusinessException(ChangeSetPersister.NotFoundException.class.getName(),
                        "The user doesn't have areas"
                )
        );
    }

    public Area update(AreaRequestPayload payload, UUID areaId) {
        Area areaFound = this.findAreaById(areaId);
        areaFound.setTitle(payload.title());
        repository.save(areaFound);
        return areaFound;
    }

    public void delete(UUID areaId){
        Area area = this.findAreaById(areaId);
        repository.delete(area);
    }
}
