import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FortuneCookieServer extends Thread
{
   private ServerSocket serverSocket;
   private int[] cookies = new int[50];
   private List<Integer> sentCookies = new ArrayList<Integer>();
   public FortuneCookieServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(10000);
      for(int i=0;i<cookies.length;i++) {
        cookies[i] = i;
      }
      
   }

   public void run()
   {
      while(true)
      { 
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
          //  System.out.println(in.readUTF());
            int numCookiesRequested = Integer.parseInt(in.readUTF());
            int[] output = sendCookies(numCookiesRequested);
            System.out.println(output[0]);
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("Here are your cookies" +output);
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public int[] sendCookies(int numCookiesRequested) {
     int[] cookiesToBeSent = new int[numCookiesRequested];
     for (int i = 0; i< numCookiesRequested; i++) {
       if (!sentCookies.contains(cookies[i])) {
       cookiesToBeSent[i] = cookies[i];
       sentCookies.add(cookies[i]);
     } else {
       //TODO try a different cookie
     }
     }
     return cookiesToBeSent;
   }
   public static void main(String [] args)
   {
   
    
   
      //int port = Integer.parseInt(args[0]);
      try
      {
         Thread t = new FortuneCookieServer(9998);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}