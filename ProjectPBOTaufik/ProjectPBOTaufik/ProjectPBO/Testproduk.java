package ProjectPBO;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Testproduk extends JFrame {
    private String[] judul = {"kode_barang", "nama_barang", "harga_barang", "stok_barang"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblkode_barang = new JLabel("Kode Barang");
    JTextField txkode_barang = new JTextField(10);
    JLabel lblnama_barang = new JLabel("Nama Barang");
    JTextField txnama_barang = new JTextField(20);
    JLabel lblharga_barang = new JLabel("Harga Barang");
    JTextField txharga_barang = new JTextField(10);
    JLabel lblstok_barang = new JLabel("Stok Barang");
    JTextField txstok_barang = new JTextField(10);
    JButton btadd = new JButton("Simpan");
    JButton btnew = new JButton("Baru");
    JButton btdel = new JButton("Hapus");
    JButton btedit = new JButton("Ubah");

    Testproduk() {
        super("Data Barang");
        setSize(460, 400);
        pnl.setLayout(null);
        
        pnl.add(lblkode_barang);
        lblkode_barang.setBounds(20, 10, 100, 20);
        pnl.add(txkode_barang);
        txkode_barang.setBounds(125, 10, 150, 20);

        pnl.add(lblnama_barang);
        lblnama_barang.setBounds(20, 40, 100, 20);
        pnl.add(txnama_barang);
        txnama_barang.setBounds(125, 40, 150, 20);

        pnl.add(lblharga_barang);
        lblharga_barang.setBounds(20, 70, 100, 20);
        pnl.add(txharga_barang);
        txharga_barang.setBounds(125, 70, 150, 20);

        pnl.add(lblstok_barang);
        lblstok_barang.setBounds(20, 100, 100, 20);
        pnl.add(txstok_barang);
        txstok_barang.setBounds(125, 100, 150, 20);

        pnl.add(btnew);
        btnew.setBounds(300, 10, 125, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnewAksi(e);
            }
        });
        
        pnl.add(btadd);
        btadd.setBounds(300, 40, 125, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btaddAksi(e);
            }
        });
        
        pnl.add(btedit);
        btedit.setBounds(300, 70, 125, 20);
        btedit.setEnabled(false);
        btedit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bteditAksi(e);
            }
        });
        
        pnl.add(btdel);
        btdel.setBounds(300, 100, 125, 20);
        btdel.setEnabled(false);
        btdel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btdelAksi(e);
            }
        });

        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.getViewport().add(tab);
        tab.setEnabled(true);
        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scp.setBounds(20, 150, 405, 200);
        pnl.add(scp);
        
        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        // Load data on start
        loadData();
    }

    void loadData() {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "SELECT * FROM produk";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String kode_barang, nama_barang, harga_barang, stok_barang;
                kode_barang = rs.getString("kode_barang");
                nama_barang = rs.getString("nama_barang");
                harga_barang = rs.getString("harga_barang");
                stok_barang = rs.getString("stok_barang");
                String[] data = {kode_barang, nama_barang, harga_barang, stok_barang};
                df.addRow(data);
            }
            rs.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void clearTable() {
        df.setRowCount(0);
    }

    void clearTextField() {
        txkode_barang.setText(null);
        txnama_barang.setText(null);
        txharga_barang.setText(null);
        txstok_barang.setText(null);
    }

    void simpanData(Produk p) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "INSERT INTO produk (kode_barang, nama_barang, harga_barang, stok_barang) " +
                    "VALUES ('" + p.getkode_barang() + "', '" + p.getnama_barang() + "', '" + p.getharga_barang() + "','" + p.getstok_barang() + "')";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                    "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            String[] data = {p.getkode_barang(), p.getnama_barang(), p.getharga_barang(), p.getstok_barang()};
            df.addRow(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "DELETE FROM produk WHERE kode_barang = '" + kode_barang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            df.removeRow(tab.getSelectedRow());
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Produk p, String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "UPDATE produk SET kode_barang='" + p.getkode_barang() + "', nama_barang='" + p.getnama_barang() + "', harga_barang='" + p.getharga_barang() + "', stok_barang='" + p.getstok_barang() + "' WHERE kode_barang='" + kode_barang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            clearTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btnewAksi(ActionEvent evt) {
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
    }

    private void btaddAksi(ActionEvent evt) {
        Produk p = new Produk();
        p.setkode_barang(txkode_barang.getText());
        p.setnama_barang(txnama_barang.getText());
        p.setharga_barang(txharga_barang.getText());
        p.setstok_barang(txstok_barang.getText());
        simpanData(p);
    }

    private void btdelAksi(ActionEvent evt) {
        int status;
        status = JOptionPane.showConfirmDialog(null, "Yakin data akan dihapus?",
                "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if (status == 0) {
            hapusData(txkode_barang.getText());
        }
    }

    private void bteditAksi(ActionEvent evt) {
        Produk p = new Produk();
        p.setkode_barang(txkode_barang.getText());
        p.setnama_barang(txnama_barang.getText());
        p.setharga_barang(txharga_barang.getText());
        p.setstok_barang(txstok_barang.getText());
        ubahData(p, tab.getValueAt(tab.getSelectedRow(), 0).toString());
    }

    private void tabMouseClicked(MouseEvent evt) {
        btedit.setEnabled(true);
        btdel.setEnabled(true);
        btadd.setEnabled(false);
        String kode_barang, nama_barang, harga_barang, stok_barang;
        kode_barang = tab.getValueAt(tab.getSelectedRow(), 0).toString();
        nama_barang = tab.getValueAt(tab.getSelectedRow(), 1).toString();
        harga_barang = tab.getValueAt(tab.getSelectedRow(), 2).toString();
        stok_barang = tab.getValueAt(tab.getSelectedRow(), 3).toString();
        txkode_barang.setText(kode_barang);
        txnama_barang.setText(nama_barang);
        txharga_barang.setText(harga_barang);
        txstok_barang.setText(stok_barang);
    }

    public static void main(String[] args) {
        new Testproduk();
    }
}
