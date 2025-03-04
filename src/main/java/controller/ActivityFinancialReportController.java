package controller;

import java.awt.Color;
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
import model.ActivityFinancialReportModel;
import util.SwingMain;
import util.SyntacticValidations;
import view.ActivityFinancialReportView;

public class ActivityFinancialReportController {
    
    protected ActivityFinancialReportModel model;
    protected ActivityFinancialReportView view; 
    
    private boolean valid;
    
    public ActivityFinancialReportController(ActivityFinancialReportModel m, ActivityFinancialReportView v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
        
        this.valid = true;
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
    	showCurrentData();
        view.setVisible();
    }
    
    public void loadStatus() {
        List<Object[]> sponsorList = model.getStatusListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponsorList);
        view.getStatusComboBox().setModel(lmodel);
    }
    
    public void showCurrentData() {
    	// Table
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(SwingMain.getTodayDate());
        String startDate = calendario.get(Calendar.YEAR) + "-01-01";
        String endDate = calendario.get(Calendar.YEAR) + "-12-31";
        
        List<ActivitiesDTO> currentYearActivities = model.getActivitiesFromCurrentYear(startDate, endDate);
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Incomes", "Expenses", "Balance"}, 0);
        
        int totalEstimatedIncomes = 0;
        int totalEstimatedExpenses = 0;
        int totalPaidIncomes = 0;
        int totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : currentYearActivities) {
            tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                model.getAmountIncomeByActivityId(activity.getId()),
                model.getAmountExpenseByActivityId(activity.getId())
            });
            
            totalEstimatedIncomes = Integer.parseInt(model.getAmountEstimatedIncomeByActivityId(activity.getId()).getAmount());
            totalEstimatedExpenses = Integer.parseInt(model.getAmountEstimatedExpenseByActivityId(activity.getId()).getAmount());
            totalPaidIncomes = Integer.parseInt(model.getAmountIncomeByActivityId(activity.getId()).getAmount());
            totalPaidExpenses = Integer.parseInt(model.getAmountExpenseByActivityId(activity.getId()).getAmount());
        }
        
        view.getReportTable().setModel(tableModel);
        
        // Totals
        int profit = totalPaidIncomes - totalPaidExpenses;
        
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncomes);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncomes);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
        view.getProfitLabel().setText("Profit: " + profit);
    }
    
    private void applyFilters() {
    	if (!SyntacticValidations.isDate(view.getStartDateField().getText()) || !SyntacticValidations.isDate(view.getEndDateField().getText())) {
    		if (!SyntacticValidations.isDate(view.getStartDateField().getText())) {
    			view.getStartDateField().setForeground(Color.RED);
    		}
    		if (!SyntacticValidations.isDate(view.getEndDateField().getText())) {
        		view.getEndDateField().setForeground(Color.RED);
    		}
    		valid = false;
    	}
    	view.getFilterButton().setEnabled(valid);
    	
        Date startDate = Util.isoStringToDate(view.getStartDateField().getText());
        Date endDate = Util.isoStringToDate(view.getEndDateField().getText());
        String status = (String) view.getStatusComboBox().getSelectedItem();

        List<ActivitiesDTO> filteredActivities = model.getFilteredActivities(startDate, endDate, status);
        
        update(filteredActivities);
    }
    
    private void update(List<ActivitiesDTO> filteredActivities) {
    	// Table
    	DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Incomes", "Expenses", "Balance"}, 0);
        
        int totalEstimatedIncomes = 0;
        int totalEstimatedExpenses = 0;
        int totalPaidIncomes = 0;
        int totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : filteredActivities) {
            tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                model.getAmountIncomeByActivityId(activity.getId()),
                model.getAmountExpenseByActivityId(activity.getId())
            });
            
            totalEstimatedIncomes = Integer.parseInt(model.getAmountEstimatedIncomeByActivityId(activity.getId()).getAmount());
            totalEstimatedExpenses = Integer.parseInt(model.getAmountEstimatedExpenseByActivityId(activity.getId()).getAmount());
            totalPaidIncomes = Integer.parseInt(model.getAmountIncomeByActivityId(activity.getId()).getAmount());
            totalPaidExpenses = Integer.parseInt(model.getAmountExpenseByActivityId(activity.getId()).getAmount());
        }
        
        view.getReportTable().setModel(tableModel);
        
        // Totals
        int profit = totalPaidIncomes - totalPaidExpenses;
        
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncomes);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncomes);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
        view.getProfitLabel().setText("Profit: " + profit);
    }
}
