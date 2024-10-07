package org.example.lab4.repositories;

import lombok.NoArgsConstructor;
import org.example.lab4.models.Job;
import org.example.lab4.repositories.interfaces.IJobRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
public class FakeJobRepository implements IJobRepository {
    // In-memory list to hold jobs
    private List<Job> jobs;

    public FakeJobRepository() {
        // Initialize the list with some fake data
        this.jobs = new ArrayList<>(Arrays.asList(
                new Job(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Model X",
                        "In Progress",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        null,
                        "Fix engine issue",
                        BigDecimal.valueOf(1000),
                        new ArrayList<>()),

                new Job(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Model Y", "Completed",
                        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        LocalDateTime.now().minusDays(2), LocalDateTime.now(), "Replace tires",
                        BigDecimal.valueOf(500), new ArrayList<>()),

                new Job(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Model Z", "New",
                        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        LocalDateTime.now(), null, "Check brakes",
                        BigDecimal.valueOf(300), new ArrayList<>())
        ));
    }
    @Override
    public void create(Job job) {
        // Simulate saving to a database by adding to the list
        jobs.add(job);
    }

    @Override
    public List<Job> readAll() {
        // Return all jobs from the in-memory list
        return new ArrayList<>(jobs);
    }

    @Override
    public void update(Job updatedJob) {
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            if (job.getId().equals(updatedJob.getId())) {
                // Replace the existing job with the updated job
                jobs.set(i, updatedJob);
                break;
            }
        }
    }

    @Override
    public void delete(UUID id) {
        // Remove the job from the list by its ID
        jobs.removeIf(job -> job.getId().equals(id));
    }
}
