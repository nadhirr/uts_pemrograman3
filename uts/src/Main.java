import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;




// koneksi ke database 
import java.sql.*;

public class Main {
	public void init() throws SQLException{
		// koneksi database nya
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/nopal", "root", "");
				
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("Tes DB");
				frame.setSize(400,600);
				frame.setLocationRelativeTo(null);
				
				// komponen frame nya
				JLabel lblId = new JLabel("ID");
				JLabel lblQty = new JLabel("Qty");
				JLabel lblNull = new JLabel("");
				JTextField txtId = new JTextField(5);
				JTextField txtQty = new JTextField(5);
				
				// Query ke database
				String query = "select * from list_mobil";
				PreparedStatement ps = c.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				// ambil jumlah data nya
				ResultSetMetaData meta = rs.getMetaData();
				int jumlahdata = meta.getColumnCount();
				System.out.println("Jumlah data : " + jumlahdata);
				
				
				String dataVal[][] = new String[jumlahdata][5];
				int hitung = 0;
				while(rs.next()){
					dataVal[hitung][0] = rs.getString("id");
					dataVal[hitung][1] = rs.getString("Nama");
					dataVal[hitung][2] = rs.getString("No_pol");
					dataVal[hitung][3] = Integer.toString(rs.getInt("Harga"));
					dataVal[hitung][4] = Integer.toString(rs.getInt("Stock"));
					
					hitung++;
				}
				
				// generate tabel nya
				String columnHeader[] = {"id", "nama","nopol", "harga", "stok"};
				JTable tabel = new JTable(dataVal, columnHeader);
				tabel.setSize(100,20);
				JScrollPane pane = new JScrollPane(tabel);
				
				JButton btnOK = new JButton("Beli"); 
				//btnOK.setActionCommand("beli_mobil");
				//btnOK.addActionListener(new Pendengar());
				btnOK.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String id = txtId.getText().toString();
						int Qty = Integer.parseInt(txtQty.getText());
						
						// masukin ke tabel penjualan
						String query_kurang_stok = "update list_mobil set stock = stock - ? where id = ?";
						String query_penjualan = "insert into penjualan values(?,?)";
						try {
							// siapin query ke tabel penjualan
							PreparedStatement ps_penjualan = c.prepareStatement(query_penjualan);
							ps_penjualan.setString(1, id);
							ps_penjualan.setInt(2, Qty);
							
							// masukin data ke tabel penjualan
							ps_penjualan.executeUpdate();
							
							// siapin query buat kurangin stok
							ps_penjualan = c.prepareStatement(query_kurang_stok);
							ps_penjualan.setInt(1, Qty);
							ps_penjualan.setString(2, id);
							
							// jalanin query untuk kurangin stok
							ps_penjualan.executeUpdate();
							
							System.out.println("tranksaksi berhasil");
							JOptionPane.showMessageDialog(null, "Data berhasil dimasukkan");
							
							// sembunyiin frame nya, jalanin frame baru 
							frame.setVisible(false);
							ListPenjualan lp = new ListPenjualan();
							lp.init();
							
						} catch (SQLException e1) {
							e1.printStackTrace();
							System.out.println("ada kesalahan pemasukan data");
						}
						
					}
				});
				
				Container con = new Container();
				con.setLayout(new GridLayout(3, 3));
				con.add(lblId);
				con.add(txtId);
				con.add(lblQty);
				con.add(txtQty);
				con.add(lblNull);
				con.add(btnOK);
				
				// masukin semua komponen ke dalam frame
				BorderLayout bLayout = new BorderLayout();
				frame.getContentPane().setLayout(bLayout);
				frame.getContentPane().add(pane, BorderLayout.NORTH);
				frame.getContentPane().add(con, BorderLayout.SOUTH);
				
				// tampilin frame nya
				frame.setVisible(true);// bikin menu bar untuk navigasi
				
				JMenuBar menubar = new JMenuBar();
				
				JMenu file = new JMenu("File");
				JMenuItem itemMain = new JMenuItem("Menu Utama");
				JMenuItem itemList = new JMenuItem("List Penjualan");
				JMenuItem itemAbout = new JMenuItem("About");
				file.add(itemMain);
				file.add(itemList);
				file.add(itemAbout);
				
				itemMain.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						frame.setVisible(false);
						Main m = new Main();
						try {
							m.init();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				});
				itemAbout.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Created By");
					}
				});
				
				menubar.add(file);
				frame.setJMenuBar(menubar);
	}
	
	public static void main(String[] args) throws SQLException{
		Main m = new Main();
		m.init();
	}
}
