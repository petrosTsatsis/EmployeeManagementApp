package com.employess.app.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Home")
@CrossOrigin("*")
public class HomeController {

    @GetMapping("")
    public String Home(){
        return "Welcome to E-Management !";
    }

}