package controller;

import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;
import util.ModelManager;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterPaymentView;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
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
	
	private List<String> invoicesId;
	
	private boolean submit;

	public RegisterPaymentController(RegisterPaymentView view) {
		this.agreementsModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
		this.paymentsModel = ModelManager.getInstance().getSponsorshipPaymentsModel();
		this.invoicesModel = ModelManager.getInstance().getInvoicesModel();
		this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
		this.sponsorsModel = ModelManager.getInstance().getSponsorOrganizationsModel();

		this.view = view;
		
		this.invoicesId = new ArrayList<String>();
		
		this.submit = false;

		this.initController();
		this.initView();
	}

	public void initController() {
		this.view.getInvoicesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> invoiceSelection());
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
	
	public void clearFields() {
		// Reset amount field with placeholder effect
		this.view.getDateTextField().setText("");
		this.view.getAmountTextField().setText("");
						
		// Reset summary panel
		this.view.getTotalInvoiceLabel().setText("0.0");
		this.view.getTotalPaymentsLabel().setText("0.0");
		this.view.getRemainingBalanceLabel().setText("0.0");
						
		this.view.getCompensationCheckBox().setSelected(false);
	}

	public void restartView() {
		this.getInvoices();

		this.view.getInvoicesTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getPaymentsTable().getModel();
		model.setRowCount(0); // Clears the table

		this.clearFields();

		this.setInputsEnabled(false);
	}
	
	public void updateView() {
		this.view.getPaymentsTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getPaymentsTable().getModel();
		model.setRowCount(0); // Clears the table
		this.getPayments();
		this.calculateTotals();
	}
	
	public void calculateTotals() {
		// Update Labels in the UI
	    double invoiceTotalAmount = this.invoicesModel.getTotalAmount(lastSelectedInvoice);
	    double totalPayments = this.paymentsModel.getTotalAmountPaid(lastSelectedInvoice);
	    this.view.getTotalInvoiceLabel().setText(String.valueOf(invoiceTotalAmount));
	    this.view.getTotalPaymentsLabel().setText(String.valueOf(totalPayments));
	    this.view.getRemainingBalanceLabel().setText(String.valueOf(invoiceTotalAmount - totalPayments));
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
	    String[] columnNames = {"dateIssued", "activity", "sponsor", "status", "totalAmount"};
	    
	    // Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
	    
	    // Populate table model
	    invoicesId.clear();
	    for (InvoicesDTO invoice : invoices) {
	    	invoicesId.add(invoice.getId());
	        Object[] rowData = {
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
	    // Create larger font
	 	Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
	 		
	 	// Set the model in the JTable
	 	this.view.getInvoicesTable().setModel(model);
	    JTableHeader header = this.view.getInvoicesTable().getTableHeader();
 	    header.setFont(largerFont);
 	    this.view.adjustColumns();
	}
	
	public void invoiceSelection() {
		this.clearFields();
		this.getPayments();
		this.updateDetail();
		this.calculateTotals();
	}
	
	private String calculateActivity(String idInvoice) {
	    return this.activitiesModel.getActivityByInvoice(idInvoice).getName();
	}

	private String calculateSponsor(String idInvoice) {
	    return this.sponsorsModel.getSOByInvoiceId(idInvoice).getName(); // Implement actual logic
	}
	
	private void getPayments() {
		List<SponsorshipPaymentsDTO> payments = this.paymentsModel.getSponsorshipPayment(lastSelectedInvoice);
		
		payments.sort(Comparator.comparing(SponsorshipPaymentsDTO::getDate));
		
		// Create table model
		TableModel tmodel = SwingUtil.getTableModelFromPojos(payments, new String[] {"date", "amount"});
	    
	    // Set the model in the JTable
	    this.view.getPaymentsTable().setModel(tmodel);
	    
	    // Create larger font
	 	Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
	 	 		
	 	// Set the model in the JTable
	 	this.view.getPaymentsTable().setModel(tmodel);
	 	JTableHeader header = this.view.getPaymentsTable().getTableHeader();
	    header.setFont(largerFont);
	    this.view.adjustColumns();
	}

	private void setInputsEnabled(boolean enabled) {
		this.view.getDateTextField().setEnabled(enabled);
		this.view.getAmountTextField().setEnabled(enabled);
	}

	public void updateDetail() {
		// ------------------------------------------------------------
		// If an invoice is selected in the table do:
		int row = this.view.getInvoicesTable().getSelectedRow();
		if (row < 0) {
			return;
		}
		
		this.lastSelectedInvoice = invoicesId.get(row);
		
		if ("".equals(this.lastSelectedInvoice)) {
			restartView();
		} else {
			this.getPayments();
			this.setInputsEnabled(true);
		}

		// ------------------------------------------------------------
		// Check JTextField inputs:
		this.submit = true;

		// Validate amount
		String amount = this.view.getAmountTextField().getText();
		if (!SyntacticValidations.isDecimal(amount) || !SyntacticValidations.isNotEmpty(amount) || !SyntacticValidations.isPositiveNumber(amount, true))
		{
			this.view.getAmountTextField().setForeground(Color.RED);
			this.submit = false;
		} else {
			this.view.getAmountTextField().setForeground(Color.BLACK);
		}

		// Validate date
		String paymentDate = this.view.getDateTextField().getText();
		if (!SyntacticValidations.isDate(paymentDate) || !SyntacticValidations.isNotEmpty(paymentDate)) {
			this.view.getDateTextField().setForeground(Color.RED);
			this.submit = false;
		} else {
			this.view.getDateTextField().setForeground(Color.BLACK);
		}
	}

	// ================================================================================

	private void showSubmitDialog() {
		if (!submit) {
			// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"Some of the fields have an incorrect format", // Error message
        		"Payment Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
		}
		
		// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
		int invoiceRow = this.view.getInvoicesTable().getSelectedRow();

		String sponsor = "";
		String idInvoice = "";
		String activity = "";

		if (invoiceRow >= 0) {
			idInvoice = lastSelectedInvoice;
			sponsor = (String) this.view.getInvoicesTable().getModel().getValueAt(invoiceRow, 2);
			activity = (String) this.view.getInvoicesTable().getModel().getValueAt(invoiceRow, 1);
		}

		String amount = this.view.getAmountTextField().getText();
		String date = this.view.getDateTextField().getText();
		
		String message = "";
        boolean validated = true;
        
        if (!SyntacticValidations.isNotEmpty(amount)) {
        	message = "<html><body>"
                    + "<p>Amount must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(date)) {
        	message = "<html><body>"
                    + "<p>Date must be provided</p>"
                    + "</body></html>";
        	validated = false;
        }
        
        if (!validated) {
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + message, // Error message
        		"Payment Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
        }
		
		if (this.view.getCompensationCheckBox().isSelected())
		{
    		Double aux = Double.parseDouble(amount);
			aux = (-1)*aux;
			amount = String.valueOf(aux);
		}
		
		String balance = this.view.getRemainingBalanceLabel().getText();
		Double newBalance = Double.parseDouble(balance) - Double.parseDouble(amount);

		message = "<html><body>" + "<p>You are about to add a payment received from the sponsor: <b>" + sponsor
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

		try {
			SemanticValidations.validateAllowedValues(this.activitiesModel.getActivityByName(activity).getStatus(), new String[] { "planned" }, "Activity not open");
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
			return;
		}
		try {
			this.invoicesModel.validatePaymentDate(date, idInvoice);
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
			return;
		}
		try {
			this.paymentsModel.registerPayment(idInvoice, date, Double.parseDouble(amount));
			if (newBalance == 0 )
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
		
		this.updateView();
	}
}