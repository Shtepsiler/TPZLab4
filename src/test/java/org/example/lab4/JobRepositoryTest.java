package org.example.lab4;

import org.example.lab4.models.Job;
import org.example.lab4.models.MechanicsTasks;
import org.example.lab4.repositories.JobRepository;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobRepositoryTest {

    private JobRepository jobRepository;

    @BeforeAll
    void beforeAll() {
        jobRepository = new JobRepository();
    }

    @BeforeEach
    void setUp() {
        List<MechanicsTasks> tasks = new ArrayList<>();
        tasks.add(MechanicsTasks.builder()
                .id(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .name("Task1")
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(2))
                .task("Replace engine")
                .status("In Progress")
                .build());

        Job job1 = Job.builder()
                .id(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .modelId(UUID.randomUUID())
                .modelName("Model1")
                .status("In Progress")
                .clientId(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(2))
                .description("Test Job 1")
                .price(new BigDecimal("500.00"))
                .tasks(tasks)
                .build();

        jobRepository.create(job1);
    }

    @AfterEach
    void tearDown() {
        List<Job> jobs = jobRepository.readAll();
        for (Job job : jobs) {
            jobRepository.delete(job.getId());
        }
    }

    @Test
    void shouldCreateNewJob() {
        // given
        Job newJob = createSampleJob();

        // when
        jobRepository.create(newJob);

        // then
        List<Job> jobs = jobRepository.readAll();
        assertTrue(jobs.stream().anyMatch(job -> job.getModelName().equals("Model2")));
    }

    @Test
    void shouldReadAllJobs() {
        // when
        List<Job> jobs = jobRepository.readAll();

        // then
        assertEquals(1, jobs.size()); // Setup adds one job
    }

    @Test
    void shouldUpdateExistingJob() {
        // given
        List<Job> jobs = jobRepository.readAll();
        Job jobToUpdate = jobs.get(0);
        jobToUpdate.setStatus("Completed");

        // when
        jobRepository.update(jobToUpdate);

        // then
        Job updatedJob = jobRepository.readAll().stream()
                .filter(job -> job.getId().equals(jobToUpdate.getId()))
                .findFirst().orElse(null);

        assertNotNull(updatedJob);
        assertEquals("Completed", updatedJob.getStatus());
    }

    @Test
    void shouldDeleteJobById() {
        // given
        List<Job> jobs = jobRepository.readAll();
        UUID jobIdToDelete = jobs.get(0).getId();

        // when
        jobRepository.delete(jobIdToDelete);

        // then
        List<Job> updatedJobs = jobRepository.readAll();
        assertFalse(updatedJobs.stream().anyMatch(job -> job.getId().equals(jobIdToDelete)));
    }

    @Test
    void shouldFindById() {
        // given
        List<Job> jobs = jobRepository.readAll();
        Job jobToFind = jobs.get(0);

        // when
        Job foundJob = jobRepository.readAll().stream()
                .filter(job -> job.getId().equals(jobToFind.getId()))
                .findFirst().orElse(null);

        // then
        assertNotNull(foundJob);
        assertEquals(jobToFind.getModelName(), foundJob.getModelName());
    }

//    @Test
//    void shouldCreateJobWithMechanicsTasks() {
//        // given
//        Job jobWithTasks = createSampleJobWithTasks();
//
//        // when
//        jobRepository.create(jobWithTasks);
//
//        // then
//        Job retrievedJob = jobRepository.readAll().stream()
//                .filter(job -> job.getId().equals(jobWithTasks.getId()))
//                .findFirst().orElse(null);
//
//        assertNotNull(retrievedJob);
//        assertEquals(1, retrievedJob.getTasks().size());
//        assertEquals("Replace brakes", retrievedJob.getTasks().get(0).getTask());
//    }

//    @Test
//    void shouldUpdateJobTasks() {
//        // given
//        List<Job> jobs = jobRepository.readAll();
//        Job jobToUpdate = jobs.get(0);
//        MechanicsTasks newTask = MechanicsTasks.builder()
//                .id(UUID.randomUUID())
//                .name("New Task")
//                .task("Install new wheels")
//                .status("Pending")
//                .build();
//        jobToUpdate.getTasks().add(newTask);
//
//        // when
//        jobRepository.update(jobToUpdate);
//
//        // then
//        Job updatedJob = jobRepository.readAll().stream()
//                .filter(job -> job.getId().equals(jobToUpdate.getId()))
//                .findFirst().orElse(null);
//
//        assertNotNull(updatedJob);
//        assertEquals(2, updatedJob.getTasks().size());
//    }

    @Test
    void shouldHandleNullFinishDate() {
        // given
        Job jobWithNullFinishDate = createSampleJob();
        jobWithNullFinishDate.setFinishDate(null);

        // when
        jobRepository.create(jobWithNullFinishDate);

        // then
        Job retrievedJob = jobRepository.readAll().stream()
                .filter(job -> job.getId().equals(jobWithNullFinishDate.getId()))
                .findFirst().orElse(null);

        assertNotNull(retrievedJob);
        assertNull(retrievedJob.getFinishDate());
    }

    @Test
    void shouldReturnEmptyWhenJobNotFound() {
        // when
        UUID randomId = UUID.randomUUID();
        Job job = jobRepository.readAll().stream()
                .filter(j -> j.getId().equals(randomId))
                .findFirst()
                .orElse(null);

        // then
        assertNull(job);
    }

    @Test
    void shouldDeleteAllJobs() {
        // given
        jobRepository.delete(jobRepository.readAll().get(0).getId());

        // when
        List<Job> jobs = jobRepository.readAll();

        // then
        assertTrue(jobs.isEmpty());
    }

    @Test
    void shouldHandleLargePriceValue() {
        // given
        Job jobWithLargePrice = createSampleJob();
        jobWithLargePrice.setPrice(new BigDecimal("999999999999.99"));

        // when
        jobRepository.create(jobWithLargePrice);

        // then
        Job retrievedJob = jobRepository.readAll().stream()
                .filter(job -> job.getId().equals(jobWithLargePrice.getId()))
                .findFirst().orElse(null);

        assertNotNull(retrievedJob);
        assertEquals(new BigDecimal("999999999999.99"), retrievedJob.getPrice());
    }

//    @Test
//    void shouldHandleMultipleMechanicsTasks() {
//        // given
//        Job jobWithMultipleTasks = createSampleJobWithMultipleTasks();
//
//        // when
//        jobRepository.create(jobWithMultipleTasks);
//
//        // then
//        Job retrievedJob = jobRepository.readAll().stream()
//                .filter(job -> job.getId().equals(jobWithMultipleTasks.getId()))
//                .findFirst().orElse(null);
//
//        assertNotNull(retrievedJob);
//        assertEquals(3, retrievedJob.getTasks().size());
//    }


    @Test
    void shouldUpdateJobPrice() {
        // given
        List<Job> jobs = jobRepository.readAll();
        Job jobToUpdate = jobs.get(0);
        jobToUpdate.setPrice(new BigDecimal("1200.00"));

        // when
        jobRepository.update(jobToUpdate);

        // then
        Job updatedJob = jobRepository.readAll().stream()
                .filter(job -> job.getId().equals(jobToUpdate.getId()))
                .findFirst().orElse(null);

        assertNotNull(updatedJob);
        assertEquals(new BigDecimal("1200.00"), updatedJob.getPrice());
    }

//    @Test
//    void shouldCreateJobWithDefaultStatus() {
//        // given
//        Job jobWithoutStatus = createSampleJob();
//        jobWithoutStatus.setStatus(null);
//
//        // when
//        jobRepository.create(jobWithoutStatus);
//
//        // then
//        Job retrievedJob = jobRepository.readAll().stream()
//                .filter(job -> job.getId().equals(jobWithoutStatus.getId()))
//                .findFirst().orElse(null);
//
//        assertNotNull(retrievedJob);
//        assertEquals("New", retrievedJob.getStatus());
//    }

    // Helper methods
    private Job createSampleJob() {
        return Job.builder()
                .id(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .modelId(UUID.randomUUID())
                .modelName("Model2")
                .status("Pending")
                .clientId(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(3))
                .description("New Test Job")
                .price(new BigDecimal("1500.00"))
                .build();
    }

    private Job createSampleJobWithTasks() {
        List<MechanicsTasks> tasks = new ArrayList<>();
        tasks.add(MechanicsTasks.builder()
                .id(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .name("Task1")
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(2))
                .task("Replace brakes")
                .status("In Progress")
                .build());

        return Job.builder()
                .id(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .modelId(UUID.randomUUID())
                .modelName("Model3")
                .status("Pending")
                .clientId(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(4))
                .description("Job with tasks")
                .price(new BigDecimal("2000.00"))
                .tasks(tasks)
                .build();
    }

    private Job createSampleJobWithMultipleTasks() {
        List<MechanicsTasks> tasks = new ArrayList<>();
        tasks.add(MechanicsTasks.builder()
                .id(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .name("Task1")
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(2))
                .task("Replace brakes")
                .status("In Progress")
                .build());
        tasks.add(MechanicsTasks.builder()
                .id(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .name("Task2")
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(1))
                .task("Change oil")
                .status("Pending")
                .build());
        tasks.add(MechanicsTasks.builder()
                .id(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .name("Task3")
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(3))
                .task("Install new tires")
                .status("Completed")
                .build());

        return Job.builder()
                .id(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .modelId(UUID.randomUUID())
                .modelName("Model4")
                .status("Pending")
                .clientId(UUID.randomUUID())
                .mechanicId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .issueDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusDays(4))
                .description("Job with multiple tasks")
                .price(new BigDecimal("3000.00"))
                .tasks(tasks)
                .build();
    }
}
