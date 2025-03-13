package controller;

import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterPaymentView;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.UnexpectedException;
import java.sql.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;

public class RegisterPaymentController {
	
	protected SponsorshipAgreementsModel agreementsModel;
	protected SponsorshipPaymentsModel paymentsModel;
	protected InvoicesModel invoicesModel;
	
	
	private RegisterPaymentView view;
	
    private String lastSelectedInvoice;
	
	public RegisterPaymentController(SponsorshipAgreementsModel sam, SponsorshipPaymentsModel spm, InvoicesModel im, RegisterPaymentView view) {
		this.agreementsModel = sam;
		this.paymentsModel = spm;
		this.invoicesModel = im;
		
		this.view = view;
		
		this.initController();
		this.initView();
	}
	
	public void initController() {
		this.view.getSponsorsTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> getInvoices());
			}
		});
		
		this.view.getInvoicesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
		
		this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {view.disposeView();});}
		});
		
		this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {restartView();});}
		});
		
		this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
			}
		});
		
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
    	
    	
    	this.view.getDateTextField().getDocument().addDocumentListener(new DocumentListener() {
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
	
	public void restartView()
    {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getSponsors();
		
		this.view.getSponsorsTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getInvoicesTable().getModel();
		model.setRowCount(0); // Clears the table

    	
    	// Reset amount field with placeholder effect
    	this.view.getDateTextField().setText("");
    	this.view.getAmountTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
	
	private void getSponsors()
    {
    	List<SponsorOrganizationsDTO> sponsors = this.agreementsModel.getSponsorOrganizations();
		TableModel tmodel = SwingUtil.getTableModelFromPojos(sponsors, new String[] {"id", "name", "type", "address", "nif", "vat"});
		this.view.getSponsorsTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getSponsorsTable());
    }
	
	private void getInvoices()
	{
		int row = this.view.getSponsorsTable().getSelectedRow(); 
        
        String idSponsor = "";
        
        if (row >= 0)
        {
        	idSponsor = (String) this.view.getSponsorsTable().getModel().getValueAt(row, 0);
        }
    	List<InvoicesDTO> invoices = this.agreementsModel.getInvoicesBySponsor(idSponsor);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(invoices, new String[] {"id", "idSponsorshipAgreement", "dateIssued", "dateSent", "dateExpiration", "totalAmount", "taxRate", "status"});
		this.view.getInvoicesTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getInvoicesTable());
	}
	
	private void setInputsEnabled(boolean enabled)
    {
    	this.view.getDateTextField().setEnabled(enabled);
    	this.view.getAmountTextField().setEnabled(enabled);
    }
	
	public void updateDetail()
	{	
		// ------------------------------------------------------------
		// If an invoice is selected in the table do:
		
		this.lastSelectedInvoice = SwingUtil.getSelectedKey(this.view.getInvoicesTable());
		if("".equals(this.lastSelectedInvoice)) {  restartView(); }
		else
		{
			this.setInputsEnabled(true);
		}
				
		// ------------------------------------------------------------
		// Check JTextField inputs:
		boolean valid = true;
		
		// Validate amount
		String amount = this.view.getAmountTextField().getText();
		if(!SyntacticValidations.isDecimal(amount))
		{
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
		
		// Validate date
		String paymentDate = this.view.getDateTextField().getText();
		if(!SyntacticValidations.isDate(paymentDate))
		{
			this.view.getDateTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getDateTextField().setForeground(Color.BLACK); }
		
		// Activate/Deactivate the submit button
		this.view.getButtonLowRight().setEnabled(valid);
		
	}
	
	// ================================================================================

    private void showSubmitDialog()
    {
        int sponsorRow = this.view.getSponsorsTable().getSelectedRow(); 
        
        String sponsor = "";
        String idSponsor = "";
        
        if (sponsorRow >= 0)
        {
        	idSponsor = (String) this.view.getSponsorsTable().getModel().getValueAt(sponsorRow, 0);
        	sponsor = (String) this.view.getSponsorsTable().getModel().getValueAt(sponsorRow, 1);
        }
        
        int invoiceRow = this.view.getInvoicesTable().getSelectedRow(); 
        
        String invoice = "";
        String idInvoice = "";
        
        if (invoiceRow >= 0)
        {
        	idInvoice = (String) this.view.getSponsorsTable().getModel().getValueAt(invoiceRow, 0);
        	invoice = (String) this.view.getSponsorsTable().getModel().getValueAt(invoiceRow, 1);
        }

        String amount = this.view.getAmountTextField().getText();
        String date = this.view.getDateTextField().getText();

        String message = "<html><body>"
                + "<p>You are about to add a payment received from the sponsor: <b>" + sponsor + "</b>.</p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Invoice ID:</b></td><td style='padding: 2px 5px;'>" + idInvoice + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + amount + " euros</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Payment Date:</b></td><td style='padding: 2px 5px;'>" + date + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this sponsorship agreement?</i></p>"
                + "</body></html>";

        Object[] options = {"Yes", "No"};
        
        int response = JOptionPane.showOptionDialog(null,
                "Are you sure you want to continue?", 		// The message
                "Confirmation of Movement Registration",    // The title
                JOptionPane.DEFAULT_OPTION,          		// The option type
                JOptionPane.QUESTION_MESSAGE,        		// The message type
                null,                               		// Icon (null means default question icon)
                options,                            		// Custom buttons
                options[0]); 
        
        if (response == 1) return;
        
        try {
        	SemanticValidations.validateDateInFuture(date, true, "Payment cannot be made in the future");
        	this.invoicesModel.validatePaymentDate(date, Integer.parseInt(idInvoice));
        	this.paymentsModel.registerPayment(Integer.parseInt(idInvoice), date, Double.parseDouble(amount));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        this.restartView();
    }
}