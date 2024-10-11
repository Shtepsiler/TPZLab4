package org.example.lab4.controllers;

import org.example.lab4.DTOs.CreateJobDTO;
import org.example.lab4.DTOs.JobDTO;
import org.example.lab4.DTOs.UpdateJobDTO;
import org.example.lab4.models.Job;
import org.example.lab4.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@EnableCaching
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            jobService.deleteJob(id); // Call the service to delete the job
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateJobDTO command) {
        System.out.println("Received request to create job: " + command);
        try {
            // Use the DTO's mapToEntity() to map to the Job model
            Job newJob = command.mapToEntity();
            jobService.createJob(newJob);
            return ResponseEntity.ok(new JobDTO(newJob)); // Return created Job as a DTO
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<JobDTO>> getAll() {
        try {
            List<Job> jobs = jobService.getAllJobs();
            // Map the Job entities to JobDTOs
            List<JobDTO> jobDTOs = jobs.stream()
                    .map(JobDTO::new) // Use the JobDTO constructor to convert each Job
                    .collect(Collectors.toList());
            return ResponseEntity.ok(jobDTOs);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

//    @GetMapping("/byMechanicId")
//    public ResponseEntity<List<JobDTO>> getJobsByMechanicId(@RequestParam UUID mechanicId) {
//        try {
//            List<Job> jobs = jobService.getAllJobs().stream()
//                    .filter(job -> job.getMechanicId().equals(mechanicId))
//                    .collect(Collectors.toList());
//
//            List<JobDTO> jobDTOs = jobs.stream()
//                    .map(JobDTO::new)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(jobDTOs);
//        } catch (Exception ex) {
//            return ResponseEntity.status(500).body(null);
//        }
//    }

//    @GetMapping("/byUserId")
//    public ResponseEntity<List<JobDTO>> getJobsByUserId(@RequestParam UUID userId) {
//        try {
//            List<Job> jobs = jobService.getAllJobs().stream()
//                    .filter(job -> job.getClientId().equals(userId))
//                    .collect(Collectors.toList());
//
//            List<JobDTO> jobDTOs = jobs.stream()
//                    .map(JobDTO::new)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(jobDTOs);
//        } catch (Exception ex) {
//            return ResponseEntity.status(500).body(null);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getById(@PathVariable UUID id) {
        try {
            Job job = jobService.getJobById(id);
            if (job != null) {
                JobDTO jobDTO = new JobDTO(job); // Convert Job entity to JobDTO
                return ResponseEntity.ok(jobDTO);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateJobDTO command) {
        try {
            JobDTO jobToUpdate = new JobDTO(jobService.getJobById(command.getId()));
            if (jobToUpdate != null) {
                jobToUpdate.setManagerId(command.getManagerId());
                jobToUpdate.setModelId(command.getModelId());
                jobToUpdate.setModelName(command.getModelName());
                jobToUpdate.setStatus(command.getStatus());
                jobToUpdate.setClientId(command.getClientId());
                jobToUpdate.setMechanicId(command.getMechanicId());
                jobToUpdate.setOrderId(command.getOrderId());
                jobToUpdate.setIssueDate(command.getIssueDate());
                jobToUpdate.setFinishDate(command.getFinishDate());
                jobToUpdate.setDescription(command.getDescription());
                jobToUpdate.setPrice(command.getPrice());

                jobService.updateJob(jobToUpdate);
                return ResponseEntity.ok(jobToUpdate); // Return updated Job as a DTO
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
