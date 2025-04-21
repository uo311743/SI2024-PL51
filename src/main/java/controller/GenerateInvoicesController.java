package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipAgreementsDTO;
import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;
import util.InvoiceInstance;
import util.ModelManager;
import util.PDFGenerator;
import util.Params;
import util.SwingUtil;
import util.SyntacticValidations;
import view.GenerateInvoicesView;

public class GenerateInvoicesController {
		
	protected SponsorshipAgreementsModel saModel;
	protected InvoicesModel invoicesModel;
	protected ActivitiesModel activitiesModel;
	protected SponsorOrganizationsModel soModel;
	protected SponsorshipPaymentsModel spModel;
	
	protected Params params;
	
	protected GenerateInvoicesView view;
	
    private String lastSelectedAgreement;
	
	public GenerateInvoicesController(GenerateInvoicesView v) {
		this.saModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
		this.invoicesModel = ModelManager.getInstance().getInvoicesModel();
		this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
		this.soModel = ModelManager.getInstance().getSponsorOrganizationsModel();
		this.spModel = ModelManager.getInstance().getSponsorshipPaymentsModel();
		
		params = new Params();
		
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
    	this.view.getIdTextField().getDocument().addDocumentListener(new DocumentListener() {
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
    	
    	this.view.getDateIssuedTextField().getDocument().addDocumentListener(new DocumentListener() {
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
    	List<SponsorshipAgreementsDTO> agreements = saModel.getSignedAgreementsByActivityName(splitString(String.valueOf(view.getActivityComboBox().getSelectedItem()))[0], splitString(String.valueOf(view.getActivityComboBox().getSelectedItem()))[1]);
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
    	view.getIdTextField().setEnabled(enabled);
    	view.getDateIssuedTextField().setEnabled(enabled);
    }
        
	public void updateDetail() {	
		this.lastSelectedAgreement = SwingUtil.getSelectedKey(this.view.getAgreementsTable());
		if("".equals(this.lastSelectedAgreement)) {  
			restoreDetail();
		}
		else {
			view.getAmountLabel().setText("Total Amount: " + (String) this.view.getAgreementsTable().getModel().getValueAt(view.getAgreementsTable().getSelectedRow(), 2));
			view.getTaxRateLabel().setText("Tax Rate: " + String.valueOf(params.getTaxVAT()) + " %");
			
			this.setInputsEnabled(true);
		}
		
		boolean valid = true;
		
		// Validate Id
		String id = this.view.getIdTextField().getText();
		if(!SyntacticValidations.isNotEmpty(id)) {
			this.view.getIdTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getIdTextField().setForeground(Color.BLACK); 
		}
		
		// Validate Date
		String date = this.view.getDateIssuedTextField().getText();
		if(!SyntacticValidations.isDate(date)) {
			this.view.getDateIssuedTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getDateIssuedTextField().setForeground(Color.BLACK); 
		}
		
		// Generate Invoice button
		this.view.getButtonLowRight().setEnabled(valid);
	}
	
	public void restoreDetail() {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getAgreements();
		
    	this.view.getIdTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
    
    private void showSubmitDialog() {
        int row = this.view.getAgreementsTable().getSelectedRow(); 
        
        String idAgreement = "";
        
        if (row >= 0) {
        	idAgreement = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 0);
        }
        
        String id = view.getIdTextField().getText();
        String dateIssued = view.getDateIssuedTextField().getText();

        String amount = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 2);
        String dateAgreement = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 3);
        double taxRate = params.getTaxVAT();
        
        String taxAmount = String.valueOf(Double.valueOf(amount) * taxRate / 100);
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
                
        int numInvoices = this.invoicesModel.getNumberInvoicesByAgreement(idAgreement);
        
    	SyntacticValidations.isDate(dateIssued);
    	
    	if (this.invoicesModel.getNumberInvoices(id) == 0) {
    		if(numInvoices == 0) {
            	this.invoicesModel.insertNewInvoice(id, lastSelectedAgreement, dateIssued, totalAmount, String.valueOf(taxRate), dateAgreement);
    	        
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
    	        	this.invoicesModel.updateInsertInvoice(id, lastSelectedAgreement, dateIssued, totalAmount, String.valueOf(taxRate), dateAgreement);
    	        	this.spModel.updatePaymentsInvoiceId(id, idAgreement);
    		        JOptionPane.showMessageDialog(
    		    			this.view.getFrame(),
    		    			"Invoice added correctly",
    		    			"This operation has been succesful",
    		    			JOptionPane.INFORMATION_MESSAGE
    		    	);
    		        this.restoreDetail();
    	        }
        	}
    		
    		this.generatePDF(id);
    	}
    	else {
    		JOptionPane.showMessageDialog(
    				this.view.getFrame(),
		    		"This ID already exists in the system",
		    		"ERROR",
		    		JOptionPane.INFORMATION_MESSAGE
		    );
    	}
    }
    
    public static String[] splitString(String input) {
        String[] parts = input.split("-");
        return parts;
    }
    
    private void generatePDF(String id) {
    	
    	InvoicesDTO invoice = this.invoicesModel.getInvoiceById(id);
        SponsorOrganizationsDTO sponsor = this.soModel.getSOByInvoiceId(id);
        ActivitiesDTO activity = this.activitiesModel.getActivityByInvoice(id);
    	
    	InvoiceInstance invoiceInstance = new InvoiceInstance(invoice, sponsor, activity);
    	PDFGenerator.generateInvoice(invoiceInstance);
    }
}
