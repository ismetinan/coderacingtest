package network;

import game.GameState;
import java.io.*;
import java.net.*;
import java.util.*;

public class Host {
    private static final int PORT = 12345;
    private final List<PlayerHandler> players = new ArrayList<>();

    public static void main(String[] args) {
        GameState state = new GameState("Player1", 0, 0);
        System.out.println("GameState: " + state.toJson());
        new Host().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Host started. Waiting for players...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player connected: " + clientSocket.getInetAddress());
                PlayerHandler playerHandler = new PlayerHandler(clientSocket);
                players.add(playerHandler);
                new Thread(playerHandler).start(); // Handle each player in a separate thread
            }
        } catch (IOException e) {
            System.err.println("Error starting host: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void broadcast(String message, PlayerHandler sender) {
        for (PlayerHandler player : players) {
            if (player != sender) { // Don't send the message back to the sender
                player.sendMessage(message);
            }
        }
    }

    private class PlayerHandler implements Runnable {
        private final Socket socket;
        private PrintWriter output;
        private BufferedReader input;

        public PlayerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = input.readLine()) != null) {
                    System.out.println("Received from player: " + message);

                    // Broadcast the message to all players
                    broadcast(message, this);
                }
            } catch (IOException e) {
                System.err.println("Player disconnected: " + e.getMessage());
            } finally {
                closeConnection();
            }
        }

        public void sendMessage(String message) {
            if (output != null) {
                output.println(message);
            }
        }

        private void closeConnection() {
            try {
                socket.close();
                players.remove(this);
                System.out.println("Player disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
