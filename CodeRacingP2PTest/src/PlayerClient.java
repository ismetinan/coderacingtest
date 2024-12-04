import java.io.*;
import java.net.*;
import java.util.Scanner;

public class PlayerClient {
    public static void main(String[] args) {
        try {
            // Create a URI object for localhost and convert to URL
            URI uri = new URI("http://localhost:8080/update"); // Localhost address
            URL url = uri.toURL(); // Convert URI to URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Initialize game state and send to the server
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();
            GameState myState = new GameState(playerName, 0, 0);

            // Send the JSON data to the server
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = myState.toJson().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the server response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = in.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Server response: " + response.toString());
            }

        } catch (IOException | URISyntaxException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
