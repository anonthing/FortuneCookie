import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sidhhu on 7/31/15.
 */
public class BrokerThread extends Thread {

    Socket broker;
    HashMap<SocketAddress, String> registration;

    public BrokerThread (Socket broker, HashMap<SocketAddress, String> registration) {
        System.out.println("Start new thread for broker");
        this.broker = broker;
        this.registration = registration;

    }

    @Override
    public void run() {
        try {
        DataInputStream in = null;
        DataOutputStream out = null;

        // create new input stream if not exists
        in = new DataInputStream(broker.getInputStream());

        // create new output stream if not exists
        out = new DataOutputStream(broker.getOutputStream());

        while (true) {
            String input = in.readUTF();
            String[] split = input.split("\\s");
            if (split[0].equalsIgnoreCase("register")) {
                System.out.println("Got request from server to register");


                handleRegistration(broker.getRemoteSocketAddress(), split[1]);
                out.writeUTF("Registration done");
                //broker.close();
            } else if (split[0].equalsIgnoreCase("unregister")) {
                System.out.println("Got request from server to unregister");
                handleUnregistration(broker.getRemoteSocketAddress(), split[1]);
                out.writeUTF("URegistration done");
                // broker.close();
            } else if (split[0].equalsIgnoreCase("info")) {

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
                } else if (split[1].equalsIgnoreCase("EncryptDecrypt")) {
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

            } else {

            }
        }
    } catch (IOException e) {
        if (broker != null) {
            System.out.println("Client Connection closed" + broker.getRemoteSocketAddress());
        }
        // e.printStackTrace();
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

}
