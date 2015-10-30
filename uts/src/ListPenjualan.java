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

import java.sql.*;

public class ListPenjualan {

	public void init() throws SQLException{
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/nopal", "root", "");
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Tes DB");
		frame.setSize(400,600);
		frame.setLocationRelativeTo(null);
		
		// hitung jumlah kolom pada tabel /////////////////////////////
		String query_hitung = "select count(*) from penjualan";
		PreparedStatement ps_hitung = c.prepareStatement(query_hitung);
		int columCount = 0;
		ResultSet rs_hitung = ps_hitung.executeQuery();
		while(rs_hitung.next()){
			columCount = Integer.parseInt(rs_hitung.getString(1));
		}
		rs_hitung.close();
		System.out.println("Jumlah kolomnya : " + columCount);
		//////////////////////////////////////////////////////////
		
		/*ResultSetMetaData meta = rs.getMetaData();
		int jumlahdata = meta.getColumnCount();
		System.out.println("Jumlah data : " + jumlahdata);*/
		
		// Query ke database
		String query = "select * from penjualan";
		PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
				
		
		String dataValue[][] = new String[columCount][2];
		int hitung = 0;
		while(rs.next()){
			dataValue[hitung][0] = rs.getString(1);
			dataValue[hitung][1] = Integer.toString(rs.getInt(2));
			//dataVal[hitung][1] = Integer.toString(rs.getInt("quantity"));
			
			hitung++;
		}
		
		// generate tabel nya
		String columnHeader[] = {"id", "quantity"};
		JTable tabel = new JTable(dataValue, columnHeader);
		tabel.setSize(100,20);
		JScrollPane pane = new JScrollPane(tabel);
		
		BorderLayout bLayout = new BorderLayout();
		frame.getContentPane().setLayout(bLayout);
		frame.getContentPane().add(pane, BorderLayout.NORTH);
		frame.setVisible(true);
		
		// bikin menu bar untuk navigasi
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
		ListPenjualan lp = new ListPenjualan();
		lp.init();
	}
	
}
