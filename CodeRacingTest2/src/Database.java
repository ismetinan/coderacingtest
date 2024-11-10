import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:game.db"; // SQLite veritabanı dosyası

    // Veritabanına bağlanma metodu
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Veritabanına bağlanıldı.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Answers tablosunu oluşturma metodu
    public static void createAnswersTable() {
        String createAnswersTable = "CREATE TABLE IF NOT EXISTS answers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "playerName TEXT NOT NULL,"
                + "question TEXT NOT NULL,"
                + "isCorrect BOOLEAN,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createAnswersTable);
            System.out.println("Answers tablosu oluşturuldu.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
