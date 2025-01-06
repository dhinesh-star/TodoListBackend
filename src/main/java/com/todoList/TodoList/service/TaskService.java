package com.todoList.TodoList.service;

import com.todoList.TodoList.entity.Task;
import com.todoList.TodoList.repository.TaskRepository;
import com.todoList.TodoList.requestDTO.AddTaskDTO;
import com.todoList.TodoList.transformer.AddTaskRequestDTOTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public String addTask(AddTaskDTO addTaskDTO, String userName) throws Exception{
        Task task = AddTaskRequestDTOTransformer.addTaskRequestDTOTransformer(addTaskDTO, userName);
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

    public List<Task> getAllTask(String userName){
        List<Task> taskList = taskRepository.getUsersTask(userName);
        Collections.sort(taskList,(a,b)->{
            return (int) (a.getCompletionDate() - b.getCompletionDate());
        });
        return taskList;
    }

    public Task updateBasedOnKey(String taskId, String key, Object value) throws Exception {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional==null){
            throw new Exception("Task with id not found");
        }

        Task toBeUpdated = taskOptional.get();
        Field field = Task.class.getDeclaredField(key);

        if(key.equals("completionDate")){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse((String) value);
            Instant instant = date.toInstant();
            value = instant.getEpochSecond()+(5.5*60*60);
        }
        Class<?> fieldType = field.getType();
        if (fieldType == Integer.class || fieldType == int.class) {
            if (value instanceof String) {
                value = Integer.parseInt((String) value);
            } else if (value instanceof Number) {
                value = ((Number) value).intValue();
            }
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            if (value instanceof String) {
                value = Boolean.parseBoolean((String) value);
            }
        } else if (fieldType == Long.class || fieldType == long.class) {
            if (value instanceof String) {
                value = Long.parseLong((String) value);
            } else if (value instanceof Number) {
                value = ((Number) value).longValue();
            }
        } else if (fieldType == Double.class || fieldType == double.class) {
            if (value instanceof String) {
                value = Double.parseDouble((String) value);
            } else if (value instanceof Number) {
                value = ((Number) value).doubleValue();
            }
        } else if (fieldType.isEnum()) {
            if (value instanceof String) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum> enumType = (Class<? extends Enum>) fieldType;
                value = Enum.valueOf(enumType, (String) value);
            }
        }

        field.setAccessible(true);
        field.set(toBeUpdated,value);
        Task updatedTask = taskRepository.save(toBeUpdated);
        return updatedTask;
    }
    public Task getTaskBasedOnTaskId(String taskId) throws Exception {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional==null){
            throw new Exception("Task Id not found");
        }
        Task task = taskOptional.get();
        return task;
    }
    public List<Task> getTimeExceedTasks(String userName){
        long currentEpochTime = Instant.now().getEpochSecond();
        System.out.println("currentEpochTime"+currentEpochTime);
        List<Task> getExpiredTasks = taskRepository.getTimeExceedTasks(currentEpochTime, userName);
//        AllTaskResponseDTO allTaskResponseDTO = AllTaskResponseDTOTransformer.allTaskResponseDTOTransformer(getExpiredTasks);
//        return allTaskResponseDTO;
        return getExpiredTasks;
    }
}
