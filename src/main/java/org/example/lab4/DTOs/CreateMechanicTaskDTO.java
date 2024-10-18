package org.example.lab4.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lab4.models.MechanicsTasks;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class CreateMechanicTaskDTO {
    public CreateMechanicTaskDTO(){}
    private UUID mechanicId;
    private String name;
    private UUID jobId;
    private LocalDateTime issueDate;
    private LocalDateTime finishDate;
    private String task;
    private String status;

    // Constructor to create MechanicsTasksDTO from MechanicsTasks entity
    public CreateMechanicTaskDTO(MechanicsTasks mechanicsTasks) {
        this.mechanicId = mechanicsTasks.getMechanicId();
        this.name = mechanicsTasks.getName();
        this.jobId = mechanicsTasks.getJobId();
        this.issueDate = mechanicsTasks.getIssueDate();
        this.finishDate = mechanicsTasks.getFinishDate();
        this.task = mechanicsTasks.getTask();
        this.status = mechanicsTasks.getStatus();
    }

    // Method to map this DTO back to MechanicsTasks entity
    public MechanicsTasks mapToEntity() {
        MechanicsTasks mechanicsTasks = new MechanicsTasks();
        mechanicsTasks.setMechanicId(this.mechanicId);
        mechanicsTasks.setName(this.name);
        mechanicsTasks.setJobId(this.jobId);
        mechanicsTasks.setIssueDate(this.issueDate);
        mechanicsTasks.setFinishDate(this.finishDate);
        mechanicsTasks.setTask(this.task);
        mechanicsTasks.setStatus(this.status);
        return mechanicsTasks;
    }
}
