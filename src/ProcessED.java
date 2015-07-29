import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

// this class will run, when the encryptdecrypt server spawns out a new thread

public class ProcessED extends Thread {
  Socket server;
  ProcessED(Socket server) {
    this.server = server;
  }
  
  // this will do the processing work and interact with the client
  public void run()
  {
   
    try
    {
      
    //  System.out.println("Waiting for client on port " +
     //     serverSocket.getLocalPort() + "...");
    //Socket server = serverSocket.accept();
  //  System.out.println("hey! Just connected to "
   //     + server.getRemoteSocketAddress());
    DataInputStream in = null ;
    DataOutputStream out = null ;
    while(true)
    {
           if (in == null) {
           in = new DataInputStream(server.getInputStream());                 
           }
           if (out == null) {
             out = new DataOutputStream(server.getOutputStream());
             }
           String input = in.readUTF();
           System.out.println("Got request from client to process(encrypt / decrypt) " +input);
           String output = encryptDecrypt(input);
           
           if(input.charAt(0)=='e' || input.charAt(0)=='E') {
            out.writeUTF("The encrypted code is: " +new String(output));
           }
           else if(input.charAt(0)=='d' || input.charAt(0)=='D') {
             out.writeUTF("The decrypted code is: " +new String(output));
           } else {
             out.writeUTF("Cannot process, please prefix your string with e or d (encrypr or decrypt)");
           }
           
          // server.close();
       }}
        catch(SocketTimeoutException s)
        {
           System.out.println("Socket timed out!");
        }catch(IOException e)
        {
          System.out.println("Connection closed");
          // e.printStackTrace();
        }
     }
  
  // strip out the first char in the string, coz we know we need 
 //  e or d to encrypt or decrypt
  public String encryptDecrypt(String input) {

  String temp = input.substring(1, input.length());
  
   if(input.charAt(0)=='e' || input.charAt(0)=='E')  {
     System.out.println("Encrypting string " +temp);
     return new String(DatatypeConverter.printBase64Binary(new String(temp).getBytes()));
     // return Base64.getEncoder().encode(temp.getBytes());
   }
   else if(input.charAt(0)=='d' || input.charAt(0)=='D') {
     System.out.println("Decrypting string " +temp);
      return new String(DatatypeConverter.parseBase64Binary(temp));
   } else {
     return "Cannot Process, Please prefix your string with e or d";
   }
      
 }
  
}
