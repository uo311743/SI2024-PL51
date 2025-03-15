package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.table.DefaultTableModel;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterActivityView;

public class RegisterActivityController {
	
	protected TemplatesModel templatesModel;
	protected LevelsModel levelsModel;
		
	protected RegisterActivityView view;
	
    private String lastSelectedAgreement;
	
	public RegisterActivityController(TemplatesModel tm, LevelsModel lm, RegisterActivityView v) {
		this.templatesModel = tm;
		this.levelsModel = lm;
		
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
    }
    
    public void initView() {
    	this.loadTemplates();
    	view.setVisible();
    }
    
    public void loadTemplates() {
        List<Object[]> templateList = templatesModel.getTemplatesListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(templateList);
        view.getTemplatesComboBox().setModel(lmodel);
    }
    
    private void setInputsEnabled(boolean enabled) {
    	view.getLevelNameTextField().setEnabled(enabled);
    	view.getLevelAmountTextField().setEnabled(enabled);
    	view.getAddButton().setEnabled(enabled);
    }
        
	private void getLevels() {
    	List<LevelsDTO> levels = levelsModel.getLevelsByActivityId();
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"id", "name", "fee"}, 0);
        for (LevelsDTO level : levels) {
        	tableModel.addRow(new Object[] {
        			level.getId(),
        			level.getName(),
        			level.getFee()
        	});
        }
		this.view.getLevelTable().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getLevelTable());
    }
	
	public void restoreDetail() {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.view.getNameTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
    /*
    private void showSubmitDialog() {
        int row = this.view.getAgreementsTable().getSelectedRow(); 
        
        String idAgreement = "";
        String nameActivity = String.valueOf(view.getActivityComboBox().getSelectedItem());
        
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
        
        int numOldInvoices = this.invoicesModel.getNumberOldInvoicesByActivityName(nameActivity);
    	
        if(numOldInvoices == 0) {
        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        
			this.invoicesModel.insertNewInvoice(lastSelectedAgreement, Util.dateToIsoString(SwingMain.getTodayDate()), Util.dateToIsoString(SwingMain.getTodayDate()), Util.dateToIsoString(SwingMain.getTodayDate()), totalAmount, taxRate);
	        
			JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Invoice added correctly",
	    			"This operation has been succesful",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restoreDetail();
        }
    	else {
    		message = "It will modify " + numOldInvoices + " invoices for that activity.";
    		response = JOptionPane.showConfirmDialog(
    	            this.view.getFrame(), message,
    	            "Confirm modification of old invoices",
    	            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
	        );
	        
	        if (response == JOptionPane.YES_OPTION) {
	        	SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        		SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        		SyntacticValidations.isDate(Util.dateToIsoString(SwingMain.getTodayDate()));
        		
	        	this.invoicesModel.insertUpdateInvoice(lastSelectedAgreement, Util.dateToIsoString(SwingMain.getTodayDate()), Util.dateToIsoString(SwingMain.getTodayDate()), Util.dateToIsoString(SwingMain.getTodayDate()), totalAmount, taxRate);
		        JOptionPane.showMessageDialog(
		    			this.view.getFrame(),
		    			"Invoice added correctly",
		    			"This operation has been succesful",
		    			JOptionPane.INFORMATION_MESSAGE
		    	);
		        this.restoreDetail();
	        }
    	}
    }*/
}
