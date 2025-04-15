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
    private JTextField amountTextField1;
    private JTextField dateTextField1;
    private JTextArea conceptTextField1;
    private JTextField amountTextField2;
    private JTextField dateTextField2;
    private JTextArea conceptTextField2;
    private JLabel estimatedIncomesLabel;
    private JLabel paidIncomesLabel;
    private JLabel estimatedExpensesLabel;
    private JLabel paidExpensesLabel;
    private JLabel remainingBalanceLabel;
    private JCheckBox compensationCheckBox;
    private JButton btnIncomesExpenses;
    private JButton btnMovements;
    
    public RegisterMovementsView() { super("Register Movement"); }
    
    @Override
    protected void initialize()
    {
    	this.setType(new JComboBox<>());
    	this.setStatus(new JComboBox<>(new String[] { "estimated", "paid" }));
    	this.setMovementsTable(new JTable());
    	this.setActivitiesTable(new JTable());
    	this.setIncomesExpensesTable(new JTable());
    	this.setCompensationCheckBox(new JCheckBox("Compensation Movement"));
    	this.amountTextField1 = new JTextField("");
    	this.dateTextField1 = new JTextField("");
    	this.conceptTextField1 = new JTextArea(3, 20);
    	this.amountTextField2 = new JTextField("");
    	this.dateTextField2 = new JTextField("");
    	this.conceptTextField2 = new JTextArea(3, 20);
    	this.estimatedIncomesLabel = new JLabel("");
    	this.paidIncomesLabel = new JLabel("");
    	this.estimatedExpensesLabel = new JLabel("");
    	this.paidExpensesLabel = new JLabel("");
    	this.remainingBalanceLabel = new JLabel("");
    	this.btnIncomesExpenses = new JButton("Register Income/Expense");
    	this.btnMovements = new JButton("Register Movement");
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Reset");
        //super.createButtonLowRight("Register");
    }
    
    @Override
    protected void configMainPanel()
    {
    	super.getMainPanel().setLayout(new BorderLayout());
    	super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));

    	JPanel leftPanel = createLeftPanel();
    	JPanel rightPanel = createRightPanel();

    	// Create the split pane
    	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
    	splitPane.setResizeWeight(0.5); // Equal distribution of extra space
    	splitPane.setDividerLocation(0.5); // Start at 50% position
    	splitPane.setBorder(null);
    	splitPane.setContinuousLayout(true); // Smooth resizing

    	// Ensure panels take all available space
    	leftPanel.setMinimumSize(new Dimension(100, 100)); // Minimum sizes
    	rightPanel.setMinimumSize(new Dimension(100, 100));
    	leftPanel.setPreferredSize(new Dimension(300, 300)); // Initial sizes
    	rightPanel.setPreferredSize(new Dimension(300, 300));

		// Add to parent container
		super.getMainPanel().add(splitPane);
    }
    
    private JPanel createLeftPanel()
	{   
        // Create title labels with bold text
        JLabel estimatedIncomesTitle = new JLabel("Estimated Incomes (€): ");
        estimatedIncomesTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel paidIncomesTitle = new JLabel("Paid Incomes (€): ");
        paidIncomesTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel estimatedExpensesTitle = new JLabel("Estimated Expenses (€): ");
        estimatedExpensesTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel paidExpensesTitle = new JLabel("Paid Expenses (€): ");
        paidExpensesTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel remainingBalanceTitle = new JLabel("Total amount paid (€): ");
        remainingBalanceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a panel with GridLayout to align labels properly
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 6, 6)); // 3 rows, 2 columns, spacing of 10px
        
        // Create value labels (dynamic data placeholders)
        this.estimatedIncomesLabel = new JLabel("0.00");
        estimatedIncomesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estimatedIncomesLabel.setForeground(Color.BLUE); // Highlight in blue

        this.paidIncomesLabel = new JLabel("0.00");
        this.paidIncomesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.paidIncomesLabel.setForeground(Color.GREEN.darker()); // Highlight in green
        
        this.estimatedExpensesLabel = new JLabel("0.00");
        estimatedExpensesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estimatedExpensesLabel.setForeground(Color.BLUE); // Highlight in blue

        this.paidExpensesLabel = new JLabel("0.00");
        this.paidExpensesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.paidExpensesLabel.setForeground(Color.GREEN.darker()); // Highlight in green
        
        this.remainingBalanceLabel = new JLabel("0.00");
        this.remainingBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.remainingBalanceLabel.setForeground(Color.RED.darker()); // Highlight in green

        // Add elements to the panel
        summaryPanel.add(estimatedIncomesTitle);
        summaryPanel.add(this.estimatedIncomesLabel);
        summaryPanel.add(estimatedExpensesTitle);
        summaryPanel.add(this.estimatedExpensesLabel);
        summaryPanel.add(paidIncomesTitle);
        summaryPanel.add(this.paidIncomesLabel);
        summaryPanel.add(paidExpensesTitle);
        summaryPanel.add(this.paidExpensesLabel);
        summaryPanel.add(remainingBalanceTitle);
        summaryPanel.add(remainingBalanceLabel);
        
        UIManager.put("TitledBorder.font", new Font("Arial", Font.PLAIN, 14));
        
        // Wrap it in a titled border for a professional look
        JPanel borderedPanel = new JPanel(new BorderLayout());
        borderedPanel.setBorder(BorderFactory.createTitledBorder("Movement Summary"));
        borderedPanel.add(summaryPanel);

        // Set table properties
        JLabel activitiesTableLabel = new JLabel("Select an Activity to see the Incomes and Expenses");
        activitiesTableLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        activitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activitiesTable.setDefaultEditor(Object.class, null);
        // Visual improvements
	    activitiesTable.setRowHeight(25); // Bigger rows
	    activitiesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
	    activitiesTable.setIntercellSpacing(new Dimension(10, 5));
	    
	    // Optional: Grid lines
	    activitiesTable.setShowGrid(true);
	    activitiesTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane activitiesTableScroll = new JScrollPane(activitiesTable);
        
        JLabel incomeExpensesTableLabel = new JLabel("Select an Income/Expense to register a Movement");
        incomeExpensesTableLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        incomesExpensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomesExpensesTable.setDefaultEditor(Object.class, null);
        // Visual improvements
        incomesExpensesTable.setRowHeight(25); // Bigger rows
        incomesExpensesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
        //incomesExpensesTable.setPreferredScrollableViewportSize(new Dimension(300, 100)); // Bigger view
	    
	    // Optional: Intercell spacing for better readability
        incomesExpensesTable.setIntercellSpacing(new Dimension(10, 5));
	    
	    // Optional: Grid lines
        incomesExpensesTable.setShowGrid(true);
        incomesExpensesTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane incomesExpensesTableScroll = new JScrollPane(incomesExpensesTable);
        
        JLabel movementsTableLabel = new JLabel("Movements registered for the selected Income/Expense");
        movementsTableLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        movementsTable.setDefaultEditor(Object.class, null);
        // Visual improvements
        movementsTable.setRowHeight(25); // Bigger rows
        movementsTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
        movementsTable.setIntercellSpacing(new Dimension(10, 5));
	    
	    // Optional: Grid lines
        movementsTable.setShowGrid(true);
        movementsTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane movementsTableScroll = new JScrollPane(movementsTable);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Left/right padding

        // Activity Panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel activitiesLabelPanel = new JPanel(new BorderLayout());
        activitiesLabelPanel.add(activitiesTableLabel, BorderLayout.CENTER);
        activitiesLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Bottom padding

        activitiesPanel.add(activitiesLabelPanel, BorderLayout.NORTH);
        activitiesPanel.add(activitiesTableScroll, BorderLayout.CENTER);
        
        // Incomes/Exoenses Panel
        JPanel incomesExpensesPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel incomesExpensesLabelPanel = new JPanel(new BorderLayout());
        incomesExpensesLabelPanel.add(incomeExpensesTableLabel, BorderLayout.CENTER);
        incomesExpensesLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Bottom padding

        incomesExpensesPanel.add(incomesExpensesLabelPanel, BorderLayout.NORTH);
        incomesExpensesPanel.add(incomesExpensesTableScroll, BorderLayout.CENTER);
        
        // Movement Panel
        JPanel movementPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(movementsTableLabel, BorderLayout.CENTER);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Bottom padding

        movementPanel.add(labelPanel, BorderLayout.NORTH);
        movementPanel.add(movementsTableScroll, BorderLayout.CENTER);

        panel.add(activitiesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(incomesExpensesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(movementPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(summaryPanel); // Adds spacing
	
		return panel;
	}
    
    public void adjustColumns() {
    	activitiesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	incomesExpensesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	movementsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private JPanel createRightPanel() {
    	JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        // Add panels
        containerPanel.add(createIncomesExpensesPanel());

        // Configure button to stretch
        btnIncomesExpenses.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIncomesExpenses.setMaximumSize(new Dimension(Short.MAX_VALUE, btnIncomesExpenses.getPreferredSize().height));
        containerPanel.add(btnIncomesExpenses);

        // Add second panel
        containerPanel.add(createMovementsPanel());

        // Configure second button to stretch
        btnMovements.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMovements.setMaximumSize(new Dimension(Short.MAX_VALUE, btnMovements.getPreferredSize().height));
        containerPanel.add(btnMovements);
        
        return containerPanel;
    }
    
    public JPanel createIncomesExpensesPanel() {
        // Main panel setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fields Panel with Border
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Income/Expense Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);  // Consistent padding
        gbc.weightx = 1.0;

        // Initialize text fields with proper sizes
        amountTextField1 = new JTextField(20);
        dateTextField1 = new JTextField(20);
        conceptTextField1 = new JTextArea(10, 30);  // Increased to 8 rows
        conceptTextField1.setLineWrap(true);
        conceptTextField1.setWrapStyleWord(true);
        conceptTextField1.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane conceptScrollPane = new JScrollPane(conceptTextField1);
        conceptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        conceptScrollPane.setPreferredSize(new Dimension(450, 250));  // Larger size

        // Create labels with consistent font
        JLabel typeLabel = new JLabel("Type:");
        JLabel statusLabel = new JLabel("Status:");
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        JLabel amountLabel = new JLabel("Amount (€):");
        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        JLabel conceptLabel = new JLabel("Concept:");
        
        amountLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);
        conceptLabel.setFont(labelFont);
        typeLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        // Add components in a clean grid
        int row = 0;
        
        // Type row
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(type, gbc);
        
        // Status row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(status, gbc);
        
        // Amount row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(amountLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(amountTextField1, gbc);
        
        // Date row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(dateTextField1, gbc);
        
        // Concept row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(conceptLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(conceptTextField1, gbc);
        
        // Add glue to push components to top
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        fieldsPanel.add(Box.createGlue(), gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    public JPanel createMovementsPanel() {
        // Main panel setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fields Panel with Border
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Movement Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);  // Consistent padding
        gbc.weightx = 1.0;

        // Initialize text fields with proper sizes
        amountTextField2 = new JTextField(20);
        dateTextField2 = new JTextField(20);
        conceptTextField2 = new JTextArea(10, 13);
        JScrollPane conceptScrollPane = new JScrollPane(conceptTextField2);
        conceptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Create labels with consistent font
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        JLabel amountLabel = new JLabel("Amount (€):");
        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        JLabel conceptLabel = new JLabel("Concept:");
        
        amountLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);
        conceptLabel.setFont(labelFont);

        // Add components in a clean grid
        int row = 0;
        
        // CheckBox row
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(compensationCheckBox, gbc);
        gbc.gridwidth = 1;
        
        // Amount row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(amountLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(amountTextField2, gbc);
        
        // Date row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(dateTextField2, gbc);
        
        // Concept row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(conceptLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(conceptTextField2, gbc);
        
        // Add glue to push components to top
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        fieldsPanel.add(Box.createGlue(), gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    public JTextField getAmountTextField1() {
    	return amountTextField1;
    }

    public JTextField getDateTextField1() {
    	return dateTextField1;
    }
    
    public JTextArea getConceptTextField1() {
    	return conceptTextField1;
    }
    
    public JTextField getAmountTextField2() {
    	return amountTextField2;
    }

    public JTextField getDateTextField2() {
    	return dateTextField2;
    }
    
    public JTextArea getConceptTextField2() {
    	return conceptTextField2;
    }
    
    public JLabel getEstimatedIncomesLabel() {
    	return estimatedIncomesLabel;
    }
    
    public JLabel getPaidIncomesLabel() {
    	return paidIncomesLabel;
    }
    
    public JLabel getEstimatedExpensesLabel() {
    	return estimatedExpensesLabel;
    }
    
    public JLabel getPaidExpensesLabel() {
    	return paidExpensesLabel;
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
	
	public JLabel getRemainingBalanceLabel() {
		return remainingBalanceLabel;
	}

	public JCheckBox getCompensationCheckBox() {
		return compensationCheckBox;
	}

	public void setCompensationCheckBox(JCheckBox compensationCheckBox) {
		this.compensationCheckBox = compensationCheckBox;
	}
	
	public JButton getBtnIncomesExpenses() {
		return this.btnIncomesExpenses;
	}
	
	public JButton getBtnMovements() {
		return this.btnMovements;
	}
}