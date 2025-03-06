package controller;

import model.Model;
import util.ApplicationException;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterIncomesExpensesView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ComboBoxModel;

public class RegisterIncomesExpensesController {
	private Model model;
	private RegisterIncomesExpensesView view;
	
	public RegisterIncomesExpensesController(Model model, RegisterIncomesExpensesView view) {
		this.model = model;
		this.view = view;
		
		initController();
		initView();
	}
	
	public void initController() {
		this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {view.disposeView();});}
		});
		
		this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {view.clearFields();});}
		});
		
		view.getTypeComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				String formTitle = "Register " + view.getType();
				view.changeTitlePanel(formTitle);
			}
		});
		
		view.getStatusComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if (view.getStatus() == "Estimated") {
					view.concealReceiptField();
				} else {
					view.showReceiptField();
				}
			}
		});
		
		view.getButtonLowRight().addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent a)
        	{
        		// Retrieve data entered from the View
        		String activity = view.getActivity();
	            String amount = view.getAmount();
	            String date = view.getDate();
	            String type = view.getType();
	            String concept = view.getConcept();
	            String receipt = view.getReceipt();
	            String status = view.getStatus();
	            
	            String idActivity = model.getActivityId(activity);
	            
	            switch (status) {
		            case "Estimated":
		            	if (!validateEstimatedMovementFields(amount, date, concept)) {
		            		return;
		            	}
		            	break;
		            case "Paid":
		            	if (!validatePaidMovementFields(amount, date, concept, receipt)) {
		            		return;
		            	}
		            	try {
		            		SemanticValidations.validateDateInPast(date, true, "Date cannot be in the future");
		            	} catch (Exception e) {
		            		view.showError("Invalid Date");
		            		throw new ApplicationException(e.getMessage());
		            	}
		            	break;
		            default:
		            	break;
	            }
	            
	            try {
	            	model.registerMovement(idActivity, amount, date, type, concept, receipt, status);
	            } catch (Exception e) {
	            	view.showError("Internal Error: Could Not Register Income/Expense");
	            	e.printStackTrace();
	            	return;
	            } 
	            
            	view.configureSummaryPanel();
        	}
        });
	}
	
	public void initView() {
		loadActivities();
		view.setVisible();
	}
	
	public void loadActivities() {
		List<Object[]> activitiesList = model.getListActivities();
		
		ComboBoxModel<Object> lModel = SwingUtil.getComboModelFromList(activitiesList);
		view.getActivities().setModel(lModel); 
	}
	
	public Boolean validateFields(String amount, String nif, String date, String invoiceId) {
		if (!SyntacticValidations.isNotEmpty(amount) || !SyntacticValidations.isNotEmpty(nif) || !SyntacticValidations.isNotEmpty(date)) {
			view.showError("All fields must be filled except Invoice ID (Optional)");
			return false;
		}
		if (!SyntacticValidations.isDate(date)) {
			view.showError("Please enter date in format: yyyy-MM-dd");
			return false;
		}
		if (!SyntacticValidations.isNumber(amount)) {
			view.showError("Please enter a valid number for the amount");
			return false;
		}
		if (SyntacticValidations.isNotEmpty(invoiceId) && !SyntacticValidations.isNumber(invoiceId)) {
			view.showError("Please enter a valid number to identify the invoice");
			return false;
		}
		
		return true;
	}
	
	public Boolean validateEstimatedMovementFields(String amount, String date, String concept) {
		if (!SyntacticValidations.isNotEmpty(amount) || !SyntacticValidations.isNotEmpty(concept)) {
			view.showError("Amount and Concept fields must be filled");
			return false;
		}
		
		if (SyntacticValidations.isNotEmpty(date) && !SyntacticValidations.isDate(date)) {
			view.showError("Please enter date in format: yyyy-MM-dd");
			return false;
		}
		if (!SyntacticValidations.isNumber(amount)) {
			view.showError("Please enter a valid number for the amount");
			return false;
		}
		
		return true;
	}
	
	public Boolean validatePaidMovementFields(String amount, String date, String concept, String receipt) {
		if (!SyntacticValidations.isNotEmpty(amount)
				|| !SyntacticValidations.isNotEmpty(date) || !SyntacticValidations.isNotEmpty(concept) 
				|| !SyntacticValidations.isNotEmpty(receipt)) {
			view.showError("All fields must be filled");
			return false;
		}
		if (!SyntacticValidations.isDate(date)) {
			view.showError("Please enter date in format: yyyy-MM-dd");
			return false;
		}
		if (!SyntacticValidations.isNumber(amount)) {
			view.showError("Please enter a valid number for the amount");
			return false;
		}
		
		return true;
	}
}