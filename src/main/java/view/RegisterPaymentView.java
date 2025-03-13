package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegisterPaymentView extends AbstractView {
	private JTable invoicesTable;
	private JTable sponsorsTable;
    private JTextField amountTextField;
    private JTextField dateTextField;
    
    public RegisterPaymentView() { super("Register Payment"); }
    
    @Override
    protected void initialize()
    {
    	this.invoicesTable = new JTable();
    	this.sponsorsTable = new JTable();
    	this.amountTextField = new JTextField("");
    	this.dateTextField = new JTextField("");
    	
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
        JLabel activityLabel = new JLabel("Select a Sponsor to see the Invoices");
        JLabel invoicesTableLabel = new JLabel("Select an Invoice to Register a Payment");

        // Set table properties
        sponsorsTable.setName("Sponsors");
        sponsorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sponsorsTable.setDefaultEditor(Object.class, null);
        JScrollPane activitiesTableScroll = new JScrollPane(sponsorsTable);

        invoicesTable.setName("Invoices");
        invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicesTable.setDefaultEditor(Object.class, null);
        JScrollPane invoicesTableScroll = new JScrollPane(invoicesTable);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Activity Panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.add(activityLabel, BorderLayout.NORTH);
        activitiesPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        activitiesPanel.add(activitiesTableScroll, BorderLayout.CENTER);

        // Invoices Panel
        JPanel invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.add(invoicesTableLabel, BorderLayout.NORTH);
        invoicesPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        invoicesPanel.add(invoicesTableScroll, BorderLayout.CENTER);

        // Add panels in correct order
        panel.add(activitiesPanel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing
        panel.add(invoicesPanel);
	
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

	public JTable getSponsorsTable() {
		return sponsorsTable;
	}
}