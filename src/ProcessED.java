import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Base64;


public class ProcessED extends Thread {
  Socket server;
  ProcessED(Socket server) {
    this.server = server;
  }
  public void run()
  {
    try
    {
    //  System.out.println("Waiting for client on port " +
     //     serverSocket.getLocalPort() + "...");
    //Socket server = serverSocket.accept();
    System.out.println("hey! Just connected to "
        + server.getRemoteSocketAddress());
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
           byte[] output = encryptDecrypt(input);
           
           if(input.charAt(0)=='e' || input.charAt(0)=='E') {
            out.writeUTF("The encrypted code is: " +new String(output));
           }
           if(input.charAt(0)=='d' || input.charAt(0)=='D') {
             out.writeUTF("The decrypted code is: " +new String(output));
           }
           
          // server.close();
       }}
        catch(SocketTimeoutException s)
        {
           System.out.println("Socket timed out!");
        }catch(IOException e)
        {
           e.printStackTrace();
        }
     }
  
  public byte[] encryptDecrypt(String input) {
//  for(int i=1;input[i]!='\0';i++) {
//      temp[i-1]=input[i];
//      i++
//    }
  String temp = input.substring(1, input.length());
  
   if(input.charAt(0)=='e' || input.charAt(0)=='E')  {
      return Base64.getEncoder().encode(temp.getBytes());
   }
   else if(input.charAt(0)=='d' || input.charAt(0)=='D') {
      return Base64.getDecoder().decode(temp.getBytes());
   } else {
     return null;
   }
      
 }
  
}
