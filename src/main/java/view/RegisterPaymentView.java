package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import util.SwingUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegisterPaymentView extends AbstractView {
    private JComboBox<String> activityComboBox;
    private JTextField amountField;
    private JTextField nifField;
    private JTextField dateField;
    private JTextField invoiceIdField;
    
    private String currentActivities[];
    
    private JPanel formPanel;
    private JPanel summaryPanel;
    
    private Boolean summaryConcealed;
    
    public RegisterPaymentView()
    {
    	super("Register Payment");
    	initialize();
    	//configMainPanel();
    }
    
    @Override
    protected void initialize()
    {
    	currentActivities = new String[] {""};
    	activityComboBox = new JComboBox<>(currentActivities);
    	amountField = new JTextField("0.00", 1);
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
        
    	nifField = new JTextField("", 1);
    	dateField = new JTextField("YYYY-MM-DD", 1);
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
        
    	invoiceIdField = new JTextField("", 1);
    	formPanel = new JPanel();
    	summaryPanel = new JPanel();
    	summaryConcealed = true;
    	
    	super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Register");
    }
    
    @Override
    protected void configMainPanel()
    {
    	super.getMainPanel().setLayout(new GridLayout(2, 1));
    	
    	configureFormPanel();
        
        super.getButtonLowLeft().addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e)
        	{
        		clearFields();
        	}
        });
    }
    
    /*public void setActionRegisterButton(ActionListener a) {
    	super.getButtonLowRight().addActionListener(a);
    }*/
    
    public void initializeActivities(String[] activities) {
    	currentActivities = activities;
    	activityComboBox.removeAllItems();
    	
    	for (String a : activities)
    	{
    		activityComboBox.addItem(a);
    	}
    	
    	resetMainPanel();
    	configMainPanel();
    }

    private void configureFormPanel()
    {
    	// Set layout for the form panel
    	formPanel.setLayout(new GridLayout(0, 2)); // 0 rows means "as many as needed", 2 columns
    	formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
               
        // Add components to the main panel
    	formPanel.add(new JLabel("Activity:"));
    	formPanel.add(activityComboBox);

    	formPanel.add(new JLabel("Amount (EUR):"));
    	formPanel.add(amountField);

    	formPanel.add(new JLabel("NIF:"));
    	formPanel.add(nifField);

    	formPanel.add(new JLabel("Date:"));
    	formPanel.add(dateField);

    	formPanel.add(new JLabel("Invoice ID:"));
    	formPanel.add(invoiceIdField);
    	
    	super.getMainPanel().add(formPanel, BorderLayout.CENTER);
    }
    
    public void configureSummaryPanel()
    {
    	JLabel summaryLabel = new JLabel("<html><div style='padding:10px;'>" +
    	        "<b>Activity:</b> " + getActivity() + "<br>" +
    	        "<b>Amount (EUR):</b> " + getAmount() + "<br>" +
    	        "<b>NIF:</b> " + getNIF() + "<br>" +
    	        "<b>Date:</b> " + getDate() + "<br>" +
    	        "<b>Invoice ID:</b> " + getInvoiceId() +
    	        "</div></html>");

    	summaryPanel.removeAll();
    	summaryPanel.setLayout(new BorderLayout());
    	summaryPanel.add(summaryLabel, BorderLayout.CENTER);
    	summaryPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    	
    	super.getMainPanel().add(summaryPanel);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    }

    public void clearFields() {
    	// Reset JComboBox to first item or empty selection
        activityComboBox.setSelectedIndex(0);

        // Reset amount field with placeholder effect
        amountField.setText("0.00");
        amountField.setForeground(Color.GRAY);
        amountField.setFont(amountField.getFont().deriveFont(Font.ITALIC));

        // Reset NIF field
        nifField.setText("");

        // Reset date field with placeholder effect
        dateField.setText("YYYY-MM-DD");
        dateField.setForeground(Color.GRAY);
        dateField.setFont(dateField.getFont().deriveFont(Font.ITALIC));

        // Reset Invoice ID field
        invoiceIdField.setText("");

        // Clear the summary panel if needed
        summaryPanel.removeAll();
        summaryPanel.revalidate();
        summaryPanel.repaint();
    }
    
    public String getActivity() {
        return (String) activityComboBox.getSelectedItem();
    }

    public String getAmount() {
        return amountField.getText(); //Double.parseDouble(amountField.getText());
    }

    public String getNIF() {
        return nifField.getText();
    }

    public String getDate() {
        return dateField.getText();
    }

    public String getInvoiceId() {
        return invoiceIdField.getText();
    }

    public void setInvoiceId(String id) {
        invoiceIdField.setText(id);
    }
    
    public void setSummaryConcealed() {
    	summaryConcealed = !summaryConcealed;
    }
    
    public Boolean summaryConcealed() {
    	return summaryConcealed;
    }
    
    public void display()
    {
    	super.setVisible();
    }
}