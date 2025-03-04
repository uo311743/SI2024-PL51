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
	// Create an Object of Payment type for the Model
	// Create View Object
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
	            	view.showError("All fields must be filled");
	            } else {
	            	// Syntactic validation of date entered by the user
	        		try {
	                	SyntacticValidations.validateDate(view.getDate(), "");
	                } catch (ApplicationException ex) {
	                	view.showError("Invalid date format. Please use yyyy-MM-dd.");
	                	throw new ApplicationException(ex.getMessage());
	                }
	        		
	            	// Track Company to Agreement
		            Integer idSponsorshipAgreement = -1;
					try {
						idSponsorshipAgreement = model.getSponsorshipAgreementId(nif, activity);
					} catch (Exception e) {
						view.showError("No Agreement Found");
						e.printStackTrace();
						return;
					}
		            
		            // Check if Invoice entered in the Payment is a correct ID attribute
		            // Fetch in DB if there is an Invoice generated for the Agreement identified by id
					// Retrieve Invoice ID in case it was not enter or check if Invoice ID entered was the correct one
	            	try {
	            		model.getInvoiceId(Integer.parseInt(invoiceId), idSponsorshipAgreement);
	            	} catch (Exception e) {
	            		view.showError("No Invoice Found");
	            		e.printStackTrace();
	            		return;
	            	}
	            	
		            if (model.validateDate(date, Integer.parseInt(invoiceId))) {
		            	try {
			            	model.registerPayment(idSponsorshipAgreement, date, Double.parseDouble(amount));
			            } catch (Exception e) {
			            	view.showError("Internal Error: Could Not Register Payment");
			            	e.printStackTrace();
			            	return;
			            } 
		            	view.configureSummaryPanel();
		            } else {
		            	view.showError("Payment received before Invoice generation");
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
		if (amount.isEmpty() || nif.isEmpty() || date.isEmpty() || invoiceId.isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	// Control Model Object
	// Interact with the items inside the Model Class
	// Methods to set the properties in the actual Model
	
	// Control View Object to be updated any time there is a change in the Model related to Payments
}