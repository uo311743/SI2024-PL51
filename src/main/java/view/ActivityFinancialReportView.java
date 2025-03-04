package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class ActivityFinancialReportView extends AbstractView {
    
    private JTable reportTable;
    private JComboBox<Object> statusComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JLabel totalEstimatedIncomeLabel;
    private JLabel totalPaidIncomeLabel;
    private JLabel totalEstimatedExpensesLabel;
    private JLabel totalPaidExpensesLabel;
    private JLabel profitLabel;
    private JButton filterButton;
    
    public ActivityFinancialReportView() {
		super("Activity Financial Report");
    }
    
    @Override
    protected void initialize() {
        this.reportTable = new JTable();
        this.statusComboBox = new JComboBox<>();
        this.startDateField = new JTextField(10); 
        this.endDateField = new JTextField(10);  
        this.totalEstimatedIncomeLabel = new JLabel("Estimated Income: 0");
        this.totalPaidIncomeLabel = new JLabel("Paid Income: 0");
        this.totalEstimatedExpensesLabel = new JLabel("Estimated Expenses: 0");
        this.totalPaidExpensesLabel = new JLabel("Paid Expenses: 0");
        this.profitLabel = new JLabel("Profit: 0");
        this.filterButton = new JButton("Filter");

        super.createButtonLowLeft("Cancel");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

        // Filters Panel
        JPanel filterPanel = new JPanel(new MigLayout("", "[center]"));
        filterPanel.add(new JLabel("Start Date (yyyy-mm-dd): "));
        filterPanel.add(startDateField);
        filterPanel.add(new JLabel("End Date (yyyy-mm-dd): "));
        filterPanel.add(endDateField);
        filterPanel.add(new JLabel("Status: "));
        filterPanel.add(statusComboBox);
        filterPanel.add(filterButton);
        getMainPanel().add(filterPanel, "cell 0 0, align center");

        // Report Table
        reportTable.setName("reportTable");
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tablePanel = new JScrollPane(reportTable);
        getMainPanel().add(tablePanel, "cell 0 1, grow");

        // Totals Panel
        JPanel totalsPanel = new JPanel(new MigLayout("", "[center]"));
        totalsPanel.add(totalEstimatedIncomeLabel);
        totalsPanel.add(totalPaidIncomeLabel);
        totalsPanel.add(totalEstimatedExpensesLabel);
        totalsPanel.add(totalPaidExpensesLabel);
        totalsPanel.add(profitLabel);
        getMainPanel().add(totalsPanel, "cell 0 2, align center");
    }
    
    // Getters y setters
    public JTable getReportTable() {
        return reportTable;
    }
    
    public JComboBox<Object> getStatusComboBox() {
        return statusComboBox;
    }
    
    public JTextField getStartDateField() {
        return startDateField;
    }
    
    public JTextField getEndDateField() {
        return endDateField;
    }
    
    public JLabel getTotalEstimatedIncomeLabel() {
        return totalEstimatedIncomeLabel;
    }
    
    public JLabel getTotalPaidIncomeLabel() {
        return totalPaidIncomeLabel;
    }
    
    public JLabel getTotalEstimatedExpensesLabel() {
        return totalEstimatedExpensesLabel;
    }
    
    public JLabel getTotalPaidExpensesLabel() {
        return totalPaidExpensesLabel;
    }
    
    public JLabel getProfitLabel() {
        return profitLabel;
    }
    
    public JButton getFilterButton() {
        return filterButton;
    }
}
