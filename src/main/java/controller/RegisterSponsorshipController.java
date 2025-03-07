package controller;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.UnexpectedException;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import model.ActivitiesModel;
import model.GBMembersModel;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterSponsorshipView;

public class RegisterSponsorshipController {
		
	protected SponsorOrganizationsModel soModel;
	protected SponsorshipAgreementsModel saModel;
	protected SponsorContactsModel scModel;
	protected GBMembersModel gbmModel;
	protected ActivitiesModel activitiesModel;
	
    protected RegisterSponsorshipView view; 
    
	private final static String DEFAULT_VALUE_COMBOBOX = "--------------------";

    private String lastSelectedActivity;
    private String lastSelectedSponsor;

    // ================================================================================

    public RegisterSponsorshipController(SponsorOrganizationsModel som, SponsorshipAgreementsModel sam, SponsorContactsModel scm, GBMembersModel gbmm, ActivitiesModel am, RegisterSponsorshipView v) { 
        this.soModel = som;
        this.saModel = sam;
        this.scModel = scm;
        this.gbmModel = gbmm;
        this.activitiesModel = am;
        
        this.view = v;
        this.initView();
        this.initController();
    }
    
    // ================================================================================

    public void initController()
    {
    	this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
			}
		});
    	
    	this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
    		@Override
			public void mouseReleased(MouseEvent e) {
    			SwingUtil.exceptionWrapper(() -> restartView());
    		}
		});
    	
    	this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
			}
		});
    	
    	this.view.getActivityTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    	
    	this.view.getSponsorComboBox().addItemListener(e -> {
    	    if (e.getStateChange() == ItemEvent.SELECTED) {
    	    	SwingUtil.exceptionWrapper(() -> updateDetail());
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
    	
    	
    	this.view.getAgreementDateTextField().getDocument().addDocumentListener(new DocumentListener() {
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
    
    public void initView()
    {
    	this.restartView();
    	view.setVisible();
    }
        
	public void restoreDetail()
	{
		this.lastSelectedActivity = SwingUtil.selectAndGetSelectedKey(this.view.getActivityTable(), this.lastSelectedActivity);
		if (!"".equals(this.lastSelectedActivity)) this.updateDetail();
	}
	
	public void updateDetail()
	{		
		// ------------------------------------------------------------
		// If an activity is selected in the table do:
		
		this.lastSelectedActivity = SwingUtil.getSelectedKey(this.view.getActivityTable());
		if("".equals(this.lastSelectedActivity)) {  restartView(); }
		else
		{
			String selectedSponsor = (String) view.getSponsorComboBox().getSelectedItem();
			if (this.lastSelectedSponsor != selectedSponsor)
			{
				this.lastSelectedSponsor = selectedSponsor;
				String id = SwingUtil.getKeyFromText(lastSelectedSponsor);
				this.getContacts(id); // Populate ComboBox with contacts
			}
			
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
		
		// Validate agreement date
		String agreementDate = this.view.getAgreementDateTextField().getText();
		if(!SyntacticValidations.isDate(agreementDate))
		{
			this.view.getAgreementDateTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getAgreementDateTextField().setForeground(Color.BLACK); }
		
		// Activate/Deactivate the submit button
		this.view.getButtonLowRight().setEnabled(valid);
		
	}
	
	public void restartView()
    {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getActivities();
		this.getSponsors();
		this.getGBMembers();
		
		this.view.getContactComboBox().removeAllItems();
		this.view.getContactComboBox().addItem(DEFAULT_VALUE_COMBOBOX);
		this.view.getContactComboBox().setSelectedItem(DEFAULT_VALUE_COMBOBOX);
    	
    	this.view.getAgreementDateTextField().setText("");
    	this.view.getAmountTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
    
    
    // ================================================================================
    
    private void getActivities()
    {
    	List<ActivitiesDTO> activities = activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"id", "name", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
    private void getSponsors()
    {
    	List<Object[]> sponshors = soModel.getAllSponsorsArray();
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponshors);
		view.getSponsorComboBox().setModel(lmodel);
		view.getSponsorComboBox().setEnabled(true);
    }
    
    private void getContacts(String sponshor)
    {
    	List<Object[]> contacts = scModel.getContactsBySponshorArray(sponshor);
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(contacts);
		view.getContactComboBox().setModel(lmodel);
		view.getContactComboBox().setEnabled(true);
    }
    
    private void getGBMembers()
    {
    	List<Object[]> GBMembers = gbmModel.getAllGBMembersArray();
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(GBMembers);
		view.getGbMemberComboBox().setModel(lmodel);
		view.getGbMemberComboBox().setEnabled(true);
    }
    
    private void setInputsEnabled(boolean enabled)
    {
    	this.view.getSponsorComboBox().setEnabled(enabled);
		this.view.getGbMemberComboBox().setEnabled(enabled);
		this.view.getContactComboBox().setEnabled(enabled);
    	this.view.getAgreementDateTextField().setEnabled(enabled);
    	this.view.getAmountTextField().setEnabled(enabled);
    }
    
    // ================================================================================

    private void showSubmitDialog()
    {
        int row = this.view.getActivityTable().getSelectedRow(); 
        
        String activity = "";
        String idActivity = "";
        
        if (row >= 0)
        {
        	idActivity = (String) this.view.getActivityTable().getModel().getValueAt(row, 0);
        	activity = (String) this.view.getActivityTable().getModel().getValueAt(row, 1);
        }

        String sponsor = (String) view.getSponsorComboBox().getSelectedItem();
        
        String contact = (String) view.getContactComboBox().getSelectedItem();
        String idContact = SwingUtil.getKeyFromText(contact);
        
        String GBmember = (String) view.getGbMemberComboBox().getSelectedItem();
        String idGBmember = SwingUtil.getKeyFromText(GBmember);

        String amount = this.view.getAmountTextField().getText();
        String agreementDate = this.view.getAgreementDateTextField().getText();

        String message = "<html><body>"
                + "<p>You are about to add a sponsorship agreement for the activity: <b>" + activity + "</b>.</p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Sponsor:</b></td><td style='padding: 2px 5px;'>" + sponsor + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Contact:</b></td><td style='padding: 2px 5px;'>" + contact + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>GB Member:</b></td><td style='padding: 2px 5px;'>" + GBmember + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + amount + " euros</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Agreement Date:</b></td><td style='padding: 2px 5px;'>" + agreementDate + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this sponsorship agreement?</i></p>"
                + "</body></html>";

        int response = JOptionPane.showConfirmDialog(
            this.view.getFrame(),  message,
            "Confirm Sponsorship Agreement Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        
        if (response != JOptionPane.YES_OPTION) return;
        
        int numOldSponshorshipAgreements = this.saModel.getNumberOldSponsorshipAgreements(idContact, idActivity);
    	
        if(numOldSponshorshipAgreements == 0)
        {
        	try {
				this.saModel.insertNewSponsorshipAgreement(idContact, idGBmember, idActivity, amount, agreementDate);
			} catch (UnexpectedException e) {
				e.printStackTrace();
			}
	        JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Sponshorship agreement añadido correctamente.",
	    			"Operación realizada correctamente",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restartView();
        }
    	else
    	{
    		message = "This action will modify " + numOldSponshorshipAgreements + " sponshorship agreements for that activity and sponshor.";
    		response = JOptionPane.showConfirmDialog(
    	            this.view.getFrame(), message,
    	            "Confirm modification of old Sponsorship Agreements",
    	            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
	        );
	        
	        if (response == JOptionPane.YES_OPTION)
	        {
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
