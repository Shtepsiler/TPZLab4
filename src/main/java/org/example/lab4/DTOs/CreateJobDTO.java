package org.example.lab4.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lab4.models.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobDTO {
    private String modelId;
    private String modelName;
    private String clientId;
    private String issueDate; // Assuming this is a string representing the date
    private String description;

    // Method to map this DTO back to a Job entity
    public Job mapToEntity() {
        Job job = new Job();
        job.setModelId(UUID.fromString(this.modelId));
        job.setModelName(this.modelName);
        job.setClientId(UUID.fromString(this.clientId));

        // Parse the issueDate string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // Adjust the pattern as needed
        job.setIssueDate(LocalDateTime.parse(this.issueDate, formatter)); // Proper parsing

        job.setDescription(this.description);
        return job;
    }
}
