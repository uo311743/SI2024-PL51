package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class InvoiceManagementView extends AbstractView {
    
    private JTable invoiceTable;
    private JComboBox<Object> sponsorComboBox;
    private JComboBox<Object> activityComboBox;
    private JButton filterButton;
    
    public InvoiceManagementView() {
        super("Invoice Management");
    }
    
    @Override
    protected void initialize() {
        this.invoiceTable = new JTable();
        this.sponsorComboBox = new JComboBox<>();
        this.activityComboBox = new JComboBox<>();
        this.filterButton = new JButton("Filter");

        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Send Invoice");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

        // FilterPanel
        JPanel sponsorPanel = new JPanel(new MigLayout("", "[center]"));
        sponsorPanel.add(new JLabel("Sponsor: "));
        sponsorPanel.add(sponsorComboBox);
        sponsorPanel.add(new JLabel("Activity: "));
        sponsorPanel.add(activityComboBox);
        sponsorPanel.add(filterButton);
        getMainPanel().add(sponsorPanel, "cell 0 0, align center");

        // Invoice Table
        invoiceTable.setName("invoiceTable");
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane invoiceTablePanel = new JScrollPane(invoiceTable);
        getMainPanel().add(invoiceTablePanel, "cell 0 1, grow");
    }
    
    // Getters and setters
    public JTable getInvoiceTable() {
        return invoiceTable;
    }
    
    public JComboBox<Object> getSponsorComboBox() {
        return sponsorComboBox;
    }
    
    public JComboBox<Object> getActivityComboBox() {
        return activityComboBox;
    }
    
    public JButton getFilterButton() {
        return filterButton;
    }
}
