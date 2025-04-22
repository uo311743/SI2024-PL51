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
import DTOs.SponsorContactsDTO;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import util.ModelManager;
import util.SwingUtil;
import util.SyntacticValidations;
import view.ModifySponsorContactView;

public class ModifySponsorContactController {
	
	protected SponsorContactsModel scModel;
	protected SponsorOrganizationsModel soModel;
		
	protected ModifySponsorContactView view;
	
    protected String lastSelectedContact;
	
    public ModifySponsorContactController(ModifySponsorContactView v) {
		this.scModel = ModelManager.getInstance().getSponsorContactsModel();
		this.soModel = ModelManager.getInstance().getSponsorOrganizationsModel();
		
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
    	
    	// Sponsor ComboBox
    	this.view.getSponsorComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    	
    	// Contacts Table
    	this.view.getContactsTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    	
    	// Name TextField
    	this.view.getNameTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> checkFields());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> checkFields());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Email TextField
    	this.view.getEmailTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> checkFields());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> checkFields());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Phone TextField
    	this.view.getPhoneTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> checkFields());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> checkFields());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    }
    
    public void initView() {
    	this.getSponsors();
    	this.restoreDetail();
    	view.setVisible();
    }
    
    public void getSponsors() {
    	List<Object[]> sponsors = soModel.getSponsorListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponsors);
        view.getSponsorComboBox().setModel(lmodel);
    }
    
    public void getContacts() {
    	List<SponsorContactsDTO> contacts = scModel.getContactsBySponsorId(soModel.getSponsorByName(String.valueOf(view.getSponsorComboBox().getSelectedItem())).getId());
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"idSponsorOrganization", "name", "email", "phone"}, 0);
        for (SponsorContactsDTO contact : contacts) {
        	tableModel.addRow(new Object[] {
        			contact.getIdSponsorOrganization(),
        			contact.getName(),
        			contact.getEmail(),
        			contact.getPhone()
        	});
        }
		this.view.getContactsTable().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getContactsTable());
    }
    
    public void updateDetail() {
    	this.lastSelectedContact = SwingUtil.getSelectedKey(this.view.getContactsTable());
		if("".equals(this.lastSelectedContact)) {  
			restoreDetail();
		}
		else {
			String idSponsorOrganization = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 0);
			String nameContact = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 1);
			String emailContact = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 2);
			String phoneContact = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 3);

			this.view.getIdSOLabel().setText("Sponsor Organization ID: " + idSponsorOrganization);
			this.view.getNameTextField().setText(nameContact);
			this.view.getEmailTextField().setText(emailContact);
			this.view.getPhoneTextField().setText(phoneContact);
			
			this.setInputsEnabled(true);
		}
    }
    
    public void checkFields() {
		boolean valid = true;
		
		// Validate Name
		String name = this.view.getNameTextField().getText();
    	if(!SyntacticValidations.isNotEmpty(name)) {
    		this.view.getNameTextField().setForeground(Color.RED);
    		valid = false;
    	} 
    	else { 
    		this.view.getNameTextField().setForeground(Color.BLACK); 
    	}
    	
    	// Validate Email
    	String email = this.view.getEmailTextField().getText();
		if(!SyntacticValidations.matchesPattern(email, SyntacticValidations.PATTERN_EMAIL)) {
			this.view.getEmailTextField().setForeground(Color.RED); 
			valid = false;
		} 
		else { 
			this.view.getEmailTextField().setForeground(Color.BLACK); 
		}
		
		// Validate Phone
		String phone = this.view.getPhoneTextField().getText();
		if(!SyntacticValidations.matchesPattern(phone, SyntacticValidations.PATTERN_PHONE)) {
			this.view.getPhoneTextField().setForeground(Color.RED); 
			valid = false;
		} 
		else { 
			this.view.getPhoneTextField().setForeground(Color.BLACK); 
		}
		
		this.view.getButtonLowRight().setEnabled(valid);
    }
    
    // RESTORE METHODS
    
    public void restoreDetail() {
		this.view.getContactsTable().setModel(new DefaultTableModel());
		
		this.getContacts();
		
    	this.view.getIdSOLabel().setText("Sponsor Organization ID: ");
		this.view.getNameTextField().setEnabled(false);
		this.view.getEmailTextField().setEnabled(false);
		this.view.getPhoneTextField().setEnabled(false);
		this.view.getNameTextField().setText("");;
		this.view.getEmailTextField().setText("");;
		this.view.getPhoneTextField().setText("");
				
		this.view.getButtonLowRight().setEnabled(false);
	}
	
	public void setInputsEnabled(boolean valid) {
		this.view.getNameTextField().setEnabled(valid);
		this.view.getEmailTextField().setEnabled(valid);
		this.view.getPhoneTextField().setEnabled(valid);
	}
	
	// OTHER METHODS
	
	private void showSubmitDialog() {
		String idSponsorOrganization = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 0);
		String nameContact = this.view.getNameTextField().getText();
		String emailContact = this.view.getEmailTextField().getText();
		String phoneContact = this.view.getPhoneTextField().getText();
        
        String message = "<html><body>"
                + "<p>Modify a Sponsor Contact: </p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Sponsor Organization ID:</b></td><td style='padding: 2px 5px;'>" + idSponsorOrganization + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + nameContact + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Email:</b></td><td style='padding: 2px 5px;'>" + emailContact + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Phone:</b></td><td style='padding: 2px 5px;'>" + phoneContact + "</td></tr>"
                + "</table>"
                + "<p><i>Proceed with these changes for the selected sponsor contact?</i></p>"
                + "</body></html>";

        int response = JOptionPane.showConfirmDialog(
            this.view.getFrame(),  message,
            "Confirm Sponsor Contact Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        
        if (response != JOptionPane.YES_OPTION) {
        	return;
        }
        
        String preName = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 1);
        String preEmail = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 2);
        String prePhone = (String) this.view.getContactsTable().getModel().getValueAt(this.view.getContactsTable().getSelectedRow(), 3);
        
        SyntacticValidations.matchesPattern(preEmail, SyntacticValidations.PATTERN_EMAIL);
        SyntacticValidations.matchesPattern(prePhone, SyntacticValidations.PATTERN_PHONE);
        
        SponsorContactsDTO contactData = this.scModel.getContactByFilters(idSponsorOrganization, preName, preEmail, prePhone);
        
        SyntacticValidations.matchesPattern(emailContact, SyntacticValidations.PATTERN_EMAIL);
        SyntacticValidations.matchesPattern(phoneContact, SyntacticValidations.PATTERN_PHONE);
        	
        this.scModel.updateContact(contactData.getId(), nameContact, emailContact, phoneContact);
		JOptionPane.showMessageDialog(
			this.view.getFrame(), "Sponsor Contact updated correctly",
			"This operation has been succesful",
			JOptionPane.INFORMATION_MESSAGE
		);

		this.restoreDetail();
    }
}
