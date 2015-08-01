import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client
{
  public static void main(String [] args)
  {
    String serverName = args[0];
    Socket client = null;

    // int port = Integer.parseInt(args[0]);
    try
    {


      Scanner s = new Scanner(System.in);
      while(true) {
        OutputStream outToServer ;
        DataOutputStream out ;
        InputStream inFromServer ;
        DataInputStream in ;
        System.out.println("Please enter 'Fortune Cookie' for Fortune Cookie Server or "
            + "enter 'encrypt' or 'decrypt' EncryptDcrypt Server or close");
        String input = s.nextLine();
        // Processes Fortune Cookie logic
        if (input.equalsIgnoreCase("Fortune Cookie")) {
          System.out.println("Requesting broker  for Fortune Cookie server info ");
          client = new Socket(serverName, 9999);
          System.out.println("Just connected to "
                  + client.getRemoteSocketAddress());
          outToServer = client.getOutputStream();
          out =
                  new DataOutputStream(outToServer);
          inFromServer = client.getInputStream();
          in =
                  new DataInputStream(inFromServer);
          out.writeUTF("info FortuneCookie");
          String receivedFromBroker = in.readUTF();
          System.out.println(receivedFromBroker);
          if (!receivedFromBroker.equalsIgnoreCase("Sorry, Can't find Server")) {
            String[] split = receivedFromBroker.split("\\s");

            // Now connect to Real Server
            System.out.println("Connecting to Fortune Cookie Server");
            client = new Socket(split[0], Integer.parseInt(split[1]));
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            outToServer = client.getOutputStream();
            out =
                    new DataOutputStream(outToServer);
            inFromServer = client.getInputStream();
            in =
                    new DataInputStream(inFromServer);

            System.out.println("Please enter the number of cookie you want");
            String inputToServer = s.nextLine();

            if (inputToServer.equalsIgnoreCase("close")) {
              client.close();
              System.exit(0);
            } else {

              out.writeUTF(inputToServer);

              System.out.println(":" + in.readUTF());
              client.close();
            }
          }
        }
        // Processes Encrypt Decrypt logic
        if (input.equalsIgnoreCase("encrypt") || input.equalsIgnoreCase("decrypt")) {

          System.out.println("Requesting broker  for EncryptDecrypt server info ");
          client = new Socket(serverName, 9999);
          System.out.println("Just connected to "
                  + client.getRemoteSocketAddress());
          outToServer = client.getOutputStream();
          out =
                  new DataOutputStream(outToServer);
          inFromServer = client.getInputStream();
          in =
                  new DataInputStream(inFromServer);
          out.writeUTF("info EncryptDecrypt");
          String receivedFromBroker = in.readUTF();
          System.out.println(receivedFromBroker);
          if (!receivedFromBroker.contains("Sorry")) {
            System.out.println("We can't do anything");
          }
          if (!receivedFromBroker.contains("Sorry")) {
            String[] split = receivedFromBroker.split("\\s");

            // Now connect to Real Server

            System.out.println("Connecting to " + serverName
                    + " on port " + 9997);
            client = new Socket(split[0], Integer.parseInt(split[1]));
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());

            outToServer = client.getOutputStream();
            out =
                    new DataOutputStream(outToServer);
            inFromServer = client.getInputStream();
            in =
                    new DataInputStream(inFromServer);
            while (true) {
              System.out.println("Enter your string that needs to be encrypted/ decrypted. \n"
                      + "For encryption prefix your string with e \n"
                      + "For decryption prefix your string with d");
              String inputToServer = s.nextLine();
              if (inputToServer.equalsIgnoreCase("close")) {
                client.close();
                break;
              } else {
                out.writeUTF(inputToServer);
                System.out.println(":" + in.readUTF());
              }
            }
          }
        }
        if (input.equalsIgnoreCase("close")) {
          if (client != null) {
            client.close();
            System.exit(0);
          }
          System.exit(0);
        }
      } 

      //client.close();
    }catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}
