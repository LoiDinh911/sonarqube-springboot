package com.example.sonarqube_springboot.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

  @Test
  void testTodoCreation() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    String title = "Test Todo";
    String description = "Test Description";

    // Act
    Todo todo = new Todo();
    todo.setTitle(title);
    todo.setDescription(description);
    todo.setCompleted(false);
    todo.setCreatedAt(now);
    todo.setUpdatedAt(now);

    // Assert
    assertNotNull(todo);
    assertEquals(title, todo.getTitle());
    assertEquals(description, todo.getDescription());
    assertFalse(todo.isCompleted());
    assertEquals(now, todo.getCreatedAt());
    assertEquals(now, todo.getUpdatedAt());
  }

  @Test
  void testTodoAllArgsConstructor() {
    // Arrange
    Long id = 1L;
    String title = "Test Todo";
    String description = "Test Description";
    boolean completed = true;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    // Act
    Todo todo = new Todo(id, title, description, completed, createdAt, updatedAt);

    // Assert
    assertEquals(id, todo.getId());
    assertEquals(title, todo.getTitle());
    assertEquals(description, todo.getDescription());
    assertEquals(completed, todo.isCompleted());
    assertEquals(createdAt, todo.getCreatedAt());
    assertEquals(updatedAt, todo.getUpdatedAt());
  }

  @Test
  void testTodoEqualsAndHashCode() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    Todo todo1 = new Todo(1L, "Test Todo", "Description", false, now, now);
    Todo todo2 = new Todo(1L, "Test Todo", "Description", false, now, now);
    Todo todo3 = new Todo(2L, "Different Todo", "Description", false, now, now);

    // Assert
    assertEquals(todo1, todo2);
    assertNotEquals(todo1, todo3);
    assertEquals(todo1.hashCode(), todo2.hashCode());
    assertNotEquals(todo1.hashCode(), todo3.hashCode());
  }

  @Test
  void testTodoToString() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    Todo todo = new Todo(1L, "Test Todo", "Description", false, now, now);

    // Act
    String toString = todo.toString();

    // Assert
    assertTrue(toString.contains("Test Todo"));
    assertTrue(toString.contains("Description"));
    assertTrue(toString.contains("false"));
  }
}