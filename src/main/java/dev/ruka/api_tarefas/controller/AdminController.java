package dev.ruka.api_tarefas.controller;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.services.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AreaService areaService;

    @GetMapping("/areas")
    public ResponseEntity<Set<AreaResponseDTO>> getAllAreas(){
        Set<Area> areas = areaService.findAll();
        Set<AreaResponseDTO> responseDTOSet = areas.stream()
                .map(
                        (area) -> new AreaResponseDTO(area.getId(), area.getTitle())
                )
                .collect(Collectors.toSet());

        return ResponseEntity.ok(responseDTOSet);
    }
}
