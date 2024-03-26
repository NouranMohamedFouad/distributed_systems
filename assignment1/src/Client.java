import java.io.*;
import java.net.*;

public class Client {
    private static  DataInputStream input = null;
    private static DataOutputStream out = null;
    public static void main(String[] args){
        try{
            Socket socket=new Socket("localhost", 1234);
            input = new DataInputStream(System.in);
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            //Register();
            //AddBook();
            RemoveBook();
            socket.close();
        }catch(Exception e){System.out.println(e);}
    }

    static void Register() throws IOException{
        System.out.print("Enter your name: ");
        String name = input.readLine();
        System.out.print("Enter username: ");
        String un = input.readLine();
        System.out.print("Enter password: ");
        String password = input.readLine();
        String Data="1:"+name +":" + un + ":" + password;
        out.writeUTF(Data);
    }
    /////////////////////////////////////////////////////////////////////////////
    void Login(){

    }
    /////////////////////////////////////////////////////////////////////////////

    static void BrowseBooks(){

    }
    /////////////////////////////////////////////////////////////////////////////

    static void AddBook() throws IOException {
        System.out.print("Enter book name: ");
        String name = input.readLine();
        System.out.print("Enter your id: ");
        String clientID = input.readLine();
        String Data="6:"+name +":" + clientID + ":lend";
        out.writeUTF(Data);
    }
    /////////////////////////////////////////////////////////////////////////////
    static void RemoveBook() throws IOException {
        System.out.print("Enter book name: ");
        String bookName = input.readLine();
        System.out.print("Enter your id: ");
        String clientID = input.readLine();
        String Data="7:"+bookName +":" + clientID + ":lend";
        out.writeUTF(Data);

    }
    /////////////////////////////////////////////////////////////////////////////

    static void BorrowBook(){

    }
    /////////////////////////////////////////////////////////////////////////////

    static void Notification(){

    }
    /////////////////////////////////////////////////////////////////////////////

    static void History(){

    }
    /////////////////////////////////////////////////////////////////////////////
}
