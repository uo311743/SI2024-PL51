package view;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import net.miginfocom.swing.MigLayout;

public class GenerateInvoicesView extends AbstractView {
    
    private JComboBox<Object> activityComboBox;
    private JTable agreementsTable;
    private JLabel idLabel;
    private JLabel dateIssuedLabel;
    private JLabel dateExpLabel;
    private JLabel amountLabel;
    private JTextField taxRateTextField;
    
    public GenerateInvoicesView() {
        super("Generate Invoices");
    }
    
    @Override
    protected void initialize() {
        this.agreementsTable = new JTable();
        this.activityComboBox = new JComboBox<>();
        this.idLabel = new JLabel("ID Sponsorship Agreement: -");
        this.dateIssuedLabel = new JLabel("Date Issued: -");
        this.dateExpLabel = new JLabel("Date Expired: -");
        this.amountLabel = new JLabel("Amount: -");
        this.taxRateTextField = new JTextField(10);
        
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Generate Invoice");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow 50][grow 50]", "[][][grow][]"));

        // FilterPanel
        JPanel filterPanel = new JPanel(new MigLayout("", "[center]"));
        filterPanel.add(new JLabel("Select Activity: "));
        filterPanel.add(activityComboBox);
        getMainPanel().add(filterPanel, "cell 0 0 2 1, align center");

        // Agreements Table
        agreementsTable.setName("agreementsTable");
        agreementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane agreementsTablePanel = new JScrollPane(agreementsTable);
        getMainPanel().add(agreementsTablePanel, "cell 0 1, grow");
        
        // Invoice Details Panel
        JPanel invoiceDetailsPanel = new JPanel(new MigLayout("", "[][grow]", ""));
        invoiceDetailsPanel.add(new JLabel("Invoice Details"), "wrap");
        invoiceDetailsPanel.add(idLabel, "wrap");
        invoiceDetailsPanel.add(dateIssuedLabel, "wrap");
        invoiceDetailsPanel.add(dateExpLabel, "wrap");
        invoiceDetailsPanel.add(amountLabel, "wrap");
        invoiceDetailsPanel.add(new JLabel("Tax Rate: "));
        invoiceDetailsPanel.add(taxRateTextField, "wrap");
        getMainPanel().add(invoiceDetailsPanel, "cell 1 1, grow");
    }
    
    // Getters and setters
    public JTable getAgreementsTable() {
        return agreementsTable;
    }
    
    public JComboBox<Object> getActivityComboBox() {
        return activityComboBox;
    }
    
    public JLabel getIdLabel() {
    	return idLabel;
    }
    
    public JLabel getDateIssuedLabel() {
    	return dateIssuedLabel;
    }
    
    public JLabel getDateExpLabel() {
    	return dateExpLabel;
    }
    
    public JLabel getAmountLabel() {
        return amountLabel;
    }
    
    public JTextField getTaxRateTextField() {
        return taxRateTextField;
    }
}
