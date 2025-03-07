package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class RegisterIncomesExpensesView extends AbstractView{
	private JComboBox<Object> activityComboBox;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> statusComboBox;
    private JTextField conceptField;
    private JTextField dateField;
    private JTextField amountField;
    private JTextField receiptField;
    
    private JLabel receiptLabel;
    
    private JPanel formPanel;
    private JPanel summaryPanel;
    private JPanel errorPanel;
    
    public RegisterIncomesExpensesView()
    {
    	super("Register Income/Expense");
    }
    
    @Override
    protected void initialize()
    {
    	activityComboBox = new JComboBox<>();
    	typeComboBox = new JComboBox<>(new String[]{"Expense", "Income"});
    	statusComboBox = new JComboBox<>(new String[]{"Paid", "Estimated"});
    	conceptField = new JTextField("", 1);
    	receiptField = new JTextField("", 1);
    	amountField = new JTextField("0.00", 1);
    	dateField = new JTextField("YYYY-MM-DD", 1);
    	formPanel = new JPanel();
    	summaryPanel = new JPanel();
    	errorPanel = new JPanel();
    	receiptLabel = new JLabel();
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Clear");
        super.createButtonLowRight("Register");
    }
    
    @Override
    protected void configMainPanel()
    {
    	super.getMainPanel().setLayout(new GridLayout(2, 1));
    	
    	super.setFrameTitle("Register " + getType());
    	
    	receiptLabel.setText("Receipt Number:");
    	
    	amountField.setForeground(Color.GRAY); // Set placeholder color initially
    	amountField.setFont(amountField.getFont().deriveFont(Font.ITALIC));
    	
    	amountField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (amountField.getText().equals("0.00")) {
                    amountField.setText("");
                    amountField.setForeground(Color.BLACK); // Normal text color
                    amountField.setFont(amountField.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (amountField.getText().isEmpty()) {
                    amountField.setText("0.00");
                    amountField.setForeground(Color.GRAY); // Placeholder color
                    amountField.setFont(amountField.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    	
    	dateField.setForeground(Color.GRAY); // Set placeholder color initially
    	dateField.setFont(dateField.getFont().deriveFont(Font.ITALIC));

    	dateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateField.getText().equals("YYYY-MM-DD")) {
                	dateField.setText("");
                	dateField.setForeground(Color.BLACK); // Normal text color
                	dateField.setFont(dateField.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (dateField.getText().isEmpty()) {
                	dateField.setText("YYYY-MM-DD");
                	dateField.setForeground(Color.GRAY); // Placeholder color
                	dateField.setFont(dateField.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    	
    	configureFormPanel();
    }

    private void configureFormPanel()
    {
    	// Set layout for the form panel
    	formPanel.setLayout(new GridLayout(0, 2)); // 0 rows means "as many as needed", 2 columns
    	formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
               
        // Add components to the main panel
    	formPanel.add(new JLabel("Type:"));
    	formPanel.add(typeComboBox);
    	
    	formPanel.add(new JLabel("Status:"));
    	formPanel.add(statusComboBox);
    	
    	formPanel.add(new JLabel("Activity:"));
    	formPanel.add(activityComboBox);
    	
    	formPanel.add(new JLabel("Date:"));
    	formPanel.add(dateField);

    	formPanel.add(new JLabel("Amount (EUR):"));
    	formPanel.add(amountField);
    	
    	formPanel.add(new JLabel("Concept:"));
    	formPanel.add(conceptField);
    	
    	formPanel.add(receiptLabel);
    	formPanel.add(receiptField);
    	
    	super.getMainPanel().add(formPanel, BorderLayout.CENTER);
    }
    
    public void configureSummaryPanel()
    {
    	JLabel headerLabel = new JLabel("<html><div style='font-size:14px; font-weight:bold;'>Summary of " + getType() + " Registered</div></html>", SwingConstants.CENTER);
    	headerLabel.setFont(new Font("Arial", Font.BOLD, 11));
    	headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    	JLabel summaryLabel = new JLabel("<html><div style='font-size:11px; padding:2px 5px;'>" +
    		    "<b>Type:</b> " + getType() + "<br>" +
    		    "<b>Status:</b> " + getStatus() + "<br>" +
    		    "<b>Activity:</b> " + getActivity() + "<br>" +
    		    "<b>Date:</b> " + getDate() + "<br>" +
    		    "<b>Amount (EUR):</b> " + getAmount() + "<br>" +
    		    "<b>Concept:</b> " + getConcept() + "<br>" +
    		    "<b>Receipt Number:</b> " + getReceipt() + "<br>" +
    		    "</div></html>", SwingConstants.CENTER);

    	summaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    	summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	
    	clearFields();
    	summaryPanel.removeAll();
    	summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
    	summaryPanel.add(headerLabel, BorderLayout.NORTH);
    	summaryPanel.add(summaryLabel, BorderLayout.CENTER);
    	summaryPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    	
    	super.getMainPanel().add(summaryPanel);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }
    
    public void showError(String errorMessage) {
    	clearFields();

    	errorPanel.setLayout(new BorderLayout());
    	errorPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Error header
        JLabel errorHeader = new JLabel("<html><div style='color:red; font-size:14px; font-weight:bold;'>Error</div></html>", SwingConstants.CENTER);
        errorHeader.setFont(new Font("Arial", Font.BOLD, 11));
        errorHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Error message
        JLabel errorLabel = new JLabel("<html><div style='color:red; font-size:11px; padding:2px 5px;'>" + errorMessage + "</div></html>", SwingConstants.CENTER);
        
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.add(errorHeader, BorderLayout.NORTH); // Add header
        errorPanel.add(errorLabel, BorderLayout.CENTER); // Add error message
        
        super.getMainPanel().add(errorPanel);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }

    public void clearFields() {
    	super.getMainPanel().remove(summaryPanel);
    	super.getMainPanel().remove(errorPanel);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    	summaryPanel.removeAll();
    	errorPanel.removeAll();
    	
    	// Reset JComboBox to first item or empty selection
        activityComboBox.setSelectedIndex(0);

        // Reset amount field with placeholder effect
        amountField.setText("0.00");
        amountField.setForeground(Color.GRAY);
        amountField.setFont(amountField.getFont().deriveFont(Font.ITALIC));

        // Reset Concept field
        conceptField.setText("");
        
        // Reset Receipt field
        receiptField.setText("");

        // Reset date field with placeholder effect
        dateField.setText("YYYY-MM-DD");
        dateField.setForeground(Color.GRAY);
        dateField.setFont(dateField.getFont().deriveFont(Font.ITALIC));
    }
    
    public void changeTitlePanel(String title) {
    	super.setFrameTitle(title);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }
    
    public void concealReceiptField() {
    	formPanel.remove(receiptLabel);
    	formPanel.remove(receiptField);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }
    
    public void showReceiptField() {
    	formPanel.add(receiptLabel);
    	formPanel.add(receiptField);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }
    
    public String getActivity() {
        return (String) activityComboBox.getSelectedItem();
    }
    
    public String getType() {
    	return (String) typeComboBox.getSelectedItem();
    }
    
    public String getStatus() {
    	return (String) statusComboBox.getSelectedItem();
    }
    
    public JComboBox<Object> getActivities() {
    	return activityComboBox;
    }

    public String getAmount() {
    	if (amountField.getText().equals("0.00")) {
    		return "";
    	}
    	
        return amountField.getText();
    }

    public String getDate() {
    	if (dateField.getText().equals("YYYY-MM-DD")) {
    		return "";
    	}
    	
        return dateField.getText();
    }
    
    public String getConcept() {
    	return conceptField.getText();
    }
    
    public String getReceipt() {
    	return receiptField.getText();
    }

	public JComboBox<String> getTypeComboBox() {
		return typeComboBox;
	}

	public void setTypeComboBox(JComboBox<String> typeComboBox) {
		this.typeComboBox = typeComboBox;
	}

	public JComboBox<String> getStatusComboBox() {
		return statusComboBox;
	}

	public void setStatusComboBox(JComboBox<String> statusComboBox) {
		this.statusComboBox = statusComboBox;
	}

	public JTextField getConceptField() {
		return conceptField;
	}

	public void setConceptField(JTextField conceptField) {
		this.conceptField = conceptField;
	}

	public JTextField getReceiptField() {
		return receiptField;
	}

	public void setReceiptField(JTextField receiptField) {
		this.receiptField = receiptField;
	}
}