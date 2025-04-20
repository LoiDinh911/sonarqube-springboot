package com.example.sonarqube_springboot.service;

import com.example.sonarqube_springboot.entity.Todo;
import com.example.sonarqube_springboot.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

  private final TodoRepository todoRepository;

  @Autowired
  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public List<Todo> getAllTodos() {
    return todoRepository.findAll();
  }

  public Optional<Todo> getTodoById(Long id) {
    return todoRepository.findById(id);
  }

  public Todo createTodo(Todo todo) {
    return todoRepository.save(todo);
  }

  public Todo updateTodo(Long id, Todo todoDetails) {
    return todoRepository.findById(id)
        .map(existingTodo -> {
          existingTodo.setTitle(todoDetails.getTitle());
          existingTodo.setDescription(todoDetails.getDescription());
          existingTodo.setCompleted(todoDetails.isCompleted());
          existingTodo.setUpdatedAt(todoDetails.getUpdatedAt());
          return todoRepository.save(existingTodo);
        })
        .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
  }

  public void deleteTodo(Long id) {
    todoRepository.deleteById(id);
  }
}