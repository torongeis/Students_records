package com.example.Students.Queue;

import com.example.Students.Repository.StudentRepository;
import com.example.Students.Student.Student;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.Students.Config.RabbitMQConfig.QUEUE_ONE;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class StudentConsumer {

    private final StudentRepository studentRepository;
    private final List<Student> studentBatch = new ArrayList<>();
    private final int BATCH_SIZE = 10;

    public StudentConsumer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

//   @RabbitListener(queues = QUEUE_ONE)
    public void processStudent(Student student) {
        synchronized (studentBatch) {
            if (!studentRepository.existsByStudentId(student.getStudentId()) &&
                    !studentRepository.existsByEmail(student.getEmail())) {
                studentBatch.add(student);
                System.out.println("Added student to batch: " + student);

                if (studentBatch.size() >= BATCH_SIZE) {
                    saveBatch();  // Save when batch is full
                }
            } else {
                System.out.println("Duplicate student detected: " + student);
            }
        }
    }

    private void saveBatch() {
        if (!studentBatch.isEmpty()) {
            studentRepository.saveAll(studentBatch);  // Batch insert
            System.out.println("Saved batch of " + studentBatch.size() + " students.");
            studentBatch.clear();  // Clear batch after saving
        }
    }

    //   Ensure remaining students get saved if batch size is not reached
    @Scheduled(fixedRate = 5000)  // Runs every 5 seconds
    public void saveRemainingStudents() {
        synchronized (studentBatch) {
            if (!studentBatch.isEmpty()) {
                saveBatch();
            }
        }
    }
}

