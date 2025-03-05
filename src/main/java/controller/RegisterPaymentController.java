package controller;

import model.Model;
import util.ApplicationException;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterPaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ComboBoxModel;

public class RegisterPaymentController {
	private Model model;
	private RegisterPaymentView view;
	
	public RegisterPaymentController(Model model, RegisterPaymentView view) {
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
		
		view.getButtonLowRight().addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent a)
        	{
        		// Retrieve data entered from the View
        		String activity = view.getActivity();
	            String amount = view.getAmount();
	            String nif = view.getNIF();
	            String date = view.getDate();
	            String invoiceId = view.getInvoiceId();
	            
	            if (validateFields(amount, nif, date, invoiceId) == false) {
	            	return;
	            } else {
	            	// Track Sponsorship to Agreement
		            Integer idSponsorshipAgreement = -1;
					try {
						idSponsorshipAgreement = model.getSponsorshipAgreementId(nif, activity);
					} catch (Exception e) {
						view.showError("No Agreement Found");
						e.printStackTrace();
						return;
					}
		            
					// Retrieve Invoice ID
					String invoiceRetrieved = "";
					try {
						invoiceRetrieved = model.getInvoiceId(idSponsorshipAgreement);
	            	} catch (Exception e) {
	            		view.showError("No Invoice Found");
	            		e.printStackTrace();
	            		return;
	            	}
					
					// Check if Invoice entered is correct
					if (SyntacticValidations.isNotEmpty(invoiceId) && !invoiceId.equals(invoiceRetrieved)) {
						view.showError("Incorrect Invoice");
						return;
					}
	            	
		            try {
		            	model.validatePaymentDate(date, Integer.parseInt(invoiceId));
		            	try {
		            		if (model.getSponsorshipPayment(invoiceId) == null) {
		            			model.registerPayment(idSponsorshipAgreement, date, Double.parseDouble(amount));
		            			view.configureSummaryPanel();
		            			return;
		            		}
		            		view.showError("Payment for that Invoice Already Exists");
			            } catch (Exception e) {
			            	view.showError("Internal Error: Could Not Register Payment");
			            	e.printStackTrace();
			            	return;
			            }
		            } catch (Exception e) {
		            	view.showError("Payment received before Invoice generation");
		            	throw new ApplicationException(e.getMessage());
		            }
	            }
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
}