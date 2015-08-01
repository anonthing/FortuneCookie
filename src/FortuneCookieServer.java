import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.*;

public class FortuneCookieServer extends Thread
{
  // server socket on where the server will be serving
  private ServerSocket serverSocket;
  // A tiny little database of where we will be storing our cookins
  List<String> cookies = new ArrayList<String>();
  // A list where we will update the cookies that are sent to the client
  private List<String> sentCookies = new ArrayList<String>();
  
  // Ctor
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
    while(true)
    { 
      Socket server = null;
      try
      {
        System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
        server = serverSocket.accept();
        System.out.println("Just connected to "
            + server.getRemoteSocketAddress());
        DataInputStream in = null ;
        DataOutputStream out = null ;

        if (in == null) { // create new input stream if not exists
          in = new DataInputStream(server.getInputStream());                 
        }
        if (out == null) { // create new output stream if not exists
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
        }}
    catch(SocketTimeoutException s)
    {
      System.out.println("Socket timed out!");
    }catch(IOException e)
    {
      if (server != null) {
      System.out.println("Client Connection closed" +server.getRemoteSocketAddress());
      }
      // e.printStackTrace();
    }}
  }

  /*
   * Function that will be called when client requests for cookies
   * @param number of cookies that the client requested
   * checks if we have enough cookies that the client asked for
   * and pick random cookies from the list of cookies we have
   */
  public String[] sendCookies(int numCookiesRequested) {
    String[] cookiesToBeSent = new String[numCookiesRequested]; 
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

    try
    { 
      // create a thread that will accept the connections and process
      // clients request. 
      // Note that there will be only one thread running so the handling of requests 
      // from client would be sequential and we will not be having concurrency issues.
      Thread t = new FortuneCookieServer(9998);
      t.start();
      Thread nt = new ServerClient(args[0], "FortuneCookie:9998");
      nt.start();
    }catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}
