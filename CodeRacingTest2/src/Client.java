import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {
    private PrintWriter out;
    private BufferedReader in;
    private TextArea raceTrack;
    private TextField answerField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        connectToServer();

        // Ana layout
        HBox mainLayout = new HBox(10);
        mainLayout.setPrefSize(600, 400);

        // Sol kısım: Yarış Pisti
        raceTrack = new TextArea();
        raceTrack.setEditable(false);
        raceTrack.setText("Yarış Pisti:\n"); // Basit bir yarış pisti simülasyonu
        mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, null)));
        VBox leftPane = new VBox(raceTrack);
        leftPane.setPrefWidth(400);

        // Sağ kısım: Soru ve Cevap
        VBox rightPane = new VBox(10);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        Label questionLabel = new Label("5 + 3");
        answerField = new TextField();
        Button submitButton = new Button("Gönder");

        submitButton.setOnAction(e -> sendAnswer());
        rightPane.getChildren().addAll(questionLabel, answerField, submitButton);

        mainLayout.getChildren().addAll(leftPane, rightPane);
        
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345); // Sunucu bağlantısı
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Mesajları dinle ve yarış pistini güncelle
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("Soru:")) {
                            raceTrack.appendText(message + "\n"); // Soru görüntüle
                        } else {
                            raceTrack.appendText(message + "\n"); // Cevaba göre araba ilerlemesi
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswer() {
        String answer = answerField.getText();
        out.println(answer); // Cevabı sunucuya gönder
        answerField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
