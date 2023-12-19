import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class giris extends JFrame{
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton girişButton;
    private JPanel panel4;

    public giris() {
        add(panel4);
        setSize(500,500);
        setTitle("Giriş Ekranı");
        girişButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisKontrol();
            }
        });
    }
    private Connection baglantiyiKontrolEt() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/kutuphane", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanına bağlanırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }
    private void girisKontrol() {
        Connection connection = baglantiyiKontrolEt();

        if (connection != null) {
            try {
                String kullaniciAdi = textField1.getText();
                String sifre = new String(passwordField1.getPassword());

                // Veritabanında kullanıcı adı ve şifreye karşılık gelen kaydı sorgula
                String sql = "SELECT * FROM giris WHERE kullaniciAdi = ? AND sifre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, kullaniciAdi);
                preparedStatement.setString(2, sifre);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Eğer kayıt bulunursa giriş başarılı mesajı ver
                    anasayfa kg = new anasayfa();
                    kg.show();
                } else {
                    // Kayıt bulunamazsa giriş başarısız mesajı ver
                    JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı", "Hata", JOptionPane.ERROR_MESSAGE);
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanında sorgu yapılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            giris girisFrame = new giris();
            girisFrame.setVisible(true);
        });
    }

}


