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
   // this stores the thread that we use to process the request of the client
   // this essentially becomes a threadpool of threads used and whenever client closes
   // the connection, the entry will be removed from this pool.
   // Since we know we use 20 threads, if a new request comes and the pool has
   // less than 20, then we are good. 
   HashMap<Socket, Thread> threadsUsed = new HashMap<Socket, Thread>();
   // Counter so that we will know that we use 20 threads to the max
   int THREADS_USED=0;
   // a list of all socket connections we accept from our clients
   ArrayList<Socket> socks = new ArrayList<Socket>();
   Thread[] threads = new Thread[20];
   
   // Ctor
   public EncryptDecryptServer() throws IOException
   {
     if(serverSocket==null) {
      serverSocket = new ServerSocket(9997);
    //  fillThreads();
      waitForConnections(serverSocket);
     }
      
      
   }
   
   /*
    * This the the method, that waits for clients, and accepts the connections
    * Once connected, it will spawn of a new thread that will do the process 
    * based on clients request. Once the client ends the connection, that thread
    * can be used for a different client.
    */
   public void waitForConnections(ServerSocket serverSocket) throws IOException {
     Socket server = new Socket();
     server = serverSocket.accept();
     socks.add(server);
     // if we have less than 20 in our used thread poll then we can server
     if (threadsUsed.size() < 20) {
       // if the user threads are less than 20, then we are ok to create new threads
       if (THREADS_USED<20) {
     threads[THREADS_USED] = new ProcessED(server);
     threads[THREADS_USED].start();
     threadsUsed.put(server, threads[THREADS_USED]);
     THREADS_USED++;
     
     } else {
     // else, we need to find a thread that is already created and not in the threadsUsed
       // once we get any such, then put it into threadsUsed
       int usable = Integer.MAX_VALUE;
       for (int i=0; i< threads.length; i++) {
         if (!threadsUsed.values().contains(threads[i])) {
           usable = i;
         }
       }
       threads[usable] = new ProcessED(server);
       threadsUsed.put(server, threads[usable]);
     }
     
    waitForConnections(serverSocket);
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