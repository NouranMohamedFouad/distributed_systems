import java.io.*;
import java.net.*;
import java.sql.*;

public class db {
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        db.connection = connection;
    }
    public void connectToDatabase() {
        try {
            String url = "jdbc:postgresql://localhost:5432/bookStore";
            String username = "postgres";
            String password = "Hana.2002";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int getUserIdByUsername(String username) {
        int userId = -1;
        try {
            String query = "SELECT userid FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("userid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
