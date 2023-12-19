import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private Connection conn = null;
    private Statement st = null;
    private String url = "jdbc:mysql://localhost:3306/"; // Port numarasını belirtin
    private String dbName = "kutuphane";
    private String userName = "root";
    private String pw = "";
    private String driver = "com.mysql.cj.jdbc.Driver";

    public Statement baglan() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url + dbName, userName, pw);
            st = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Bağlanma Hatası: " + e.getMessage());
        }
        return st;
    }

    public static void main(String[] args) {
        DB db = new DB();
        db.baglan();
    }
}
