import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345; // Sunucu portu
    private static Map<String, PrintWriter> clients = new HashMap<>(); // Bağlı oyuncuları takip

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Database.createAnswersTable();
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
            try {
                // Socket'ten gelen giriş ve çıkış akışlarını başlatma
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("I/O stream başlatılamadı: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                

                // 2. Örnek cevaplar ekle
                String playerName = in.readLine();
                String question = in.readLine();
                AnswerDAO.addAnswer(playerName, question, true);
                out.write("Cevap kaydedildi.");
                out.flush();
                // Veritabanı işlemlerinin tamamlandığını göster
                System.out.println("Örnek veriler eklendi.");
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sendMessageToAll(playerName + " bağlandı");
                clients.put(playerName, out); // Oyuncuyu listeye ekle

                // Soruları gönder ve oyuncu yanıtlarını al
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
