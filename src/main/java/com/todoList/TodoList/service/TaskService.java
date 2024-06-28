package com.todoList.TodoList.service;

import com.todoList.TodoList.entity.Task;
import com.todoList.TodoList.repository.TaskRepository;
import com.todoList.TodoList.requestDTO.AddTaskDTO;
import com.todoList.TodoList.requestDTO.StatusAndCompletionPercentageRequestDTO;
import com.todoList.TodoList.transformer.AddTaskRequestDTOTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public String addTask(AddTaskDTO addTaskDTO) throws Exception{
        Task task = AddTaskRequestDTOTransformer.addTaskRequestDTOTransformer(addTaskDTO);
        Task newTaskAdded = taskRepository.save(task);
        return newTaskAdded.getId();
    }

    public String deleteTask(String taskId){
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional==null){
            return "Task with id not found";
        }
        Task task = taskOptional.get();
        taskRepository.delete(task);
        return "Task deleted";
    }

    public List<Task> getAllTask(){
        List<Task> taskList = taskRepository.findAll();
        Collections.sort(taskList,(a,b)->{
            return a.getId().compareTo(b.getId());
        });
        return taskList;
    }

    public Task updateStatusAndCompletionPercentage(StatusAndCompletionPercentageRequestDTO statusAndCompletionPercentageRequestDTO) throws Exception{
        Optional<Task> taskOptional = taskRepository.findById(statusAndCompletionPercentageRequestDTO.getTaskId());
        if(taskOptional==null){
            throw new Exception("Task with id not found");
        }
        Task updatedTask = taskOptional.get();
        updatedTask.setCompletedPercentage(statusAndCompletionPercentageRequestDTO.getCompletionPercentage());
        updatedTask.setStatus(statusAndCompletionPercentageRequestDTO.getStatus());
        Task newTaskAdded = taskRepository.save(updatedTask);
        return newTaskAdded;
    }
    public Task updateDate(String taskId, String updatedDate) throws Exception{
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional==null){
            throw new Exception("Task with id not found");
        }
        //Calculating the epoch time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(updatedDate);
        Instant instant = date.toInstant();
        long epochTime = instant.getEpochSecond();

        Task updatedTask = taskOptional.get();
        updatedTask.setCompletionDate(epochTime);
        Task newTaskAdded = taskRepository.save(updatedTask);
        return newTaskAdded;
    }
}
