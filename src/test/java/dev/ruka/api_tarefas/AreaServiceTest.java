package dev.ruka.api_tarefas;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaRequestPayload;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.AreaRepository;
import dev.ruka.api_tarefas.repository.UserRepository;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
public class AreaServiceTest {

    @TestConfiguration
    static class AreaServiceTestConfig{

        @Bean
        public AreaService areaService(){
            return new AreaService();
        }

        @Bean
        public UserService userService(){
            return new UserService();
        }
    }

    //mocka repository do usuário
    @MockBean
    UserRepository userRepository;

    @MockBean
    AreaRepository areaRepository;

    @Autowired
    @InjectMocks
    AreaService areaService;

    @Autowired
    @MockBean
    UserService userService;


    @Test
    public void shouldRegisterAnAreaAndReturnItsDTO(){
        UUID testAreaId = UUID.randomUUID();

        AreaRequestPayload payload = new AreaRequestPayload("Area Teste");
        User user = new User("lucas", "lucas123");
        Area newArea = new Area(payload.title(), user);

        //mock areaRepository
        Mockito.when(areaRepository.save(newArea)).thenReturn(new Area(testAreaId, newArea.getTitle(), user));

        Area saved = areaService.save(newArea);
        AreaResponseDTO responseDTO = new AreaResponseDTO(saved.getId(), saved.getTitle());

        Assertions.assertEquals(new AreaResponseDTO(testAreaId, "Area Teste"),
                responseDTO);

    }

    @Test
    void shouldReturnAllAreasFromUser(){
        User user = new User("Lucas", "lucas123");
        Set<Area> areasTestResponse = Set.of(
                new Area("Programação", user),
                new Area("Pessoal", user)
        );
        //mocka repositório
        Mockito.when(areaRepository.findAllByUser(user)).thenReturn(Optional.of(areasTestResponse));

        Set<Area> areasFound = areaService.findAllByUser(user);

        Assertions.assertEquals(areasTestResponse, areasFound);
    }

    @Test
    void shouldUpdateAnArea(){
        UUID testAreaId = UUID.randomUUID();
        Area areaTest = new Area(testAreaId, "Antes do Update");

        //mocka repository
        Mockito.when(areaRepository.findById(testAreaId)).thenReturn(Optional.of(areaTest));
        Mockito.when(areaRepository.save(areaTest)).thenReturn(areaTest);

        AreaRequestPayload payload = new AreaRequestPayload("Teste");

        Area updatedArea = areaService.update(payload, testAreaId);

        Assertions.assertEquals(areaTest.getTitle(), updatedArea.getTitle());
    }
}
