package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentView extends AbstractView {
    private JComboBox<String> activityComboBox;
    private JTextField amountField;
    private JTextField nifField;
    private JTextField dateField;
    private JTextField invoiceIdField;
    
    private String currentActivities[];
    
    private JPanel formPanel;
    private JPanel summaryPanel;
    
    private JTextArea summaryTextArea;
    
    private Boolean summaryConcealed;
    
    private JButton testBtn;
    
    public PaymentView()
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
    	amountField = new JTextField("", 1);
    	nifField = new JTextField("", 1);
    	dateField = new JTextField("", 1);
    	invoiceIdField = new JTextField("", 1);
    	formPanel = new JPanel();
    	summaryPanel = new JPanel();
    	summaryTextArea = new JTextArea();
    	summaryConcealed = true;
    	testBtn = new JButton("Test Button");
    }
    
    @Override
    protected void configMainPanel()
    {
    	configureFormPanel();
    	
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Register");
        
        super.getButtonLowLeft().addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e)
        	{
        		clearFields();
        	}
        });
        
        super.getMainPanel().add(testBtn);
    }
    
    public JButton getButton() {return this.testBtn; }
    
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
               
        // Add components to the main panel
    	formPanel.add(new JLabel("Activity:"));
    	formPanel.add(activityComboBox);

    	formPanel.add(new JLabel("Amount:"));
    	formPanel.add(amountField);

    	formPanel.add(new JLabel("NIF:"));
    	formPanel.add(nifField);

    	formPanel.add(new JLabel("Date:"));
    	formPanel.add(dateField);

    	formPanel.add(new JLabel("Invoice ID:"));
    	formPanel.add(invoiceIdField);
    	
    	super.getMainPanel().add(formPanel);
    }
    
    public void configureSummaryPanel()
    {
    	String summary = "Activity: " + getActivity() + 
    			"\nAmount: " + getAmount() + "\nNIF: " +
    			getNIF() + "\nDate: " + getDate() + "\nInvoice ID: " + getInvoiceId();
    	summaryTextArea.insert(summary, 0);
    	summaryTextArea.setEditable(false);
    	summaryTextArea.setOpaque(false);
    	
    	summaryPanel.add(summaryTextArea, BorderLayout.CENTER);
    	summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 12));
    	super.getMainPanel().add(summaryPanel);
    	super.getMainPanel().revalidate();
    	super.getMainPanel().repaint();
    	System.out.println("hi");
    }

    public void clearFields() {
        activityComboBox.setSelectedIndex(0);
        amountField.setText("");
        nifField.setText("");
        dateField.setText("");
        invoiceIdField.setText("");
        summaryConcealed = true;
        super.getMainPanel().remove(summaryPanel);
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