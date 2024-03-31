package com.example.demo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static void addUser(String username) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username) VALUES (?)");
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }
    public static void updateWins(String username, int wins) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET wins = ? WHERE username = ?");
            statement.setInt(1, wins);
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }
    public static boolean isNicknameExists(String username) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }
    public static int getWinsByUsername(String username) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT wins FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("wins");
            } else {
                throw new SQLException("User not found");
            }
        }
    }

    public static List<PlayerInfo> getLeaderboard() throws SQLException {
        List<PlayerInfo> leaderboard = new ArrayList<>();
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users ORDER BY wins DESC");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int wins = resultSet.getInt("wins");
                System.out.println(resultSet.getString("username"));
                PlayerInfo user = new PlayerInfo(username, wins);
                leaderboard.add(user);
            }
        }
        return leaderboard;
    }
}
