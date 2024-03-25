import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static  DataInputStream input = null;
    private static  DataInputStream inputServer = null;
    private static DataOutputStream out = null; 
	public static void main(String[] args){
        try{
            Socket socket=new Socket("localhost", 1234);
            input = new DataInputStream(System.in);
            inputServer = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Choose an option: ");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Request Add Book");
            System.out.println("4. Request Remove Book");
            System.out.println("5. Add Review");

            int choice = Integer.parseInt(input.readLine());
            if (choice == 1) {
                Register();
            } else if (choice == 2) {
                Login();
            } else if (choice==3) {
                AddBook();
            }else if(choice==4){
                RemoveBook();
            }else if(choice==5){
                AddReview();
            }
            else{
                System.out.println("Invalid choice");
            }
            socket.close();
        }catch(Exception e){System.out.println(e);}
    }

    static void Login() throws IOException{
        out.writeUTF("LOGIN");
        System.out.print("Enter username: ");
        String userName = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        String userData = userName + ":" + password;
        out.writeUTF(userData);
        String response = inputServer.readUTF();
        System.out.println(response);
    }

    static void Register() throws IOException{
        out.writeUTF("REGISTER");
        System.out.print("Enter your name: ");
        String name = input.readLine();
        System.out.print("Enter username: ");
        String un = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        String data = name + ":" + un + ":" + password;
        out.writeUTF(data);
        String registerInfo = inputServer.readUTF();
        System.out.println(registerInfo);
    }

    void BrowseBooks(){

    }
    static void AddReview() throws IOException {
        out.writeUTF("REQUEST_ADD_REVIEW");
        System.out.print("Enter book ID: ");
        int bookID = Integer.parseInt(input.readLine());
        System.out.print("Enter your ID: ");
        int clientID = Integer.parseInt(input.readLine());
        System.out.print("Enter your review: ");
        String reviewText = input.readLine();
        System.out.print("Enter your rating (1-5): ");
        int rating = Integer.parseInt(input.readLine());
        String data = "7:" + bookID + ":" + clientID + ":" + reviewText + ":" + rating;
        out.writeUTF(data);
        String reviewInfo = inputServer.readUTF();
        System.out.println(reviewInfo);
    }

    static void AddBook() throws IOException {
        out.writeUTF("REQUEST_ADD_BOOK");
        System.out.print("Enter book name: ");
        String name = input.readLine();
        System.out.print("Enter author: ");
        String author = input.readLine();
        System.out.print("Enter genre: ");
        String genre = input.readLine();
        System.out.print("Enter price: ");
        String price = input.readLine();
        System.out.print("Enter your id: ");
        String clientID = input.readLine();
        String data = "6:" + name + ":" + author + ":" + genre + ":" + price + ":" + clientID;
        out.writeUTF(data);
        String bookInfo = inputServer.readUTF();
        System.out.println(bookInfo);
    }

    static void RemoveBook() throws IOException {
        out.writeUTF("REMOVE_BOOK");
        System.out.print("Enter book name: ");
        String bookName = input.readLine();
        System.out.print("Enter your id: ");
        String clientID = input.readLine();
        String Data="7:"+bookName +":" + clientID + ":lend";
        out.writeUTF(Data);
        String bookRemovedInfo = inputServer.readUTF();
        System.out.println(bookRemovedInfo);
    }
    void BorrowBook(){

    }
    void Notification(){

    }
    void History(){

    }
}
