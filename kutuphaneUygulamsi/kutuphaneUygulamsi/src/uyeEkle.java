import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class uyeEkle extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton üyeEkleButton;
    private JPanel panel2;

    public uyeEkle() {
     add(panel2);
     setSize(500,500);
     setTitle("Üye Ekleme Sayfası");
        üyeEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                veriTabaninaEkle();
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

    private void veriTabaninaEkle() {
        try {
            Connection connection = baglantiyiKontrolEt();

            if (connection != null) {
                String okulNo = textField1.getText();
                String adi = textField2.getText();
                String soyadi = textField3.getText();
                String telNo = textField4.getText();



                String sql = "INSERT INTO uyeekle (okulNo, adi,soyadi,telNo) VALUES (?, ?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, okulNo);
                preparedStatement.setString(2, adi);
                preparedStatement.setString(3, soyadi);
                preparedStatement.setString(4, telNo);
               






                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Veri başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Veri eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

                preparedStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanına veri eklerken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uyeEkle ex = new uyeEkle();
            ex.setVisible(true);
        });
    }
}
