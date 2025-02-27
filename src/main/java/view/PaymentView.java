package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PaymentView extends AbstractView {
    private JComboBox<String> activityComboBox;
    private JTextField amountField;
    private JTextField nifField;
    private JTextField isbnField;
    //private JComboBox<String> transferTypeComboBox;
    //private JTextField transferDetailsField;
    private JTextField dateField;
    private JTextField invoiceIdField;
    
    private JTextArea summaryTextArea;
    
    public PaymentView()
    {
    	super("Register Payment");
    }
    
    protected void initializeAttrib()
    {
    	activityComboBox = new JComboBox<>(new String[]{"Olimpics", "ImpulsoTIC Week", "Hour of Code"});
    	amountField = new JTextField();
    	nifField = new JTextField();
    	isbnField = new JTextField();
    	//transferTypeComboBox = new JComboBox<>(new String[]{"SWIFT", "BIC", "Bank Office Address"});
    	//transferDetailsField = new JTextField();
    	dateField = new JTextField();
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
        super.getMainPanel().add(nifField);

        super.getMainPanel().add(new JLabel("ISBN:"));
        super.getMainPanel().add(isbnField);

        //super.getMainPanel().add(new JLabel("International Transfer:"));
        //super.getMainPanel().add(transferTypeComboBox);

        //super.getMainPanel().add(new JLabel("Transfer Details:"));
        //super.getMainPanel().add(transferDetailsField);

        super.getMainPanel().add(new JLabel("Date:"));
        super.getMainPanel().add(dateField);

        super.getMainPanel().add(new JLabel("Invoice ID:"));
        super.getMainPanel().add(invoiceIdField);
        
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Register");
    }

    private void showSummary() {
        String summary = "Payment Registration Summary:\n" +
                "Activity: " + activityComboBox.getSelectedItem() + "\n" +
                "Amount(EUR): " + amountField.getText() + "\n" +
                "Company NIF: " + nifField.getText() + "\n" +
                "Bank ISBN: " + isbnField.getText() + "\n" +
                "Payment Date: " + dateField.getText() + "\n" +
                "Invoice ID: " + invoiceIdField.getText();
        
        super.resetMainPanel();
        
        // Set layout for the main panel
        super.getMainPanel().setLayout(new GridLayout(0, 1)); // 0 rows means "as many as needed", 1 column

        summaryTextArea = new JTextArea(summary);
        summaryTextArea.setEditable(false);
        getMainPanel().add(summaryTextArea);
    }

    private void clearFields() {
        activityComboBox.setSelectedIndex(0);
        amountField.setText("");
        nifField.setText("");
        isbnField.setText("");
        //transferTypeComboBox.setSelectedIndex(0);
        //transferDetailsField.setText("");
        dateField.setText("");
        invoiceIdField.setText("");
    }
    
    public String getActivity() {
        return (String) activityComboBox.getSelectedItem();
    }

    public double getAmount() {
        return Double.parseDouble(amountField.getText());
    }

    public String getNIF() {
        return nifField.getText();
    }

    public String getISBN() {
        return isbnField.getText();
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

    public void addRegisterListener(ActionListener listener) {
        super.getButtonLowRight().addActionListener(listener);
    }

    public void showError(String message) {
        
    }

    public void showSuccess(String message) {
        
    }
}