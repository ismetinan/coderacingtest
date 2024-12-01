

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class PlayerClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to the host!");
            Scanner scanner = new Scanner(System.in);

            // Initialize GameState
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();
            GameState myState = new GameState(playerName, 0, 0);

            // Thread to listen for updates from the host
            new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        GameState updatedState = GameState.fromJson(message);
                        System.out.println("Update: " + updatedState.getPlayerName() +
                                " is at position " + updatedState.getPosition());
                    }
                } catch (IOException e) {
                    System.err.println("Disconnected from host.");
                }
            }).start();

            while (true) {
                System.out.print("Enter your new position (or -1 to quit): ");
                int newPosition = scanner.nextInt();
            
                // Exit condition
                if (newPosition == -1) {
                    System.out.println("Exiting the game...");
                    break; // Exit the loop
                }
            
                myState.setPosition(newPosition);
                output.println(myState.toJson());
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
    }
}
