package org.example.lab4.repositories;

import org.example.lab4.models.Job;
import org.example.lab4.repositories.interfaces.IJobRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobRepository implements IJobRepository {
    public JobRepository() {}
    private final String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=javalab4;encrypt=true;trustServerCertificate=true;";
    private final String username = "sa";
    private final String password = "Qwerty123";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public void create(Job job) {
        String sql = "INSERT INTO Job (Id, ManagerId, ModelId, ModelName, Status, ClientId, MechanicId, OrderId, IssueDate, FinishDate, Description, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, job.getId());
            statement.setObject(2, job.getManagerId());
            statement.setObject(3, job.getModelId());
            statement.setString(4, job.getModelName());
            statement.setString(5, job.getStatus());
            statement.setObject(6, job.getClientId());
            statement.setObject(7, job.getMechanicId());
            statement.setObject(8, job.getOrderId());
            statement.setTimestamp(9, Timestamp.valueOf(job.getIssueDate()));
            statement.setTimestamp(10, job.getFinishDate() != null ? Timestamp.valueOf(job.getFinishDate()) : null);
            statement.setString(11, job.getDescription());
            statement.setBigDecimal(12, job.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Job> readAll() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM Job";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Job job = new Job();
                job.setId(UUID.fromString(resultSet.getString("Id"))); // Convert String to UUID
                job.setManagerId(resultSet.getString("ManagerId") != null ? UUID.fromString(resultSet.getString("ManagerId")) : null); // Handle null
                job.setModelId(resultSet.getString("ModelId") != null ? UUID.fromString(resultSet.getString("ModelId")) : null); // Handle null
                job.setModelName(resultSet.getString("ModelName"));
                job.setStatus(resultSet.getString("Status"));
                job.setClientId(resultSet.getString("ClientId") != null ? UUID.fromString(resultSet.getString("ClientId")) : null); // Handle null
                job.setMechanicId(resultSet.getString("MechanicId") != null ? UUID.fromString(resultSet.getString("MechanicId")) : null); // Handle null
                job.setOrderId(resultSet.getString("OrderId") != null ? UUID.fromString(resultSet.getString("OrderId")) : null); // Handle null
                job.setIssueDate(resultSet.getTimestamp("IssueDate").toLocalDateTime());
                job.setFinishDate(resultSet.getTimestamp("FinishDate") != null ? resultSet.getTimestamp("FinishDate").toLocalDateTime() : null);
                job.setDescription(resultSet.getString("Description"));
                job.setPrice(resultSet.getBigDecimal("Price"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    @Override
    public void update(Job job) {
        String sql = "UPDATE Job SET ManagerId = ?, ModelId = ?, ModelName = ?, Status = ?, ClientId = ?, MechanicId = ?, OrderId = ?, IssueDate = ?, FinishDate = ?, Description = ?, Price = ? WHERE Id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, job.getManagerId());
            statement.setObject(2, job.getModelId());
            statement.setString(3, job.getModelName());
            statement.setString(4, job.getStatus());
            statement.setObject(5, job.getClientId());
            statement.setObject(6, job.getMechanicId());
            statement.setObject(7, job.getOrderId());
            statement.setTimestamp(8, Timestamp.valueOf(job.getIssueDate()));
            statement.setTimestamp(9, job.getFinishDate() != null ? Timestamp.valueOf(job.getFinishDate()) : null);
            statement.setString(10, job.getDescription());
            statement.setBigDecimal(11, job.getPrice());
            statement.setObject(12, job.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM Job WHERE Id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
