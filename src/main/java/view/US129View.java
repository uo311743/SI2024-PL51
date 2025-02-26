package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/*
As the secretary I want to consult a report of total income and expenses for activities organized in a time interval to inform the governing board

For the Governing Board of the College, it is essential to have updated information on the income and expenses associated with the activities, so they know which activities have a surplus (more income than expenses) and which are in deficit.
By default on the report we need all the activities corresponding with the current year. We also need the following filters:
- Start and end dates for the consultation.
- Status of the activities to be consulted
The report must clearly indicate, for each activity: dates, name, status of the activity, total income, total expenses and balance.
The report needs to show also totals for income and expenses, both estimated and paid.
[Restriction SP1: In this sprint every functionality is executed by the administrative secretary (Rosa). In latter sprints it needs to be studied if we will need user stories to be executed by other users such as, f.i. Dean, Treasurer or Member of the GB].
*/

public class US129View extends AbstractView {
    
    private JTable reportTable;
    private JComboBox<String> statusComboBox;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JLabel totalEstimatedIncomeLabel;
    private JLabel totalPaidIncomeLabel;
    private JLabel totalEstimatedExpensesLabel;
    private JLabel totalPaidExpensesLabel;
    
    public US129View() {
        super("Activity Financial Report", false);
        initialize();
    }
    
    @Override
    protected void initialize() {
        this.reportTable = new JTable();
        this.statusComboBox = new JComboBox<>(new String[]{"All", "Planned", "Ongoing", "Completed", "Cancelled"});
        this.startDateSpinner = new JSpinner(new SpinnerDateModel());
        this.endDateSpinner = new JSpinner(new SpinnerDateModel());
        this.totalEstimatedIncomeLabel = new JLabel("Estimated Income: 0");
        this.totalPaidIncomeLabel = new JLabel("Paid Income: 0");
        this.totalEstimatedExpensesLabel = new JLabel("Estimated Expenses: 0");
        this.totalPaidExpensesLabel = new JLabel("Paid Expenses: 0");

        super.createButtonLowLeft("Cancel");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

        // Filters Panel
        JPanel filterPanel = new JPanel(new MigLayout("", "[center]"));
        filterPanel.add(new JLabel("Start Date: "));
        filterPanel.add(startDateSpinner);
        filterPanel.add(new JLabel("End Date: "));
        filterPanel.add(endDateSpinner);
        filterPanel.add(new JLabel("Status: "));
        filterPanel.add(statusComboBox);
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
        getMainPanel().add(totalsPanel, "cell 0 2, align center");
    }
    
    // Getters and setters
    public JTable getReportTable() {
        return reportTable;
    }
    
    public JComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }
    
    public JSpinner getStartDateSpinner() {
        return startDateSpinner;
    }
    
    public JSpinner getEndDateSpinner() {
        return endDateSpinner;
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
}
