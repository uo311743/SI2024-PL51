package controller;

import model.Model;
import view.PaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PaymentController {
	// Create an Object of Payment type for the Model
	// Create View Object
	private Model model;
	private PaymentView view;
	
	public PaymentController(Model model, PaymentView view) {
		this.model = model;
		this.view = view;
		
		this.view.initializeActivities(model.getListActivities());
		//addListener();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public PaymentView getView() {
		return this.view;
	}

	public void setView(PaymentView view) {
		this.view = view;
	}
	
	public void addRegisterListener()
	{
		this.view.getButton().addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
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
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		
		            if (invoiceId == "") {
		            	model.getInvoiceId(Integer.parseInt(invoiceId));
		            }
		            
		            model.validateDate(date, Integer.parseInt(invoiceId));
		            model.registerPayment(idSponsorshipAgreement, date, Double.parseDouble(amount));
	        	}
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