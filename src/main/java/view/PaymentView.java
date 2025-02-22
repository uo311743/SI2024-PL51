package view;

import javax.swing.*;
import java.awt.*;

public class PaymentView extends AbstractView {
    private JComboBox<String> activityComboBox;
    private JTextField amountField;
    private JTextField companyNIFField;
    private JTextField isbnField;
    private JComboBox<String> transferTypeComboBox;
    private JTextField transferDetailsField;
    private JTextField paymentDateField;
    private JTextField invoiceIdField;
    
    private JTextArea summaryTextArea;
    
    public PaymentView()
    {
    	super("Payment Registration");
    }
    
    protected void initializeAttrib()
    {
    	activityComboBox = new JComboBox<>(new String[]{"Olimpics", "ImpulsoTIC Week", "Hour of Code"});
    	amountField = new JTextField();
    	companyNIFField = new JTextField();
    	isbnField = new JTextField();
    	transferTypeComboBox = new JComboBox<>(new String[]{"SWIFT", "BIC", "Bank Office Address"});
    	transferDetailsField = new JTextField();
    	paymentDateField = new JTextField();
    	invoiceIdField = new JTextField();
    }
    
    @Override
    protected void configMainPanel()
    {
    	// Set layout for the main panel
        super.getMainPanel().setLayout(new GridLayout(0, 2)); // 0 rows means "as many as needed", 2 columns

        // Add components to the main panel
        super.getMainPanel().add(new JLabel("Activity:"));
        super.getMainPanel().add(activityComboBox);

        super.getMainPanel().add(new JLabel("Amount(EUR):"));
        super.getMainPanel().add(amountField);

        super.getMainPanel().add(new JLabel("NIF:"));
        super.getMainPanel().add(companyNIFField);

        super.getMainPanel().add(new JLabel("ISBN:"));
        super.getMainPanel().add(isbnField);

        super.getMainPanel().add(new JLabel("International Transfer:"));
        super.getMainPanel().add(transferTypeComboBox);

        super.getMainPanel().add(new JLabel("Transfer Details:"));
        super.getMainPanel().add(transferDetailsField);

        super.getMainPanel().add(new JLabel("Date:"));
        super.getMainPanel().add(paymentDateField);

        super.getMainPanel().add(new JLabel("Invoice ID:"));
        super.getMainPanel().add(invoiceIdField);
        
        super.addButtonToMain("Submit", () -> {showSummary();});
        super.addButtonToMain("Clear", () -> {clearFields();});
    }

    private void showSummary() {
        String summary = "Payment Registration Summary:\n" +
                "Activity: " + activityComboBox.getSelectedItem() + "\n" +
                "Amount(EUR): " + amountField.getText() + "\n" +
                "Company NIF: " + companyNIFField.getText() + "\n" +
                "Bank ISBN: " + isbnField.getText() + "\n" +
                "International Transfer: " + transferTypeComboBox.getSelectedItem() + "\n" +
                "Transfer Details: " + transferDetailsField.getText() + "\n" +
                "Payment Date: " + paymentDateField.getText() + "\n" +
                "Invoice ID: " + invoiceIdField.getText();
        
        super.resetMainPanel();
        
        // Set layout for the main panel
        super.getMainPanel().setLayout(new GridLayout(0, 1)); // 0 rows means "as many as needed", 1 column

        summaryTextArea = new JTextArea(summary);
        summaryTextArea.setEditable(false);
        getMainPanel().add(summaryTextArea);
        
        super.addButtonToMain("Submit", () -> {
	 
	    });
    }

    private void clearFields() {
        activityComboBox.setSelectedIndex(0);
        amountField.setText("");
        companyNIFField.setText("");
        isbnField.setText("");
        transferTypeComboBox.setSelectedIndex(0);
        transferDetailsField.setText("");
        paymentDateField.setText("");
        invoiceIdField.setText("");
    }

    public static void main(String[] args) {
        new PaymentView();
    }
}