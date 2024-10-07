package org.example.lab4.models;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Builder // Generates a builder pattern for the class
public class MechanicsTasks {

    private UUID id;
    private UUID mechanicId;
    private UUID jobId;
    private String name;
    private LocalDateTime issueDate;
    private LocalDateTime  finishDate;
    private Job job; // Assuming Job is another entity class
    private String task;
    private String status;
}