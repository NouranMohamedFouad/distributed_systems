import java.io.*;
import java.net.*;

public class Admin{
    private static  DataInputStream input = null;
    private static DataOutputStream out = null;
    public static void main(String[] args){
        try{
            Socket socket=new Socket("localhost", 1234);
            input = new DataInputStream(System.in);
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            manageBooks();
            socket.close();
        }catch(Exception e){System.out.println(e);}
    }


    static void manageBooks() throws IOException{
        System.out.print("Enter book title: ");
        String title = input.readLine();
        System.out.print("Enter author: ");
        String author = input.readLine();
        System.out.print("Enter genre: ");
        String genre = input.readLine();
        System.out.print("Enter price: ");
        String price = input.readLine();
        System.out.print("Enter quantity: ");
        String quantity =input.readLine();
        String Data="3:"+title +":" + author + ":" + genre+ ":" + price+ ":" + quantity;
        out.writeUTF(Data);
    }

}
