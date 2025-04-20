package com.example.sonarqube_springboot.controller;

import com.example.sonarqube_springboot.entity.Todo;
import com.example.sonarqube_springboot.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {

  @Mock
  private TodoService todoService;

  @InjectMocks
  private TodoController todoController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllTodos_ShouldReturnListOfTodos() {
    // Arrange
    List<Todo> expectedTodos = Arrays.asList(
        new Todo(1L, "Task 1", "Description 1", false, LocalDateTime.now(), LocalDateTime.now()),
        new Todo(2L, "Task 2", "Description 2", true, LocalDateTime.now(), LocalDateTime.now()));
    when(todoService.getAllTodos()).thenReturn(expectedTodos);

    // Act
    List<Todo> actualTodos = todoController.getAllTodos();

    // Assert
    assertEquals(expectedTodos.size(), actualTodos.size());
    assertEquals(expectedTodos.get(0).getTitle(), actualTodos.get(0).getTitle());
    verify(todoService, times(1)).getAllTodos();
  }

  @Test
  void getTodoById_WhenTodoExists_ShouldReturnTodo() {
    // Arrange
    Long id = 1L;
    Todo expectedTodo = new Todo(id, "Task 1", "Description 1", false, LocalDateTime.now(), LocalDateTime.now());
    when(todoService.getTodoById(id)).thenReturn(Optional.of(expectedTodo));

    // Act
    ResponseEntity<Todo> response = todoController.getTodoById(id);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedTodo.getTitle(), response.getBody().getTitle());
    verify(todoService, times(1)).getTodoById(id);
  }

  @Test
  void getTodoById_WhenTodoDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    Long id = 1L;
    when(todoService.getTodoById(id)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Todo> response = todoController.getTodoById(id);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(todoService, times(1)).getTodoById(id);
  }

  @Test
  void createTodo_ShouldReturnCreatedTodo() {
    // Arrange
    Todo todoToCreate = new Todo(null, "New Task", "New Description", false, null, null);
    Todo savedTodo = new Todo(1L, "New Task", "New Description", false, LocalDateTime.now(), LocalDateTime.now());
    when(todoService.createTodo(todoToCreate)).thenReturn(savedTodo);

    // Act
    Todo result = todoController.createTodo(todoToCreate);

    // Assert
    assertNotNull(result.getId());
    assertEquals(savedTodo.getTitle(), result.getTitle());
    verify(todoService, times(1)).createTodo(todoToCreate);
  }

  @Test
  void updateTodo_WhenTodoExists_ShouldReturnUpdatedTodo() {
    // Arrange
    Long id = 1L;
    Todo updatedTodo = new Todo(id, "Updated Task", "Updated Description", true, null, null);
    Todo savedTodo = new Todo(id, "Updated Task", "Updated Description", true, LocalDateTime.now(),
        LocalDateTime.now());
    when(todoService.updateTodo(id, updatedTodo)).thenReturn(savedTodo);

    // Act
    ResponseEntity<Todo> response = todoController.updateTodo(id, updatedTodo);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(savedTodo.getTitle(), response.getBody().getTitle());
    verify(todoService, times(1)).updateTodo(id, updatedTodo);
  }

  @Test
  void updateTodo_WhenTodoDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    Long id = 1L;
    Todo updatedTodo = new Todo(id, "Updated Task", "Updated Description", true, null, null);
    when(todoService.updateTodo(id, updatedTodo)).thenThrow(new RuntimeException("Todo not found"));

    // Act
    ResponseEntity<Todo> response = todoController.updateTodo(id, updatedTodo);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(todoService, times(1)).updateTodo(id, updatedTodo);
  }

  @Test
  void deleteTodo_ShouldReturnOk() {
    // Arrange
    Long id = 1L;

    // Act
    ResponseEntity<Void> response = todoController.deleteTodo(id);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(todoService, times(1)).deleteTodo(id);
  }
}