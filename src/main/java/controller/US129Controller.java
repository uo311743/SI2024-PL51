package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.table.DefaultTableModel;
import DTOs.ActivitiesDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
import model.US129Model;
import util.SwingMain;
import view.US129View;

public class US129Controller {
    
    protected US129Model model;
    protected US129View view; 
    
    public US129Controller(US129Model m, US129View v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }

    public void initController() {
    	view.getFilterButton().addActionListener(e -> applyFilters());
        
        this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
        	}
        });
    }
    
    public void initView() {
    	loadStatus();
    	showCurrentYearData();
        view.setVisible();
    }
    
    public void loadStatus() {
        List<Object[]> sponsorList = model.getStatusListArray();
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
    
    public void showCurrentYearData() {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(SwingMain.getTodayDate());
        String startDate = calendario.get(Calendar.YEAR) + "-01-01";
        String endDate = calendario.get(Calendar.YEAR) + "-12-31";
        
        List<ActivitiesDTO> currentYearActivities = model.getActivitiesFromCurrentYear(startDate, endDate);
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Start Date", "End Date", "Name", "Status", "Income", "Expense", "Balance"}, 0);
        
        for (ActivitiesDTO activity : currentYearActivities) {
            
            tableModel.addRow(new Object[]{
                activity.getDateStart(), 
                activity.getDateEnd(),
                activity.getName(),
                activity.getStatus()
            });
        }
        
        view.getReportTable().setModel(tableModel);
    }
    
    private void updateReportTable(List<ActivitiesDTO> filteredActivities) {
    	// Pendiente apuntes Ángel
        // view.getReportTable().setModel(tmodel);
    }

    private void updateTotals(List<ActivitiesDTO> filteredActivities) {
    	// Pendiente apuntes Ángel
    	/*
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
        view.getProfitLabel().setText("Profit: " + profit);*/
    }
}
