package thuipc.same;

public class PlayerMain {
    public static void main(String[] args) {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        player1.setCommunicationPartner(player2);
        player2.setCommunicationPartner(player1);
        player1.setInitiator(true);

        Thread thread1 = new Thread(player1);
        Thread thread2 = new Thread(player2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

