package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    }

    public void initController() {
    	view.getFilterButton().addActionListener(e -> applyFilters());
        
        this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
        	}
        });
        
        this.view.getStartDateField().getDocument().addDocumentListener(new DocumentListener() {
        	@Override
        	public void insertUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> applyFilters());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> applyFilters());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
        
        this.view.getEndDateField().getDocument().addDocumentListener(new DocumentListener() {
        	@Override
        	public void insertUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> applyFilters());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> applyFilters());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    public void initView() {
    	showCurrentData();
        view.setVisible();
    }
    
    public void showCurrentData() {
    	// Table
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(SwingMain.getTodayDate());
        String startDate = calendario.get(Calendar.YEAR) + "-01-01";
        String endDate = calendario.get(Calendar.YEAR) + "-12-31";
        
        List<ActivitiesDTO> currentYearActivities = model.getActivitiesFromCurrentYear(startDate, endDate);
        String value1, value2, value3, value4;
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Incomes", "Expenses", "Balance"}, 0);
        
        int totalEstimatedIncomes = 0;
        int totalEstimatedExpenses = 0;
        int totalPaidIncomes = 0;
        int totalPaidExpenses = 0;
        for (ActivitiesDTO activity : currentYearActivities) {
        	try {
        		value1 = model.getAmountIncomeByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value1 = "0";
        	}
        	try {
        		value2 = model.getAmountExpenseByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value2 = "0";
        	}
        	try {
        		value3 = model.getAmountEstimatedIncomeByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value3 = "0";
        	}
        	try {
        		value4 = model.getAmountEstimatedExpenseByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value4 = "0";
        	}
        	tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                value1,
                value2,
                Double.parseDouble(value1) - Double.parseDouble(value2)
            });
            
            totalEstimatedIncomes += Double.parseDouble(value3);
            totalEstimatedExpenses += Double.parseDouble(value4);
            totalPaidIncomes += Double.parseDouble(value1);
            totalPaidExpenses += Double.parseDouble(value2);
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
    	// Validations
    	if (!SyntacticValidations.isDate(view.getStartDateField().getText()) || !SyntacticValidations.isDate(view.getEndDateField().getText())) {
    		if (!SyntacticValidations.isDate(view.getStartDateField().getText())) {
    			view.getStartDateField().setForeground(Color.RED);
    		}
    		if (!SyntacticValidations.isDate(view.getEndDateField().getText())) {
        		view.getEndDateField().setForeground(Color.RED);
    		}
    		valid = false;
    	} 
    	else {
    		view.getStartDateField().setForeground(Color.BLACK);
    		view.getEndDateField().setForeground(Color.BLACK);    		
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
    	String value1, value2, value3, value4;
    	
    	DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Incomes", "Expenses", "Balance"}, 0);
        
        int totalEstimatedIncomes = 0;
        int totalEstimatedExpenses = 0;
        int totalPaidIncomes = 0;
        int totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : filteredActivities) {
        	try {
        		value1 = model.getAmountIncomeByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value1 = "0";
        	}
        	try {
        		value2 = model.getAmountExpenseByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value2 = "0";
        	}
        	try {
        		value3 = model.getAmountEstimatedIncomeByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value3 = "0";
        	}
        	try {
        		value4 = model.getAmountEstimatedExpenseByActivityId(activity.getId()).getAmount();
        	}
        	catch (Exception e){
        		value4 = "0";
        	}
            tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                value1,
                value2,
                Double.parseDouble(value1) - Double.parseDouble(value2)
            });
            
            totalEstimatedIncomes += Double.parseDouble(value3);
            totalEstimatedExpenses += Double.parseDouble(value4);
            totalPaidIncomes += Double.parseDouble(value1);
            totalPaidExpenses += Double.parseDouble(value2);
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
