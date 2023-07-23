package app.demo;

import java.sql.*;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public boolean wordExists(String word) throws SQLException {
        String query = "SELECT word FROM word WHERE word = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    public void insertWord(String word, int frequency) throws SQLException {
        String query = "INSERT INTO word (word, frequency) VALUES (?, ?) " +
                       "ON DUPLICATE KEY UPDATE frequency = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            statement.setInt(2, frequency);
            statement.setInt(3, frequency);
            statement.executeUpdate();
        }
    }

    @SuppressWarnings("exports")
    public ResultSet getTopWords(int limit) throws SQLException {
        String query = "SELECT word, frequency FROM word ORDER BY frequency DESC LIMIT ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        return statement.executeQuery();
    }
}
