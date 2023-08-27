package com.employess.app.Controllers;

import com.employess.app.Entities.Contract;
import com.employess.app.Entities.Department;
import com.employess.app.Entities.Employee;
import com.employess.app.Repositories.DepartmentRepository;
import com.employess.app.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // get all departments
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("")
    List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    // get department by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{department_id}")
    Optional<Department> get(@PathVariable int department_id){
        return departmentRepository.findById(department_id);
    }

    // add department
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-department")
    Department save(@Validated @RequestBody Department department){
        departmentRepository.save(department);
        return department;
    }

    // add employee to department
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PostMapping("/{department_id}/employees/{employee_id}")
    Employee addEmployee(@PathVariable int department_id, @PathVariable int employee_id){


        Department department = departmentRepository.findById(department_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        Employee employee = employeeRepository.findById(employee_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        employee.setDepartment(department);
        department.getEmployees().add(employee);
        department.setEmployees(department.getEmployees());
        department.setNumberOfEmployees(department.getNumberOfEmployees() + 1);

        departmentRepository.save(department);
        employeeRepository.save(employee);


        return employee;
    }

    // delete department by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{department_id}")
    public ResponseEntity<String> delete(@PathVariable int department_id) {
        Optional<Department> departmentOptional = departmentRepository.findById(department_id);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();

            // Set the department to null for every employee associated with the department
            for (Employee employee : department.getEmployees()) {
                employee.setDepartment(null);
                employeeRepository.save(employee);

            }
            departmentRepository.deleteById(department_id);

            return ResponseEntity.ok("Department deleted successfully !");
        }
        return ResponseEntity.ok("Failed to delete department !");
    }

    //update a department
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{department_id}/update-department")
    public ResponseEntity<Department> updateDepartment(@PathVariable int department_id, @RequestBody Department departmentDetails){

        Optional<Department> optionalDepartment = departmentRepository.findById(department_id);

        // checks if the department exists
        if(optionalDepartment.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Department not found"
            );
        }

        // modify the optional type to Department type
        Department updateDepartment = optionalDepartment.get();

        //update department
        updateDepartment.setDepartmentName(departmentDetails.getDepartmentName());

        departmentRepository.save(updateDepartment);

        return ResponseEntity.ok(updateDepartment);
    }

    // view the employees of the department
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{department_id}/view-employees")
    List<Employee> viewEmployees(@PathVariable int department_id){
        Optional<Department> optionalDepartment = departmentRepository.findById(department_id);

        // checks if the department exists
        if(optionalDepartment.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Department not found"
            );
        }

        // modify the optional type to Department type
        Department department= optionalDepartment.get();

        return department.getEmployees();

    }
}
