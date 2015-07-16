import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client
{
   public static void main(String [] args)
   {
      String serverName = "localhost";
      int port = 9997;
      try
      {
        
         System.out.println("Connecting to " + serverName
                             + " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to "
                      + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out =
                       new DataOutputStream(outToServer);
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
         Scanner s = new Scanner(System.in);
         while(true) {
           System.out.println("Please enter one of the following to get started, Fortune Cookie, Encrypt/Decrypt, close");
         String input = s.nextLine();
         if (input.equals("close")) {
           client.close();
           System.exit(0);
         }
         if (input.equalsIgnoreCase("Fortune Cookie")) {
           System.out.println("Please enter the number of cookie you want");
          String inputToServer = s.nextLine();
         
         

         out.writeUTF(inputToServer);
         
         System.out.println(":" + in.readUTF());
         }
         if (input.contains("encrypt")) {
           System.out.println("Sorry, not implemented here");
           String inputToServer = s.nextLine();
           out.writeUTF(inputToServer);
           System.out.println(":" + in.readUTF());
         }
         } 
         
         //client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}