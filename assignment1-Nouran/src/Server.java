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
    private static ObjectOutputStream outObject = null;

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
                 ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
                 DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
                db_connection.connectToDatabase();
                String requestType = input.readUTF();
                if (requestType.equals("REGISTER")) {
                    ValidateSignUp(input, out);
                } else if (requestType.equals("LOGIN")) {
                    ValidateSignIn(input, out);
//            } else if (requestType.equals("CHECK_BOOK")) {
//                String[] bookDetails = input.readUTF().split(":");
//                ValidateManageInventory(bookDetails);
                } else if (requestType.equals("REMOVE_BOOK")) {
                    String[] requestDetails = input.readUTF().split(":");
                    RequestRemoveBook(out,requestDetails);
                } else if (requestType.equals("REQUEST_ADD_BOOK")) {
                    String[] requestDetails = input.readUTF().split(":");
                    RequestAddBook(out,requestDetails);
                }else if (requestType.equals("REQUEST_ADD_REVIEW")){
                    String[] requestDetails = input.readUTF().split(":");
                    AddReview(out,requestDetails);
                }else if (requestType.equals("BORROW_BOOK")) {
                    BorrowingRequest(outObject);
                    sendReq();
                }else if (requestType.equals("REQUEST_HISTORY")) {
                    RequestHistory(outObject,input);
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
            out.writeUTF("500 Internal Server Error");
        }
    }
    static void ValidateManageInventory(String[] bookDetails) throws SQLException {
        Connection con=db.getConnection();

        String title=bookDetails[1].replace("\0", "");
        String author=bookDetails[2].replace("\0", "");
        String genre=bookDetails[3].replace("\0", "");
        double price= Double.parseDouble(bookDetails[4].replace("\0", ""));
        int quantity= Integer.parseInt(bookDetails[5].replace("\0", ""));
        String query="INSERT INTO books (title,author,genre,price,quantity) VALUES (?, ?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, author);
        preparedStatement.setString(3, genre);
        preparedStatement.setDouble(4, price);
        preparedStatement.setInt(5,quantity);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("book added successful");
        } else {
            System.out.println("error in adding the book");
        }
    }
    void getSearchedData(){
    }
    static void RequestAddBook(DataOutputStream out, String[] requestDetails) throws SQLException, IOException {
        Connection con = db.getConnection();

        System.out.println("start");

        String bookName = requestDetails[1].replace("\0", "");
        String author = requestDetails[2].replace("\0", "");
        String genre = requestDetails[3].replace("\0", "");
        double price = Double.parseDouble(requestDetails[4].replace("\0", ""));
        int clientID = Integer.parseInt(requestDetails[5].replace("\0", ""));
        String queryCheckBook = "SELECT bookid FROM books WHERE title=? AND author=?";
        PreparedStatement preparedStatementCheckBook = con.prepareStatement(queryCheckBook);
        preparedStatementCheckBook.setString(1, bookName);
        preparedStatementCheckBook.setString(2, author);
        ResultSet resCheckBook = preparedStatementCheckBook.executeQuery();

        int bookID;
        if (resCheckBook.next()) {
            bookID = resCheckBook.getInt("bookid");
            out.writeUTF("Book already exists with ID: " + bookID);
        } else {
            String queryInsertBook = "INSERT INTO books (title, author, genre, price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatementInsertBook = con.prepareStatement(queryInsertBook, Statement.RETURN_GENERATED_KEYS);
            preparedStatementInsertBook.setString(1, bookName);
            preparedStatementInsertBook.setString(2, author);
            preparedStatementInsertBook.setString(3, genre);
            preparedStatementInsertBook.setDouble(4, price);
            int rowsInserted = preparedStatementInsertBook.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatementInsertBook.getGeneratedKeys();
                if (generatedKeys.next()) {
                    bookID = generatedKeys.getInt(1); // Retrieve the auto-generated book ID
                    out.writeUTF("Book added with ID: " + bookID);
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            } else {
                throw new SQLException("Creating book failed, no rows affected.");
            }
        }
        String queryInsertClientBook = "INSERT INTO client_books (clientid , bookid) VALUES (?, ?)";
        PreparedStatement preparedStatementInsertClientBook = con.prepareStatement(queryInsertClientBook);
        preparedStatementInsertClientBook.setInt(1, clientID);
        preparedStatementInsertClientBook.setInt(2, bookID);
        int rowsAffected = preparedStatementInsertClientBook.executeUpdate();
        if (rowsAffected > 0) {
            out.writeUTF("Client added book successfully");
        } else {
            out.writeUTF("Error in adding the book for the client");
        }
    }
    static void RequestRemoveBook(DataOutputStream out,String[] requestDetails) throws SQLException, IOException {
        Connection con=db.getConnection();
        String bookName = requestDetails[1].replace("\0", "");
        int clientID = Integer.parseInt(requestDetails[2].replace("\0", ""));
        String status = requestDetails[3].replace("\0", "");
        String query1 = "SELECT bookid FROM books WHERE title=?";
        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
        preparedStatement1.setString(1, bookName);
        ResultSet res = preparedStatement1.executeQuery();
        int bookID = -1;
        if (res.next()){
            bookID = res.getInt("bookid");
        } else{
            out.writeUTF("Book with title '" + bookName + "' not found.");
        }
        String query = "DELETE FROM client_books WHERE clientid =? AND bookid =? AND status =?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, clientID);
        preparedStatement.setInt(2, bookID);
        preparedStatement.setString(3, status);
        int rowsDeleted = preparedStatement.executeUpdate();
        if(rowsDeleted > 0) {
            System.out.println("book removed successfully");
        }else{
            System.out.println("you are not lending this book already");
        }
    }
    static void AddReview(DataOutputStream out, String[] requestDetails) throws SQLException, IOException {
        Connection con = db.getConnection();
        int bookID = Integer.parseInt(requestDetails[1].replace("\0", ""));
        int userID = Integer.parseInt(requestDetails[2].replace("\0", ""));
        String reviewText = requestDetails[3].replace("\0", "");
        int rating = Integer.parseInt(requestDetails[4].replace("\0", ""));
        String query = "INSERT INTO Reviews (BookID, UserID, Rating, ReviewText, Timestamp) VALUES (?, ?, ?, ?, NOW())";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, bookID);
        preparedStatement.setInt(2, userID);
        preparedStatement.setInt(3, rating);
        preparedStatement.setString(4, reviewText);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            out.writeUTF("Review added successfully");
            updateTotalReviewForBook(bookID);
        } else {
            out.writeUTF("Error in adding the review");
        }
    }
    static void updateTotalReviewForBook(int bookID) throws SQLException {
        Connection con = db.getConnection();
        String query = "SELECT AVG(Rating) AS AverageRating FROM reviews WHERE bookid = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, bookID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            double averageRating = resultSet.getDouble("AverageRating");
            String updateQuery = "UPDATE books SET TotalReview = ? WHERE bookid = ?";
            PreparedStatement updateStatement = con.prepareStatement(updateQuery);
            updateStatement.setDouble(1, averageRating);
            updateStatement.setInt(2, bookID);
            updateStatement.executeUpdate();
        }
    }
    static void BorrowingRequest(ObjectOutputStream outObject) throws SQLException, IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        Connection con=db.getConnection();

        Statement q=con.createStatement();
        String query="SELECT * From client_books ";
        ResultSet res=q.executeQuery(query);
        while(res.next()){
            Map<String, Object> row = new HashMap<>();
            row.put("clientid", res.getObject("clientid"));
            row.put("bookid", res.getObject("bookid"));
            row.put("status", res.getObject("status"));
            data.add(row);
        }
        outObject.writeObject(data);
        res.close();
        q.close();
        con.close();

    }
    static void sendReq() throws SQLException, IOException {
        Connection con=db.getConnection();

        // Read input from client
        String[] requestDetails = input.readUTF().split(":");
        int bookID = Integer.parseInt(requestDetails[0]);
        int lenderID = Integer.parseInt(requestDetails[1]);
        int borrowerID = Integer.parseInt(requestDetails[2]);
        String status = requestDetails[3];

        String query2 = "INSERT INTO requests (borrowerID, lenderID, bookID, status) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = con.prepareStatement(query2);
        preparedStatement.setInt(1, borrowerID);
        preparedStatement.setInt(2, lenderID);
        preparedStatement.setInt(3, bookID);
        preparedStatement.setString(4, status);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            out.writeUTF("request sent successfully");
            updateTotalReviewForBook(bookID);
        } else {
            out.writeUTF("Error in sending the request");
        }

        con.close();
    }

    static void RequestHistory(ObjectOutputStream outObject,DataInputStream input) throws IOException, SQLException {
        Connection con=db.getConnection();
        List<Map<String, Object>> data = new ArrayList<>();

        System.out.print("before id");

        String id =input.readUTF();
        System.out.print(id);
        System.out.print("after id");



        String query = "SELECT * FROM requesthistory WHERE requestid = ? OR userid = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(id));
        preparedStatement.setInt(2, Integer.parseInt(id));

        ResultSet res = preparedStatement.executeQuery();

        while(res.next()){
            Map<String, Object> row = new HashMap<>();
            row.put("clientid", res.getObject("clientid"));
            row.put("bookid", res.getObject("bookid"));
            row.put("status", res.getObject("status"));
            data.add(row);
        }
        outObject.writeObject(data);

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