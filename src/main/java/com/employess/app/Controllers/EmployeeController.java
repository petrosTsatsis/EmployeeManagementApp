package com.employess.app.Controllers;

import com.employess.app.Entities.*;
import com.employess.app.Repositories.ContractRepository;
import com.employess.app.Repositories.EmployeeRepository;
import com.employess.app.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private TaskRepository taskRepository;

    // get all employees
    @GetMapping("")
    List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // add an employee
    @PostMapping("/add-employee")
    Employee save(@Validated @RequestBody Employee employee){
        employeeRepository.save(employee);
        return employee;
    }

    // get employee by id
    @GetMapping("/{employee_id}")
    Optional<Employee> get(@PathVariable int employee_id){
        return employeeRepository.findById(employee_id);
    }

    // add contract to employee
    @PostMapping("/{employee_id}/add-contract")
    Contract addContract(@PathVariable int employee_id, @RequestBody Contract contract){

        Optional<Employee> employeeOptional = employeeRepository.findById(employee_id);

        if(employeeOptional.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found"
            );
        }

        Employee employee = employeeOptional.get();

        contract.setEmployee(employee);
        employee.setContract(contract);

        contractRepository.save(contract);
        employeeRepository.save(employee);


        return contract;
    }

    // delete employee by id
    @DeleteMapping("/{employee_id}")
    public void delete(@PathVariable int employee_id) {
        employeeRepository.deleteById(employee_id);
    }

    //update an employee
    @PutMapping("/{employee_id}/update-employee")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int employee_id, @RequestBody Employee employeeDetails){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employee_id);

        // checks if the employee exists
        if(optionalEmployee.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found"
            );
        }

        // modify the optional type to Employee type
        Employee updateEmployee = optionalEmployee.get();

        //update employee
        updateEmployee.setFirstName(employeeDetails.getFirstName());
        updateEmployee.setLastName(employeeDetails.getLastName());
        updateEmployee.setEmail(employeeDetails.getEmail());
        updateEmployee.setPhoneNum(employeeDetails.getPhoneNum());
        updateEmployee.setAddress(employeeDetails.getAddress());

        employeeRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }


    // add task to employee
    @PostMapping("/{employee_id}/add-task")
    Task addTask(@PathVariable int employee_id, @RequestBody Task task){

        Optional<Employee> employeeOptional = employeeRepository.findById(employee_id);

        if(employeeOptional.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found"
            );
        }

        Employee employee = employeeOptional.get();

        // initialize the employees list of the task if it's null
        if (task.getEmployees() == null) {
            task.setEmployees(new ArrayList<>());
        }

        // initialize the departments list of the task if it's null
        if (task.getDepartments() == null) {
            task.setDepartments(new ArrayList<>());
        }

        // add the task to the employee tasks list
        task.getEmployees().add(employee);
        employee.getTasks().add(task);

        // add the department in the task departments
        Department department = employee.getDepartment();
        task.getDepartments().add(department);

        // add the task in the department tasks
        department.getTasks().add(task);
        department.setTasks(department.getTasks());

        taskRepository.save(task);
        employeeRepository.save(employee);

        return task;
    }

    //update an employee's task
    @PutMapping("/{employee_id}/{task_id}/update-task")
    public ResponseEntity<Task> updateTask(
            @PathVariable int task_id,
            @PathVariable int employee_id,
            @RequestBody Task taskDetails
    ) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee_id);
        Optional<Task> optionalTask = taskRepository.findById(task_id);

        // checks if the employee exists
        if (optionalEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        // checks if the task exists
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        // modify the optional type to Employee type
        Employee employee = optionalEmployee.get();

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

    // get the employee's reviews
    @GetMapping("/{employee_id}/view-reviews")
    List<Review> getAllReviews(@PathVariable int employee_id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee_id);

        // checks if the employee exists
        if (optionalEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        Employee employee = optionalEmployee.get();

        return employee.getReviews();

    }








}
