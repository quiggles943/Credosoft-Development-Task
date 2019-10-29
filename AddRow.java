import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AddRow extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private static JPanel contentPanel;
	private static JTextField firstText;
	private static JTextField secondText;
	private static JLabel tableNameLabel,idLabel,otherLabel;
	private static JButton okButton,cancelButton;
	private static JLabel errorlbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			contentPanel = new JPanel();
			AddRow dialog = new AddRow();
			tableNameLabel.setText(args[0]);
			idLabel.setText(args[1]);
			otherLabel.setText(args[2]);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			okButton.addActionListener((e) -> {
						try {
							if(firstText.getText().equals("") || secondText.getText().equals(""))
							{
								
								errorlbl.setText("Both text boxes must have something in them");
							}
							else if(!isNumber(firstText.getText()))
							{
								errorlbl.setText("Id number supplied is not a valid number");
							}
							else
							{
								
								String id = firstText.getText();
								String other = secondText.getText();
								Database.addToDatabase(args[0],id,other);
								errorlbl.setText("success");
								System.out.println("success");
								MainWindow.loadTable(args[0]);
								SwingUtilities.getWindowAncestor(firstText).dispose();
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							errorlbl.setText(e1.toString());
						}
					});
			
			cancelButton.addActionListener((e)->{
				SwingUtilities.getWindowAncestor(firstText).dispose();
			});
		} catch (Exception e) {
			errorlbl.setText(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddRow() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//gets window icon and sets it
		Image icon = Toolkit.getDefaultToolkit().getImage(AddRow.class.getResource("/images/favicon-32x32.png"));
		setIconImage(icon);
		setTitle("Add Row");
		//creates Panel layout
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		{
			tableNameLabel = new JLabel("Title");
			contentPanel.add(tableNameLabel, "cell 1 0");
		}
		{
			idLabel = new JLabel("Id");
			contentPanel.add(idLabel, "cell 0 1,alignx trailing");
		}
		{
			firstText = new JFormattedTextField();
			contentPanel.add(firstText, "cell 1 1,growx");
			firstText.setColumns(10);
		}
		{
			otherLabel = new JLabel("New label");
			contentPanel.add(otherLabel, "cell 0 2,alignx trailing");
		}
		{
			secondText = new JTextField();
			contentPanel.add(secondText, "cell 1 2,growx");
			secondText.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				errorlbl = new JLabel("");
				errorlbl.setEnabled(false);
				buttonPane.add(errorlbl);
			}
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	//checks if input string is a number for id
	public static boolean isNumber(String text)
	{
		try
		{
			Integer.parseInt(text);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
}
