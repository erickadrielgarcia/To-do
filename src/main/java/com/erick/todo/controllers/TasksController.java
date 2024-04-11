package com.erick.todo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erick.todo.domain.task.Priority;
import com.erick.todo.domain.task.RequestTask;
import com.erick.todo.domain.task.Task;
import com.erick.todo.domain.task.TaskRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/tasks")
public class TasksController {
	@Autowired
    private TaskRepository repository;
	
	@Operation(description = "Busca todas as tarefas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna as tarefas")
	})
	@GetMapping
	public ResponseEntity<List<Task>> getAllProducts(@RequestParam(defaultValue = "ASC") String orderBy) {
	    Sort.Direction direction = orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
	    Sort sort = Sort.by(direction, "priority", "createdAt", "completed");
	    List<Task> allTasks = repository.findAll(sort);
	    return ResponseEntity.ok(allTasks);
	}

	@Operation(description = "Busca uma tarefa de acordo com o id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna a tarefa"),
			@ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
	})
	@GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isPresent()) {
            return ResponseEntity.ok(optionalTask.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@Operation(description = "Cria uma nova tarefa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cria a tarefa"),
	})
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
	    task.setCreatedAt(LocalDateTime.now());
	    Task savedTask = repository.save(task);
	    return ResponseEntity.ok(savedTask);
	}

	@Operation(description = "Atualiza a descrição de uma tarefa de acordo com o id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Atualiza a descrição da tarefa"),
			@ApiResponse(responseCode = "403", description = "Não é possível alterar a descrição de uma tarefa completa"),
			@ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
	})
    @PutMapping("/{id}")
    public ResponseEntity updateTaskDescription(@PathVariable Long id, @RequestBody @Valid RequestTask requestTask) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Task task = repository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (task.isCompleted()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Não é possível alterar a descrição de uma tarefa completa.");
        }

        task.setDescription(requestTask.description());
        task.setUpdatedAt(LocalDateTime.now());

        repository.save(task);

        return ResponseEntity.ok().body("Descrição da tarefa atualizada com sucesso.");

    }
    
	
	@Operation(description = "Atualiza a prioridade uma tarefa de acordo com o id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Atualiza a prioridade da tarefa"),
			@ApiResponse(responseCode = "400", description = "Não é possível alterar a descrição de uma tarefa completa"),
			@ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
	})
	@PutMapping("/{id}/{priority}")
	public ResponseEntity updateTaskPriority(@PathVariable Long id, @PathVariable Priority priority) {
	    Optional<Task> optionalTask = repository.findById(id);
	    if (!optionalTask.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }

	    Task task = optionalTask.get();

	    if (task.isCompleted()) {
	        return ResponseEntity.badRequest().body("Não é possível atualizar a prioridade de uma tarefa completa.");
	    }

	    task.setPriority(priority);
	    task.setUpdatedAt(LocalDateTime.now());

	    repository.save(task);

	    return ResponseEntity.ok().body("Prioridade da tarefa atualizada com sucesso.");
	}


	@Operation(description = "Deleta uma tarefa de acordo com o id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleta a tarefa"),
			@ApiResponse(responseCode = "403", description = "A tarefa está completa e não pode ser excluída."),
			@ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
	})
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id){
        Optional<Task> optionalTask = repository.findById(id);
        
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            
            if (!task.isCompleted()) {
                repository.deleteById(id); 
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A tarefa está completa e não pode ser excluída.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
	