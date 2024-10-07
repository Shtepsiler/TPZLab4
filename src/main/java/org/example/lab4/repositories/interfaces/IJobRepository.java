package org.example.lab4.repositories.interfaces;

import org.example.lab4.models.Job;

import java.util.List;
import java.util.UUID;

public interface IJobRepository {
    void create(Job job);

    List<Job> readAll();

    void update(Job job);

    void delete(UUID id);
}
