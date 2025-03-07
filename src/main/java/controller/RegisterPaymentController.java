package controller;

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
import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;

public class RegisterPaymentController {
	
	protected SponsorshipAgreementsModel saModel;
	protected SponsorshipPaymentsModel spModel;
	protected ActivitiesModel activitiesModel;
	protected InvoicesModel invoicesModel;
	
	private RegisterPaymentView view;
	
	public RegisterPaymentController(SponsorshipAgreementsModel sam, SponsorshipPaymentsModel spm, ActivitiesModel am, InvoicesModel im, RegisterPaymentView view) {
		this.saModel = sam;
		this.spModel = spm;
		this.activitiesModel = am;
		this.invoicesModel = im;
		
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
						idSponsorshipAgreement = saModel.getSponsorshipAgreementId(nif, activity);
					} catch (Exception e) {
						view.showError("No Agreement Found");
						e.printStackTrace();
						return;
					}
		            
					// Retrieve Invoice ID
					String invoiceRetrieved = "";
					try {
						invoiceRetrieved = invoicesModel.getInvoiceId(idSponsorshipAgreement);
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
		            	invoicesModel.validatePaymentDate(date, Integer.parseInt(invoiceId));
		            	try {
		            		if (spModel.getSponsorshipPayment(invoiceId) == null) {
		            			spModel.registerPayment(idSponsorshipAgreement, date, Double.parseDouble(amount));
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
		List<Object[]> activitiesList = activitiesModel.getActivityListArray();
		
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