import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnswerDAO {

    // Yeni bir cevap ekleme metodu
    public static void addAnswer(String playerName, String question, boolean isCorrect) {
        String sql = "INSERT INTO answers(playerName, question, isCorrect) VALUES(?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, question);
            pstmt.setBoolean(3, isCorrect);
            pstmt.executeUpdate();
            System.out.println(playerName + " adlı oyuncunun cevabı eklendi.");
        } catch (SQLException e) {
            System.out.println("SQL hatası: " + e.getMessage());
        }
    }
}
