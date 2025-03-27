package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import DTOs.LevelsDTO;
import DTOs.SponsorContactsDTO;
import model.ActivitiesModel;
import model.GBMembersModel;
import model.LevelsModel;
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
	protected LevelsModel levelsModel;
	
    protected RegisterSponsorshipView view; 
    
	private final static String DEFAULT_VALUE_COMBOBOX = "--------------------";

    private String lastSelectedActivity;
    private String lastSelectedSponsor;
    private String lastSelectedContact;
    

    // ================================================================================

    public RegisterSponsorshipController(SponsorOrganizationsModel som, SponsorshipAgreementsModel sam, SponsorContactsModel scm, GBMembersModel gbmm, ActivitiesModel am, LevelsModel lm, RegisterSponsorshipView v) { 
        this.soModel = som;
        this.saModel = sam;
        this.scModel = scm;
        this.gbmModel = gbmm;
        this.activitiesModel = am;
        this.levelsModel = lm;
        
        this.view = v;
        this.initView();
        this.initController();
    }
    
    // ================================================================================

    public void initController()
    {
    	this.view.getButtonLowLeft().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
			}
		});
    	
    	this.view.getButtonLowMiddle().addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent  e) {
    			SwingUtil.exceptionWrapper(() -> restartView());
    		}
		});
    	
    	this.view.getButtonLowRight().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
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
    	
    	this.view.getContactComboBox().addItemListener(e -> {
    	    if (e.getStateChange() == ItemEvent.SELECTED) {
    	    	SwingUtil.exceptionWrapper(() -> updateDetail());
    	    }
    	});
    	
    	this.view.getLevelsComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> updateRange());
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
    	this.getActivities();
    	this.getSponsors();
		this.getGBMembers();
		
		this.view.getButtonLowRight().setEnabled(false);
		this.setInputsEnabled(false);
		this.view.getContactEmailTextField().setEnabled(false);
		
    	this.restoreDetail();
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
		if(!"".equals(this.lastSelectedActivity))
		{
			String selectedSponsor = (String) view.getSponsorComboBox().getSelectedItem();
			if (this.lastSelectedSponsor != selectedSponsor)
			{
				this.lastSelectedSponsor = selectedSponsor;
				String sponsorId = SwingUtil.getKeyFromText(this.lastSelectedSponsor);
				this.getContacts(sponsorId); // Populate ComboBox with contacts
			}
			
			String selectedContact = (String) view.getContactComboBox().getSelectedItem();
			if (this.lastSelectedContact != selectedContact)
			{
				this.lastSelectedContact = selectedContact;
				String contactId = SwingUtil.getKeyFromText(this.lastSelectedContact);
				this.getContactEmail(contactId);
			}
			this.getLevels();
			this.updateRange();
			
			this.setInputsEnabled(true);
		}
		
		// ------------------------------------------------------------
		// Check JTextField inputs:
		boolean valid = true;
		
		// Validate amount
		String amount = this.view.getAmountTextField().getText();	
		
		String activityId = (String) this.view.getActivityTable().getModel().getValueAt(this.view.getActivityTable().getSelectedRow(), 0);
		String levelName = String.valueOf(this.view.getLevelsComboBox().getSelectedItem());
		LevelsDTO levelSelected = levelsModel.getLevelsByActivityIdAndLevelName(activityId, levelName);
		
		String amountMax = saModel.getFeeMaxByLevelFee(levelSelected.getFee());
		String amountMin = levelSelected.getFee();
		
		if (amountMax == "isTheMax") {
			if(!SyntacticValidations.isDecimal(amount) || Double.valueOf(amount) < Double.valueOf(amountMin))
			{
				this.view.getAmountTextField().setForeground(Color.RED);
				valid = false;
			} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
		}
		else {
			if(!SyntacticValidations.isDecimal(amount) || Double.valueOf(amount) >= Double.valueOf(amountMax) || Double.valueOf(amount) < Double.valueOf(amountMin))
			{
				this.view.getAmountTextField().setForeground(Color.RED);
				valid = false;
			} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
		}
		
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
	
	public void updateRange() {
		String activityId = (String) this.view.getActivityTable().getModel().getValueAt(this.view.getActivityTable().getSelectedRow(), 0);
		String levelName = String.valueOf(this.view.getLevelsComboBox().getSelectedItem());
		LevelsDTO levelSelected = levelsModel.getLevelsByActivityIdAndLevelName(activityId, levelName);
		String amountMax = saModel.getFeeMaxByLevelFee(levelSelected.getFee());
		
		if (amountMax == "isTheMax") {
			this.view.getAmountLabel().setText("Amount (euro): (" + levelSelected.getFee() + "-" + "Limitless)");
		}
		else {
			this.view.getAmountLabel().setText("Amount (euro): (" + levelSelected.getFee() + "-" + String.valueOf(Double.valueOf(amountMax) - 1) + ")");
		}
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
    	
    	this.view.getContactEmailTextField().setText("");
    	this.view.getContactEmailTextField().setEditable(false);
    	
    	this.setInputsEnabled(false);
    }
    
    
    // ================================================================================
    
    private void getActivities()
    {
    	List<ActivitiesDTO> activities = activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"id", "name", "edition", "status", "dateStart", "dateEnd"});
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
    
    private void getContacts(String sponshorId)
    {
    	List<Object[]> contacts = scModel.getContactsBySponshorArray(sponshorId);
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(contacts);
		view.getContactComboBox().setModel(lmodel);
		view.getContactComboBox().setEnabled(true);
    }
    
    private void getContactEmail(String contactId)
    {
    	SponsorContactsDTO contact = this.scModel.getContactById(contactId);
    	this.view.getContactEmailTextField().setText(contact.getEmail());;
    }
    
    private void getGBMembers()
    {
    	List<Object[]> GBMembers = gbmModel.getAllGBMembersArray();
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(GBMembers);
		view.getGbMemberComboBox().setModel(lmodel);
		view.getGbMemberComboBox().setEnabled(true);
    }
    
    private void getLevels() {
		String activityId = (String) this.view.getActivityTable().getModel().getValueAt(this.view.getActivityTable().getSelectedRow(), 0);    	
    	List<Object[]> levelsList = levelsModel.getLevelsListArray(activityId);
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(levelsList);
        view.getLevelsComboBox().setModel(lmodel);
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
        	this.saModel.insertNewSponsorshipAgreement(idContact, idGBmember, idActivity, amount, agreementDate);
			
	        JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Sponshorship agreement added successfully.",
	    			"Operation completed successfully",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restartView();
        }
    	else
    	{
    		message = "This action will modify " + numOldSponshorshipAgreements + " sponsorship agreements for that activity and sponsor.";
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
		    			"Sponsorship agreement added successfully. The old ones have been marked as 'modified'.",
		    			"Operation completed successfully",
		    			JOptionPane.INFORMATION_MESSAGE
		    	);
		        this.restartView();
	        }
    	}
    }


}
