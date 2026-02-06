import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tip_calculator";
    private static final String DB_USER = "root"; // Veritabanı kullanıcı adınız
    private static final String DB_PASSWORD = "2rpk2tvh"; // Veritabanı şifreniz

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
