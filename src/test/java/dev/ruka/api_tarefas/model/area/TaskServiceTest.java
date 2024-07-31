package dev.ruka.api_tarefas.model.area;

import dev.ruka.api_tarefas.model.task.Task;
import dev.ruka.api_tarefas.model.task.TaskRequestPayload;
import dev.ruka.api_tarefas.model.user.Role;
import dev.ruka.api_tarefas.model.user.User;
import dev.ruka.api_tarefas.repository.TaskRepository;
import dev.ruka.api_tarefas.services.AreaService;
import dev.ruka.api_tarefas.services.TaskService;
import dev.ruka.api_tarefas.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
public class TaskServiceTest {

    @TestConfiguration
    public class TaskServiceConfig{

        @Bean
        public TaskService taskService(){
            return new TaskService();
        };
    }

    @MockBean
    AreaService areaService;

    @MockBean
    UserService userService;

    @MockBean
    TaskRepository taskRepository;

    @Autowired
    @InjectMocks
    TaskService taskService;

    static User testUser;
    static Area testArea;

    @BeforeAll
    public static void setup(){
        UUID testUserId = UUID.randomUUID();
        UUID testAreaId = UUID.randomUUID();

        testUser = new User(testUserId, "Lucas", "lucas123", Role.USER);
        testArea = new Area(testAreaId, "Area Teste");
    }

    @Test
    public void shouldCreateATask(){
        UUID testAreaId = UUID.randomUUID();
        UUID testUserId = UUID.randomUUID();
        UUID testTaskId = UUID.randomUUID();

        TaskRequestPayload testePayload = new TaskRequestPayload("Teste", testAreaId, 0, 0);
        Area area = areaService.findAreaById(testePayload.areaId());
        User user = userService.findById(testUserId);

        //Mock Services
        Mockito.when(areaService.findAreaById(testePayload.areaId())).thenReturn(testArea);
        Mockito.when(userService.findById(testUserId)).thenReturn(testUser);

        Task mockedTask = new Task(testTaskId, "Teste", false, false, false, area, user);

        Task newTask = new Task(testePayload.taskTitle(), area, user, testePayload.important(), testePayload.urgent());

        //Mock TaskRepository
        Mockito.when(taskRepository.save(newTask)).thenReturn(mockedTask);

        Task savedTask = taskService.save(newTask);

        Assertions.assertEquals(mockedTask, savedTask);
    }

    @Test
    public void shouldGetAllTasksFromUser(){
        Set<Task> tasks = Set.of(
                new Task("TaskTeste1", testArea, testUser, 0, 0),
                new Task("TaskTeste2", testArea, testUser, 1, 0),
                new Task("TaskTeste3", testArea, testUser, 0, 1),
                new Task("TaskTeste4", testArea, testUser, 1, 1)
        );

        Mockito.when(taskRepository.findAllByUser(testUser)).thenReturn(Optional.of(tasks));

        Set<Task> returnedTasks = taskService.findAllByUser(testUser);

        Assertions.assertEquals(tasks, returnedTasks);
    }

    @Test
    public void shouldGetAllTasksFromArea(){
        Set<Task> tasks = Set.of(
                new Task("TaskTeste1", testArea, testUser, 0, 0),
                new Task("TaskTeste2", testArea, testUser, 1, 0),
                new Task("TaskTeste3", testArea, testUser, 0, 1),
                new Task("TaskTeste4", testArea, testUser, 1, 1)
        );

        Mockito.when(taskRepository.findAllByArea(testArea)).thenReturn(Optional.of(tasks));

        Set<Task> returnedTasks = taskService.findAllByArea(testArea);

        Assertions.assertEquals(tasks, returnedTasks);
    }

    @Test
    public void shouldReturnAnTaskFromId(){
        UUID testTaskId = UUID.randomUUID();
        Task testTask = new Task(testTaskId, "Teste", false, false, false, testArea, testUser);

        Mockito.when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));
        Task returnedTask = taskService.findById(testTaskId);

        Assertions.assertEquals(testTask, returnedTask);
    }

    @Test
    public void shouldCompleteATask(){
        UUID testTaskId = UUID.randomUUID();
        UUID testAreaId = testArea.getId();

        TaskRequestPayload payload = new TaskRequestPayload("Teste Atualizado", testAreaId, 1, 1);
        Task testTask = new Task(testTaskId, "Teste", false, true, true, testArea, testUser);

        Mockito.when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));
        Mockito.when(taskRepository.save(testTask)).thenReturn(testTask);

        Task returnedTask = taskService.complete(testTaskId);

        Assertions.assertTrue(returnedTask.getComplete());
    }

    @Test
    public void shouldUpdateATask(){
        //get
        UUID testTaskId = UUID.randomUUID();
        UUID testAreaId = testArea.getId();
        TaskRequestPayload payload = new TaskRequestPayload("Teste Atualizado", testAreaId, 1, 1);
        Task testTask = new Task(testTaskId, "Teste", false, true, true, testArea, testUser);

        //mock
        Mockito.when(areaService.findAreaById(payload.areaId())).thenReturn(testArea);
        Mockito.when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));

        //when
        Task updatedTask = taskService.update(payload, testTaskId);
        //then
        Assertions.assertEquals(payload.taskTitle(), updatedTask.getTitle());
        Assertions.assertEquals(testArea, updatedTask.getArea());
        Assertions.assertTrue(updatedTask.getImportant());
        Assertions.assertTrue(updatedTask.getUrgent());
    }
}
