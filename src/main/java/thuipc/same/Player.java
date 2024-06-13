package thuipc.same;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Runnable {
    private final String name;
    private final BlockingQueue<String> receivedMessages = new LinkedBlockingQueue<>();
    private Player communicationPartner;
    private final AtomicInteger messageCounter = new AtomicInteger(0);
    private boolean isInitiator;

    public Player(String name) {
        this.name = name;
    }

    public void setCommunicationPartner(Player partner) {
        this.communicationPartner = partner;
    }

    public void setInitiator(boolean isInitiator) {
        this.isInitiator = isInitiator;
    }

    public void sendMessage(String message) {
        System.out.println(name + " sending: " + message);
        communicationPartner.receiveMessage(message);
        messageCounter.incrementAndGet();
    }

    public void receiveMessage(String message) {
        try {
            receivedMessages.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        if (isInitiator) {
            for (int i = 0; i < 10; i++) {
                sendMessage("Message " + (i + 1));
                try {
                    Thread.sleep(500); // simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            while (messageCounter.get() < 10) {
                try {
                    String receivedMessage = receivedMessages.take();
                    System.out.println(name + " received: " + receivedMessage);
                    String responseMessage = receivedMessage + " - Response " + messageCounter.get();
                    sendMessage(responseMessage);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

