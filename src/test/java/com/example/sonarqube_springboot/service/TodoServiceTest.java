package com.example.sonarqube_springboot.service;

import com.example.sonarqube_springboot.entity.Todo;
import com.example.sonarqube_springboot.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

  @Mock
  private TodoRepository todoRepository;

  @InjectMocks
  private TodoService todoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllTodos() {
    // Arrange
    List<Todo> expectedTodos = Arrays.asList(
        new Todo(1L, "Task 1", "Description 1", false, null, null),
        new Todo(2L, "Task 2", "Description 2", true, null, null));
    when(todoRepository.findAll()).thenReturn(expectedTodos);

    // Act
    List<Todo> actualTodos = todoService.getAllTodos();

    // Assert
    assertEquals(expectedTodos.size(), actualTodos.size());
    assertEquals(expectedTodos.get(0).getTitle(), actualTodos.get(0).getTitle());
    verify(todoRepository, times(1)).findAll();
  }

  @Test
  void getTodoById() {
    // Arrange
    Long id = 1L;
    Todo expectedTodo = new Todo(id, "Task 1", "Description 1", false, null, null);
    when(todoRepository.findById(id)).thenReturn(Optional.of(expectedTodo));

    // Act
    Optional<Todo> actualTodo = todoService.getTodoById(id);

    // Assert
    assertTrue(actualTodo.isPresent());
    assertEquals(expectedTodo.getTitle(), actualTodo.get().getTitle());
    verify(todoRepository, times(1)).findById(id);
  }

  @Test
  void createTodo() {
    // Arrange
    Todo todoToCreate = new Todo(null, "New Task", "New Description", false, null, null);
    Todo savedTodo = new Todo(1L, "New Task", "New Description", false, null, null);
    when(todoRepository.save(todoToCreate)).thenReturn(savedTodo);

    // Act
    Todo result = todoService.createTodo(todoToCreate);

    // Assert
    assertNotNull(result.getId());
    assertEquals(savedTodo.getTitle(), result.getTitle());
    verify(todoRepository, times(1)).save(todoToCreate);
  }

  @Test
  void updateTodo() {
    // Arrange
    Long id = 1L;
    Todo existingTodo = new Todo(id, "Old Task", "Old Description", false, null, null);
    Todo updatedTodo = new Todo(id, "Updated Task", "Updated Description", true, null, null);
    when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
    when(todoRepository.save(existingTodo)).thenReturn(updatedTodo);

    // Act
    Todo result = todoService.updateTodo(id, updatedTodo);

    // Assert
    assertEquals(updatedTodo.getTitle(), result.getTitle());
    assertEquals(updatedTodo.getDescription(), result.getDescription());
    assertEquals(updatedTodo.isCompleted(), result.isCompleted());
    verify(todoRepository, times(1)).findById(id);
    verify(todoRepository, times(1)).save(existingTodo);
  }

  @Test
  void deleteTodo() {
    // Arrange
    Long id = 1L;

    // Act
    todoService.deleteTodo(id);

    // Assert
    verify(todoRepository, times(1)).deleteById(id);
  }
}