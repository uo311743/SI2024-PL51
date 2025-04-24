package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class RegisterPaymentView extends AbstractView {
	private JTable invoicesTable;
	private JTable paymentsTable;
    private JTextField amountTextField;
    private JTextField dateTextField;
    private JLabel totalInvoiceLabel;
    private JLabel totalPaymentsLabel;
    private JLabel remainingBalanceLabel;
    private JCheckBox compensationCheckBox;
    
    public RegisterPaymentView() { super("Register Payment", 1500, 800); }
    
    @Override
    protected void initialize()
    {
    	this.invoicesTable = new JTable();
    	this.paymentsTable = new JTable();
    	this.amountTextField = new JTextField("");
    	this.dateTextField = new JTextField("");
    	this.totalInvoiceLabel = new JLabel("");
    	this.totalPaymentsLabel = new JLabel("");
    	this.remainingBalanceLabel = new JLabel("");
    	this.compensationCheckBox = new JCheckBox("Compensation Payment");
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Reset");
        super.createButtonLowRight("Register");
    }
    
    @Override
    protected void configMainPanel()
    {
    	super.getMainPanel().setLayout(new BorderLayout());
		super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));

		JPanel topPanel = createTopPanel();
    	JPanel buttomPanel = createButtomPanel();
    	
    	// Create the split pane
    	JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, buttomPanel);
    	splitPane.setResizeWeight(0.5); // Equal distribution of extra space
    	splitPane.setDividerLocation(0.5); // Start at 50% position
    	splitPane.setContinuousLayout(true); // Smooth resizing
    	
    	// COMPLETELY REMOVE THE DIVIDER VISUALLY
    	splitPane.setDividerSize(0);  // This is the key line
    	splitPane.setBorder(null);

    	// Ensure panels take all available space
    	topPanel.setMinimumSize(new Dimension(300, 300)); // Minimum sizes
    	buttomPanel.setMinimumSize(new Dimension(100, 100));
    	topPanel.setPreferredSize(new Dimension(300, 300)); // Initial sizes
    	buttomPanel.setPreferredSize(new Dimension(300, 300));

		// Add to parent container
		super.getMainPanel().add(splitPane);
    }
    
    private JPanel createTopPanel()
	{
    	// Labels
        JLabel invoicesTableLabel = new JLabel("Select an Invoice to Register a Payment");
        invoicesTableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel paymentsTableLabel = new JLabel("Payments registered for the Invoice");
        paymentsTableLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Create title labels with bold text
        JLabel totalInvoiceTitle = new JLabel("Total Invoice (€): ");
        totalInvoiceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel totalPaymentsTitle = new JLabel("Total Payments (€): ");
        totalPaymentsTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel remainingBalanceTitle = new JLabel("Remaining Balance (€): ");
        remainingBalanceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Create value labels (dynamic data placeholders)
        this.totalInvoiceLabel = new JLabel("0.0");
        totalInvoiceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInvoiceLabel.setForeground(Color.BLUE); // Highlight in blue

        this.totalPaymentsLabel = new JLabel("0.0");
        this.totalPaymentsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.totalPaymentsLabel.setForeground(Color.GREEN.darker()); // Highlight in green

        this.remainingBalanceLabel = new JLabel("0.0");
        this.remainingBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.remainingBalanceLabel.setForeground(Color.RED.darker()); // Highlight in red

        // Add elements to the panel
        JPanel summaryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand
        gbc.insets = new Insets(0, 6, 0, 6); // Horizontal spacing only

        // Row 1: Titles
        gbc.gridy = 0;
        gbc.gridx = 0;
        summaryPanel.add(totalInvoiceTitle, gbc);

        gbc.gridx = 1;
        summaryPanel.add(totalPaymentsTitle, gbc);

        gbc.gridx = 2;
        summaryPanel.add(remainingBalanceTitle, gbc);

        // Row 2: Values
        gbc.gridy = 1;
        gbc.gridx = 0;
        summaryPanel.add(totalInvoiceLabel, gbc);

        gbc.gridx = 1;
        summaryPanel.add(totalPaymentsLabel, gbc);

        gbc.gridx = 2;
        summaryPanel.add(remainingBalanceLabel, gbc);
        
        UIManager.put("TitledBorder.font", new Font("Arial", Font.PLAIN, 14));

        // Set table properties
        invoicesTable.setName("Invoices");
        invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicesTable.setDefaultEditor(Object.class, null);
        // Visual improvements
        invoicesTable.setRowHeight(25); // Bigger rows
        invoicesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
        invoicesTable.setIntercellSpacing(new Dimension(30, 5));
	    
	    // Optional: Grid lines
        invoicesTable.setShowGrid(true);
        invoicesTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane invoicesTableScroll = new JScrollPane(invoicesTable);
        
        paymentsTable.setName("Payments");
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentsTable.setDefaultEditor(Object.class, null);
        // Visual improvements
        paymentsTable.setRowHeight(25); // Bigger rows
        paymentsTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
        paymentsTable.setIntercellSpacing(new Dimension(30, 5));
	    
	    // Optional: Grid lines
        paymentsTable.setShowGrid(true);
        paymentsTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane paymentsTableScroll = new JScrollPane(paymentsTable);

        // Invoices Panel
        JPanel invoicesPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel invoicesLabelPanel = new JPanel(new BorderLayout());
        invoicesLabelPanel.add(invoicesTableLabel, BorderLayout.CENTER);
        invoicesLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); 
        invoicesPanel.add(invoicesLabelPanel, BorderLayout.NORTH);
        invoicesPanel.add(invoicesTableScroll, BorderLayout.CENTER);
        
        // Payments Panel
        JPanel paymentsPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel paymentsLabelPanel = new JPanel(new BorderLayout());
        paymentsLabelPanel.add(paymentsTableLabel, BorderLayout.CENTER);
        paymentsLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); 
        paymentsPanel.add(paymentsLabelPanel, BorderLayout.NORTH);
        paymentsPanel.add(paymentsTableScroll, BorderLayout.CENTER);
        
        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 10, 10, 10); // Left/right padding

        // Add first table
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(invoicesPanel, gbc);

        // Add second table
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(paymentsPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(summaryPanel, gbc);
	
		return panel;
	}

    private JPanel createButtomPanel()
	{
    	// Main panel setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fields Panel with Border
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);  // Consistent padding
        gbc.weightx = 1.0;

        // Initialize text fields with proper sizes
        amountTextField = new JTextField(20);
        dateTextField = new JTextField(20);

        // Create labels with consistent font
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        JLabel amountLabel = new JLabel("Amount (€):");
        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        
        amountLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);

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
        fieldsPanel.add(amountTextField, gbc);
        
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
        fieldsPanel.add(dateTextField, gbc);
        
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
    
    public void adjustColumns() {
    	invoicesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	paymentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public JTextField getAmountTextField() {
    	return amountTextField;
    }

    public JTextField getDateTextField() {
    	return dateTextField;
    }

	public JTable getInvoicesTable() {
		return invoicesTable;
	}
	
	public JTable getPaymentsTable() {
		return paymentsTable;
	}
	public JLabel getTotalInvoiceLabel() { return totalInvoiceLabel; }
	public JLabel getTotalPaymentsLabel() { return totalPaymentsLabel; }
	public JLabel getRemainingBalanceLabel() { return remainingBalanceLabel; }

	public JCheckBox getCompensationCheckBox() {
		return compensationCheckBox;
	}
}