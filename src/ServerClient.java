import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by sidhhu on 7/28/15.
 */
public class ServerClient extends Thread {
    String brokerName = null;
    Socket client = null;
    String serverName = null;
    public ServerClient(String brokerName, String serverName) {
        this.brokerName = brokerName;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Connecting to " + brokerName
                    + " on port " + 9999);
            client = new Socket(brokerName, 9999);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out =
                    new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
                System.out.println("Enter register or unregister");
            while (true) {
                String input = s.nextLine();
                // Processes Fortune Cookie logic
                if (input.equalsIgnoreCase("register") || input.equalsIgnoreCase("unregister")) {


                    // String inputToServer = s.nextLine();

//                    if (inputToServer.equalsIgnoreCase("close")) {
//                        client.close();
//                        System.exit(0);
//                    } else {
                    System.out.println("Writing " + input + " " + serverName);
                    out.writeUTF(input + " " + serverName);

                    System.out.println(in.readUTF());

                } else if (input.equalsIgnoreCase("close")) {
                    out.writeUTF(input);
                    client.close();
                    System.exit(0);
                } else {
                    /* Err. Do nothing */
                }
            }
            } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
