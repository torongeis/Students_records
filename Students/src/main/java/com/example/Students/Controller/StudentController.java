package com.example.Students.Controller;

import com.example.Students.Service.ExcelService;
import com.example.Students.Service.StudentService;
import com.example.Students.Student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/students")
public class StudentController {

    private final ExcelService excelService;
    private final StudentService studentService;

    public StudentController(ExcelService excelService, StudentService studentService) {
        this.excelService = excelService;
        this.studentService = studentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadStudentFile(@RequestParam("file") MultipartFile file) {
        try {
            List<Student> students = excelService.parseExcelFile(file);
            studentService.processAndQueueStudents(students);

            // Return JSON response
            Map<String, String> response = new HashMap<>();
            response.put("message", "File processed successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "File processing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
