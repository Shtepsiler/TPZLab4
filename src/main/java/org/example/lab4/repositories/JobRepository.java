package org.example.lab4.repositories;

import org.example.lab4.models.Job;
import org.example.lab4.repositories.interfaces.IJobRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobRepository implements IJobRepository {
    private String jdbcUrl = null;
    private String username = null;
    private String password = null;
    private String serverHost = null;
    private String serverPort = null;
    private String databaseName = null;

    public JobRepository() {
        // Отримуємо змінні оточення
        this.serverHost = System.getenv("SQL_SERVER_HOST");
        this.serverPort = System.getenv("SQL_SERVER_PORT");
        this.databaseName = System.getenv("SQL_SERVER_DB");
        this.username = System.getenv("SQL_SERVER_USER");
        this.password = System.getenv("SQL_SERVER_PASSWORD");

        // Якщо змінна не знайдена, можна задати значення за замовчуванням
        if (serverHost == null || serverPort == null || databaseName == null || username == null || password == null) {
            this.serverHost = (serverHost != null) ? serverHost : "localhost";
            this.serverPort = (serverPort != null) ? serverPort : "1433";
            this.databaseName = (databaseName != null) ? databaseName : "javalab4";
            this.username = (username != null) ? username : "sa";
            this.password = (password != null) ? password : "Qwerty123";
        }

        // Формуємо рядок підключення
        this.jdbcUrl = String.format(
                "jdbc:sqlserver://%s:%s;encrypt=true;trustServerCertificate=true;",
                serverHost, serverPort
        );

        // Забезпечуємо створення бази даних і таблиць
        createDatabaseIfNotExists();
        createTableIfNotExists();
    }

    private void createDatabaseIfNotExists() {
        String sql = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'javalab4') " +
                "CREATE DATABASE javalab4;";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        // Connect to the newly created database
        return DriverManager.getConnection(jdbcUrl + "databaseName=javalab4;", username, password);
    }

    private void createTableIfNotExists() {
        String sqlJobTable = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Job') " +
                "CREATE TABLE Job (" +
                "Id UNIQUEIDENTIFIER PRIMARY KEY," +
                "ManagerId UNIQUEIDENTIFIER," +
                "ModelId UNIQUEIDENTIFIER," +
                "ModelName NVARCHAR(100)," +
                "Status NVARCHAR(50) DEFAULT 'New'," +
                "ClientId UNIQUEIDENTIFIER," +
                "MechanicId UNIQUEIDENTIFIER," +
                "OrderId UNIQUEIDENTIFIER," +
                "IssueDate DATETIME," +
                "FinishDate DATETIME," +
                "Description NVARCHAR(MAX)," +
                "Price DECIMAL(18, 2)" +
                ");";

        String sqlMechanicsTasksTable = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'MechanicsTasks') " +
                "CREATE TABLE MechanicsTasks (" +
                "Id UNIQUEIDENTIFIER PRIMARY KEY," +
                "MechanicId UNIQUEIDENTIFIER," +
                "JobId UNIQUEIDENTIFIER," +
                "Name NVARCHAR(100)," +
                "IssueDate DATETIME," +
                "FinishDate DATETIME," +
                "Task NVARCHAR(MAX)," +
                "Status NVARCHAR(50)" +
                ");";

        try (Connection connection = getConnection();
             PreparedStatement jobStatement = connection.prepareStatement(sqlJobTable);
             PreparedStatement mechanicsTasksStatement = connection.prepareStatement(sqlMechanicsTasksTable)) {
            jobStatement.execute();
            mechanicsTasksStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
