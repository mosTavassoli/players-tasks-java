// Player.java
package thuipc.separate;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Player class represents a participant in the message exchange game.
 * It can act as both a server to receive messages and a client to send messages.
 */
public class Player implements Runnable {
    private final String name;
    private final String partnerHost;
    private final int partnerPort;
    private final int myPort;
    private final AtomicInteger messageCounter;
    private boolean isInitiator;

    public Player(String name, String partnerHost, int partnerPort, int myPort) {
        this.name = name;
        this.partnerHost = partnerHost;
        this.partnerPort = partnerPort;
        this.myPort = myPort;
        this.messageCounter = new AtomicInteger(0);
    }

    public void setInitiator(boolean isInitiator) {
        this.isInitiator = isInitiator;
    }

    public void sendMessage(String message) {
        try (Socket socket = new Socket(partnerHost, partnerPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println(name + " sending: " + message);
            out.println(message);
            messageCounter.incrementAndGet();
            if (isInitiator) {
                // Wait for response
                String response = in.readLine();
                System.out.println(name + " received: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(myPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String receivedMessage = in.readLine();
                    if (receivedMessage != null) {
                        System.out.println(name + " received: " + receivedMessage);
                        if (!isInitiator) {
                            String responseMessage = receivedMessage + " - Response " + messageCounter.incrementAndGet();
                            System.out.println(name + " sending: " + responseMessage);
                            out.println(responseMessage);
                        }
                        if (messageCounter.get() >= 10) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (isInitiator) {
            for (int i = 0; i < 10; i++) {
                sendMessage("Message " + (i + 1));
            }
            // Initiator should stop after sending and receiving 10 messages.
            Thread.currentThread().interrupt();
        } else {
            startServer();
        }
    }
}