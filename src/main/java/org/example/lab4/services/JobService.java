package org.example.lab4.services;

import org.example.lab4.DTOs.JobDTO;
import org.example.lab4.models.Job;
import org.example.lab4.repositories.*;
import org.example.lab4.repositories.interfaces.IJobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class JobService {
    private final IJobRepository jobRepository;

    public JobService() {
        this.jobRepository = new JobRepository(); // Initialize repository here
    }

    // Create a new job
    public void createJob(Job job) {
        if (job != null) {
            job.setId(UUID.randomUUID()); // Set a new unique ID for the job
            jobRepository.create(job);
        } else {
            throw new IllegalArgumentException("Job cannot be null");
        }
    }

    // Read all jobs
    public List<Job> getAllJobs() {
        return jobRepository.readAll();
    }

    // Update an existing job
    public void updateJob(JobDTO job) {
        if (job != null && job.getId() != null) {
            jobRepository.update(job.mapToEntity());
        } else {
            throw new IllegalArgumentException("Invalid job details");
        }
    }

    // Delete a job by its ID
    public void deleteJob(UUID id) {
        if (id != null) {
            jobRepository.delete(id);
        } else {
            throw new IllegalArgumentException("Job ID cannot be null");
        }
    }

    // Optional: Get a job by its ID (implement this in JobRepository if needed)
    public Job getJobById(UUID id) {
        List<Job> jobs = jobRepository.readAll();
        return jobs.stream()
                .filter(job -> job.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
