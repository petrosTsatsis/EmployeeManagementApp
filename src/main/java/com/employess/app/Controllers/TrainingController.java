package com.employess.app.Controllers;

import com.employess.app.Entities.Attendance;
import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Training;
import com.employess.app.Repositories.EmployeeRepository;
import com.employess.app.Repositories.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // get all trainings
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("")
    List<Training> getAllTrainings(){
        return trainingRepository.findAll();
    }

    //get training by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{training_id}")
    Optional<Training> get(@PathVariable int training_id){
        return trainingRepository.findById(training_id);
    }

    // delete training by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{training_id}")
    public ResponseEntity<String> delete(@PathVariable int training_id) {
        Optional<Training> trainingOptional = trainingRepository.findById(training_id);

        if (trainingOptional.isPresent()) {
            Training training = trainingOptional.get();

            // Disassociate employees from training
            for (Employee employee : training.getEmployees()) {
                employee.setTraining(null);
                employeeRepository.save(employee);
            }

            // Clear the list of employees from training
            training.getEmployees().clear();

            trainingRepository.deleteById(training_id);

            return ResponseEntity.ok("Training session succesfully deleted !");
        }
        return ResponseEntity.ok("Failed to delete training session !");
    }

    // add a training session
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-training")
    Training addTraining(@RequestBody Training training) {
        return  trainingRepository.save(training);
    }

    // update a training session
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{training_id}/update-training")
    public ResponseEntity<Training> updateTraining(@PathVariable int training_id, @RequestBody Training trainingDetails){

        Optional<Training> optionalTraining = trainingRepository.findById(training_id);

        // checks if the training exists
        if(optionalTraining.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Training not found"
            );
        }

        // modify the optional type to Training type
        Training updateTraining = optionalTraining.get();

        //update training
        updateTraining.setTrainingTitle(trainingDetails.getTrainingTitle());
        updateTraining.setDescription(trainingDetails.getDescription());
        updateTraining.setStartDate(trainingDetails.getEndDate());
        updateTraining.setEndDate(trainingDetails.getEndDate());
        updateTraining.setTrainer(trainingDetails.getTrainer());

        trainingRepository.save(updateTraining);

        return ResponseEntity.ok(updateTraining);
    }

    // add an employee to a training session
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PostMapping("/{training_id}/employees/{employee_id}/add-to-training")
    Employee addEmployee (@PathVariable int training_id, @PathVariable int employee_id){

        Optional<Training> optionalTraining = trainingRepository.findById(training_id);

        // checks if the training exists
        if(optionalTraining.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Training not found"
            );
        }

        // modify the optional type to Training type
        Training training = optionalTraining.get();

        Optional<Employee> optionalEmployee = employeeRepository.findById(employee_id);

        // checks if the employee exists
        if(optionalEmployee.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found"
            );
        }

        // modify the optional type to Employee type
        Employee employee = optionalEmployee.get();

        if( employee.getTraining() != null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This employee is already part of a Training!"
            );
        }

        training.getEmployees().add(employee);
        training.setEmployees(training.getEmployees());
        employee.setTraining(training);

        trainingRepository.save(training);
        employeeRepository.save(employee);

        return employee;
    }

    // view the participants of a training session
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{training_id}/viewParticipants")
    List<Employee> trainingParticipants(@PathVariable int training_id){

        Optional<Training> optionalTraining = trainingRepository.findById(training_id);

        // checks if the training exists
        if(optionalTraining.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Training not found"
            );
        }

        // modify the optional type to Training type
        Training training = optionalTraining.get();

        return  training.getEmployees();

    }


}
