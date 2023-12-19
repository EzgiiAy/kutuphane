import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class anasayfa extends JFrame {
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton üyeAraButton;
    private JTextField textField3;
    private JButton kitapAraButton;
    private JTextField textField4;
    private JButton kaydetButton;
    private JPanel panel3;
    private JTextField textField5;
    private JButton kitapEkleButton;
    private JButton üyeEkleButton;
    private JTable table2;

    private DefaultTableModel tableModel;


    public anasayfa() {
        add(panel3);
        setSize(1000,1000);
        setTitle("Kitap Ödünç Verme Sayfası");
        tabloyuDoldur();
        üyeAraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okulNoKontrolEt();
                teslimAlKontrol();
            }
        });
        kitapAraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rafNoKontrolEt();
            }
        });
        kaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kitapAlisEkle();

            }
        });

        üyeEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uyeEkle kg = new uyeEkle();
                kg.show();
            }
        });
        kitapEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                kitapEkle kg = new kitapEkle();
                kg.show();
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

    // Miras metodu uygulanmıştır.
    public void UyeKontrols(JTextField textField1) {
        this.textField1 = textField1;
    }
    public void okulNoKontrolEt() {
        Connection connection = baglantiyiKontrolEt();

        if (connection != null) {
            try {
                String okulNo = textField1.getText();

                // Veritabanında okulNo'ya karşılık gelen ad soyadı sorgula
                String sql = "SELECT adi,soyadi FROM uyeekle WHERE okulNo = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, okulNo);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            // Eğer kayıt bulunursa ad soyadı textField2'ye yaz
                            String adSoyad = resultSet.getString("adi");
                            String soyad = resultSet.getString("soyadi");
                            textField2.setText(adSoyad + " " + soyad);
                        } else {
                            // Kayıt bulunamazsa uygun bir mesaj ver
                            textField2.setText("Kayıt bulunamadı");
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Veritabanında sorgu yapılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Genel sınıf/yöntem metodu uygulanmıştır.
    private void rafNoKontrolEt() {
        Connection connection = baglantiyiKontrolEt();

        if (connection != null) {
            try {
                String rafNo = textField3.getText();

                // Veritabanında okulNo'ya karşılık gelen ad soyadı sorgula
                String sql = "SELECT kitapAdi FROM kitapekle WHERE rafNo = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, rafNo);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Eğer kayıt bulunursa ad soyadı textField2'ye yaz
                    String kitapAdi = resultSet.getString("kitapAdi");


                    textField4.setText(kitapAdi);
                } else {
                    // Kayıt bulunamazsa uygun bir mesaj ver
                    textField4.setText("Kayıt bulunamadı");
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanında sorgu yapılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void kitapAlisEkle() {
        try {
            Connection connection = baglantiyiKontrolEt();

            if (connection != null) {
                // Bugünün tarihini al
                LocalDate bugun = LocalDate.now();

                // Tarihi bir String'e dönüştür
                DateTimeFormatter alisTarihiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String bugununTarihiString = bugun.format(alisTarihiFormatter);

                // TextField'tan alınan tarihi LocalDate'e çevir
                DateTimeFormatter textFieldFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate teslimTarihi = LocalDate.parse(textField5.getText(), textFieldFormatter);

                // Diğer verileri al
                String okulNo = textField1.getText();
                String adiSoyadi = textField2.getText();
                String rafNo = textField3.getText();
                String kitapAdi = textField4.getText();

                // LocalDate'leri java.sql.Date'e dönüştür
                java.sql.Date alisSqlDate = java.sql.Date.valueOf(bugununTarihiString);
                java.sql.Date teslimSqlDate = java.sql.Date.valueOf(teslimTarihi);

                // PreparedStatement oluştur
                String sql = "INSERT INTO kitapteslimalis (okulNo, adiSoyadi, rafNo, kitapAdi, alisTarihi, teslimTarihi) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    // Değerleri PreparedStatement'e ekle
                    preparedStatement.setString(1, okulNo);
                    preparedStatement.setString(2, adiSoyadi);
                    preparedStatement.setString(3, rafNo);
                    preparedStatement.setString(4, kitapAdi);
                    preparedStatement.setDate(5, alisSqlDate);
                    preparedStatement.setDate(6, teslimSqlDate);

                    // PreparedStatement'i çalıştır
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Veri başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Veri eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanına veri eklerken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void tabloyuDoldur() {
        Connection connection = baglantiyiKontrolEt();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM kitapteslimalis";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Tabloyu temizle
                DefaultTableModel model = new DefaultTableModel();
                AbstractButton jTable;
                table1.setModel(model);

                // Sütun başlıklarını ayarla
                model.addColumn("okulNo");
                model.addColumn("adiSoyadi");
                model.addColumn("rafNo");
                model.addColumn("kitapAdi");
                model.addColumn("alisTarihi");
                model.addColumn("teslimTarihi");

                // Veritabanından gelen verileri tabloya ekle
                while (resultSet.next()) {
                    model.addRow(new Object[]{
                            resultSet.getString("okulNo"),
                            resultSet.getString("adiSoyadi"),
                            resultSet.getString("rafNo"),
                            resultSet.getString("kitapAdi"),
                            resultSet.getDate("alisTarihi"),
                            resultSet.getDate("teslimTarihi")
                    });
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Veritabanından veri çekerken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Jenerik koleksiyonlar (Liste, Set ve Haritadan en az ikisi), her ikiside uygulanmıştır
    public class VeritabaniIslemleri {

        public List<KitapTeslimBilgisi> getKitapTeslimBilgileri(String okulNo) {
            List<KitapTeslimBilgisi> kitapTeslimBilgileri = new ArrayList<>();

            try (Connection connection = baglantiyiKontrolEt()) {
                if (connection != null) {
                    String kontrolSQL = "SELECT * FROM kitapteslimalis WHERE okulNo = ?";
                    try (PreparedStatement kontrolStatement = connection.prepareStatement(kontrolSQL)) {
                        kontrolStatement.setString(1, okulNo);

                        try (ResultSet resultSet = kontrolStatement.executeQuery()) {
                            while (resultSet.next()) {
                                String kitapAdi = resultSet.getString("kitapAdi");
                                String alisTarihi = resultSet.getString("alisTarihi");

                                KitapTeslimBilgisi bilgi = new KitapTeslimBilgisi(okulNo, kitapAdi, alisTarihi);
                                kitapTeslimBilgileri.add(bilgi);
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return kitapTeslimBilgileri;
        }
    }

    class KitapTeslimBilgisi {
        private String okulNo;
        private String kitapAdi;
        private String alisTarihi;

        public KitapTeslimBilgisi(String okulNo, String kitapAdi, String alisTarihi) {
            this.okulNo = okulNo;
            this.kitapAdi = kitapAdi;
            this.alisTarihi = alisTarihi;
        }

        public String getOkulNo() {
            return okulNo;
        }

        public String getKitapAdi() {
            return kitapAdi;
        }

        public String getAlisTarihi() {
            return alisTarihi;
        }
    }

    private void teslimAlKontrol() {
        String okulNo = textField1.getText();

        VeritabaniIslemleri veritabaniIslemleri = new VeritabaniIslemleri();
        List<KitapTeslimBilgisi> kitapTeslimBilgileri = veritabaniIslemleri.getKitapTeslimBilgileri(okulNo);

        if (!kitapTeslimBilgileri.isEmpty()) {
            if (tableModel == null) {
                tableModel = new DefaultTableModel();
                tableModel.addColumn("Okul No");
                tableModel.addColumn("Kitap Adı");
                tableModel.addColumn("Alış Tarihi");
                table2.setModel(tableModel);
            }

            for (KitapTeslimBilgisi bilgi : kitapTeslimBilgileri) {
                Object[] rowData = {bilgi.getOkulNo(), bilgi.getKitapAdi(), bilgi.getAlisTarihi()};
                tableModel.addRow(rowData);
            }

            JOptionPane.showMessageDialog(this, "Kitap teslim alındı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Okul numarasına ait teslim kaydı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            anasayfa ex = new anasayfa();
            ex.setVisible(true);
        });
    }
}
