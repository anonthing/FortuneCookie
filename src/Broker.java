import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sidhhu on 7/28/15.
 */
public class Broker extends Thread {

    // server socket on where the server will be serving
    private ServerSocket brokerSocket;
    private Map<SocketAddress, String> registration= new HashMap<SocketAddress, String >();
    public Broker() {
        try {
            brokerSocket = new ServerSocket(9999);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            Socket broker = null;
            try {
                System.out.println("Waiting for client on port " +
                        brokerSocket.getLocalPort() + "...");
                broker = brokerSocket.accept();
                System.out.println("Just connected to "
                        + broker.getRemoteSocketAddress());
                        DataInputStream in = null;
                DataOutputStream out = null;

               // create new input stream if not exists
                    in = new DataInputStream(broker.getInputStream());

               // create new output stream if not exists
                    out = new DataOutputStream(broker.getOutputStream());


                String input = in.readUTF();
                String[] split = input.split("\\s");
                if (split[0].equalsIgnoreCase("register")) {
                    System.out.println("Got request from server to register");


                    handleRegistration(broker.getRemoteSocketAddress(), split[1]);
                    out.writeUTF("Registration done");
                    broker.close();
                }
                if (split[0].equalsIgnoreCase("unregister")) {
                    System.out.println("Got request from server to unregister");
                    handleUnregistration(broker.getRemoteSocketAddress(), split[1]);
                    out.writeUTF("URegistration done");
                    broker.close();
                }
                if (split[0].equalsIgnoreCase("info")) {

                    if (split[1].equalsIgnoreCase("FortuneCookie")) {
                        if (registration.size() < 1) {
                            out.writeUTF("Sorry, Can't find Server");
                        } else {
                            for (Map.Entry<SocketAddress, String> entry : registration.entrySet()) {
                                if (entry.getValue().contains("FortuneCookie")) {

                                    System.out.println(entry.getKey().toString().split(":")[0].substring(1) + " " + entry.getValue().split(":")[1]);
                                    out.writeUTF(entry.getKey().toString().split(":")[0].substring(1) + " " + entry.getValue().split(":")[1]);
                                }
                            }
                        }
                    }
                    if (split[1].equalsIgnoreCase("EncryptDecrypt")) {
                        if (registration.size() < 1) {
                            out.writeUTF("Sorry, Can't find Server");
                        } else {
                            for (Map.Entry<SocketAddress, String> entry : registration.entrySet()) {
                                if (entry.getValue().contains("EncryptDecrypt")) {
                                    out.writeUTF(entry.getKey().toString().split(":")[0].substring(1) + " " + entry.getValue().split(":")[1]);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                if (broker != null) {
                    System.out.println("Client Connection closed" + broker.getRemoteSocketAddress());
                }
                // e.printStackTrace();
            }
        }
    }

    public void handleRegistration(SocketAddress input, String serviceName) {
        registration.put(input, serviceName);
        System.out.println("Registration map has" +registration);
    }
    public void handleUnregistration(SocketAddress input, String serviceName) {
        registration.remove(input);
        System.out.println("Registration map has" + registration);
    }
    public static void main(String args[]) {
        Thread t = new Broker();
        t.start();
    }
}
