import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345; // Sunucu portu
    private static Map<String, PrintWriter> clients = new HashMap<>(); // Bağlı oyuncuları takip

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server başlatıldı ve bağlantı bekleniyor...");

        while (true) {
            Socket socket = serverSocket.accept(); // Yeni bir oyuncu bağlandığında
            new ClientHandler(socket).start(); // Her oyuncu için yeni bir thread
        }
    }

    // Oyuncu bağlantılarını yöneten sınıf
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String playerName = in.readLine(); // Oyuncu adını al
                sendMessageToAll(playerName + " bağlandı");
                clients.put(playerName, out); // Oyuncuyu listeye ekle

                // Soruları gönder ve oyuncu yanıtlarını al
                String question = "5 + 3 kaç eder?";
                out.println("Soru:" + question);

                while (true) {
                    String answer = in.readLine();
                    if (answer.equals("8")) { // Doğru cevap kontrolü
                        sendMessageToAll(playerName + " doğru cevap verdi! Arabası ilerliyor.");
                    } else {
                        sendMessageToAll(playerName + " Yanlış cevap, tekrar deneyin.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    clients.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Tüm oyunculara mesaj gönderme
        private void sendMessageToAll(String message) {
            for (PrintWriter client : clients.values()) {
                client.println(message);
            }
        }
    }
}
