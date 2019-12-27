package com.example.demo.endpoint;

import com.example.demo.error.ResourceNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskEndpoint {
    private final TaskRepository taskDAO;

    @Autowired
    public TaskEndpoint(TaskRepository taskDAO) {
        this.taskDAO = taskDAO;
    }

    @GetMapping
    public ResponseEntity<?> listall() {
        return new ResponseEntity<>(taskDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/done")
    public ResponseEntity<?> alldone() {
        List<Task> tasks = (List<Task>) taskDAO.findAll();
        List<Task> done = null;
        for (Task i:
             tasks) {
            if(i.isDone()) {
                done.add(i);
            }
        }
        return new ResponseEntity<>(done,HttpStatus.OK);
    }

    @GetMapping(path = "/pending")
    public ResponseEntity<?> allpending() {
        List<Task> tasks = (List<Task>) taskDAO.findAll();
        List<Task> pending = null;
        for (Task i:
                tasks) {
            if(i.isDone()) {
                pending.add(i);
            }
        }
        return new ResponseEntity<>(pending,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Task task) {
        return new ResponseEntity<>(taskDAO.save(task), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Task task) throws ResourceNotFoundException {
        verifyID(task.getId());
        taskDAO.save(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        try{
            verifyID(id);
            List<Task> tasks = (List<Task>) taskDAO.findAll();
            taskDAO.deleteById(id);
        } catch (ResourceNotFoundException e) {
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyID(Long id) throws ResourceNotFoundException {
        if(!taskDAO.existsById(id)) {
            throw new ResourceNotFoundException("Task not found for ID: " + id);
        }
    }
}
