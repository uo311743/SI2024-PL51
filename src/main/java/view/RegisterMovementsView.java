package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class RegisterMovementsView extends AbstractView {
	private JComboBox<Object> type;
	private JComboBox<Object> status;
	private JTable activitiesTable;
	private JTable movementsTable;
    private JTextField amountTextField;
    private JTextField dateTextField;
    private JTextField conceptTextField;
    private JLabel totalIncomesLabel;
    private JLabel totalExpensesLabel;
    private JLabel remainingBalanceLabel;
    
    public RegisterMovementsView() { super("Register Movement"); }
    
    @Override
    protected void initialize()
    {
    	this.setType(new JComboBox<>());
    	this.setStatus(new JComboBox<>());
    	this.setMovementsTable(new JTable());
    	this.setActivitiesTable(new JTable());
    	this.amountTextField = new JTextField("");
    	this.dateTextField = new JTextField("");
    	this.conceptTextField = new JTextField("");
    	this.totalIncomesLabel = new JLabel("");
    	this.totalExpensesLabel = new JLabel("");
    	this.remainingBalanceLabel = new JLabel("");
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Clear");
        super.createButtonLowRight("Register");
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
    	// Labels
        JLabel activityLabel = new JLabel("Select an Activity to register a Movement");
        JLabel movementLabel = new JLabel("Movements registered for the selected Activity");
        JLabel typeLabel = new JLabel("Type:");
        JLabel statusLabel = new JLabel("Status:");
        
        // Create title labels with bold text
        JLabel totalIncomesTitle = new JLabel("Total Income (euro): ");
        totalIncomesTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel totalExpensesTitle = new JLabel("Total Expense (euro): ");
        totalExpensesTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel remainingBalanceTitle = new JLabel("Remaining Balance (euro): ");
        remainingBalanceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a panel with GridLayout to align labels properly
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, spacing of 10px
        
        // Create value labels (dynamic data placeholders)
        this.totalIncomesLabel = new JLabel("0.00");
        totalIncomesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalIncomesLabel.setForeground(Color.BLUE); // Highlight in blue

        this.totalExpensesLabel = new JLabel("0.00");
        this.totalExpensesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.totalExpensesLabel.setForeground(Color.GREEN); // Highlight in green

        this.remainingBalanceLabel = new JLabel("0.00");
        this.remainingBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.remainingBalanceLabel.setForeground(Color.RED); // Highlight in red

        // Add elements to the panel
        summaryPanel.add(totalIncomesTitle);
        summaryPanel.add(this.totalIncomesLabel);
        summaryPanel.add(totalExpensesTitle);
        summaryPanel.add(this.totalExpensesLabel);
        summaryPanel.add(remainingBalanceTitle);
        summaryPanel.add(this.remainingBalanceLabel);

        // Wrap it in a titled border for a professional look
        JPanel borderedPanel = new JPanel(new BorderLayout());
        borderedPanel.setBorder(BorderFactory.createTitledBorder("Movement Summary"));
        borderedPanel.add(summaryPanel);

        // Set table properties
        activitiesTable.setName("Activites");
        activitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activitiesTable.setDefaultEditor(Object.class, null);
        JScrollPane activitiesTableScroll = new JScrollPane(activitiesTable);
        
        movementsTable.setName("Movements");
        movementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movementsTable.setDefaultEditor(Object.class, null);
        JScrollPane movementsTableScroll = new JScrollPane(movementsTable);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Activity Panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.add(activityLabel, BorderLayout.NORTH);
        activitiesPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        activitiesPanel.add(activitiesTableScroll, BorderLayout.CENTER);
        
        // Movement Panel
        JPanel movementPanel = new JPanel(new BorderLayout());
        movementPanel.add(movementLabel, BorderLayout.NORTH);
        movementPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        movementPanel.add(movementsTableScroll, BorderLayout.CENTER);

        // Type Panel
        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.add(typeLabel, BorderLayout.NORTH);
        typePanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        typePanel.add(type, BorderLayout.CENTER);
        
        // Status Panel
        JPanel satusPanel = new JPanel(new BorderLayout());
        satusPanel.add(statusLabel, BorderLayout.NORTH);
        satusPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        satusPanel.add(status, BorderLayout.CENTER);

        // Add panels in correct order
        panel.add(typePanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(satusPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(activitiesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(movementPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(summaryPanel); // Adds spacing
	
		return panel;
	}

    private JPanel createRightPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(30, 30, 30, 30);
		gbc.anchor = GridBagConstraints.WEST;

		// Fields Panel with Border
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		fieldsPanel.setBorder(BorderFactory.createTitledBorder("Movement Details"));
		GridBagConstraints fieldsGbc = new GridBagConstraints();
		fieldsGbc.insets = new Insets(30, 30, 30, 30);
		fieldsGbc.anchor = GridBagConstraints.WEST;

		// Labels
		JLabel amountLabel = new JLabel("Amount (euro):");
		JLabel dateLabel = new JLabel("Date:");
		JLabel conceptLabel = new JLabel("Concept:");

		// Fields
		JComponent[][] fields = {
		    {amountLabel, amountTextField},
		    {dateLabel, dateTextField},
		    {conceptLabel, conceptTextField}
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

    public JTextField getAmountTextField() {
    	return amountTextField;
    }

    public JTextField getDateTextField() {
    	return dateTextField;
    }
    
    public JTextField getConceptTextField() {
    	return conceptTextField;
    }
    
    public JLabel getTotalIncomesLabel() {
    	return totalIncomesLabel;
    }
    
    public JLabel getTotalExpensesLabel() {
    	return totalExpensesLabel;
    }
    
    public JLabel getRemainingBalanceLabel() {
    	return remainingBalanceLabel;
    }

	public JComboBox<Object> getType() {
		return type;
	}

	public void setType(JComboBox<Object> type) {
		this.type = type;
	}

	public JComboBox<Object> getStatus() {
		return status;
	}

	public void setStatus(JComboBox<Object> status) {
		this.status = status;
	}

	public JTable getActivitiesTable() {
		return activitiesTable;
	}

	public void setActivitiesTable(JTable activitiesTable) {
		this.activitiesTable = activitiesTable;
	}

	public JTable getMovementsTable() {
		return movementsTable;
	}

	public void setMovementsTable(JTable movementsTable) {
		this.movementsTable = movementsTable;
	}
}