package com.employess.app.Controllers;

import com.employess.app.Entities.Announcement;
import com.employess.app.Entities.Attendance;
import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Training;
import com.employess.app.Repositories.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // get all announcements
    @GetMapping("")
    List<Announcement> getAllAnnouncements(){
        return announcementRepository.findAll();
    }

    // get announcement by id
    @GetMapping("/{announcement_id}")
    Optional<Announcement> get(@PathVariable int announcement_id){
        return announcementRepository.findById(announcement_id);
    }

    // delete announcement by id
    @DeleteMapping("/{announcement_id}")
    public ResponseEntity<String> delete(@PathVariable int announcement_id) {

        Optional<Announcement> announcementOptional = announcementRepository.findById(announcement_id);

        if(announcementOptional.isEmpty()){
            return ResponseEntity.ok("Failed to delete announcement !");
        }

        announcementRepository.deleteById(announcement_id);
        return ResponseEntity.ok("Announcement successfully deleted ! ");
    }

    // add an announcement
    @PostMapping("/add-announcement")
    Announcement addAnnouncement(@RequestBody Announcement announcement) {
        return  announcementRepository.save(announcement);
    }

    // update an announcement
    @PutMapping("/{announcement_id}/update-announcement")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable int announcement_id, @RequestBody Announcement announcementDetails){

        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(announcement_id);

        // checks if the announcement exists
        if(optionalAnnouncement.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Announcement not found"
            );
        }

        // modify the optional type to Announcement type
        Announcement updateAnnouncement = optionalAnnouncement.get();

        //update announcement
        updateAnnouncement.setTitle(announcementDetails.getTitle());
        updateAnnouncement.setContent(announcementDetails.getContent());
        updateAnnouncement.setDate(announcementDetails.getDate());

        announcementRepository.save(updateAnnouncement);

        return ResponseEntity.ok(updateAnnouncement);
    }
}
