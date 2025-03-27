package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegisterPaymentView extends AbstractView {
	private JTable invoicesTable;
	private JTable paymentsTable;
    private JTextField amountTextField;
    private JTextField dateTextField;
    private JLabel totalInvoiceLabel;
    private JLabel totalPaymentsLabel;
    private JLabel remainingBalanceLabel;
    private JCheckBox compensationCheckBox;
    
    public RegisterPaymentView() { super("Register Payment"); }
    
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
    	this.compensationCheckBox = new JCheckBox("Register Compensation Payment");
    	
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
    	
		amountTextField.setForeground(Color.GRAY); // Set placeholder color initially
		amountTextField.setFont(amountTextField.getFont().deriveFont(Font.ITALIC));
    	
		amountTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (amountTextField.getText().equals("0.00")) {
                	amountTextField.setText("");
                	amountTextField.setForeground(Color.BLACK); // Normal text color
                	amountTextField.setFont(amountTextField.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (amountTextField.getText().isEmpty()) {
                	amountTextField.setText("0.00");
                	amountTextField.setForeground(Color.GRAY); // Placeholder color
                	amountTextField.setFont(amountTextField.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    	
		dateTextField.setForeground(Color.GRAY); // Set placeholder color initially
		dateTextField.setFont(dateTextField.getFont().deriveFont(Font.ITALIC));

    	dateTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateTextField.getText().equals("YYYY-MM-DD")) {
                	dateTextField.setText("");
                	dateTextField.setForeground(Color.BLACK); // Normal text color
                	dateTextField.setFont(dateTextField.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (dateTextField.getText().isEmpty()) {
                	dateTextField.setText("YYYY-MM-DD");
                	dateTextField.setForeground(Color.GRAY); // Placeholder color
                	dateTextField.setFont(dateTextField.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }
    
    private JPanel createLeftPanel()
	{
    	// Labels
        JLabel invoicesTableLabel = new JLabel("Select an Invoice to Register a Payment");
        JLabel paymentsTableLabel = new JLabel("Payments registered for the invoice");
        
        // Create a panel with GridLayout to align labels properly
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, spacing of 10px

        // Create title labels with bold text
        JLabel totalInvoiceTitle = new JLabel("Total Invoice Amount (euro): ");
        totalInvoiceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel totalPaymentsTitle = new JLabel("Total Payments (euro): ");
        totalPaymentsTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel remainingBalanceTitle = new JLabel("Remaining Balance (euro): ");
        remainingBalanceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Create value labels (dynamic data placeholders)
        this.totalInvoiceLabel = new JLabel("0.00");
        totalInvoiceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInvoiceLabel.setForeground(Color.BLUE); // Highlight in blue

        this.totalPaymentsLabel = new JLabel("0.00");
        this.totalPaymentsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.totalPaymentsLabel.setForeground(Color.GREEN.darker()); // Highlight in green

        this.remainingBalanceLabel = new JLabel("0.00");
        this.remainingBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.remainingBalanceLabel.setForeground(Color.RED); // Highlight in red

        // Add elements to the panel
        summaryPanel.add(totalInvoiceTitle);
        summaryPanel.add(this.totalInvoiceLabel);
        summaryPanel.add(totalPaymentsTitle);
        summaryPanel.add(this.totalPaymentsLabel);
        summaryPanel.add(remainingBalanceTitle);
        summaryPanel.add(this.remainingBalanceLabel);

        // Wrap it in a titled border for a professional look
        JPanel borderedPanel = new JPanel(new BorderLayout());
        borderedPanel.setBorder(BorderFactory.createTitledBorder("Payment Summary"));
        borderedPanel.add(summaryPanel);

        // Set table properties
        invoicesTable.setName("Invoices");
        invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicesTable.setDefaultEditor(Object.class, null);
        JScrollPane invoicesTableScroll = new JScrollPane(invoicesTable);
        
        paymentsTable.setName("Payments");
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentsTable.setDefaultEditor(Object.class, null);
        JScrollPane paymentsTableScroll = new JScrollPane(paymentsTable);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Invoices Panel
        JPanel invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.add(invoicesTableLabel, BorderLayout.NORTH);
        invoicesPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        invoicesPanel.add(invoicesTableScroll, BorderLayout.CENTER);
        
        // Payments Panel
        JPanel paymentsPanel = new JPanel(new BorderLayout());
        paymentsPanel.add(paymentsTableLabel, BorderLayout.NORTH);
        paymentsPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        paymentsPanel.add(paymentsTableScroll, BorderLayout.CENTER);
        
        // Compensation Payment Check Panel
        JPanel compensationCheckPanel = new JPanel(new BorderLayout());
        compensationCheckPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        compensationCheckPanel.add(compensationCheckBox);

        // Add panels in correct order
        panel.add(compensationCheckPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(invoicesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(paymentsPanel);
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
		fieldsPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
		GridBagConstraints fieldsGbc = new GridBagConstraints();
		fieldsGbc.insets = new Insets(30, 30, 30, 30);
		fieldsGbc.anchor = GridBagConstraints.WEST;

		// Labels
		JLabel amountLabel = new JLabel("Amount (euro):");
		JLabel paymentDateLabel = new JLabel("Date Received:");

		// Fields
		JComponent[][] fields = {
		    {amountLabel, amountTextField},
		    {paymentDateLabel, dateTextField}
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