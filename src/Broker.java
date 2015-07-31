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
    private HashMap<SocketAddress, String> registration= new HashMap<SocketAddress, String >();
    int THREADS_USED = 0;
    Thread[] threads = new Thread[20];
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
                if (THREADS_USED<20) {
                    threads[THREADS_USED] = new BrokerThread(broker, registration);
                    threads[THREADS_USED].start();
                    THREADS_USED++;
                }
                } catch (Exception e) {
                e.printStackTrace();
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
