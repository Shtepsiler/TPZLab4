package org.example.lab4.models;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder // Generates a builder pattern
public class Job {

    private UUID id;
    private UUID managerId;
    private UUID modelId;
    private String modelName;
    private String status;
    private UUID clientId;
    private UUID mechanicId;
    private UUID orderId;
    private LocalDateTime issueDate;
    private LocalDateTime  finishDate;
    private String description;
    private BigDecimal price;
    private List<MechanicsTasks> tasks = new ArrayList<>(); // Assuming MechanicsTasks is another entity

    public Job(){}
    // If you want to add any specific constructor logic, you can define it here
    // For example, you might want to set a default value for `status` when creating a new Job
    public Job(UUID id,
               UUID managerId,
               UUID modelId,
               String modelName,
               String status,
               UUID clientId,
               UUID mechanicId,
               UUID orderId,
               LocalDateTime  issueDate,
               LocalDateTime  finishDate,
               String description,
               BigDecimal price,
               List<MechanicsTasks> tasks) {
        this.id = id;
        this.managerId = managerId;
        this.modelId = modelId;
        this.modelName = modelName;
        this.status = (status != null) ? status : "New"; // Default status
        this.clientId = clientId;
        this.mechanicId = mechanicId;
        this.orderId = orderId;
        this.issueDate = issueDate;
        this.finishDate = finishDate;
        this.description = description;
        this.price = price;
        this.tasks = tasks;
    }
}