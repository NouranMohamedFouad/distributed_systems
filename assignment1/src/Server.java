import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;
    private static final db db_connection=new db();
    private static  DataInputStream input = null;
    private static DataOutputStream out = null;


    public static void main(String[] args) throws IOException {
        System.out.println("Opening connection...\n");
        try{
            serverSocket=new ServerSocket(PORT);
            Socket clientsocket=serverSocket.accept();
            input=new DataInputStream(clientsocket.getInputStream());
            out=new DataOutputStream(clientsocket.getOutputStream());
            db_connection.connectToDatabase();
            System.out.println("test");
            String[] data = input.readLine().split(":");
            String choosedNum = data[0].replace("\0", "");
            System.out.println("test2");
            out.flush();
            System.out.println(choosedNum);
            if(choosedNum.contains("1")){
                ValidateSignUP(data);
            }else if(choosedNum.contains("2")){
                ValidateSignIn();
            }else if(choosedNum.contains("3")){
                ValidateManageInventory(data);
            }else if(choosedNum.contains("4")){

            }else if(choosedNum.contains("5")){
            }
            else if(choosedNum.contains("6")){
                ReuestAddBook(data);
            }else if(choosedNum.contains("7")){
                ReuestRemoveBook(data);

            }else if(choosedNum.contains("8")){

            }else if(choosedNum.contains("9")){

            }else if(choosedNum.contains("10")){

            }else if(choosedNum.contains("11")){

            }else if(choosedNum.contains("12")){

            }
            //serverSocket.close();

        }catch(Exception e){System.out.println(e);}
    }

    /////////////////////////////////////////////////////////////////////////////

    static void ValidateSignUP(String[] registerInfo) throws IOException{
        try{
            Connection con=db.getConnection();

            String name = registerInfo[1].replace("\0", "");
            String userName = registerInfo[2].replace("\0", "");
            String password = registerInfo[3].replace("\0", "");

            //System.out.println("name: "+name+" usn: "+userName+" pas "+password);
            String query = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);
            System.out.println("name: "+name+" ,usn: "+userName+" ,pas: "+password);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.writeUTF("Registration successful");
            } else {
                out.writeUTF("Username already exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.writeUTF("500 Internal Server Error");
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    static void ValidateSignIn(){

    }
    /////////////////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////
    static void getSearchedData(){

    }
    /////////////////////////////////////////////////////////////////////////////
    static void ReuestAddBook(String[] requestDetails) throws SQLException, IOException {
        Connection con=db.getConnection();

        System.out.println("start");

        String bookName = requestDetails[1].replace("\0", "");
        int clientID = Integer.parseInt(requestDetails[2].replace("\0", ""));
        String status = requestDetails[3].replace("\0", "");
        String query1 = "SELECT bookid FROM books WHERE title=?";
        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
        preparedStatement1.setString(1, bookName);
        ResultSet res = preparedStatement1.executeQuery();

        int bookID = -1; // Default value in case the book is not found
        if (res.next()) {
            bookID = res.getInt("bookid"); // Retrieve the ID from the ResultSet
        } else {
            // Handle the case where the book with the specified title is not found
            out.writeUTF("Book with title '" + bookName + "' not found.");
        }

        String query="INSERT INTO client_books (clientid , bookid ,status) VALUES (?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(query);
        preparedStatement.setInt(1,clientID);
        preparedStatement.setInt(2,bookID);
        preparedStatement.setString(3,status);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("client added book successful");
        } else {
            System.out.println("error in adding the book");
        }
    }
    /////////////////////////////////////////////////////////////////////////////
    static void ReuestRemoveBook(String[] requestDetails) throws SQLException, IOException {
        Connection con=db.getConnection();

        String bookName = requestDetails[1].replace("\0", "");
        int clientID = Integer.parseInt(requestDetails[2].replace("\0", ""));
        String status = requestDetails[3].replace("\0", "");
        String query1 = "SELECT bookid FROM books WHERE title=?";
        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
        preparedStatement1.setString(1, bookName);
        ResultSet res = preparedStatement1.executeQuery();

        int bookID = -1; // Default value in case the book is not found
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
    /////////////////////////////////////////////////////////////////////////////
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