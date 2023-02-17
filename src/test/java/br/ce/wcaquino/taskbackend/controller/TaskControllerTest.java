package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

public class TaskControllerTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mustNotSaveATaskWithoutDescription() {
        Task todo = new Task();
        todo.setDueDate(LocalDate.now());
        ValidationException validationException = Assert.assertThrows(ValidationException.class, () -> controller.save(todo));
        Assert.assertEquals("Fill the task description", validationException.getMessage());
    }

    @Test
    public void mustNotSaveATaskWithoutDate() {
        Task todo = new Task();
        todo.setTask("Study");
        ValidationException validationException = Assert.assertThrows(ValidationException.class, () -> controller.save(todo));
        Assert.assertEquals("Fill the due date", validationException.getMessage());
    }

    @Test
    public void mustNotSaveATaskWithPastDate() {
        Task todo = new Task();
        todo.setTask("Study");
        todo.setDueDate(LocalDate.of(2010, 01, 01));
        ValidationException validationException = Assert.assertThrows(ValidationException.class, () -> controller.save(todo));
        Assert.assertEquals("Due date must not be in past", validationException.getMessage());
    }

    @Test
    public void mustSaveATaskSuccessfully() throws ValidationException {
        Task todo = new Task();
        todo.setTask("Study");
        todo.setDueDate(LocalDate.now());
        when(taskRepo.save(todo)).thenReturn(todo);
        Assert.assertEquals(new ResponseEntity<>(todo, HttpStatus.CREATED), controller.save(todo));
        Mockito.verify(taskRepo).save(todo);
    }
}
