package com.employess.app.Controllers;

import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Review;
import com.employess.app.Repositories.EmployeeRepository;
import com.employess.app.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // get all reviews
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("")
    List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    // get review by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping("/{review_id}")
    Optional<Review> get(@PathVariable int review_id){
        return reviewRepository.findById(review_id);
    }

    // delete review by id
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @DeleteMapping("/{review_id}")
    public ResponseEntity<String> delete(@PathVariable int review_id) {

        Optional<Review> reviewOptional = reviewRepository.findById(review_id);

        if(reviewOptional.isEmpty()){
            return ResponseEntity.ok("Failed to delete review !");
        }

        reviewRepository.deleteById(review_id);
        return ResponseEntity.ok("Review successfully deleted ! ");
    }

    // add review to employee
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PostMapping("/employees/{employee_id}/add-review")
    public ResponseEntity<Review> addReview(
            @PathVariable int employee_id,
            @RequestBody Review review
    ) {
        // Find the employee being reviewed
        Optional<Employee> employeeOptional = employeeRepository.findById(employee_id);
        if (employeeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        Employee employee = employeeOptional.get();

        // Find the reviewer by email
        Optional<Employee> reviewerOptional = employeeRepository.findByEmail(review.getReviewerEmail());
        if (reviewerOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewer not found");
        }
        Employee reviewer = reviewerOptional.get();

        review.setEmployee(employee);

        // Save the review
        reviewRepository.save(review);

        // Update the relationships
        employee.getReviews().add(review);

        // Save the changes to the employees
        employeeRepository.save(reviewer);
        employeeRepository.save(employee);

        return ResponseEntity.ok(review);
    }

    //update a review
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PutMapping("/{review_id}/update-review")
    public ResponseEntity<Review> updateReview(@PathVariable int review_id, @RequestBody Review reviewDetails){

        Optional<Review> optionalReview = reviewRepository.findById(review_id);

        // checks if the review exists
        if(optionalReview.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Review not found"
            );
        }

        // modify the optional type to Review type
        Review updateReview = optionalReview.get();

        //update review
        updateReview.setReviewDate(reviewDetails.getReviewDate());
        updateReview.setComments(reviewDetails.getComments());
        updateReview.setRating(reviewDetails.getRating());
        updateReview.setReviewerEmail(reviewDetails.getReviewerEmail());


        reviewRepository.save(updateReview);

        return ResponseEntity.ok(updateReview);
    }


}
