import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Server {
	private static ServerSocket serverSocket;
	private static final int PORT = 1234;
    private static final db db_connection=new db();
    private static  DataInputStream input = null;
    private static DataOutputStream out = null;
    private static int userID ;
	public static void main(String[] args) throws IOException {
        System.out.println("Opening connection...\n");
        serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket clientsocket = serverSocket.accept();
            System.out.println("Client connected.");
            Thread clientHandler = new Thread(new ClientHandler(clientsocket));
            clientHandler.start();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
                db_connection.connectToDatabase();
                String requestType = input.readUTF();
                if (requestType.equals("REGISTER")) {
                    ValidateSignUp(input, out);
                } else if (requestType.equals("LOGIN")) {
                    ValidateSignIn(input, out);
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void InventoryManagement(){

    }
    static void ValidateSignIn(DataInputStream input, DataOutputStream out) throws IOException, SQLException {
        Connection con = null;
        try {
            con = db.getConnection();
            String loginInfo = input.readUTF();
            String[] loginParts = loginInfo.split(":");
            if (loginParts.length == 2) {
                String userName = loginParts[0];
                String password = loginParts[1];
                String query = "SELECT * FROM users WHERE username = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (password.equals(storedPassword)) {
                        userID=db.getUserIdByUsername(userName);
                        System.out.println(userID+" UserID");
                        out.writeUTF("Login successful");
                    } else {
                        out.writeUTF("401 Unauthorized: Incorrect password");
                    }
                } else {
                    out.writeUTF("404 Not Found: User not found");
                }
            } else {
                out.writeUTF("400 Bad Request: Invalid login format");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.writeUTF("500 Internal Server Error");
        }
    }


    static void ValidateSignUp(DataInputStream input, DataOutputStream out) throws IOException, SQLException {
        try {
            Connection con=db.getConnection();
            String[] registerInfo = input.readUTF().split(":");
            String name = registerInfo[0].replace("\0", "");
            String userName = registerInfo[1].replace("\0", "");
            String password = registerInfo[2].replace("\0", "");
            String query = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                userID=db.getUserIdByUsername(userName);
                System.out.println(userID+" UserID");
                out.writeUTF("Registration successful");
            } else {
                out.writeUTF("Username already exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.writeUTF("500 Internal Server Error");
        }
    }
    void getSearchedData(){
    }
    void ReuestAddBook(){

    }
    void RequestRemoveBook(){

    }
    void BorrowingRequest(){
        
    }
    void RequestHistory(){

    }
    void Requeststatistics(){

    }
    void RequestReviewbook(){

    }
    void accumulatedRating(){

    }
    void DisplayList_dependONReview(){

    }
}
