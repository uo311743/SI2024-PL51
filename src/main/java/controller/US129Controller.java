package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.ComboBoxModel;

import DTOs.ActivitiesDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
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
    	loadStatus();
        view.setVisible();
    }
    
    public void loadStatus() {
        List<Object[]> sponsorList = model.getSponsorListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponsorList);
        view.getStatusComboBox().setModel(lmodel);
    }
    
    private void applyFilters() {
        String startDateStr = view.getStartDateField().getText();
        String endDateStr = view.getEndDateField().getText();
        String status = (String) view.getStatusComboBox().getSelectedItem();

        Date startDate = Util.isoStringToDate(startDateStr);
        Date endDate = Util.isoStringToDate(endDateStr);

        List<ActivitiesDTO> filteredActivities = model.getFilteredActivities(startDate, endDate, status);
        updateReportTable(filteredActivities);
        updateTotals(filteredActivities);
    }
    
    private void updateReportTable(List<ActivitiesDTO> filteredActivities) {
    	// Pendiente apuntes Ángel
        // view.getReportTable().setModel(tmodel);
    }

    private void updateTotals(List<ActivitiesDTO> filteredActivities) {
        int totalEstimatedIncome = 0;
        int totalEstimatedExpenses = 0;

        for (ActivitiesDTO activity : filteredActivities) {
            try {
                totalEstimatedIncome += Integer.parseInt(activity.getEstimatedIncomeActivities());
                totalEstimatedExpenses += Integer.parseInt(activity.getEstimatedExpensesActivities());
            } 
            catch (NumberFormatException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        int profit = model.getAmountIncome() - model.getAmountExpense();
        
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncome);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + model.getAmountIncome());
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + model.getAmountExpense());
        view.getProfitLabel().setText("Profit: " + profit);
    }
}
