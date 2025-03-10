package com.example.Students.Service;

import com.example.Students.Queue.StudentProducer;
import com.example.Students.Repository.StudentRepository;
import com.example.Students.Student.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

     final StudentRepository studentRepository;
    private final StudentProducer studentProducer;

    public StudentService(StudentRepository studentRepository, StudentProducer studentProducer) {
        this.studentRepository = studentRepository;
        this.studentProducer = studentProducer;
    }

    public void processAndQueueStudents(List<Student> students) {
        for (Student student : students) {
            studentProducer.sendStudentToQueue(student);
        }
    }
}
