package com.employess.app.Controllers;

import com.employess.app.Entities.Department;
import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Task;
import com.employess.app.Entities.Training;
import com.employess.app.Repositories.DepartmentRepository;
import com.employess.app.Repositories.EmployeeRepository;
import com.employess.app.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // get all tasks
    @GetMapping("")
    List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    // get task by id
    @GetMapping("/{task_id}")
    Optional<Task> get(@PathVariable int task_id){
        return taskRepository.findById(task_id);
    }

    // delete employee by id
    @DeleteMapping("/{task_id}")
    public ResponseEntity<String> deleteTask( @PathVariable int task_id) {
        Optional<Task> taskOptional = taskRepository.findById(task_id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            // Disassociate task from employees
            List<Employee> employees = task.getEmployees();
            for (Employee employee : employees) {
                employee.getTasks().remove(task);
                employeeRepository.save(employee);
            }

            // Disassociate task from departments
            List<Department> departments = task.getDepartments();
            for (Department department : departments) {
                department.getTasks().remove(task);
                departmentRepository.save(department);
            }

            taskRepository.delete(task);

            return ResponseEntity.ok("Task successfully deleted !");
        }
        return ResponseEntity.ok("Failed to delete task !");
    }

    //update a task
    @PutMapping("/{task_id}/update-task")
    public ResponseEntity<Task> updateTask(
            @PathVariable int task_id,
            @RequestBody Task taskDetails
    ) {

        Optional<Task> optionalTask = taskRepository.findById(task_id);

        // checks if the task exists
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        // modify the optional type to Task type
        Task updateTask = optionalTask.get();

        // update task properties with values from taskDetails
        updateTask.setTaskTitle(taskDetails.getTaskTitle());
        updateTask.setTaskExecutor(taskDetails.getTaskExecutor());
        updateTask.setCompletionDate(taskDetails.getCompletionDate());
        updateTask.setDeadline(taskDetails.getDeadline());
        updateTask.setBonus(taskDetails.getBonus());

        taskRepository.save(updateTask);

        return ResponseEntity.ok(updateTask);
    }

}
