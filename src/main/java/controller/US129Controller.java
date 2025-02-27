package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import DTOs.ActivitiesDTO;
import model.Model;
import view.US129View;

public class US129Controller {
    
    protected Model model;
    protected US129View view; 
    
    public US129Controller(Model m, US129View v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }

    public void initController() {
        // Filter button
        view.getFilterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        
        // Cancel button
        view.getButtonLowLeft().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.disposeView();
            }
        });
    }
    
    public void initView() {
        view.setVisible();
    }
    
    private void applyFilters() {
        String startDateStr = view.getStartDateField().getText();
        String endDateStr = view.getEndDateField().getText();
        String status = (String) view.getStatusComboBox().getSelectedItem();

        Date startDate = null;
        Date endDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (!startDateStr.isEmpty()) {
                startDate = sdf.parse(startDateStr);
            }
            if (!endDateStr.isEmpty()) {
                endDate = sdf.parse(endDateStr);
            }
        } 
        catch (ParseException ex) {
            ex.printStackTrace();
        }

        List<ActivitiesDTO> filteredActivities = model.getFilteredActivities(startDate, endDate, status);
        updateReportTable(filteredActivities);
        updateTotals(filteredActivities);
    }
    
    private void updateReportTable(List<ActivitiesDTO> filteredActivities) {
        // Assuming you have a method to update the report table in the view
        // You can use a TableModel to update the data in the JTable
        ReportTableModel tableModel = new ReportTableModel(filteredActivities); // Assuming this model exists
        view.getReportTable().setModel(tableModel);
    }

    private void updateTotals(List<ActivitiesDTO> filteredActivities) {
        double totalEstimatedIncome = 0;
        double totalPaidIncome = 0;
        double totalEstimatedExpenses = 0;
        double totalPaidExpenses = 0;

        for (ActivitiesDTO activity : filteredActivities) {
            try {
                totalEstimatedIncome += Double.parseDouble(activity.getEstimatedIncomeActivities());
                totalPaidIncome += Double.parseDouble(activity.getPaidIncome());
                totalEstimatedExpenses += Double.parseDouble(activity.getEstimatedExpensesActivities());
                totalPaidExpenses += Double.parseDouble(activity.getPaidExpenses());
            } 
            catch (NumberFormatException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncome);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncome);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
    }
}
