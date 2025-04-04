package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import DTOs.SponsorshipAgreementsDTO;
import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import util.ModelManager;
import util.Params;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import util.Util;
import view.GenerateInvoicesView;

public class GenerateInvoicesController {
		
	protected SponsorshipAgreementsModel saModel;
	protected InvoicesModel invoicesModel;
	protected ActivitiesModel activitiesModel;
	protected SponsorOrganizationsModel soModel;
	
	protected GenerateInvoicesView view;
	
    private String lastSelectedAgreement;
	
	public GenerateInvoicesController(GenerateInvoicesView v) {
		this.saModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
		this.invoicesModel = ModelManager.getInstance().getInvoicesModel();
		this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
		this.soModel = ModelManager.getInstance().getSponsorOrganizationsModel();
		
        this.view = v;
        this.initView();
        this.initController();
    }
    
    public void initController() {
    	// Low buttons
    	this.view.getButtonLowLeft().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
			}
		});
    	
    	this.view.getButtonLowRight().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
			}
		});
    	
    	// Activities ComboBox
    	this.view.getActivityComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> restoreDetail());
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    	
    	// Agreements Table
    	this.view.getAgreementsTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    	
    	// Invoice Details Panel
    	this.view.getTaxRateTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> updateDetail());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    }
    
    public void initView() {
    	this.loadActivities();
    	this.restoreDetail();
    	view.setVisible();
    }
    
    public void loadActivities() {
        List<Object[]> activityList = activitiesModel.getActiveActivityListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(activityList);
        view.getActivityComboBox().setModel(lmodel);
    }
    
    private void getAgreements() {
    	List<SponsorshipAgreementsDTO> agreements = saModel.getSignedAgreementsByActivityName(splitString(String.valueOf(view.getActivityComboBox().getSelectedItem()))[0]);
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"id", "Sponsor", "amount", "date", "status"}, 0);
        for (SponsorshipAgreementsDTO agreement : agreements) {
        	tableModel.addRow(new Object[] {
        			agreement.getId(),
        			soModel.getSponsorOrganizationByAgreementId(agreement.getId()).getName(),
        			agreement.getAmount(),
        			agreement.getDate(),
        			agreement.getStatus()
        	});
        }
		this.view.getAgreementsTable().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getAgreementsTable());
    }
    
    private void setInputsEnabled(boolean enabled) {
    	view.getTaxRateTextField().setEnabled(enabled);
    }
        
	public void updateDetail() {	
		this.lastSelectedAgreement = SwingUtil.getSelectedKey(this.view.getAgreementsTable());
		if("".equals(this.lastSelectedAgreement)) {  
			restoreDetail();
		}
		else {
			String today = (String) this.view.getAgreementsTable().getModel().getValueAt(this.view.getAgreementsTable().getSelectedRow(), 3);
	        Date date = Util.isoStringToDate(today);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
			Params params = new Params();
			calendar.add(Calendar.DAY_OF_MONTH, params.getTaxExpDays());
	        String expDate = Util.dateToIsoString(calendar.getTime());
			
			view.getIdLabel().setText("ID Sponsorship Agreement: " + lastSelectedAgreement); 
			view.getDateIssuedLabel().setText("Date Issued: " + today);
			view.getDateExpLabel().setText("Date Expired: " + expDate);
			view.getAmountLabel().setText("Amount: " + (String) this.view.getAgreementsTable().getModel().getValueAt(view.getAgreementsTable().getSelectedRow(), 2));
			
			this.setInputsEnabled(true);
		}
		
		boolean valid = true;
		
		// Validate TaxRate
		String taxRate = this.view.getTaxRateTextField().getText();
		if(!SyntacticValidations.isDecimal(taxRate)) {
			this.view.getTaxRateTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getTaxRateTextField().setForeground(Color.BLACK); 
		}
		
		// Generate Invoice button
		this.view.getButtonLowRight().setEnabled(valid);
	}
	
	public void restoreDetail() {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getAgreements();
		
    	this.view.getTaxRateTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
    
    private void showSubmitDialog() {
        int row = this.view.getAgreementsTable().getSelectedRow(); 
        
        String idAgreement = "";
        
        if (row >= 0) {
        	idAgreement = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 0);
        }

        String amount = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 2);
        String taxRate = this.view.getTaxRateTextField().getText();
        
        String taxAmount = String.valueOf(Double.valueOf(amount) * Double.valueOf(taxRate));
        String totalAmount = String.valueOf(Double.valueOf(amount) + Double.valueOf(taxAmount));

        String message = "<html><body>"
                + "<p>Add an invoice for the sponsorship agreement: <b>" + idAgreement + "</b>.</p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>No tax amount:</b></td><td style='padding: 2px 5px;'>" + amount + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Tax amount:</b></td><td style='padding: 2px 5px;'>" + taxAmount + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Total:</b></td><td style='padding: 2px 5px;'>" + totalAmount + "</td></tr>"
                + "</table>"
                + "<p><i>Proceed with these invoice?</i></p>"
                + "</body></html>";

        int response = JOptionPane.showConfirmDialog(
            this.view.getFrame(),  message,
            "Confirm Sponsorship Agreement Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        
        if (response != JOptionPane.YES_OPTION) {
        	return;
        }
        
        String nameActivity = String.valueOf(view.getActivityComboBox().getSelectedItem());
        String name = splitString(nameActivity)[0];
        String edition = splitString(nameActivity)[1];
        
        int numInvoices = this.invoicesModel.getNumberInvoicesByActivityNameEdition(name, edition);
    	
        if(numInvoices == 0) {
        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        	String today = (String) this.view.getAgreementsTable().getModel().getValueAt(this.view.getAgreementsTable().getSelectedRow(), 3);
	        Date date = Util.isoStringToDate(today);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
			Params params = new Params();
			calendar.add(Calendar.DAY_OF_MONTH, params.getTaxExpDays());
	        String expDate = Util.dateToIsoString(calendar.getTime());
						
			this.invoicesModel.insertNewInvoice(lastSelectedAgreement, Util.dateToIsoString(SwingMain.getTodayDate()), expDate, totalAmount, taxRate);
	        
			JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Invoice added correctly",
	    			"This operation has been succesful",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restoreDetail();
        }
    	else {
    		message = "It will modify " + numInvoices + " invoices for that activity.";
    		response = JOptionPane.showConfirmDialog(
    	            this.view.getFrame(), message,
    	            "Confirm modification of old invoices",
    	            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
	        );
	        
	        if (response == JOptionPane.YES_OPTION) {
	        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
	        	String today = (String) this.view.getAgreementsTable().getModel().getValueAt(this.view.getAgreementsTable().getSelectedRow(), 3);
		        Date date = Util.isoStringToDate(today);
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
				Params params = new Params();
				calendar.add(Calendar.DAY_OF_MONTH, params.getTaxExpDays());
		        String expDate = Util.dateToIsoString(calendar.getTime());
        		
	        	this.invoicesModel.insertUpdateInvoice(lastSelectedAgreement, Util.dateToIsoString(SwingMain.getTodayDate()), expDate, totalAmount, taxRate);
		        JOptionPane.showMessageDialog(
		    			this.view.getFrame(),
		    			"Invoice added correctly",
		    			"This operation has been succesful",
		    			JOptionPane.INFORMATION_MESSAGE
		    	);
		        this.restoreDetail();
	        }
    	}
    }
    
    public static String[] splitString(String input) {
        String[] parts = input.split("-");
        return parts;
    }
}
