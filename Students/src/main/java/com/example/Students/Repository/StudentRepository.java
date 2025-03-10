package com.example.Students.Repository;

import com.example.Students.Student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
}
