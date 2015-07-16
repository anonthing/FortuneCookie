import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.io.*;

public class EncryptDecryptServer extends Thread
{
   private ServerSocket serverSocket;
   HashMap<Thread, Integer> threadsUsed = new HashMap<Thread, Integer>();
   int i=0;
   ArrayList<Socket> socks = new ArrayList<Socket>();
   Thread[] threads = new Thread[50];
   public EncryptDecryptServer() throws IOException
   {
     if(serverSocket==null) {
      serverSocket = new ServerSocket(9997);
    //  fillThreads();
      waitForConnections(serverSocket);
     }
      
      
   }
//   public void fillThreads () {
//     System.out.println("hello" +threads);
//     for (int i=0;i<50;i++) {
//       try {
//        threads[i] = new EncryptDecryptServer();
//      } catch (IOException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//       System.out.println("here" +threads[i]);
//       threadsUsed.put(threads[i], 0);
//     }
//   }
   public void waitForConnections(ServerSocket serverSocket) throws IOException {
     Socket server = new Socket();
     server = serverSocket.accept();
     socks.add(server);
     if (i<20) {
     threads[i] = new ProcessED(server);
     threads[i].start();
     i++;
    waitForConnections(serverSocket);
     }
//     if (!threadsUsed.containsKey(1)) {
//       System.out.println("next here");
//       Boolean flag = false;
//       System.out.println(threadsUsed);
//       for (Entry<Thread, Integer> entry : threadsUsed.entrySet()) {
//        System.out.println("here");
//         if (entry.getValue() == 0 && !flag) {
//           threadsUsed.put(entry.getKey(), 1);
//           entry.getKey().start();
//           flag = true;
//         }
//       }
//     }
     
     
   }

   public void run()
   {
     try
     {
     //  System.out.println("Waiting for client on port " +
      //     serverSocket.getLocalPort() + "...");
     //Socket server = serverSocket.accept();
     //System.out.println("Just connected to "
      //   + server.getRemoteSocketAddress());
     DataInputStream in = null ;
     DataOutputStream out = null ;
      while(true)
      {      
            if (in == null) {
            in = new DataInputStream(socks.get(socks.size()-1).getInputStream());                 
            }
            if (out == null) {
              out = new DataOutputStream(socks.get(socks.size()-1).getOutputStream());
              }
            String input = in.readUTF();
            
            int output = encryptDecrypt(input);
          
            
            if(input.charAt(0)=='e' || input.charAt(0)=='E') 
             out.writeUTF("The encrypted code is: " +output);
            if(input.charAt(0)=='d' || input.charAt(0)=='D') 
              out.writeUTF("The decrypted code is: " +output);
            
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

   
   public int encryptDecrypt(String input) {
//    for(int i=1;input[i]!='\0';i++) {
//        temp[i-1]=input[i];
//        i++
//      }
    String temp = input.substring(1, input.length());
    
     if(input.charAt(0)=='e' || input.charAt(0)=='E')  {
        return temp.hashCode();
     }
     else if(input.charAt(0)=='d' || input.charAt(0)=='D') {
        return temp.hashCode();
     } else {
       return 0;
     }
        
   }
   
   
   public static void main(String [] args)
   {
   
      //int port = Integer.parseInt(args[0]);
      try
      {
         EncryptDecryptServer eds = new EncryptDecryptServer();
      //   t1.start();
     
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}