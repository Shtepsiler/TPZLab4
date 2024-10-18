package org.example.lab4.repositories;

import org.example.lab4.models.MechanicsTasks;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MechanicsTasksRepository {
    public MechanicsTasksRepository(){}
    private final String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=javalab4";
    private final String username = "sa";
    private final String password = "Qwerty123";

    // Establish a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    // Create a new MechanicsTask
    public void create(MechanicsTasks task) {
        String sql = "INSERT INTO MechanicsTasks (Id, MechanicId, JobId, Name, IssueDate, FinishDate, Task, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, task.getId());
            statement.setObject(2, task.getMechanicId());
            statement.setObject(3, task.getJobId());
            statement.setString(4, task.getName());

            // Convert LocalDateTime to Timestamp
            statement.setTimestamp(5, task.getIssueDate() != null ?
                    Timestamp.valueOf(task.getIssueDate()) : null);
            statement.setTimestamp(6, task.getFinishDate() != null ?
                    Timestamp.valueOf(task.getFinishDate()) : null);
            statement.setString(7, task.getTask());
            statement.setString(8, task.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately in production code
        }
    }

    // Read all MechanicsTasks
    public List<MechanicsTasks> readAll() {
        List<MechanicsTasks> tasks = new ArrayList<>();
        String sql = "SELECT * FROM MechanicsTasks";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                MechanicsTasks task = new MechanicsTasks();
                task.setId((UUID) resultSet.getObject("Id"));
                task.setMechanicId((UUID) resultSet.getObject("MechanicId"));
                task.setJobId((UUID) resultSet.getObject("JobId"));
                task.setName(resultSet.getString("Name"));

                // Convert Timestamp to LocalDateTime
                task.setIssueDate(resultSet.getTimestamp("IssueDate") != null ?
                        resultSet.getTimestamp("IssueDate").toLocalDateTime() : null);
                task.setFinishDate(resultSet.getTimestamp("FinishDate") != null ?
                        resultSet.getTimestamp("FinishDate").toLocalDateTime() : null);

                task.setTask(resultSet.getString("Task"));
                task.setStatus(resultSet.getString("Status"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately in production code
        }
        return tasks;
    }

    // Update an existing MechanicsTask
    public void update(MechanicsTasks task) {
        String sql = "UPDATE MechanicsTasks SET MechanicId = ?, JobId = ?, Name = ?, IssueDate = ?, FinishDate = ?, Task = ?, Status = ? WHERE Id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, task.getMechanicId());
            statement.setObject(2, task.getJobId());
            statement.setString(3, task.getName());

            // Convert LocalDateTime to Timestamp
            statement.setTimestamp(4, task.getIssueDate() != null ?
                    Timestamp.valueOf(task.getIssueDate()) : null);
            statement.setTimestamp(5, task.getFinishDate() != null ?
                    Timestamp.valueOf(task.getFinishDate()) : null);
            statement.setString(6, task.getTask());
            statement.setString(7, task.getStatus());
            statement.setObject(8, task.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately in production code
        }
    }

    // Delete a MechanicsTask by ID
    public void delete(UUID id) {
        String sql = "DELETE FROM MechanicsTasks WHERE Id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately in production code
        }
    }
}
