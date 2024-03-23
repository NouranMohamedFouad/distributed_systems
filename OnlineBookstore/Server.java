import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
            ValidateSignUP();
            serverSocket.close();

    }catch(Exception e){System.out.println(e);}
    }
    void InventoryManagement(){

    }
    void ValidateSignIn(){

    }
    static void ValidateSignUP() throws IOException{
        try {
            Connection con=db.getConnection();
            String[] registerInfo = input.readLine().split(":");
            String name = registerInfo[0].replace("\0", "");
            String userName = registerInfo[1].replace("\0", "");
            String password = registerInfo[2].replace("\0", "");
            System.out.println("name: "+name+" usn: "+userName+" pas "+password);
            String query = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);
            System.out.println("name: "+name+" usn: "+userName+" pas "+password);
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
