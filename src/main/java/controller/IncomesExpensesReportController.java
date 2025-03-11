package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import DTOs.ActivitiesDTO;
import model.ActivityFinancialReportModel;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import view.ActivityFinancialReportView;

public class ActivityFinancialReportController {
    
    protected ActivityFinancialReportModel model;
    protected ActivityFinancialReportView view; 
        
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
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
        
        this.view.getEndDateField().getDocument().addDocumentListener(new DocumentListener() {
        	@Override
        	public void insertUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    public void initView() {
    	view.getFilterButton().setEnabled(false);
    	showCurrentData();
        view.setVisible();
    }
    
    public void showCurrentData() {
    	// Table    	
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(SwingMain.getTodayDate());
        String startDate = calendario.get(Calendar.YEAR) + "-01-01";
        String endDate = calendario.get(Calendar.YEAR) + "-12-31";
        
    	view.getStartDateField().setText(startDate);
    	view.getEndDateField().setText(endDate);
        
        List<ActivitiesDTO> currentYearActivities = model.getActivitiesFromCurrentYear(startDate, endDate);
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Income Estimated", "Income Paid", "Expense Estimated", "Expense Paid", "Balance Estimated", "Balance Paid"}, 0);
        
        double totalEstimatedIncomes = 0;
        double totalEstimatedExpenses = 0;
        double totalPaidIncomes = 0;
        double totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : currentYearActivities) {
        	double ei = model.getAmountMovementIncomeByActivityId(activity.getId()) + model.getAmountSAByActivityId(activity.getId());
        	double pi = model.getAmountMovementIncomeByActivityId(activity.getId()) + model.getAmountSPByActivityId(activity.getId());
        	double ee = model.getAmountMovementExpenseByActivityId(activity.getId()) + model.getAmountSAByActivityId(activity.getId());
        	double pe = model.getAmountMovementExpenseByActivityId(activity.getId()) + model.getAmountSPByActivityId(activity.getId());
        	
        	tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                ei,
                pi,
                ee,
                pe,
                ei - ee,
                pi - pe
            });
            
        	totalEstimatedIncomes += ei;
            totalEstimatedExpenses += ee;
            totalPaidIncomes += pi;
            totalPaidExpenses += pe;
        }
        
        view.getReportTable().setModel(tableModel);
        
        // Totals
        double profit = totalPaidIncomes - totalPaidExpenses;
        
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncomes);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncomes);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
        view.getProfitLabel().setText("Profit: " + profit);
    }
    
    public void checkTextFields() {
    	boolean valid = true;
		if (!SyntacticValidations.isDate(view.getStartDateField().getText())) {
			view.getStartDateField().setForeground(Color.RED);
			valid = false;
		} 
		else {
			view.getStartDateField().setForeground(Color.BLACK);
		}
		
		if (!SyntacticValidations.isDate(view.getEndDateField().getText())) {
    		view.getEndDateField().setForeground(Color.RED);
			valid = false;
		}
		else {
    		view.getEndDateField().setForeground(Color.BLACK);
		}
    	
		view.getFilterButton().setEnabled(valid);
    }
    
    private void applyFilters() {
    	String startDate = view.getStartDateField().getText();
        String endDate = view.getEndDateField().getText();
        String status = (String) view.getStatusComboBox().getSelectedItem();

        List<ActivitiesDTO> filteredActivities = model.getFilteredActivities(startDate, endDate, status);
        
        update(filteredActivities);
    }
    
    private void update(List<ActivitiesDTO> filteredActivities) {
    	// Table
    	DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Incomes", "Expenses", "Balance"}, 0);
        
        double totalEstimatedIncomes = 0;
        double totalEstimatedExpenses = 0;
        double totalPaidIncomes = 0;
        double totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : filteredActivities) {
        	double ei = model.getAmountMovementIncomeByActivityId(activity.getId()) + model.getAmountSAByActivityId(activity.getId());
        	double pi = model.getAmountMovementIncomeByActivityId(activity.getId()) + model.getAmountSPByActivityId(activity.getId());
        	double ee = model.getAmountMovementExpenseByActivityId(activity.getId()) + model.getAmountSAByActivityId(activity.getId());
        	double pe = model.getAmountMovementExpenseByActivityId(activity.getId()) + model.getAmountSPByActivityId(activity.getId());
        	
        	tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                ei,
                pi,
                ee,
                pe,
                ei - ee,
                pi - pe
            });
            
        	totalEstimatedIncomes += ei;
            totalEstimatedExpenses += ee;
            totalPaidIncomes += pi;
            totalPaidExpenses += pe;
        }
        
        view.getReportTable().setModel(tableModel);
        
        // Totals
        double profit = totalPaidIncomes - totalPaidExpenses;
        
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncomes);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncomes);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
        view.getProfitLabel().setText("Profit: " + profit);
    }
}
