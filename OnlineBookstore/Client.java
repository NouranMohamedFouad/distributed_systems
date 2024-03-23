import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static  DataInputStream input = null; 
    private static DataOutputStream out = null; 
	public static void main(String[] args){
        try{
            Socket socket=new Socket("localhost", 1234);
            input = new DataInputStream(System.in); 
            // sends output to the socket 
            out = new DataOutputStream(socket.getOutputStream()); 
            Register();
            //String registerInfo = input.readLine();
            //System.out.println(registerInfo);
            socket.close();
        }catch(Exception e){System.out.println(e);}
    }

    void Login(){

    }
    static void Register() throws IOException{
        System.out.print("Enter your name: ");
        String name = input.readLine();
        System.out.print("Enter username: ");
        String un = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        //System.out.println("register:" + name + ":" + un + ":" + password);
        String Data=name +":" + un + ":" + password;
        out.writeUTF(Data); 
    }
    void BrowseBooks(){

    }
    void AddBook(){

    }
    void RemoveBook(){

    }
    void BorrowBook(){

    }
    void Notification(){

    }
    void History(){

    }
}
