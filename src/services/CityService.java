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

import tables.City;

public class CityService implements Services {
	private DatabaseConnectionService dbService = null;
	private JComponent view;
	private boolean isOwner;

	public CityService(DatabaseConnectionService dbService, boolean isOwner) {
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
			insert.setLayout(new BoxLayout(insert, BoxLayout.Y_AXIS));
			insert.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			JLabel insertKIDLabel = new JLabel("KID: ");
			insert.add(insertKIDLabel);
			JTextField insertKIDText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertKIDText);

			JLabel insertTIDLabel = new JLabel("TID: ");
			insert.add(insertTIDLabel);
			JTextField insertTIDText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertTIDText);

			JLabel insertNameLabel = new JLabel("Name: ");
			insert.add(insertNameLabel);
			JTextField insertNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertNameText);

			JLabel insertPopulationLabel = new JLabel("Population: ");
			insert.add(insertPopulationLabel);
			JTextField insertPopulationText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertPopulationText);

			JLabel insertCoordinatesLabel = new JLabel("Coordinates: ");
			insert.add(insertCoordinatesLabel);
			JTextField insertCoordinatesText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			insert.add(insertCoordinatesText);

			JButton insertButton = new JButton("Insert");
			insertButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					City k = new City();
					boolean noErrors = true;
					try {
						k.KID = Integer.parseInt(insertKIDText.getText());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Please enter an integer for Kingdom ID");
						noErrors = false;
					}
					try {
						k.TID = Integer.parseInt(insertTIDText.getText());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Please enter an integer for Terrain ID");
						noErrors = false;
					}
					try {
						k.Population = Integer.parseInt(insertPopulationText.getText());
					} catch (NumberFormatException e) {
						if (!insertPopulationText.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter an integer for Population");
							noErrors = false;
						}
					}
					k.Coordinates = insertCoordinatesText.getText();
					k.Name = insertNameText.getText();
					if (noErrors) {
						addCity(k);
					}

					insertKIDText.setText("");
					insertTIDText.setText("");
					insertPopulationText.setText("");
					insertNameText.setText("");
					insertCoordinatesText.setText("");

					tabbedPane.remove(view);
					view = getScrollableTable();
					tabbedPane.insertTab("View", null, view, "View", 0);

					dropDown.removeAllItems();
					for (City city : getCitys()) {
						dropDown.addItem("ID: " + city.ID + " - Name:  " + city.Name);
					}
					dropDown2.removeAllItems();
					for (City city : getCitys()) {
						dropDown2.addItem("ID: " + city.ID + " - Name:  " + city.Name);
					}
				}
			});

			insert.add(insertButton);
			tabbedPane.addTab("Insert", insert);

			JPanel update = new JPanel();
			update.setLayout(new BoxLayout(update, BoxLayout.Y_AXIS));
			update.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			for (City city : getCitys()) {
				dropDown.addItem("ID: " + city.ID + " - Name:  " + city.Name);
			}
			JPanel innerPanel = new JPanel(new FlowLayout());
			innerPanel.setMaximumSize(new Dimension(width, height + 20));
			innerPanel.add(dropDown);
			update.add(innerPanel);

			JLabel updateKIDLabel = new JLabel("KID: ");
			update.add(updateKIDLabel);
			JTextField updateKIDText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateKIDText);

			JLabel updateTIDLabel = new JLabel("TID: ");
			update.add(updateTIDLabel);
			JTextField updateTIDText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateTIDText);

			JLabel updateNameLabel = new JLabel("Name: ");
			update.add(updateNameLabel);
			JTextField updateNameText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateNameText);

			JLabel updatePopulationLabel = new JLabel("Population: ");
			update.add(updatePopulationLabel);
			JTextField updatePopulationText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updatePopulationText);

			JLabel updateCoordinatesLabel = new JLabel("Coordinates: ");
			update.add(updateCoordinatesLabel);
			JTextField updateCoordinatesText = (new JTextField() {
				public JTextField setMaxSize(Dimension d) {
					setMaximumSize(d);
					return this;
				}
			}).setMaxSize(new Dimension(width, height));
			update.add(updateCoordinatesText);

			dropDown.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					try {
						String id = dropDown.getSelectedItem().toString().split("-")[0].split(" ")[1];

						City city = null;
						for (City k : getCitys()) {
							if (Integer.toString(k.ID).equals(id)) {
								city = k;
								break;
							}
						}
						updateTIDText.setText(Integer.toString(city.TID));
						updateKIDText.setText(Integer.toString(city.KID));
						updateNameText.setText(city.Name);
						updateCoordinatesText.setText(city.Coordinates);
						updatePopulationText.setText(Integer.toString(city.Population));
					} catch (Exception e1) {
						updateTIDText.setText("");
						updateKIDText.setText("");
						updateNameText.setText("");
						updateCoordinatesText.setText("");
						updatePopulationText.setText("");
					}
				}
			});

			JButton updateButton = new JButton("Update");
			updateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					City k = new City();
					k.ID = Integer.parseInt(dropDown.getSelectedItem().toString().split("-")[0].split(" ")[1]);
					boolean noErrors = true;
					try {
						k.TID = Integer.parseInt(updateTIDText.getText());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Please enter an integer for Terrain ID");
						noErrors = false;
					}
					try {
						k.KID = Integer.parseInt(updateKIDText.getText());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Please enter an integer for Kingdom ID");
						noErrors = false;
					}
					try {
						k.Population = Integer.parseInt(updatePopulationText.getText());
					} catch (NumberFormatException e) {
						if (!updatePopulationText.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter an integer for Population");
							noErrors = false;
						}
					}
					k.Name = updateNameText.getText();
					k.Coordinates = updateCoordinatesText.getText();

					if (noErrors) {
						updateCity(k);
					}
					updateTIDText.setText("");
					updateKIDText.setText("");
					updateNameText.setText("");
					updateCoordinatesText.setText("");
					updatePopulationText.setText("");

					tabbedPane.remove(view);
					view = getScrollableTable();
					tabbedPane.insertTab("View", null, view, "View", 0);

					dropDown.removeAllItems();
					for (City city : getCitys()) {
						dropDown.addItem("ID: " + city.ID + " - Name:  " + city.Name);
					}
					dropDown2.removeAllItems();
					for (City city : getCitys()) {
						dropDown2.addItem("ID: " + city.ID + " - Name:  " + city.Name);
					}
				}
			});

			update.add(updateButton);
			tabbedPane.addTab("Update", update);

			JPanel delete = new JPanel();
			delete.setLayout(new BoxLayout(delete, BoxLayout.Y_AXIS));
			delete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// added this wth changing tables names

			for (City city : getCitys()) {
				dropDown2.addItem("ID: " + city.ID + " - Name:  " + city.Name);
			}
			JPanel innerPanel2 = new JPanel(new FlowLayout());
			innerPanel2.setMaximumSize(new Dimension(width, height + 20));
			innerPanel2.add(dropDown2);
			delete.add(innerPanel2);

			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {

					int id;
					try {
						id = Integer.parseInt(dropDown2.getSelectedItem().toString().split("-")[0].split(" ")[1]);
					} catch (NumberFormatException e) {
						id = 0;
					}
					deleteCity(id);

					tabbedPane.remove(view);
					view = getScrollableTable();
					tabbedPane.insertTab("View", null, view, "View", 0);

					dropDown.removeAllItems();
					for (City city : getCitys()) {
						dropDown.addItem("ID: " + city.ID + " - Name:  " + city.Name);
					}
					dropDown2.removeAllItems();
					for (City city : getCitys()) {
						dropDown2.addItem("ID: " + city.ID + " - Name:  " + city.Name);
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
		String[] columnNames = { "ID", "KID", "TID", "Name", "Population", "Coordinates" };
		ArrayList<City> citys = getCitys();
		Object[][] data = new Object[citys.size()][5];
		for (int i = 0; i < citys.size(); i++) {
			City k = citys.get(i);
			data[i] = k.getRow();
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return Integer.class;
				case 2:
					return Integer.class;
				case 3:
					return String.class;
				case 4:
					return Integer.class;
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

	public boolean addCity(City k) {
		try {
			CallableStatement cs = this.dbService.getConnection()
					.prepareCall("{ ? = call dbo.Insert_City(?, ?, ?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, k.KID);
			cs.setInt(3, k.TID);
			cs.setString(4, k.Name);
			cs.setString(5, k.Coordinates);
			cs.setInt(6, k.Population);
			cs.execute();
			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "The kingdom ID " + k.KID + " does not exist");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "The terrain ID " + k.TID + " does not exist");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "The City Name must be unique and non-null");
				break;
			case 4:
				JOptionPane.showMessageDialog(null,
						"Name can only include alphabetical characters, dashes, and apostrophes");
				break;
			case 5:
				JOptionPane.showMessageDialog(null,
						"Please enter coordinates that follow the format 12.34567,12.34567");
				break;
			case 6:
				JOptionPane.showMessageDialog(null,
						"Please enter coordinates that are numbers and follow 12.34567,12.34567");
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

	public boolean updateCity(City k) {
		try {
			CallableStatement cs = this.dbService.getConnection()
					.prepareCall("{ ? = call dbo.Update_City(?, ?, ?, ?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, k.ID);
			cs.setInt(3, k.KID);
			cs.setInt(4, k.TID);
			cs.setString(5, k.Name);
			cs.setString(6, k.Coordinates);
			cs.setInt(7, k.Population);
			cs.execute();

			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "Please provide a valid ID");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Please provide a valid Kingdom ID");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "Please provide a valid Terrain ID");
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "The City Name must be unique and non-null");
				break;
			case 5:
				JOptionPane.showMessageDialog(null,
						"Name can only include alphabetical characters, dashes, and apostrophes");
				break;
			case 6:
				JOptionPane.showMessageDialog(null, "The population must be greater than 10");
				break;
			case 7:
				JOptionPane.showMessageDialog(null,
						"Please enter coordinates that follow the format 12.34567,12.34567");
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

	public boolean deleteCity(int ID) {
		try {
			CallableStatement cs = this.dbService.getConnection().prepareCall("{ ? = call dbo.Delete_City(?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, ID);

			cs.execute();
			int returnVal = cs.getInt(1);
			switch (returnVal) {
			case 1:
				JOptionPane.showMessageDialog(null, "Please provide a valid id");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "The FunctionsUsing table is currently referencing this city");
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

	public ArrayList<String> getCityNames() {
		Statement stmt;
		ResultSet rs;
		ArrayList<String> sodas = new ArrayList<String>();
		try {
			stmt = dbService.getConnection().createStatement();
			stmt.execute("select distinct name from City");
			rs = stmt.getResultSet();
			while (rs.next()) {
				sodas.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sodas;
	}

	public ArrayList<City> getCitys() {
		try {
			String query = "SELECT ID, KID, TID, Name, Population, Coordinates \nFROM City\n";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			return parseResults(rs);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to retrieve cities.");
			ex.printStackTrace();
			return new ArrayList<City>();
		}

	}

	private ArrayList<City> parseResults(ResultSet rs) {
		try {
			ArrayList<City> citys = new ArrayList<City>();
			while (rs.next()) {
				City city = new City();
				city.ID = rs.getInt("ID");
				city.KID = rs.getInt("KID");
				city.TID = rs.getInt("TID");
				city.Name = rs.getString("Name");
				city.Population = rs.getInt("Population");
				city.Coordinates = rs.getString("Coordinates");

				citys.add(city);
			}
			return citys;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "An error ocurred while retrieving citys. See printed stack trace.");
			ex.printStackTrace();
			return new ArrayList<City>();
		}

	}

}
