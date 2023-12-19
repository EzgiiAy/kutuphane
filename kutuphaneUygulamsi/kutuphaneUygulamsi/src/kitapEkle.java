import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class kitapEkle  extends JFrame{
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField8;
    private JTextField kitapAdıTextField;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton kitapEkleButton;
    private JPanel panel1;
    // End of variables declaration//GEN-END:variables


    public kitapEkle() {
        add(panel1);
        setSize(500,500);
        setTitle("KİTAP EKLEME SAYFASI");
        kitapEkleButton.addActionListener(new ActionListener() {
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
    // Lambda fonksiyonları burada uygulanmıştır.
    private void veriTabaninaEkle() {
        try {
            Connection connection = baglantiyiKontrolEt();

            if (connection != null) {
                String kitapAdi = kitapAdıTextField.getText();
                String kitapYazari = textField1.getText();
                String kategori = textField2.getText();
                String dili = textField3.getText();
                String yayinevi = textField4.getText();
                String rafNo = textField5.getText();
                String acıklama = textField6.getText();


                String sql = "INSERT INTO kitapekle (kitapAdi, kitapYazari,kategori,dili,yayinevi,rafNo,acıklama) VALUES (?, ?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, kitapAdi);
                preparedStatement.setString(2, kitapYazari);
                preparedStatement.setString(3, kategori);
                preparedStatement.setString(4, dili);
                preparedStatement.setString(5, yayinevi);
                preparedStatement.setString(6, rafNo);
                preparedStatement.setString(7, acıklama);






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
            kitapEkle ex = new kitapEkle();
            ex.setVisible(true);
        });
    }

}
