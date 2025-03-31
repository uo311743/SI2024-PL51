package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class CloseActivityView extends AbstractView {
	
	private JTable activityTable;
	private JTextField statusSponsorTextField, statusIncomesTextField, statusExpensesTextField;
	
	public CloseActivityView() { super("Close Activity"); }
	
	@Override
	protected void initialize()
	{
		this.activityTable = new JTable();
        this.statusSponsorTextField = new JTextField(20);
        this.statusIncomesTextField = new JTextField(20);
		this.statusExpensesTextField = new JTextField(20);
		
		super.createButtonLowLeft("Go Back");
		super.createButtonLowRight("Close");
	}

	@Override
	protected void configMainPanel()
	{
		super.getMainPanel().setLayout(new BorderLayout());
		super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));


		super.getMainPanel().add(createLeftPanel(), BorderLayout.WEST);
		super.getMainPanel().add(createRightPanel(), BorderLayout.EAST);
	}
	
	private JPanel createLeftPanel()
	{
		JLabel activityTableLabel = new JLabel("Please select an Activity:");

		activityTable.setName("activityTable");
		activityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		activityTable.setDefaultEditor(Object.class, null);
		JScrollPane activityTableScroll = new JScrollPane(activityTable);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(activityTableLabel, BorderLayout.NORTH);
		topPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(activityTableScroll, BorderLayout.CENTER);
	
		return panel;
	}
	
	private JPanel createRightPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		// Fields Panel with Border
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		fieldsPanel.setBorder(BorderFactory.createTitledBorder("Agreement Details"));
		GridBagConstraints fieldsGbc = new GridBagConstraints();
		fieldsGbc.insets = new Insets(5, 5, 5, 5);
		fieldsGbc.anchor = GridBagConstraints.WEST;

		// Labels
		JLabel statusSponsorLabel = new JLabel("Sponsorships:");
		JLabel statusIncomesLabel = new JLabel("Incomes:");
		JLabel statusExpensesLabel = new JLabel("Expenses:");

		// Fields
		JComponent[][] fields = {
		    {statusSponsorLabel, statusSponsorTextField},
		    {statusIncomesLabel, statusIncomesTextField},
		    {statusExpensesLabel, statusExpensesTextField}
		};

		// Add labels above inputs
		for (int i = 0; i < fields.length; i++) {
		    // Set label in the first row
		    fieldsGbc.gridx = 0;
		    fieldsGbc.gridy = i * 2; // position label in even rows
		    fieldsPanel.add(fields[i][0], fieldsGbc);

		    // Set input field in the second row
		    fieldsGbc.gridx = 0;
		    fieldsGbc.gridy = i * 2 + 1; // position input in odd rows
		    fieldsGbc.gridwidth = 2; // input spans two columns
		    fieldsGbc.fill = GridBagConstraints.HORIZONTAL; // Let the field take up horizontal space
		    fieldsGbc.weightx = 1.0; // Allow the input to expand horizontally evenly
		    fieldsPanel.add(fields[i][1], fieldsGbc);
		}

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(fieldsPanel, gbc);

        return panel;
	}
		
	
	
	public JTable getActivityTable() {
	    return activityTable;
	}
	

	public JTextField getStatusSponsorTextField() {
	    return statusSponsorTextField;
	}

	public JTextField getStatusIncomesTextField() {
	    return statusIncomesTextField;
	}

	public JTextField getStatusExpensesTextField() {
	    return statusExpensesTextField;
	}

}
