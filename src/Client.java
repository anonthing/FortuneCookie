import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client
{
   public static void main(String [] args)
   {
      String serverName = "localhost";
      int port = Integer.parseInt(args[0]);
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
           if (port == 9997) {
           System.out.println("Please enter one of the following to get started Encrypt/Decrypt, close");
           } 
           if (port == 9998) {
             System.out.println("Please enter one of the following to get started FortuneCookie, close");
           }
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
         if (input.equalsIgnoreCase("encrypt") || input.equalsIgnoreCase("decrypt")) {
           String inputToServer = s.nextLine();
           out.writeUTF(inputToServer);
           System.out.println(":" + in.readUTF());
         } 
         if (input.equalsIgnoreCase("close")) {
           client.close();
         }
         } 
         
         //client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}