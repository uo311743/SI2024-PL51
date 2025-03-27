package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DTOs.SponsorContactsDTO;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import util.ModelManager;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterSponsorView;
public class RegisterSponsorController {
		

	private SponsorContactsModel contactModel;
	private SponsorOrganizationsModel sponsorModel;

	
	private RegisterSponsorView view; 
    private List<SponsorContactsDTO> contacts;
        

    // ================================================================================

    public RegisterSponsorController(RegisterSponsorView v)
    { 
        this.contactModel = ModelManager.getInstance().getSponsorContactsModel();
        this.sponsorModel = ModelManager.getInstance().getSponsorOrganizationsModel();
        
        contacts = new LinkedList<SponsorContactsDTO>();
        
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
				SwingUtil.exceptionWrapper(() -> { restoreDetail(); });
			}
		});
    	
    	
    	this.view.getButtonLowRight().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
				SwingUtil.exceptionWrapper(() -> registerSponsor());
			}
		});
    	
    	this.view.getButtonAddContact().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
				SwingUtil.exceptionWrapper(() -> addContact());
			}
		});
    	
    	
    	this.addListenerUpdateOnTextFieldChange(this.view.getSponsorNameField());
    	this.addListenerUpdateOnTextFieldChange(this.view.getSponsorAddressField());
    	this.addListenerUpdateOnTextFieldChange(this.view.getSponsorVatField());
    	
    	this.addListenerUpdateOnTextFieldChange(this.view.getContactNameField());
    	this.addListenerUpdateOnTextFieldChange(this.view.getContactEmailField());
    	this.addListenerUpdateOnTextFieldChange(this.view.getContactPhoneField());
    	
    }
    
    private void addListenerUpdateOnTextFieldChange(JTextField target)
    {
    	target.getDocument().addDocumentListener(new DocumentListener() {
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
    	this.view.getButtonLowRight().setEnabled(false);
    	this.view.getButtonAddContact().setEnabled(false);
    	
    	this.restoreDetail();
    	view.setVisible();
    }
        
	public void restoreDetail()
	{
		this.view.getSponsorNameField().setText("");
		this.view.getSponsorAddressField().setText("");
		this.view.getSponsorVatField().setText("");
		
		contacts.clear();
		restoreDetailContacts();
		
		updateDetail();
    	return;
	}
	
	public void updateDetail()
	{		
		this.loadContacts();
    	this.view.getButtonAddContact().setEnabled(this.checkValidContact());

    	this.view.getButtonLowRight().setEnabled(this.isValidSponsor() && this.contacts.size() > 0);
	}
    
	public void restoreDetailContacts()
	{
		this.view.getContactNameField().setText("");
		this.view.getContactEmailField().setText("");
		this.view.getContactPhoneField().setText("");
	}
    
    // ================================================================================
    
	private boolean isValidSponsor()
	{
		return !"".equals(this.view.getSponsorNameField().getText().strip())
				&& !"".equals(this.view.getSponsorAddressField().getText().strip())
				&& !"".equals(this.view.getSponsorVatField().getText().strip());
	}
	
	private boolean checkValidContact()
	{
		String contactName = this.view.getContactNameField().getText();
		String contactEmail = this.view.getContactEmailField().getText();
		String contactPhone = this.view.getContactPhoneField().getText();
		
		boolean isValidEmail = SyntacticValidations.matchesPattern(contactEmail, SyntacticValidations.PATTERN_EMAIL);
		
		if(isValidEmail) this.view.getContactEmailField().setForeground(Color.BLACK);
		else this.view.getContactEmailField().setForeground(Color.RED);
		
		boolean isValidOrEmptyPhone =
				SyntacticValidations.matchesPattern(contactPhone, SyntacticValidations.PATTERN_PHONE)
				|| "".equals(contactPhone.strip());
		
		if(isValidOrEmptyPhone) this.view.getContactPhoneField().setForeground(Color.BLACK);
		else this.view.getContactPhoneField().setForeground(Color.RED);
		
		
		return !"".equals(contactName) && isValidEmail && isValidOrEmptyPhone;
	}
	
    private void addContact()
    {
    	SponsorContactsDTO newContact = new SponsorContactsDTO();
    	newContact.setName(this.view.getContactNameField().getText());
    	newContact.setEmail(this.view.getContactEmailField().getText());
    	
		String contactPhone = this.view.getContactPhoneField().getText();
    	boolean isValidPhone = SyntacticValidations.matchesPattern(contactPhone, SyntacticValidations.PATTERN_PHONE);
    	
    	if(isValidPhone) newContact.setPhone(contactPhone);
    	else newContact.setPhone("");
    	
    	this.contacts.add(newContact);
    	this.restoreDetailContacts();
    	this.updateDetail();
    }
    
    private void loadContacts() {
    	String[] columnNames = {"Name", "Email", "Phone"};
	    DefaultTableModel contactsTableModel = new DefaultTableModel(columnNames, 0);
	    
	    for(SponsorContactsDTO contact : contacts)
	    	contactsTableModel.addRow(new Object[]{contact.getName(), contact.getEmail(), contact.getPhone()});
	    
    	this.view.getContactsTable().setModel(contactsTableModel);
    }
    
    // ================================================================================

    private void registerSponsor()
    {
    	
    	String sponsorName = this.view.getSponsorNameField().getText();
    	String sponsorAddress = this.view.getSponsorAddressField().getText();
    	String sponsorVat = this.view.getSponsorVatField().getText();
    	
    	if(this.sponsorModel.existsSponsorByName(sponsorName))
    	{
            String message = "A sponsor organization with the name '" + sponsorName + "' already exists.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
    	}
    	
    	if(this.sponsorModel.existsSponsorByVat(sponsorName))
    	{
            String message = "A sponsor organization with the VAT number '" + sponsorVat + "' already exists.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
    	}
    	
    	String sponsorId = this.sponsorModel.insertSponsorOrganizationAndGetID(
    			sponsorName, sponsorAddress, sponsorVat, sponsorVat
    	);
    	
	    for(SponsorContactsDTO contact : contacts)
	    	this.contactModel.insertContact(sponsorId, contact.getName(), contact.getEmail(), contact.getPhone());
	    
	    JOptionPane.showMessageDialog(
    			this.view.getFrame(),
    			"Sponsor organization " + sponsorName + " with " + contacts.size() + " contacts added successfully.",
    			"Operation completed successfully",
    			JOptionPane.INFORMATION_MESSAGE
    	);
    	    	
    	this.restoreDetail();
    	return;
    }

}
