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
    private JTextField idTextField;
    private JTextField dateIssuedTextField;
    private JLabel amountLabel;
    private JLabel taxRateLabel;
    
    public GenerateInvoicesView() {
        super("Generate Invoices");
    }
    
    @Override
    protected void initialize() {
        this.agreementsTable = new JTable();
        this.activityComboBox = new JComboBox<>();
        this.idTextField = new JTextField(10);
        this.dateIssuedTextField = new JTextField(10);
        this.amountLabel = new JLabel("Amount: -");
        this.taxRateLabel = new JLabel("Tax Rate: ");
        
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
        invoiceDetailsPanel.add(new JLabel("ID: "));
        invoiceDetailsPanel.add(idTextField, "wrap");
        invoiceDetailsPanel.add(new JLabel("Date Issued: "));
        invoiceDetailsPanel.add(dateIssuedTextField, "wrap");
        invoiceDetailsPanel.add(amountLabel, "wrap");
        invoiceDetailsPanel.add(taxRateLabel, "wrap");
        getMainPanel().add(invoiceDetailsPanel, "cell 1 1, grow");
    }
    
    // Getters and setters
    public JTable getAgreementsTable() {
        return agreementsTable;
    }
    
    public JComboBox<Object> getActivityComboBox() {
        return activityComboBox;
    }
    
    public JTextField getIdTextField() {
        return idTextField;
    }
    
    public JTextField getDateIssuedTextField() {
    	return dateIssuedTextField;
    }
    
    public JLabel getAmountLabel() {
        return amountLabel;
    }
    
    public JLabel getTaxRateLabel() {
    	return taxRateLabel;
    }
}
