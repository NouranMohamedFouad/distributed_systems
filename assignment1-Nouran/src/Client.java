import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static  DataInputStream input = null;
    private static  DataInputStream inputServer = null;
    private static DataOutputStream out = null;
    private static ObjectInputStream inObject = null;

    public static void main(String[] args){
        try{
            Socket socket=new Socket("localhost", 1234);
            input = new DataInputStream(System.in);
            inputServer = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            inObject = new ObjectInputStream(socket.getInputStream());

            System.out.println("Choose an option: ");
            System.out.println("1. Register");
            System.out.println("2. Login");
            int choice = Integer.parseInt(input.readLine());
            if (choice == 1) {
                String result=Register();
                System.out.println(result);
                if(result.contains("successful")){
                    mainMenu(socket);
                }
            }else if(choice==2){
                String result=Login();
                System.out.println(result);
                if(result.contains("successful")){
                   mainMenu(socket);
                }
            }

            mainMenu(socket);
        }catch(Exception e){System.out.println(e);}
    }

    static void mainMenu(Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("1. Request Add Book");
        System.out.println("2. Request Remove Book");
        System.out.println("3. Request Borrow Book");
        System.out.println("4. Request Notifications");
        System.out.println("5. Show Requests History");
        System.out.println("6. Add Review");
        System.out.println("7. Exit");

        int choice = Integer.parseInt(input.readLine());
        while(true){
            if(choice==1){
                AddBook();
            }else if(choice==2){
                RemoveBook();
            }else if(choice==3){
                BorrowBook();
                insertRequest();
            }else if(choice==4){

            }else if(choice==5){
                History();

            }else if(choice==6){
                AddReview();
            }else if(choice==7){
                socket.close();
                break;
            }
            else{
                System.out.println("Invalid choice");
            }
        }
    }

    static String Login() throws IOException{
        out.writeUTF("LOGIN");
        System.out.print("Enter username: ");
        String userName = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        String userData = userName + ":" + password;
        out.writeUTF(userData);
        String response = inputServer.readUTF();
        return response;
    }

    static String Register() throws IOException{
        out.writeUTF("REGISTER");
        System.out.print("Enter your name: ");
        String name = input.readLine();
        System.out.print("Enter username: ");
        String un = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        String data = name + ":" + un + ":" + password;
        out.writeUTF(data);
        String registerInfo=inputServer.readUTF();
        return registerInfo;
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
    static void BorrowBook() throws IOException, ClassNotFoundException {
        out.writeUTF("BORROW_BOOK");
        List<Map<String,Object>> data=(List<Map<String, Object>>) inObject.readObject();
        for(Map<String,Object> row:data){
            System.out.println(row);
        }
    }
    static void insertRequest() throws IOException {
        System.out.print("enter needed book id: ");
        String bookid=input.readLine();
        System.out.print("enter needed lender id: ");
        String lenderid=input.readLine();
        System.out.print("enter your id: ");
        String borrowerid=input.readLine();
        String data2=bookid+":"+lenderid+":"+borrowerid+":pending";
        out.writeUTF(data2);
        String response = inputServer.readUTF();
        System.out.println(response);
    }
    void Notification(){

    }
    static void History() throws IOException, ClassNotFoundException {
        out.writeUTF("REQUEST_HISTORY");
        System.out.print("Enter your ID: ");
        String ID =input.readLine();
        out.writeUTF(ID);

        List<Map<String,Object>> data=(List<Map<String, Object>>) inObject.readObject();
        for (Map<String,Object> row:data){
            System.out.println(row);
        }
    }
}