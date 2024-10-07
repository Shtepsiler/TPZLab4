package org.example.lab4.DTOs;

import lombok.*;
import org.example.lab4.models.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private UUID id;
    private UUID managerId;
    private UUID modelId;
    private String modelName;
    private String status;
    private UUID clientId;
    private UUID mechanicId;
    private UUID orderId;
    private LocalDateTime issueDate;
    private LocalDateTime finishDate;
    private String description;
    private BigDecimal price;

    // Constructor that accepts a Job object
    public JobDTO(Job job) {
        this.id = job.getId();
        this.managerId = job.getManagerId();
        this.modelId = job.getModelId();
        this.modelName = job.getModelName();
        this.status = job.getStatus();
        this.clientId = job.getClientId();
        this.mechanicId = job.getMechanicId();
        this.orderId = job.getOrderId();
        this.issueDate = job.getIssueDate();
        this.finishDate = job.getFinishDate();
        this.description = job.getDescription();
        this.price = job.getPrice();
    }

    // Method to map this DTO back to a Job entity
    public Job mapToEntity() {
        Job job = new Job();
        job.setId(this.id);
        job.setManagerId(this.managerId);
        job.setModelId(this.modelId);
        job.setModelName(this.modelName);
        job.setStatus(this.status);
        job.setClientId(this.clientId);
        job.setMechanicId(this.mechanicId);
        job.setOrderId(this.orderId);
        job.setIssueDate(this.issueDate);
        job.setFinishDate(this.finishDate);
        job.setDescription(this.description);
        job.setPrice(this.price);
        return job;
    }
}
