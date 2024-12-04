import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class Host {
    public static void main(String[] args) throws Exception {
        // Create and start the HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/update", new UpdateHandler()); // Map the "/update" endpoint
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server started on port 8080...");
    }

    // Handler for /update endpoint
    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Read the incoming request (expected to be JSON)
            InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                requestBody.append(line);
            }

            // Parse the incoming JSON data into a GameState object
            GameState gameState = GameState.fromJson(requestBody.toString());
            System.out.println("Received player state: " + gameState.getPlayerName() + " at position " + gameState.getPosition());

            // Send a response back to the client
            String response = "Player state received: " + gameState.getPlayerName() + " at position " + gameState.getPosition();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
