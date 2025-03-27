package controller;

import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterPaymentView;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipPaymentsDTO;

public class RegisterPaymentController {

	protected SponsorshipAgreementsModel agreementsModel;
	protected SponsorshipPaymentsModel paymentsModel;
	protected InvoicesModel invoicesModel;
	protected ActivitiesModel activitiesModel;
	protected SponsorOrganizationsModel sponsorsModel;

	private RegisterPaymentView view;

	private String lastSelectedInvoice;

	public RegisterPaymentController(SponsorshipAgreementsModel sam, SponsorshipPaymentsModel spm, InvoicesModel im,
			ActivitiesModel am, SponsorOrganizationsModel som, RegisterPaymentView view) {
		this.agreementsModel = sam;
		this.paymentsModel = spm;
		this.invoicesModel = im;
		this.activitiesModel = am;
		this.sponsorsModel = som;

		this.view = view;

		this.initController();
		this.initView();
	}

	public void initController() {
		this.view.getInvoicesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});

		this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					view.disposeView();
				});
			}
		});

		this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					restartView();
				});
			}
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
			public void changedUpdate(DocumentEvent e) {
			}
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
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}

	public void initView() {
		this.restartView();
		view.setVisible();
	}

	public void restartView() {
		this.getInvoices();

		this.view.getInvoicesTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getPaymentsTable().getModel();
		model.setRowCount(0); // Clears the table

		// Reset amount field with placeholder effect
		this.view.getDateTextField().setText("");
		this.view.getAmountTextField().setText("");
		
		// Reset summary panel
		this.view.getTotalInvoiceLabel().setText("0.00");
		this.view.getTotalPaymentsLabel().setText("0.00");
		this.view.getRemainingBalanceLabel().setText("0.00");

		this.setInputsEnabled(false);
	}

	private void getInvoices() {
		List<SponsorOrganizationsDTO> sponsors = this.sponsorsModel.getSponsorOrganizations();
		
		List<InvoicesDTO> invoices = new ArrayList<>();
		
		for (SponsorOrganizationsDTO sponsor : sponsors) {
		    List<InvoicesDTO> sponsorInvoices = this.invoicesModel.getInvoicesBySponsor(sponsor.getId()); // Retrieve invoices
		    invoices.addAll(sponsorInvoices); // Add all retrieved invoices to the main list
		}
		
		// Sort invoices by dateIssued
	    invoices.sort(Comparator.comparing(InvoicesDTO::getDateIssued));
	    
	    // Define columns with "dateIssued" moved to the left and two new columns
	    String[] columnNames = { "id", "dateIssued", "activity", "sponsor", "status", "totalAmount"};
	    
	    // Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
	    
	    // Populate table model
	    for (InvoicesDTO invoice : invoices) {
	        Object[] rowData = {
	        	invoice.getId(),
	            invoice.getDateIssued(),
	            calculateActivity(invoice.getId()), // Manually compute "activity"
	            calculateSponsor(invoice.getId()),   // Manually compute "sponsor"
	            invoice.getStatus(),
	            invoice.getTotalAmount()
	        };
	        model.addRow(rowData);
	    }
	    
	    // Set the model in the JTable
	    this.view.getInvoicesTable().setModel(model);
	    SwingUtil.autoAdjustColumns(this.view.getInvoicesTable());
	}
	
	private String calculateActivity(String idInvoice) {
	    return this.activitiesModel.getActivityByInvoice(idInvoice).getName();
	}

	private String calculateSponsor(String idInvoice) {
	    return this.sponsorsModel.getSOByInvoiceId(idInvoice).getName(); // Implement actual logic
	}
	
	private void getPayments() {
		int row = this.view.getInvoicesTable().getSelectedRow();
		
		String idInvoice = "";
		
		if (row >= 0) {
			idInvoice = (String) this.view.getInvoicesTable().getModel().getValueAt(row, 0);
		}
		
		Double invoiceTotalAmount = Double.parseDouble((String) this.view.getInvoicesTable().getModel().getValueAt(row, 5));
		
		List<SponsorshipPaymentsDTO> payments = this.paymentsModel.getSponsorshipPayment(idInvoice);
		
		payments.sort(Comparator.comparing(SponsorshipPaymentsDTO::getDate));
		
		String[] columnNames = { "invoiceId", "date", "amount"};
		
		// Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		
	    double totalPayments = this.computeTotalPayments(idInvoice);
	    for (SponsorshipPaymentsDTO payment : payments) {
	        Object[] rowData = {
	        	idInvoice,
	            payment.getDate(),
	            payment.getAmount()
	        };
	        model.addRow(rowData);
	    }
	    
	    // Set the model in the JTable
	    this.view.getPaymentsTable().setModel(model);
	    SwingUtil.autoAdjustColumns(this.view.getPaymentsTable());
	    
	    // Update Labels in the UI
	    this.view.getTotalInvoiceLabel().setText(invoiceTotalAmount.toString());
	    this.view.getTotalPaymentsLabel().setText(String.valueOf(totalPayments));
	    this.view.getRemainingBalanceLabel().setText(String.valueOf(invoiceTotalAmount - totalPayments));
	}
	
	private Double computeTotalPayments(String idInvoice)
	{
	    List<SponsorshipPaymentsDTO> payments = this.paymentsModel.getSponsorshipPayment(idInvoice);
	    
	    double totalPayments = 0.0;
	    for (SponsorshipPaymentsDTO payment : payments) {
	        totalPayments += Double.parseDouble(payment.getAmount()); // Accumulate payment total
	    }
	    
	    return totalPayments;
	}

	private void setInputsEnabled(boolean enabled) {
		this.view.getDateTextField().setEnabled(enabled);
		this.view.getAmountTextField().setEnabled(enabled);
	}

	public void updateDetail() {
		// ------------------------------------------------------------
		// If an invoice is selected in the table do:

		this.lastSelectedInvoice = SwingUtil.getSelectedKey(this.view.getInvoicesTable());
		if ("".equals(this.lastSelectedInvoice)) {
			restartView();
		} else {
			this.getPayments();
			this.setInputsEnabled(true);
		}

		// ------------------------------------------------------------
		// Check JTextField inputs:
		boolean valid = true;

		// Validate amount
		String amount = this.view.getAmountTextField().getText();
		if (!SyntacticValidations.isDecimal(amount))

		{
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} else {
			this.view.getAmountTextField().setForeground(Color.BLACK);
		}

		// Validate date
		String paymentDate = this.view.getDateTextField().getText();
		if (!SyntacticValidations.isDate(paymentDate)) {
			this.view.getDateTextField().setForeground(Color.RED);
			valid = false;
		} else {
			this.view.getDateTextField().setForeground(Color.BLACK);
		}
	}

	// ================================================================================

	private void showSubmitDialog() {
		// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
		int invoiceRow = this.view.getInvoicesTable().getSelectedRow();

		String sponsor = "";
		String idInvoice = "";
		String activity = "";

		if (invoiceRow >= 0) {
			idInvoice = (String) this.view.getInvoicesTable().getModel().getValueAt(invoiceRow, 0);
			sponsor = (String) this.view.getInvoicesTable().getModel().getValueAt(invoiceRow, 3);
			activity = (String) this.view.getInvoicesTable().getModel().getValueAt(invoiceRow, 2);
		}

		String amount = this.view.getAmountTextField().getText();
		String date = this.view.getDateTextField().getText();
		
		String balance = this.view.getRemainingBalanceLabel().getText();
		Double newBalance = Double.parseDouble(balance) - Double.parseDouble(amount);

		String message = "<html><body>" + "<p>You are about to add a payment received from the sponsor: <b>" + sponsor
				+ "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
				+ "<tr><td style='padding: 2px 5px;'><b>Activity:</b></td><td style='padding: 2px 5px;'>" + activity
				+ "<tr><td style='padding: 2px 5px;'><b>Invoice ID:</b></td><td style='padding: 2px 5px;'>" + idInvoice
				+ "</td></tr>" + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>"
				+ amount + " euros</td></tr>"
				+ "<tr><td style='padding: 2px 5px;'><b>Payment Date:</b></td><td style='padding: 2px 5px;'>" + date
				+ "<tr><td style='padding: 2px 5px;'><b>Current Balance for the Invoice:</b></td><td style='padding: 2px 5px;'>" + balance
				+ "<tr><td style='padding: 2px 5px;'><b>Balance after the Registration:</b></td><td style='padding: 2px 5px;'>" + newBalance
				+ "</td></tr>" + "</table>"
				+ "<p><i>Are you sure you want to continue?</i></p>" + "</body></html>";

		Object[] options = { "Yes", "No" };

		int response = JOptionPane.showOptionDialog(null, message, // The message
				"Confirmation of Payment Registration", // The title
				JOptionPane.DEFAULT_OPTION, // The option type
				JOptionPane.QUESTION_MESSAGE, // The message type
				null, // Icon (null means default question icon)
				options, // Custom buttons
				options[0]);

		if (response == 1)
			return;
		
		Boolean compensationPayment = this.view.getCompensationCheckBox().isSelected() ? true : false;

		try {
			if (compensationPayment) {
				SemanticValidations.validateNegativeNumber(amount, "Compensation Payments must be of negative amounts");
			} else {
				SemanticValidations.validatePositiveNumber(amount, "Payments must be of positive amounts");
			}
			SemanticValidations.validateAllowedValues(this.activitiesModel.getActivityByName(activity).getStatus(), new String[] { "planned" }, "Activity not open");
			SemanticValidations.validateDateInPast(date, true, "Payment cannot be made in the future");
			this.invoicesModel.validatePaymentDate(date, idInvoice);
			this.paymentsModel.registerPayment(idInvoice, date, Double.parseDouble(amount));
			Double updatedBalance = Double.parseDouble(this.view.getRemainingBalanceLabel().getText()) - Double.parseDouble(amount);
			if (updatedBalance == 0 )
		    {
		    	this.invoicesModel.setInvoiceStatus(idInvoice, "paid");
		    } else
		    {
		    	this.invoicesModel.setInvoiceStatus(idInvoice, "issued");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			
			// Show an error dialog
			JOptionPane.showOptionDialog(
				null, 
				"An error occurred: " + e.getMessage(), // Error message
				"Payment Registration Error", // Dialog title
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.ERROR_MESSAGE, 
				null, 
				new Object[]{"OK"}, // Force "OK" button in English
				"OK" // Default selected option
			);
		}

		this.restartView();
	}
}