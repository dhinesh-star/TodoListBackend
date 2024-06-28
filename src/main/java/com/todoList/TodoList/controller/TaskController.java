package com.todoList.TodoList.controller;

import com.todoList.TodoList.entity.Task;
import com.todoList.TodoList.requestDTO.AddTaskDTO;
import com.todoList.TodoList.requestDTO.StatusAndCompletionPercentageRequestDTO;
import com.todoList.TodoList.responseDTO.AllTaskResponseDTO;
import com.todoList.TodoList.responseDTO.TaskResponseTO;
import com.todoList.TodoList.service.TaskService;
import com.todoList.TodoList.transformer.AddTaskResponseDTOTransformer;
import com.todoList.TodoList.transformer.AllTaskResponseDTOTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity addTask(@RequestBody AddTaskDTO addTaskDTO){
        try {
            String newTaskAddedTaskId = taskService.addTask(addTaskDTO);
            TaskResponseTO addTaskresponse = AddTaskResponseDTOTransformer.addTaskResponseDTOTransformer(newTaskAddedTaskId, "New TaskAdded Successfully");
            return new ResponseEntity<>(addTaskresponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("New Task Creation failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/delete")
    public ResponseEntity deleteTask(@RequestParam("taskId") String taskId){
        try{
            String responseMsg = taskService.deleteTask(taskId);
            TaskResponseTO deleteTaskResponse = AddTaskResponseDTOTransformer.addTaskResponseDTOTransformer(taskId, responseMsg);
            return new ResponseEntity<>(deleteTaskResponse, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Task Not Deleted", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllTask")
    public ResponseEntity getAllTask(){
        try {
            List<Task> taskList = taskService.getAllTask();
            AllTaskResponseDTO allTaskResponseDTO = AllTaskResponseDTOTransformer.allTaskResponseDTOTransformer(taskList);
            return new ResponseEntity<>(allTaskResponseDTO,HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occured while taking the tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateStatusAndCompletionPercentage")
    public ResponseEntity updateStatusAndCompletionPercentage(@RequestBody StatusAndCompletionPercentageRequestDTO statusAndCompletionPercentageRequestDTO){
        try{
            Task task = taskService.updateStatusAndCompletionPercentage(statusAndCompletionPercentageRequestDTO);
            return new ResponseEntity<>(task,HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occured while taking the tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateDate")
    public ResponseEntity updateDate(@RequestParam("updatedDate") String updatedDate, @RequestParam("taskId") String taskId){
        try {
            Task task = taskService.updateDate(taskId, updatedDate);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occured while taking the tasks", HttpStatus.BAD_REQUEST);
        }
    }
}
