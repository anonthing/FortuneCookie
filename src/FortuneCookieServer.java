import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.*;

public class FortuneCookieServer extends Thread
{
   private ServerSocket serverSocket;
   List<String> cookies = new ArrayList<String>();
   private List<String> sentCookies = new ArrayList<String>();
   public FortuneCookieServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      // Creates the cookies
      for (int i=0;i<100;i++) {
        cookies.add("Cookie#"+i);
      }
      
   }

   public void run()
   {
     try
     {
       System.out.println("Waiting for client on port " +
           serverSocket.getLocalPort() + "...");
     Socket server = serverSocket.accept();
     System.out.println("Just connected to "
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
            System.out.println("Got request to give " +input+ " number of cookies");
            int numCookiesRequested = Integer.parseInt(input);
            String[] output = sendCookies(numCookiesRequested);
            if (output[0].equals("-1")) {
              out.writeUTF("Sorry, no cookies for you!!");
            } else {
            System.out.println(Arrays.toString(output));
            
            out.writeUTF("Here are your cookies" +Arrays.toString(output));
            System.out.println("Number of cookie remaining are :" +cookies.size());
            //server.close();
         }}}
         catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
         }catch(IOException e)
         {
           System.out.println("Client Connection closed");
           // e.printStackTrace();
         }
      }

   
   public String[] sendCookies(int numCookiesRequested) {
     String[] cookiesToBeSent = new String[numCookiesRequested]; //TODO handle a few obvious error cases
     for (int i = 0; i< numCookiesRequested; i++) {
           Random r = new Random();
           if (cookies.size() > numCookiesRequested) {
           int randomint = r.nextInt(cookies.size());
           cookiesToBeSent[i] = cookies.get(randomint);
           sentCookies.add(cookies.get(randomint));
           cookies.remove(randomint);
       } else {
         cookiesToBeSent[0] = "-1";
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