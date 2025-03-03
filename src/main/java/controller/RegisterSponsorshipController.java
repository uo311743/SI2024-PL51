package controller;

import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
import model.RegisterSponsorshipModel;
import util.SwingUtil;
import view.RegisterSponsorshipView;

public class RegisterSponsorshipController {
	
	private final static String DEFAULT_VALUE_COMBOBOX = "--------------------";
	
	protected RegisterSponsorshipModel model;
    protected RegisterSponsorshipView view; 
    
    private String lastSelectedActivity;
    private String lastSelectedSponsor;

    // ================================================================================

    public RegisterSponsorshipController(RegisterSponsorshipModel m, RegisterSponsorshipView v)
    { 
        this.model = m;
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
			public void insertUpdate(DocumentEvent e) { SwingUtil.exceptionWrapper(() -> updateDetail()); }

			@Override
			public void removeUpdate(DocumentEvent e) { SwingUtil.exceptionWrapper(() -> updateDetail()); }

			@Override
			public void changedUpdate(DocumentEvent e) { }
    	});
    	
    	
    	this.view.getAgreementDateTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { SwingUtil.exceptionWrapper(() -> updateDetail()); }

			@Override
			public void removeUpdate(DocumentEvent e) { SwingUtil.exceptionWrapper(() -> updateDetail()); }

			@Override
			public void changedUpdate(DocumentEvent e) { }
    	});
    	
    	this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO
			}
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
			this.lastSelectedSponsor = (String) view.getSponsorComboBox().getSelectedItem();
			String id = SwingUtil.getKeyFromText(this.lastSelectedSponsor);
			this.getContacts(id); // Populate ComboBox with contacts
			
			this.setInputsEnabled(true);
		}
		
		// ------------------------------------------------------------
		// Check JTextField inputs:
		String amount = this.view.getAmountTextField().getText();
		String agreementDate = this.view.getAgreementDateTextField().getText();
		

		
		if("".equals(amount) || "".equals(amount))
		{
			this.view.getButtonLowRight().setEnabled(true);
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
    	
    	this.setInputsEnabled(false);
    }
    
    
    // ================================================================================
    
    private void getActivities()
    {
    	List<ActivitiesDTO> activities = model.getActivitiesbyStatus("registered", "planned", "done");
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"name", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
    private void getSponsors()
    {
    	List<Object[]> sponshors = model.getAllSponsorsArray();
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponshors);
		view.getSponsorComboBox().setModel(lmodel);
		view.getSponsorComboBox().setEnabled(true);
    }
    
    private void getContacts(String sponshor)
    {
    	List<Object[]> contacts = model.getContactsBySponshorArray(sponshor);
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(contacts);
		view.getContactComboBox().setModel(lmodel);
		view.getContactComboBox().setEnabled(true);
    }
    
    private void getGBMembers()
    {
    	List<Object[]> GBMembers = model.getAllGBMembersArray();
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

}
