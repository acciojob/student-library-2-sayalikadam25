package com.driver.services;

import com.driver.models.Card;
import com.driver.models.CardStatus;
import com.driver.models.Student;
import com.driver.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {


    @Autowired
    CardService cardService4;

    @Autowired
    StudentRepository studentRepository4;

    public Student getDetailsByEmail(String email){
        return studentRepository4.findByEmailId(email);
    }

    public Student getDetailsById(int id){
        return studentRepository4.findById(id).get();
    }

    public void createStudent(Student student){
        studentRepository4.save(student);
    }

    public void updateStudent(Student student){
        Card newCard=cardService4.createAndReturn(student);
        studentRepository4.save(student);
    }

    public void deleteStudent(int id){
        if(studentRepository4.existsById(id)){
            cardService4.deactivateCard(id);
            studentRepository4.deleteById(id);
        }
    }
}
