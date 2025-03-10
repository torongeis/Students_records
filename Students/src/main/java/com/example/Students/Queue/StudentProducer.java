package com.example.Students.Queue;

import com.example.Students.Student.Student;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.Students.Config.RabbitMQConfig.QUEUE_ONE;

@Service
public class StudentProducer {

    private final RabbitTemplate rabbitTemplate;

    public StudentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendStudentToQueue(Student student) {
        rabbitTemplate.convertAndSend(QUEUE_ONE, student);
        System.out.println("Sent to queue: " + student);
    }
}
