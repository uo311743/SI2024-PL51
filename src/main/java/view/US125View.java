package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class US125View extends AbstractView {
    
    private JTable invoiceTable;
    private JTextField emailTextField;
    
    public US125View() {
        super("Invoice Management", false);
        initialize();
    }
    
    @Override
    protected void initialize() {
        this.invoiceTable = new JTable();
        this.emailTextField = new JTextField(20);
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowRight("Send Invoice");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

        // Invoice Table
        invoiceTable.setName("invoiceTable");
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane invoiceTablePanel = new JScrollPane(invoiceTable);
        getMainPanel().add(invoiceTablePanel, "cell 0 1, grow");
        
        // Email Panel
        JPanel emailPanel = new JPanel();
        emailPanel.add(new JLabel("Email: "));
        emailPanel.add(emailTextField);
        getMainPanel().add(emailPanel, "cell 0 2");
    }
    
    // Getters and setters
    public JTable getInvoiceTable() {
        return invoiceTable;
    }
    
    public JTextField getEmailTextField() {
        return emailTextField;
    }
}
