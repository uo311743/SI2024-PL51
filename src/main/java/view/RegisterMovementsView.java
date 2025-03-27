package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class RegisterMovementsView extends AbstractView {
	private JComboBox<Object> type;
	private JComboBox<Object> status;
	private JTable activitiesTable;
	private JTable incomesExpensesTable;
	private JTable movementsTable;
    private JTextField amountTextField;
    private JTextField dateTextField;
    private JTextField conceptTextField;
    private JLabel estimatedLabel;
    private JLabel paidLabel;
    private JLabel incomesExpensesLabel;
    private JLabel movementLabel;
    private JCheckBox compensationCheckBox;
    
    public RegisterMovementsView() { super("Register Movement"); }
    
    @Override
    protected void initialize()
    {
    	this.setType(new JComboBox<>());
    	this.setStatus(new JComboBox<>(new String[] { "estimated", "paid" }));
    	this.setMovementsTable(new JTable());
    	this.setActivitiesTable(new JTable());
    	this.setIncomesExpensesTable(new JTable());
    	this.setCompensationCheckBox(new JCheckBox("Register Compensation Movement"));
    	this.amountTextField = new JTextField("");
    	this.dateTextField = new JTextField("");
    	this.conceptTextField = new JTextField("");
    	this.estimatedLabel = new JLabel("");
    	this.paidLabel = new JLabel("");
    	this.incomesExpensesLabel = new JLabel("Select Income/Expense to see the Movements");
    	this.movementLabel = new JLabel("Movements registered for the selected Income/Expense");
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Reset");
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
        JLabel typeLabel = new JLabel("Type:");
        JLabel statusLabel = new JLabel("Status:");
        
        // Create title labels with bold text
        JLabel estimatedTitle = new JLabel("Estimated (euro): ");
        estimatedTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel paidTitle = new JLabel("Paid (euro): ");
        paidTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a panel with GridLayout to align labels properly
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, spacing of 10px
        
        // Create value labels (dynamic data placeholders)
        this.estimatedLabel = new JLabel("0.00");
        estimatedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estimatedLabel.setForeground(Color.BLUE); // Highlight in blue

        this.paidLabel = new JLabel("0.00");
        this.paidLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.paidLabel.setForeground(Color.GREEN.darker()); // Highlight in green

        // Add elements to the panel
        summaryPanel.add(estimatedTitle);
        summaryPanel.add(this.estimatedLabel);
        summaryPanel.add(paidTitle);
        summaryPanel.add(this.paidLabel);

        // Wrap it in a titled border for a professional look
        JPanel borderedPanel = new JPanel(new BorderLayout());
        borderedPanel.setBorder(BorderFactory.createTitledBorder("Movement Summary"));
        borderedPanel.add(summaryPanel);

        // Set table properties
        activitiesTable.setName("Activites");
        activitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activitiesTable.setDefaultEditor(Object.class, null);
        JScrollPane activitiesTableScroll = new JScrollPane(activitiesTable);
        
        incomesExpensesTable.setName("Incomes/Expenses");
        incomesExpensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomesExpensesTable.setDefaultEditor(Object.class, null);
        JScrollPane incomesExpensesTableScroll = new JScrollPane(incomesExpensesTable);
        
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
        
        // Incomes/Exoenses Panel
        JPanel incomesExpensesPanel = new JPanel(new BorderLayout());
        incomesExpensesPanel.add(this.getIncomesExpensesLabel(), BorderLayout.NORTH);
        incomesExpensesPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        incomesExpensesPanel.add(incomesExpensesTableScroll, BorderLayout.CENTER);
        
        // Movement Panel
        JPanel movementPanel = new JPanel(new BorderLayout());
        movementPanel.add(this.getMovementLabel(), BorderLayout.NORTH);
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
        
        // Compensation Movement Check Panel
        JPanel compensationCheckPanel = new JPanel(new BorderLayout());
        compensationCheckPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        compensationCheckPanel.add(compensationCheckBox);

        // Add panels in correct order
        panel.add(compensationCheckPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(typePanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(satusPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(activitiesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(incomesExpensesPanel);
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
    
    public JLabel getEstimatedLabel() {
    	return estimatedLabel;
    }
    
    public JLabel getPaidLabel() {
    	return paidLabel;
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

	public JTable getIncomesExpensesTable() {
		return incomesExpensesTable;
	}

	public void setIncomesExpensesTable(JTable invoicesExpensesTable) {
		this.incomesExpensesTable = invoicesExpensesTable;
	}

	public JLabel getIncomesExpensesLabel() {
		return incomesExpensesLabel;
	}
	
	public JLabel getMovementLabel() {
		return movementLabel;
	}

	public JCheckBox getCompensationCheckBox() {
		return compensationCheckBox;
	}

	public void setCompensationCheckBox(JCheckBox compensationCheckBox) {
		this.compensationCheckBox = compensationCheckBox;
	}
}