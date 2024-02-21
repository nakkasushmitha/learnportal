package com.training.learningportal.controller;

import com.training.learningportal.entity.LoginEntity;
import com.training.learningportal.model.Student;
import com.training.learningportal.service.Studentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Registrationcontroller {

    @Autowired
    private Studentservice studentservice;

    @PostMapping("/register")
    public String registerStudent(@RequestBody Student student){
        return this.studentservice.registerStudent(student);
    }

    @GetMapping("/confirm/{confirmToken}")
    public String confirmRegistration(@PathVariable Integer confirmToken){
        return this.studentservice.confirmRegistration(confirmToken);
    }

    @PostMapping("/auth/user")
    public String getLoggedInUserDetails(){
        return "test";
    }
}
