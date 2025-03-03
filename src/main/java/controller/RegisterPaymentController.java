package controller;

import model.Model;
import util.ApplicationException;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import util.Util;
import view.RegisterPaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JOptionPane;

public class RegisterPaymentController {
	// Create an Object of Payment type for the Model
	// Create View Object
	private Model model;
	private RegisterPaymentView view;
	
	public RegisterPaymentController(Model model, RegisterPaymentView view) {
		this.model = model;
		this.view = view;
		
		this.view.initializeActivities(model.getListActivities());
		addRegisterListener();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public RegisterPaymentView getView() {
		return this.view;
	}

	public void setView(RegisterPaymentView view) {
		this.view = view;
	}
	
	public void addRegisterListener()
	{
		view.getButtonLowRight().addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
	        	/*
	        	 * try {
                	SyntacticValidations.validateDate(view.getDate().getText(), "");
                } catch (ApplicationException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
	        	if (getView().summaryConcealed())
	        	{
	        		getView().setSummaryConcealed();
	        		getView().configureSummaryPanel();
	        	}else {
		            String activity = getView().getActivity();
		            String amount = getView().getAmount();
		            String nif = getView().getNIF();
		            String date = getView().getDate();
		            String invoiceId = getView().getInvoiceId();
		            
		            Integer idSponsorshipAgreement = -1;
					try {
						idSponsorshipAgreement = model.getSponsorshipAgreementId(nif, activity);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		
		            if (invoiceId == "") {
		            	model.getInvoiceId(Integer.parseInt(invoiceId));
		            }
		            
		            model.validateDate(date, Integer.parseInt(invoiceId));
		            model.registerPayment(idSponsorshipAgreement, date, Double.parseDouble(amount));
	        	}*/
	        }
		});
	}
	
	// Control Model Object
	// Interact with the items inside the Model Class
	// Methods to set the properties in the actual Model
	
	// Control View Object to be updated any time there is a change in the Model related to Payments


	public void updateView()
	{
		view.display();
	}
}