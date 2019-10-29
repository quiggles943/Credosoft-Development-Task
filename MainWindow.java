import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTable table;
	private static JButton addRowBtn;
	private static JButton removeRowBtn;
	static ArrayList<String> tables;
	private static JComboBox<String> comboBox;
	private static DefaultTableModel tableModel;
	private static JButton resetBtn;
	private static JLabel errorLbl;
	
	//if set to true creates a reset database button to allow the database to be reset to the default values
	private static boolean debug = false;

	
	public static void main(String[] args) {
	
				try {
					
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
					//checks if the database already exists, if not it creates one and inputs default data
					if(!Database.exists()) {
						Database.Create();
					}
					//gets the tables to populate the combobox
					tables = Database.getTables();
					for(String s : tables)
				    {
				    comboBox.addItem(s);
				    }
					comboBox.setSelectedIndex(0);
					//populates the JTable with the rows from the first table
					loadTable(comboBox.getItemAt(0).toString());
					//when you select a different table with the combobox it reloads the JTable
					comboBox.addActionListener((e) -> {
						//When a new Table is selected
						try {
							loadTable(comboBox.getSelectedItem().toString());
						} catch (SQLException e1) {
							e1.printStackTrace();
							errorLbl.setText(e1.toString());
						}
					});
					
					//add button click listener
					addRowBtn.addActionListener((e) -> {
						String[] passthrough = new String[3];
						passthrough[0] = comboBox.getSelectedItem().toString();
						passthrough[1] = tableModel.getColumnName(0);
						passthrough[2] = tableModel.getColumnName(1);
						//takes the table name and row names and then sends it to the AddRow class
						AddRow.main(passthrough);
				    	errorLbl.setText("");
				    	
				    });
					//remove row button click listener
					removeRowBtn.addActionListener((e) -> {
						String idSelected = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
						try {
							Database.removeFromDatabase(comboBox.getSelectedItem().toString(), idSelected);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
						try {
							MainWindow.loadTable(comboBox.getSelectedItem().toString());
							errorLbl.setText("Row removed Successfully");
							
						} catch (SQLException e1) {
							e1.printStackTrace();
							errorLbl.setText(e1.toString());
						}
						
					});
					//if in debug mode it adds the onclick listener to the reset button
					if(debug) {
						resetBtn.addActionListener((e) -> {
							try {
								Database.Create();
								loadTable(comboBox.getSelectedItem().toString());
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
								errorLbl.setText(e1.toString());
							} catch (SQLException e1) {
								e1.printStackTrace();
								errorLbl.setText(e1.toString());
							}
						});
					}
				    	
					
				} catch (Exception e) {
					e.printStackTrace();
					errorLbl.setText(e.toString());
				}
			}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		       if ("Nimbus".equals(info.getName())) {
		           UIManager.setLookAndFeel(info.getClassName());
		           break;
		        }
		    }
		} catch (Exception e) {
		    errorLbl.setText(e.toString());
		}
		//gets window icon and sets it
		Image icon = Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/images/favicon-32x32.png"));
		setIconImage(icon);
		setTitle("Credosoft Development Task");
		
		//creates frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[][grow][][][]"));
		
		//if in debug mode creates reset button
		if(debug) {
			resetBtn = new JButton("Reset Database");
			contentPane.add(resetBtn, "cell 0 0");
		}
		
		comboBox = new JComboBox<String>();
		contentPane.add(comboBox, "cell 1 0,growx");
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
		};
		tableModel = new DefaultTableModel();
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(new JScrollPane(table), "cell 1 1,grow");
		
		addRowBtn = new JButton("Add Row");
		contentPane.add(addRowBtn, "cell 0 2");
		
		removeRowBtn = new JButton("Remove Row");
		contentPane.add(removeRowBtn, "cell 0 4");
		
		errorLbl = new JLabel("");
		errorLbl.setEnabled(false);
		errorLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(errorLbl, "cell 1 4,alignx right");
	}
	
	public static void loadTable(String tableName) throws SQLException
	{			
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
		int rowId = 0;
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet length = statement.executeQuery("SELECT COUNT(*) from " +tableName);
        int rowCount = length.getInt(1);
		ResultSet resultSet = statement.executeQuery("SELECT * from "+tableName);
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnNum = rsmd.getColumnCount();
		for(int i = 1;i<=columnNum;i++)
		{
			tableModel.addColumn(rsmd.getColumnName(i));
		}
		table.setModel(tableModel);
		String[]columns = new String[columnNum];
		String[][] result = new String[rowCount][columnNum];
           while(resultSet.next())
           {
        	   String[] row = new String[columnNum];
              // iterate & read the result set
        	  for(int i = 1;i<=columnNum;i++)
        	  {	              
	              columns[i-1] = rsmd.getColumnName(i);
	              row[i-1] = resultSet.getString(rsmd.getColumnName(i));
	              result[rowId][(i-1)] = resultSet.getString(rsmd.getColumnName(i));
        	  }
        	  rowId ++;
        	  tableModel.addRow(row);
           }
           
           /*Attempted to add a row sorter. created issue where item deleted would not correspond with item selected if
            * they had been moved due to the sorter*/
            		
           //tableModel.setColumnIdentifiers(columns);
	   		/*TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
	   		sorter.setSortsOnUpdates(true);
	   		table.setRowSorter(sorter);
	   		sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
	   		sorter.sort();
           table.setModel(tableModel);*/
           tableModel.fireTableDataChanged();
	}
}
