package com.employess.app.Controllers;

import com.employess.app.Entities.Absence;
import com.employess.app.Entities.Announcement;
import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Training;
import com.employess.app.Repositories.AbsenceRepository;
import com.employess.app.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // get all absences
    @GetMapping("")
    List<Absence> getAllAbsences(){
        return absenceRepository.findAll();
    }

    //get absence by id
    @GetMapping("/{absence_id}")
    Optional<Absence> get(@PathVariable int absence_id){
        return absenceRepository.findById(absence_id);
    }

    // delete absence by id
    @DeleteMapping("/{absence_id}")
    public void delete(@PathVariable int absence_id) {
        absenceRepository.deleteById(absence_id);
    }

    //add an absence
    @PostMapping("/add-absence")
    Absence addAbsence(@RequestBody Absence absence) {
        int employeeId = absence.getEmployee().getId(); // Assuming Employee has an 'id' field

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        Employee employee = employeeOptional.get();

        if( employee.getAbsence() != null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This employee already has an Absence!"
            );
        }

        absence.setEmployee(employee);
        absenceRepository.save(absence);

        return absence;
    }

    // update an absence
    @PutMapping("/{absence_id}/update-absence")
    public ResponseEntity<Absence> updateAbsence(@PathVariable int absence_id, @RequestBody Absence absenceDetails){

        Optional<Absence> optionalAbsence = absenceRepository.findById(absence_id);

        // checks if the absence exists
        if(optionalAbsence.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Absence not found"
            );
        }

        // modify the optional type to Absence type
        Absence updateAbsence = optionalAbsence.get();

        //update absence
        updateAbsence.setType(absenceDetails.getType());
        updateAbsence.setStartDate(absenceDetails.getStartDate());
        updateAbsence.setEndDate(absenceDetails.getEndDate());
        updateAbsence.setStatus(absenceDetails.getStatus());
        updateAbsence.setReason(absenceDetails.getReason());

        absenceRepository.save(updateAbsence);

        return ResponseEntity.ok(updateAbsence);
    }
}
