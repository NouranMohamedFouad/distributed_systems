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
            String password = "123456789";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
