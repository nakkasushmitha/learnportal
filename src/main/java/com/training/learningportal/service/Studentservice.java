package com.training.learningportal.service;

import com.training.learningportal.model.Student;
import com.training.learningportal.repository.Studentrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Studentservice {

    @Autowired
    private Studentrepository studentrepository;

    @Autowired
    private JavaMailSender mailSender;

    public String registerStudent(Student student){

        String toEmail = student.getEmail();
        String message1 = "Hi Mr/Mrs" +student.getFirstname()+  "Thank you for registration. Please confirm your registration by clicking on below link";

        student.setRegistrationconfirmation(false);
        this.studentrepository.save(student);


        String link = "http://localhost:8080/"+ "confirm/" + student.getStudid();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("nsushmitha111@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Mail for confirmation regarding your registration");
        message.setText(message1 + link);

        this.mailSender.send(message);

        return "Mail sent successfully";

    }

    public String confirmRegistration(Integer myid) {

        Student studentdata = this.studentrepository.findById(myid).get();

        studentdata.setRegistrationconfirmation(true);
        this.studentrepository.save(studentdata);

        return "Thank you for your confirmation, Now you are a confirmed student";

    }
}
