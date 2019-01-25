package services;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tables.Kingdom;

public class KingdomService {
	private DatabaseConnectionService dbService = null;
	private static JComponent view;

	public KingdomService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public JPanel getJPanel() {
		JPanel panel = new JPanel(new CardLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		view = getScrollableTable();
		tabbedPane.addTab("View", view);

		JPanel insert = new JPanel();
		insert.setLayout(new BoxLayout(insert, BoxLayout.Y_AXIS));
		JLabel insertNameLabel = new JLabel("Name: ");
		insert.add(insertNameLabel);
		JTextField insertNameText = new JTextField();
		insert.add(insertNameText);

		JLabel insertShortNameLabel = new JLabel("ShortName: ");
		insert.add(insertShortNameLabel);
		JTextField insertShortNameText = new JTextField();
		insert.add(insertShortNameText);

		JLabel insertDateConqueredYearLabel = new JLabel("Date Conquered Year: ");
		insert.add(insertDateConqueredYearLabel);
		JTextField insertDateConqueredYearText = new JTextField();
		insert.add(insertDateConqueredYearText);

		JLabel insertDateConqueredMonthLabel = new JLabel("Date Conquered Month: ");
		insert.add(insertDateConqueredMonthLabel);
		JTextField insertDateConqueredMonthText = new JTextField();
		insert.add(insertDateConqueredMonthText);

		JLabel insertDateConqueredDayLabel = new JLabel("Date Conquered Day: ");
		insert.add(insertDateConqueredDayLabel);
		JTextField insertDateConqueredDayText = new JTextField();
		insert.add(insertDateConqueredDayText);

		JLabel insertGdpLabel = new JLabel("GDP: ");
		insert.add(insertGdpLabel);
		JTextField insertGdpText = new JTextField();
		insert.add(insertGdpText);

		JLabel insertSuccessionLabel = new JLabel("Succession: ");
		insert.add(insertSuccessionLabel);
		JTextField insertSuccessionText = new JTextField();
		insert.add(insertSuccessionText);

		JLabel insertTypeLabel = new JLabel("Type: ");
		insert.add(insertTypeLabel);
		JTextField insertTypeText = new JTextField();
		insert.add(insertTypeText);

		JButton insertButton = new JButton("Insert");
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Kingdom k = new Kingdom();
				k.Name = insertNameText.getText();
				k.ShortName = insertShortNameText.getText();

				try {
					k.DateConquered = java.sql.Date.valueOf(insertDateConqueredYearText.getText() + "-"
							+ insertDateConqueredMonthText.getText() + "-" + insertDateConqueredDayText.getText());
				} catch (IllegalArgumentException e) {

				}
				try {
					k.GDP = Integer.parseInt(insertGdpText.getText());
				} catch (NumberFormatException e) {

				}
				k.Succession = insertSuccessionText.getText();
				k.Type = insertTypeText.getText();
				addKingdom(k);

				insertNameText.setText("");
				insertShortNameText.setText("");
				insertDateConqueredYearText.setText("");
				insertDateConqueredDayText.setText("");
				insertDateConqueredMonthText.setText("");
				insertGdpText.setText("");
				insertSuccessionText.setText("");
				insertTypeText.setText("");

				tabbedPane.remove(view);
				view = getScrollableTable();
				tabbedPane.insertTab("View", null, view, "View", 0);
			}
		});

		insert.add(insertButton);
		tabbedPane.addTab("Insert", insert);

		JPanel update = new JPanel();
		update.setLayout(new BoxLayout(update, BoxLayout.Y_AXIS));

		JLabel updateIDLabel = new JLabel("ID: ");
		update.add(updateIDLabel);
		JTextField updateIDText = new JTextField();
		update.add(updateIDText);

		JLabel updateNameLabel = new JLabel("Name: ");
		update.add(updateNameLabel);
		JTextField updateNameText = new JTextField();
		update.add(updateNameText);

		JLabel updateShortNameLabel = new JLabel("ShortName: ");
		update.add(updateShortNameLabel);
		JTextField updateShortNameText = new JTextField();
		update.add(updateShortNameText);

		JLabel updateDateConqueredYearLabel = new JLabel("Date Conquered Year: ");
		update.add(updateDateConqueredYearLabel);
		JTextField updateDateConqueredYearText = new JTextField();
		update.add(updateDateConqueredYearText);

		JLabel updateDateConqueredMonthLabel = new JLabel("Date Conquered Month: ");
		update.add(updateDateConqueredMonthLabel);
		JTextField updateDateConqueredMonthText = new JTextField();
		update.add(updateDateConqueredMonthText);

		JLabel updateDateConqueredDayLabel = new JLabel("Date Conquered Day: ");
		update.add(updateDateConqueredDayLabel);
		JTextField updateDateConqueredDayText = new JTextField();
		update.add(updateDateConqueredDayText);

		JLabel updateGdpLabel = new JLabel("GDP: ");
		update.add(updateGdpLabel);
		JTextField updateGdpText = new JTextField();
		update.add(updateGdpText);

		JLabel updateSuccessionLabel = new JLabel("Succession: ");
		update.add(updateSuccessionLabel);
		JTextField updateSuccessionText = new JTextField();
		update.add(updateSuccessionText);

		JLabel updateTypeLabel = new JLabel("Type: ");
		update.add(updateTypeLabel);
		JTextField updateTypeText = new JTextField();
		update.add(updateTypeText);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Kingdom k = new Kingdom();
				try {
					k.ID = Integer.parseInt(updateIDText.getText());
				} catch (NumberFormatException e) {

				}
				k.Name = updateNameText.getText();
				k.ShortName = updateShortNameText.getText();

				try {
					k.DateConquered = java.sql.Date.valueOf(updateDateConqueredYearText.getText() + "-"
							+ updateDateConqueredMonthText.getText() + "-" + updateDateConqueredDayText.getText());
				} catch (IllegalArgumentException e) {

				}
				try {
					k.GDP = Integer.parseInt(updateGdpText.getText());
				} catch (NumberFormatException e) {

				}
				k.Succession = updateSuccessionText.getText();
				k.Type = updateTypeText.getText();
				updateKingdom(k);

				updateNameText.setText("");
				updateShortNameText.setText("");
				updateDateConqueredYearText.setText("");
				updateDateConqueredDayText.setText("");
				updateDateConqueredMonthText.setText("");
				updateGdpText.setText("");
				updateSuccessionText.setText("");
				updateTypeText.setText("");

				tabbedPane.remove(view);
				view = getScrollableTable();
				tabbedPane.insertTab("View", null, view, "View", 0);
			}
		});

		update.add(updateButton);
		tabbedPane.addTab("Update", update);

		JPanel delete = new JPanel();
		delete.setLayout(new BoxLayout(delete, BoxLayout.Y_AXIS));

		JLabel deleteIDLabel = new JLabel("ID: ");
		delete.add(deleteIDLabel);
		JTextField deleteIDText = new JTextField();
		delete.add(deleteIDText);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int id = Integer.parseInt(deleteIDText.getText());
					deleteKingdom(id);
				} catch (NumberFormatException e) {

				}

				deleteIDText.setText("");

				tabbedPane.remove(view);
				view = getScrollableTable();
				tabbedPane.insertTab("View", null, view, "View", 0);
			}
		});

		delete.add(deleteButton);
		tabbedPane.addTab("Delete", delete);

		panel.add(tabbedPane);
		return panel;
	}

	public JComponent getScrollableTable() {
		String[] columnNames = { "ID", "Name", "ShortName", "DateConquered", "GDP", "Succession", "Type" };
		ArrayList<Kingdom> kingdoms = getKingdoms();
		Object[][] data = new Object[kingdoms.size()][5];
		for (int i = 0; i < kingdoms.size(); i++) {
			Kingdom k = kingdoms.get(i);
			data[i] = k.getRow();
		}
		JTable table = new JTable(data, columnNames);
		table.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}

	public boolean addKingdom(Kingdom k) {
		try {
			CallableStatement cs = this.dbService.getConnection()
					.prepareCall("{ ? = call dbo.Insert_Kingdom(?, ?, ?, ?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, k.Name);
			cs.setString(3, k.ShortName);
			cs.setDate(4, k.DateConquered);
			cs.setLong(5, k.GDP);
			cs.setString(6, k.Succession);
			cs.setString(7, k.Type);
			cs.execute();
			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "Please provide a Name");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Please provide a ShortName");
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "Please provide a GDP");
				break;
			case 5:
				JOptionPane.showMessageDialog(null, "Please provide a Date that has happened");
				break;
			case 6:
				JOptionPane.showMessageDialog(null, "Please provide the Kingdom Type");
				break;
			default:
				break;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateKingdom(Kingdom k) {
		try {
			CallableStatement cs = this.dbService.getConnection()
					.prepareCall("{ ? = call dbo.Update_Kingdom(?,?,?,?,?,?,?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, k.ID);
			cs.setString(3, k.Name);
			cs.setString(4, k.ShortName);
			cs.setDate(5, k.DateConquered);
			cs.setLong(6, k.GDP);
			cs.setString(7, k.Succession);
			cs.setString(8, k.Type);

			cs.execute();
			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "Please provide a valid id");
				break;
			case 5:
				JOptionPane.showMessageDialog(null, "Please provide a Date that has happened");
				break;
			default:
				break;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteKingdom(int ID) {
		try {
			CallableStatement cs = this.dbService.getConnection().prepareCall("{ ? = call dbo.Delete_Kingdom(?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, ID);

			cs.execute();
			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "Please provide a valid id");
				break;
			default:
				break;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<String> getKingdomNames() {
		Statement stmt;
		ResultSet rs;
		ArrayList<String> sodas = new ArrayList<String>();
		try {
			stmt = dbService.getConnection().createStatement();
			stmt.execute("select distinct name from Kingdom");
			rs = stmt.getResultSet();
			while (rs.next()) {
				sodas.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sodas;
	}

	public ArrayList<Kingdom> getKingdoms() {
		try {
			String query = "SELECT ID, Name, ShortName, DateConquered, GDP, Succession, Type \nFROM Kingdom\n";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			return parseResults(rs);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to retrieve kingdoms.");
			ex.printStackTrace();
			return new ArrayList<Kingdom>();
		}

	}

	private ArrayList<Kingdom> parseResults(ResultSet rs) {
		try {
			ArrayList<Kingdom> kingdoms = new ArrayList<Kingdom>();
			// int IDIndex = rs.findColumn("ID");
			// int NameIndex = rs.findColumn("Name");
			// int ShortNameIndex = rs.findColumn("ShortName");
			// int DateConqueredIndex = rs.findColumn("DateConquered");
			// int GDPIndex = rs.findColumn("GDP");
			// int SuccessionIndex = rs.findColumn("Succession");
			// int TypeIndex = rs.findColumn("Type");
			while (rs.next()) {
				Kingdom kingdom = new Kingdom();
				kingdom.ID = rs.getInt("ID");
				kingdom.Name = rs.getString("Name");
				kingdom.ShortName = rs.getString("ShortName");
				kingdom.DateConquered = rs.getDate("DateConquered");
				kingdom.GDP = rs.getLong("GDP");
				kingdom.Succession = rs.getString("Succession");
				kingdom.Type = rs.getString("Type");

				kingdoms.add(kingdom);
			}
			return kingdoms;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "An error ocurred while retrieving kingdoms. See printed stack trace.");
			ex.printStackTrace();
			return new ArrayList<Kingdom>();
		}

	}

}
