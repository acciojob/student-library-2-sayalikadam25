package com.driver.controller;

import com.driver.models.Student;
import com.driver.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/studentByEmail")
    public ResponseEntity<String> getStudentByEmail(@RequestParam("email") String email){
        Student student=studentService.getDetailsByEmail(email);
        return new ResponseEntity<>("Student details printed successfully ", HttpStatus.OK);
    }

    @GetMapping("/studentById")
    public ResponseEntity<String> getStudentById(@RequestParam("id") int id){
        Student student=studentService.getDetailsById(id);
        return new ResponseEntity<>("Student details printed successfully ", HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        studentService.createStudent(student);
        return new ResponseEntity<>("the student is successfully added to the system", HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<String> updateStudent(@RequestBody Student student){
        studentService.updateStudent(student);
        return new ResponseEntity<>("student is updated", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteStudent(@RequestParam("id") int id){
        studentService.deleteStudent(id);
        return new ResponseEntity<>("student is deleted", HttpStatus.ACCEPTED);
    }

}
