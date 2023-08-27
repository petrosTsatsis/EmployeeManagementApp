package com.employess.app.Controllers;

import com.employess.app.Entities.*;
import com.employess.app.Repositories.AttendanceRepository;
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
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // get all attendances
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("")
    List<Attendance> getAllAttendances(){
        return attendanceRepository.findAll();
    }

    // get attendance by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{attendance_id}")
    Optional<Attendance> get(@PathVariable int attendance_id){
        return attendanceRepository.findById(attendance_id);
    }

    // delete attendance by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @DeleteMapping("/{attendance_id}")
    public ResponseEntity<String> delete(@PathVariable int attendance_id) {

        Optional<Attendance> attendanceOptional = attendanceRepository.findById(attendance_id);

        if (attendanceOptional.isPresent()) {
            Attendance attendance = attendanceOptional.get();

            // Disassociate employees from attendance
            Employee employee = attendance.getEmployee();
            employee.setAttendance(null);
            attendance.setEmployee(null);

            employeeRepository.save(employee);

            attendanceRepository.deleteById(attendance_id);

            return ResponseEntity.ok("Attendance successfully deleted !");

        }

        return ResponseEntity.ok("Failed to delete attendance !");
    }

    // add an attendance
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PostMapping("/add-attendance/employees/{employee_id}")
    Attendance save(@Validated @RequestBody Attendance attendance, @PathVariable int employee_id){

        Optional<Employee> employeeOptional = employeeRepository.findById(employee_id);
        if (employeeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        Employee employee = employeeOptional.get();

        attendance.setEmployee(employee);
        attendanceRepository.save(attendance);
        return attendance;
    }

    //update a review
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PutMapping("/{attendance_id}/update-attendance")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable int attendance_id, @RequestBody Attendance attendanceDetails){

        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendance_id);

        // checks if the attendance exists
        if(optionalAttendance.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Attendance not found"
            );
        }

        // modify the optional type to Attendance type
        Attendance updateAttendance = optionalAttendance.get();

        //update review
        updateAttendance.setDate(attendanceDetails.getDate());
        updateAttendance.setClockIn(attendanceDetails.getClockIn());
        updateAttendance.setClockOut(attendanceDetails.getClockOut());

        attendanceRepository.save(updateAttendance);

        return ResponseEntity.ok(updateAttendance);
    }

}
