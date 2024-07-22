package dev.ruka.api_tarefas;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.area.AreaRequestPayload;
import dev.ruka.api_tarefas.model.area.AreaResponseDTO;
import dev.ruka.api_tarefas.model.user.Role;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.model.user.UserResponseDTO;
import dev.ruka.api_tarefas.repository.AreaRepository;
import dev.ruka.api_tarefas.repository.UserRepository;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.List;
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

    @Autowired
    @MockBean
    AreaService areaService;

    @Autowired
    @MockBean
    UserService userService;


    @Test
    public void shouldRegisterAnAreaAndReturnItsDTO(){
        AreaRequestPayload payload = new AreaRequestPayload("Teste");
        Area newArea = new Area(payload.title());

        //AreaService mock config
        Area thenReturnArea = new Area(UUID.fromString("7efe8252-98d2-4cba-8685-4291e8215e07"), "Teste");
        Mockito.when(areaService.save(newArea))
                        .thenReturn(thenReturnArea);

        //salva uma área no banco de dados
        Area areaFound = areaService.save(newArea);
/*
        //UserRepository mock config
        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        //adiciona ela vinculada a um usuário
        User updatedUser = userService.addAreaToUser(UUID.randomUUID(), areaFound);


        Assertions.assertTrue(updatedUser.getAreas().contains(areaFound));*/
        //retorna o responseDTO
        AreaResponseDTO responseDTO = new AreaResponseDTO(areaFound.getId(), areaFound.getTitle());
        Assertions.assertEquals(new AreaResponseDTO(UUID.fromString("7efe8252-98d2-4cba-8685-4291e8215e07"), "Teste"), responseDTO);
    }

    @Test
    public void shouldGetAtllAreasOfAnUser(){
        Set<Area> areasReturn = Set.of(
                new Area("Teste")
        );

        UUID userId = UUID.randomUUID();

        Mockito.when(areaService.getAllAreasFromUser(userId)).thenReturn(areasReturn);

        //procura todas as áreas de um determinado userId
        Set<Area> areaReturned = areaService.getAllAreasFromUser(userId);

        Assertions.assertEquals(areasReturn, areaReturned);
    }
}
