// PlayerServer.java
package thuipc.separate;

/**
 * The PlayerServer class is responsible for starting a player instance.
 * It determines whether the player is an initiator based on the name provided.
 */
public class PlayerServer {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: java com.example.PlayerServer <name> <partnerHost> <partnerPort> <myPort>");
            System.exit(1);
        }

        String name = args[0];
        String partnerHost = args[1];
        int partnerPort = Integer.parseInt(args[2]);
        int myPort = Integer.parseInt(args[3]);

        Player player = new Player(name, partnerHost, partnerPort, myPort);
        if (name.equalsIgnoreCase("Player1")) {
            player.setInitiator(true);
        }

        new Thread(player).start();
    }
}
