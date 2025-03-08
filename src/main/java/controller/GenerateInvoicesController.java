package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import DTOs.SponsorshipAgreementsDTO;
import model.InvoicesModel;
import model.SponsorshipAgreementsModel;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import util.UnexpectedException;
import util.Util;
import view.GenerateInvoicesView;

public class GenerateInvoicesController {
		
	protected SponsorshipAgreementsModel saModel;
	protected InvoicesModel invoicesModel;
	
	protected GenerateInvoicesView view;
	
    private String lastSelectedAgreement;
	
	public GenerateInvoicesController(SponsorshipAgreementsModel sam, InvoicesModel im, GenerateInvoicesView v) {
		this.saModel = sam;
		this.invoicesModel = im;
		
        this.view = v;
        this.initView();
        this.initController();
    }
    
    public void initController() {
    	// Low buttons
    	this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
			}
		});
    	
    	this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
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
    	this.view.getAmountTextField().getDocument().addDocumentListener(new DocumentListener() {
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
    	this.restartView();
    	view.setVisible();
    }
        
	public void updateDetail() {	
		this.lastSelectedAgreement = SwingUtil.getSelectedKey(this.view.getAgreementsTable());
		if("".equals(this.lastSelectedAgreement)) {  
			restartView();
		}
		else {
			view.getIdLabel().setText("ID: " + lastSelectedAgreement); 
			view.getDateIssuedLabel().setText("Date Issued: " + (String) this.view.getAgreementsTable().getModel().getValueAt(this.view.getAgreementsTable().getSelectedRow(), 5));
			// Preguntar
			view.getDateExpLabel().setText("Date Expired: " + (String) this.view.getAgreementsTable().getModel().getValueAt(this.view.getAgreementsTable().getSelectedRow(), 5));
			
			this.setInputsEnabled(true);
		}
		
		boolean valid = true;
		
		// Validate Amount
		String amount = this.view.getAmountTextField().getText();
		if(!SyntacticValidations.isDecimal(amount)) {
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getAmountTextField().setForeground(Color.BLACK); 
		}
		
		// Validate Amount
		String taxRate = this.view.getTaxRateTextField().getText();
		if(!SyntacticValidations.isDecimal(taxRate)) {
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getAmountTextField().setForeground(Color.BLACK); 
		}
		
		// Generate Invoice button
		this.view.getButtonLowRight().setEnabled(valid);
	}
	
	public void restartView() {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getAgreements();
		
		this.view.getAmountTextField().setText("");
    	this.view.getTaxRateTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
    
    // ================================================================================
    
    private void getAgreements() {
    	List<SponsorshipAgreementsDTO> agreements = saModel.getAgreementsByActivityName(String.valueOf(view.getActivitiesComboBox().getSelectedItem()));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(agreements, new String[] {"id", "amount", "date", "status"});
		this.view.getAgreementsTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getAgreementsTable());
    }
    
    private void setInputsEnabled(boolean enabled) {
    	view.getAmountTextField().setEnabled(enabled);
    	view.getTaxRateTextField().setEnabled(enabled);
    }
    
    // ================================================================================

    private void showSubmitDialog() {
        int row = this.view.getAgreementsTable().getSelectedRow(); 
        
        String idAgreement = "";
        String dateAgreement = "";
        String nameActivity = String.valueOf(view.getActivitiesComboBox().getSelectedItem());
        
        if (row >= 0) {
        	idAgreement = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 0);
        	dateAgreement = (String) this.view.getAgreementsTable().getModel().getValueAt(row, 5);
        }

        String amount = this.view.getAmountTextField().getText();
        String taxRate = this.view.getTaxRateTextField().getText();
        
        String taxAmount = String.valueOf(Double.valueOf(amount) * Double.valueOf(taxRate) / 100);
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
        
        int numOldInvoices = this.invoicesModel.getNumberOldInvoicesByActivityName(nameActivity);
    	
        if(numOldInvoices == 0) {
        	try {
        		// Preguntar
				this.invoicesModel.insertNewInvoice(lastSelectedAgreement, dateAgreement, Util.dateToIsoString(SwingMain.getTodayDate()), Util.dateToIsoString(SwingMain.getTodayDate()), totalAmount, taxRate);
			} 
        	catch (UnexpectedException e) {
				e.printStackTrace();
			}
	        JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Sponshorship agreement añadido correctamente.",
	    			"Operación realizada correctamente",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restartView();
        }
    	else {
    		message = "It will modify " + numOldInvoices + " invoices for that activity.";
    		response = JOptionPane.showConfirmDialog(
    	            this.view.getFrame(), message,
    	            "Confirm modification of old Sponsorship Agreements",
    	            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
	        );
	        
	        if (response == JOptionPane.YES_OPTION) {
	        	this.saModel.insertUpdateSponsorshipAgreement(idContact, idGBmember, idActivity, amount, agreementDate);
		        JOptionPane.showMessageDialog(
		    			this.view.getFrame(),
		    			"Sponshorship agreement añadido correctamente. Los antigos se han marcado como 'modificados'.",
		    			"Operación realizada correctamente",
		    			JOptionPane.INFORMATION_MESSAGE
		    	);
		        this.restartView();
	        }
    	}
    }
}
