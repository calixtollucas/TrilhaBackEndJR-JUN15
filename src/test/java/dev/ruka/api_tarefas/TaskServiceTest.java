package dev.ruka.api_tarefas;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.model.task.TaskRequestPayload;
import dev.ruka.api_tarefas.model.task.TaskResponseDTO;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.AreaRepository;
import dev.ruka.api_tarefas.repository.TaskRepository;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.TaskService;
import dev.ruka.api_tarefas.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootTest
public class TaskServiceTest {

    @TestConfiguration
    static class TaskServiceConfig{

        @Bean
        public TaskService taskService(){
            return new TaskService();
        }

        @Bean
        public AreaService areaService(){
            return new AreaService();
        }

        @Bean
        public UserService userService(){
            return new UserService();
        }
    }
    @MockBean
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @MockBean
    @Autowired
    AreaService areaService;

    @MockBean
    @Autowired
    UserService userService;

    @Test
    public void shouldCreateAnTask(){
        UUID testAreaId = UUID.fromString("df7f5421-deae-4297-a89e-d165d1ad958d");
        UUID testUserId = UUID.fromString("cc2d2b2c-cb06-48e1-b8ee-3a16f7cfcd20");

        User testUser = new User("Lucas", "lucas123");
        testUser.setId(testUserId);

        //mockagem de serviços
        Mockito.when(areaService.findAreaById(testAreaId)).thenReturn(new Area(testAreaId, "Area Teste"));
        Mockito.when(userService.findById(testUserId)).thenReturn(testUser);

        TaskRequestPayload testPayload = new TaskRequestPayload("TestTask", testAreaId);
        Area area = areaService.findAreaById(testAreaId);
        User user = userService.findById(testUserId);
        Task taskToBeSaved = new Task(testPayload.taskTitle(), area, user);

        //Mocka repositório
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(taskToBeSaved);

        Task saved = taskService.save(taskToBeSaved);
        Task testReturn = new Task("TestTask", area, user);

        Assertions.assertEquals(testReturn.getTitle(), saved.getTitle());
        Assertions.assertEquals(testReturn.getArea(), saved.getArea());
        Assertions.assertEquals(testReturn.getUser(), saved.getUser());
    }
}
