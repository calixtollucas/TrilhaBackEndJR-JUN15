package dev.ruka.api_tarefas.services;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
}
