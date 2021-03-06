package services;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import tables.Kingdom;

public class KingdomService implements Services {
	private DatabaseConnectionService dbService = null;
	private JComponent view;
	private boolean isOwner;

	public KingdomService(DatabaseConnectionService dbService, boolean isOwner) {
		this.dbService = dbService;
		this.isOwner = isOwner;
	}

	public JPanel getJPanel() {
		JPanel panel = new JPanel(new CardLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		view = getScrollableTable();
		tabbedPane.addTab("View", view);
		JComboBox<String> dropDown = new JComboBox<>();
		JComboBox<String> dropDown2 = new JComboBox<>();

		int width = 500;
		int height = 20;
		if (this.isOwner) {
			JPanel insert = new JPanel();
			insert.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// insert.setMaximumSize(new Dimension(200, 200));
			insert.setLayout(new BoxLayout(insert, BoxLayout.Y_AXIS));
			// insert.setLayout(new FlowLayout());
			JLabel insertNameLabel = new JLabel("Name: ");
			insert.add(insertNameLabel);
			JTextField insertNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertNameText);

			JLabel insertShortNameLabel = new JLabel("ShortName: ");
			insert.add(insertShortNameLabel);
			JTextField insertShortNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertShortNameText);

			JLabel insertDateConqueredYearLabel = new JLabel("Date Conquered Year: ");
			insert.add(insertDateConqueredYearLabel);
			JTextField insertDateConqueredYearText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertDateConqueredYearText);

			JLabel insertDateConqueredMonthLabel = new JLabel("Date Conquered Month: ");
			insert.add(insertDateConqueredMonthLabel);
			JTextField insertDateConqueredMonthText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertDateConqueredMonthText);

			JLabel insertDateConqueredDayLabel = new JLabel("Date Conquered Day: ");
			insert.add(insertDateConqueredDayLabel);
			JTextField insertDateConqueredDayText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertDateConqueredDayText);

			JLabel insertGdpLabel = new JLabel("GDP: ");
			insert.add(insertGdpLabel);
			JTextField insertGdpText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertGdpText);

			JLabel insertSuccessionLabel = new JLabel("Succession: ");
			insert.add(insertSuccessionLabel);
			JTextField insertSuccessionText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertSuccessionText);

			JLabel insertTypeLabel = new JLabel("Type: ");
			insert.add(insertTypeLabel);
			JTextField insertTypeText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertTypeText);

			JButton insertButton = new JButton("Insert");
			insertButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Kingdom k = new Kingdom();
					k.Name = insertNameText.getText();
					k.ShortName = insertShortNameText.getText();
					boolean noErrors = true;
					try {
						k.DateConquered = java.sql.Date.valueOf(insertDateConqueredYearText.getText() + "-"
								+ insertDateConqueredMonthText.getText() + "-" + insertDateConqueredDayText.getText());
					} catch (IllegalArgumentException e) {
						if (!(insertDateConqueredYearText.getText().equals("")
								&& insertDateConqueredMonthText.getText().equals("")
								&& insertDateConqueredDayText.getText().equals(""))) {

							JOptionPane.showMessageDialog(null, "Please enter a valid date");
							noErrors = false;
						}
					}
					try {
						k.GDP = Long.parseLong(insertGdpText.getText());
					} catch (NumberFormatException e) {
						if (!insertGdpText.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter an number for GDP");
							noErrors = false;
						}
					}
					k.Succession = insertSuccessionText.getText();
					k.Type = insertTypeText.getText();
					if (noErrors) {
						addKingdom(k);
					}
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
					dropDown.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
					dropDown2.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown2.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
				}
			});

			insert.add(insertButton);

			tabbedPane.addTab("Insert", insert);

			JPanel update = new JPanel();
			update.setLayout(new BoxLayout(update, BoxLayout.Y_AXIS));
			update.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			for (Kingdom kingdom : getKingdoms()) {
				dropDown.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
			}
			JPanel innerPanel = new JPanel(new FlowLayout());
			innerPanel.setMaximumSize(new Dimension(width, height + 20));
			innerPanel.add(dropDown);
			update.add(innerPanel);

			JLabel updateNameLabel = new JLabel("Name: ");
			update.add(updateNameLabel);
			JTextField updateNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateNameText);

			JLabel updateShortNameLabel = new JLabel("ShortName: ");
			update.add(updateShortNameLabel);
			JTextField updateShortNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateShortNameText);

			JLabel updateDateConqueredYearLabel = new JLabel("Date Conquered Year: ");
			update.add(updateDateConqueredYearLabel);
			JTextField updateDateConqueredYearText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateDateConqueredYearText);

			JLabel updateDateConqueredMonthLabel = new JLabel("Date Conquered Month: ");
			update.add(updateDateConqueredMonthLabel);
			JTextField updateDateConqueredMonthText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateDateConqueredMonthText);

			JLabel updateDateConqueredDayLabel = new JLabel("Date Conquered Day: ");
			update.add(updateDateConqueredDayLabel);
			JTextField updateDateConqueredDayText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateDateConqueredDayText);

			JLabel updateGdpLabel = new JLabel("GDP: ");
			update.add(updateGdpLabel);
			JTextField updateGdpText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateGdpText);

			JLabel updateSuccessionLabel = new JLabel("Succession: ");
			update.add(updateSuccessionLabel);
			JTextField updateSuccessionText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateSuccessionText);

			JLabel updateTypeLabel = new JLabel("Type: ");
			update.add(updateTypeLabel);
			JTextField updateTypeText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateTypeText);

			dropDown.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					try {
						String id = dropDown.getSelectedItem().toString().split("-")[0].split(" ")[1];
						Kingdom kingdom = null;
						for (Kingdom k : getKingdoms()) {
							if (Integer.toString(k.ID).equals(id)) {
								kingdom = k;
								break;
							}
						}
						Calendar cal = Calendar.getInstance();
						Integer month = null, year = null, day = null;
						if (kingdom.DateConquered != null) {
							cal.setTime(kingdom.DateConquered);
							month = cal.get(Calendar.MONTH) + 1;
							day = cal.get(Calendar.DAY_OF_MONTH);
							year = cal.get(Calendar.YEAR);
							updateDateConqueredYearText.setText(year.toString());
							updateDateConqueredDayText.setText(day.toString());
							updateDateConqueredMonthText.setText(month.toString());
						} else {
							updateDateConqueredYearText.setText("");
							updateDateConqueredDayText.setText("");
							updateDateConqueredMonthText.setText("");
						}

						Long gdp = kingdom.GDP;
						updateNameText.setText(kingdom.Name);
						updateShortNameText.setText(kingdom.ShortName);
						updateGdpText.setText(gdp.toString());
						updateSuccessionText.setText(kingdom.Succession);
						updateTypeText.setText(kingdom.Type);
					} catch (Exception e1) {
						updateNameText.setText("");
						updateShortNameText.setText("");
						updateDateConqueredYearText.setText("");
						updateDateConqueredDayText.setText("");
						updateDateConqueredMonthText.setText("");
						updateGdpText.setText("");
						updateSuccessionText.setText("");
						updateTypeText.setText("");
					}
				}
			});

			JButton updateButton = new JButton("Update");
			updateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Kingdom k = new Kingdom();
					k.ID = Integer.parseInt(dropDown.getSelectedItem().toString().split("-")[0].split(" ")[1]);
					k.Name = updateNameText.getText();
					k.ShortName = updateShortNameText.getText();
					boolean noErrors = true;
					try {
						k.DateConquered = java.sql.Date.valueOf(updateDateConqueredYearText.getText() + "-"
								+ updateDateConqueredMonthText.getText() + "-" + updateDateConqueredDayText.getText());
					} catch (IllegalArgumentException e) {
						if (!(updateDateConqueredYearText.getText().equals("")
								&& updateDateConqueredMonthText.getText().equals("")
								&& updateDateConqueredDayText.getText().equals(""))) {
							JOptionPane.showMessageDialog(null, "Please enter a valid Date");
							noErrors = false;
						}
					}
					try {
						k.GDP = Long.parseLong(updateGdpText.getText());
					} catch (NumberFormatException e) {
						if (!updateGdpText.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter an number for GDP");
							noErrors = false;
						}
					}
					k.Succession = updateSuccessionText.getText();
					k.Type = updateTypeText.getText();
					if (noErrors) {
						updateKingdom(k);
					}
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
					dropDown.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
					dropDown2.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown2.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
				}
			});

			update.add(updateButton);
			tabbedPane.addTab("Update", update);

			JPanel delete = new JPanel();
			delete.setLayout(new BoxLayout(delete, BoxLayout.Y_AXIS));
			delete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			for (Kingdom kingdom : getKingdoms()) {
				dropDown2.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
			}
			JPanel innerPanel2 = new JPanel(new FlowLayout());
			innerPanel2.setMaximumSize(new Dimension(width, height + 20));
			innerPanel2.add(dropDown2);
			delete.add(innerPanel2);

			JButton deleteButton = new JButton("Delete");

			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {

					int id = Integer.parseInt(dropDown2.getSelectedItem().toString().split("-")[0].split(" ")[1]);
					deleteKingdom(id);

					tabbedPane.remove(view);
					view = getScrollableTable();
					tabbedPane.insertTab("View", null, view, "View", 0);
					dropDown.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
					dropDown2.removeAllItems();
					for (Kingdom kingdom : getKingdoms()) {
						dropDown2.addItem("ID: " + kingdom.ID + " - Name:  " + kingdom.Name);
					}
				}
			});

			delete.add(deleteButton);
			tabbedPane.addTab("Delete", delete);

		}
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
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return java.sql.Date.class;
				case 4:
					return Long.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		JTable table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
		JTextField jtfFilter = new JTextField();
		JButton jbtFilter = new JButton("Filter");

		table.setRowSorter(rowSorter);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Search:"), BorderLayout.WEST);
		panel.add(jtfFilter, BorderLayout.CENTER);

		JPanel scrollPane = new JPanel();
		scrollPane.setLayout(new BorderLayout());
		scrollPane.add(panel, BorderLayout.SOUTH);
		scrollPane.add(new JScrollPane(table), BorderLayout.CENTER);

		jtfFilter.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = jtfFilter.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = jtfFilter.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException();
			}

		});

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
				JOptionPane.showMessageDialog(null, "Please provide a shortname");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "Please provide a Date that has happened");
				break;
			case 4:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Name must be unique, non-null and only contain letters, dashes, apostraphes and spaces");
				break;
			case 5:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Short Name must only contain letters, dashes, apostraphes and spaces");
				break;
			case 6:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Succession must only contain letters, dashes, apostraphes and spaces");
				break;
			case 7:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Type must only contain letters, dashes, apostraphes and spaces");
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
			case 2:
				JOptionPane.showMessageDialog(null, "Please provide a Date that has happened");
				break;
			case 3:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Short Name must be unique and only contain letters, dashes, apostraphes and spaces");
				break;
			case 4:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Succession must only contain letters, dashes, apostraphes and spaces");
				break;
			case 5:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Type must only contain letters, dashes, apostraphes and spaces");
				break;
			case 6:
				JOptionPane.showMessageDialog(null,
						"The Kingdom Name must be unique, non-null and only contain letters, dashes, apostraphes and spaces");
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
			case 2:
				JOptionPane.showMessageDialog(null, "The City table is currently referencing this kingdom");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "The ConqueredUsing table is currently referencing this kingdom");
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "The Military table is currently referencing this kingdom");
				break;
			case 5:
				JOptionPane.showMessageDialog(null, "The Heir table is currently referencing this kingdom");
				break;
			case 6:
				JOptionPane.showMessageDialog(null, "The Ruler table is currently referencing this kingdom");
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
