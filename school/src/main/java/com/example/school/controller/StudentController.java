package com.example.school.controller;

import com.example.school.model.Student;
import com.example.school.service.StudentH2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
public class StudentController {

    @Autowired
    private StudentH2Service service;

    @GetMapping("/students")
    public ArrayList<Student> getStudents() {
        return service.getStudents();
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return service.addStudent(student);
    }

    @PostMapping("/students/bulk")
    public String addStudentsBulk(@RequestBody ArrayList<Student> students) {
        int count = service.addStudentsBulk(students);
        return count + " students added";
    }

    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable int studentId) {
        return service.getStudentById(studentId);
    }

    @PutMapping("/students/{studentId}")
    public Student updateStudent(@PathVariable int studentId, @RequestBody Student student) {
        return service.updateStudent(studentId, student);
    }

    @DeleteMapping("/students/{studentId}")
    public void deleteStudent(@PathVariable int studentId) {
        service.deleteStudent(studentId);
    }
}
