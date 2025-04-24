package controller;

import model.ActivitiesModel;
import model.GBMembersModel;
import model.LevelsModel;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import util.ModelManager;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterLongTermAgreementView;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
import DTOs.LevelsDTO;
import DTOs.SponsorContactsDTO;

public class RegisterLongTermAgreementController {
	protected SponsorOrganizationsModel soModel;
	protected SponsorshipAgreementsModel saModel;
	protected SponsorContactsModel scModel;
	protected GBMembersModel gbmModel;
	protected ActivitiesModel activitiesModel;
	protected LevelsModel levelsModel;

	private RegisterLongTermAgreementView view;

	private String lastSelectedActivity;
    private String lastSelectedSponsor;
    private String lastSelectedContact;
	
	private List<String> activitiesId;
	private List<String> activitiesSelected;
	private List<String> levels;
	private List<String> amounts;
	
	private boolean datesFieldsValidation;
	private boolean activityFieldsValidation;

	public RegisterLongTermAgreementController(RegisterLongTermAgreementView view) {
		this.soModel = ModelManager.getInstance().getSponsorOrganizationsModel();
        this.saModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
        this.scModel = ModelManager.getInstance().getSponsorContactsModel();
        this.gbmModel = ModelManager.getInstance().getGBMembersModel();
        this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
        this.levelsModel = ModelManager.getInstance().getLevelsModel();

		this.view = view;
		
		this.activitiesId = new ArrayList<String>();
		this.activitiesSelected = new ArrayList<String>();
		this.levels = new ArrayList<String>();
		this.amounts = new ArrayList<String>();
		this.lastSelectedActivity = "";
		
		this.datesFieldsValidation = false;
		this.activityFieldsValidation = false;

		this.initController();
		this.initView();
	}

	public void initController() {
		this.view.getShowActivitiesButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					showActivitiesSelection();
				});
			}
		});
		
		this.view.getAddActivityButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					addActivitySelection();
				});
			}
		});
		
		this.view.getRemoveActivityButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					removeActivitySelection();
				});
			}
		});
		
		this.view.getActivitiesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> activitySelection());
			}
		});
		
		this.view.getActivitySelectionTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> setRemoveActivityButtonVisibility(true));
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
					resetButtonSelection();
				});
			}
		});

		this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
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
			public void actionPerformed(ActionEvent  e) {
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
			public void changedUpdate(DocumentEvent e) {
			}
		});

		this.view.getAgreementStartDateTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDates());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		this.view.getAgreementEndDateTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDates());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}

	public void initView() {
    	this.restartView();
		
		this.setInputsEnabled(false);
		this.setActivityInputsEnabled(false);
		
    	view.setVisible();
	}
	
	public void clearFields() {
		// Reset amount field with placeholder effect
		this.view.getAgreementStartDateTextField().setText("");
		this.view.getAgreementEndDateTextField().setText("");
		this.view.getAmountTextField().setText("");
		this.view.getContactEmailTextField().setText("");
	}

	public void restartView() {
		this.view.getActivitySelectionTable().removeAll();
		this.getActivities();
		this.getSponsors();
		this.getGBMembers();
		
		this.view.getActivitiesTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getActivitiesTable().getModel();
		model.setRowCount(0); // Clears the table
		this.view.getActivitiesTable().clearSelection();
		model = (DefaultTableModel) this.view.getActivitySelectionTable().getModel();
		model.setRowCount(0); // Clears the table
    	
		this.clearFields();
		
		this.view.getLevelsComboBox().removeAllItems();
		
    	this.view.getContactEmailTextField().setEditable(false);
		this.view.getContactEmailTextField().setFocusable(false);

		this.setInputsEnabled(false);
		this.setActivityInputsEnabled(false);
	}
	
	private void resetButtonSelection() {
		this.lastSelectedActivity = "";
		this.datesFieldsValidation = false;
		this.activityFieldsValidation = false;
		this.clearActivitiesSelection();
		this.setRemoveActivityButtonVisibility(false);
		this.restartView();
	}
	
	private void clearActivitiesSelection() {
		this.activitiesSelected.clear();
		this.levels.clear();
		this.amounts.clear();
		this.view.getActivitySelectionTable().removeAll();
		this.view.getActivitySelectionTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getActivitySelectionTable().getModel();
		model.setRowCount(0); // Clears the table
		this.setRemoveActivityButtonVisibility(false);
	}
	
	private void setRemoveActivityButtonVisibility(boolean visibility) {
		this.view.setRemoveActivityButtonVisibility(visibility);
	}
	
	private void addActivitySelection() {
		if (!"".equals(lastSelectedActivity) && !this.activitiesSelected.contains(lastSelectedActivity) && this.activityFieldsValidation) { 
			this.activitiesSelected.add(lastSelectedActivity);
			this.amounts.add(this.view.getAmountTextField().getText().toString());
			this.levels.add(this.view.getLevelsComboBox().getSelectedItem().toString());
			this.getActivitySelection();
		}
	}
	
	private void removeActivitySelection() {
		int row = this.view.getActivitySelectionTable().getSelectedRow();
		if (row >= 0) {
			this.activitiesSelected.remove(this.activitiesSelected.get(row));
			this.amounts.remove(this.amounts.get(row));
			this.levels.remove(this.levels.get(row));
			this.view.getActivitySelectionTable().removeAll();
			this.view.getActivitySelectionTable().clearSelection();
			DefaultTableModel model = (DefaultTableModel) this.view.getActivitySelectionTable().getModel();
			model.setRowCount(0); // Clears the table
			this.getActivitySelection();
			this.setRemoveActivityButtonVisibility(false);
		}
	}
	
	private void activitySelection() {
		this.lastSelectedActivity = this.activitiesId.get(this.view.getActivitiesTable().getSelectedRow());
		this.setActivityInputsEnabled(true);
		this.setInputsEnabled(true);
		this.setRemoveActivityButtonVisibility(false);
		this.updateLevels();
		this.updateDetail();
	}
	
	private void showActivitiesSelection() {
		if (this.datesFieldsValidation) {
			this.getActivities();
			if (this.activityFieldsValidation) { this.setInputsEnabled(true); }
		}
	}
	
	private void updateDates() {
		this.datesFieldsValidation = true;
		
		// Validate agreement start date
		String agreementStartDate = this.view.getAgreementStartDateTextField().getText();
		if(!SyntacticValidations.isDate(agreementStartDate) || !SyntacticValidations.isNotEmpty(agreementStartDate))
		{
			this.view.getAgreementStartDateTextField().setForeground(Color.RED);
			this.datesFieldsValidation = false;
		} else { this.view.getAgreementStartDateTextField().setForeground(Color.BLACK); }

		// Validate agreement end date
		String agreementEndDate = this.view.getAgreementEndDateTextField().getText();
		if(!SyntacticValidations.isDate(agreementEndDate) || !SyntacticValidations.isNotEmpty(agreementEndDate))
		{
			this.view.getAgreementEndDateTextField().setForeground(Color.RED);
			this.datesFieldsValidation = false;
		} else { this.view.getAgreementEndDateTextField().setForeground(Color.BLACK); }
	}

	private void getActivities()
    {
		if (!this.datesFieldsValidation) { return; }
    	List<ActivitiesDTO> activities = this.activitiesModel.getActivitiesInDateRange(this.view.getAgreementStartDateTextField().getText().toString(), this.view.getAgreementEndDateTextField().getText().toString());
    	
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"dateStart", "dateEnd", "name", "status"});
		activitiesId.clear();
		for (ActivitiesDTO activity : activities) {
			activitiesId.add(activity.getId());
	    }
		
		// Create larger font
		Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
		
		// Set the model in the JTable
	    this.view.getActivitiesTable().setModel(tmodel);
	    JTableHeader header = this.view.getActivitiesTable().getTableHeader();
	    header.setFont(largerFont);
	    this.view.adjustColumns();
    }
	
	private void getActivitySelection() {
	    List<ActivitiesDTO> activities = new ArrayList<>();
	    
	    for (String idActivity : activitiesSelected) {
	        activities.add(this.activitiesModel.getActivityById(idActivity));
	    }
	    
	    // Create TableModel with base columns from ActivitiesDTO
	    TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"dateStart", "dateEnd", "name", "status"});
	    
	    // Create a new DefaultTableModel to include "level" and "amount"
	    DefaultTableModel customModel = new DefaultTableModel();
	    
	    // Add all columns: original + new ones
	    for (int i = 0; i < tmodel.getColumnCount(); i++) {
	        customModel.addColumn(tmodel.getColumnName(i));
	    }
	    customModel.addColumn("level");
	    customModel.addColumn("amount");
	    
	    // Populate rows with data from original TableModel and new lists
	    for (int i = 0; i < tmodel.getRowCount(); i++) {
	        Object[] rowData = new Object[tmodel.getColumnCount() + 2]; // +2 for level and amount
	        // Copy original columns
	        for (int j = 0; j < tmodel.getColumnCount(); j++) {
	            rowData[j] = tmodel.getValueAt(i, j);
	        }
	        // Add level and amount
	        rowData[tmodel.getColumnCount()] = levels.get(i);
	        rowData[tmodel.getColumnCount() + 1] = amounts.get(i);
	        customModel.addRow(rowData);
	    }
	    
	    // Create larger font
	    Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
	    
	    // Set the model in the JTable
	    this.view.getActivitySelectionTable().setModel(customModel);
	    JTableHeader header = this.view.getActivitySelectionTable().getTableHeader();
	    header.setFont(largerFont);
	    this.view.adjustColumns();
	}

	private void setInputsEnabled(boolean enabled)
    {
    	this.view.getSponsorComboBox().setEnabled(enabled);
		this.view.getGbMemberComboBox().setEnabled(enabled);
		this.view.getContactComboBox().setEnabled(enabled);
    }
	
	private void setActivityInputsEnabled(boolean enabled)
    {
		this.view.getLevelsComboBox().setEnabled(enabled);
    	this.view.getAmountTextField().setEnabled(enabled);
    }

	public void updateDetail()
	{
		// ------------------------------------------------------------
		// If an activity is selected in the table do:
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

		// ------------------------------------------------------------
		// Check JTextField inputs:
		this.activityFieldsValidation = true;
		
		// Validate amount
		if (!"".equals(lastSelectedActivity))
		{
			String amount = this.view.getAmountTextField().getText();	
			String levelName = String.valueOf(this.view.getLevelsComboBox().getSelectedItem());
			LevelsDTO levelSelected = levelsModel.getLevelsByActivityIdAndLevelName(lastSelectedActivity, levelName);

			String amountMax = saModel.getFeeMaxByLevelFee(levelSelected.getFee());
			String amountMin = levelSelected.getFee();

			if (amountMax == "isTheMax") {
				if(!SyntacticValidations.isDecimal(amount) || Double.valueOf(amount) < Double.valueOf(amountMin))
				{
					this.view.getAmountTextField().setForeground(Color.RED);
					this.activityFieldsValidation = false;
				} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
			}
			else {
				if(!SyntacticValidations.isDecimal(amount) || Double.valueOf(amount) >= Double.valueOf(amountMax) || Double.valueOf(amount) < Double.valueOf(amountMin))
				{
					this.view.getAmountTextField().setForeground(Color.RED);
					this.activityFieldsValidation = false;
				} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
			}	
		}
	}
	
	public void updateLevels() {
		this.getLevels();
		this.updateRange();
	}
	
	public void updateRange() {
		String activityId = this.lastSelectedActivity;
		String levelName = String.valueOf(this.view.getLevelsComboBox().getSelectedItem());
		LevelsDTO levelSelected = levelsModel.getLevelsByActivityIdAndLevelName(activityId, levelName);
		String amountMax = saModel.getFeeMaxByLevelFee(levelSelected.getFee());
		
		if (amountMax == "isTheMax") {
			this.view.getAmountLabel().setText("Amount (€): (" + levelSelected.getFee() + "-" + "Limitless)");
		}
		else {
			this.view.getAmountLabel().setText("Amount (€): (" + levelSelected.getFee() + "-" + String.valueOf(Double.valueOf(amountMax) - 1) + ")");
		}
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
    	List<Object[]> contacts = scModel.getValidContactsBySponsorOrganizationArray(sponshorId);
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(contacts);
		view.getContactComboBox().setModel(lmodel);
		view.getContactComboBox().setEnabled(true);
    }
    
    private void getContactEmail(String contactId)
    {
    	SponsorContactsDTO contact = this.scModel.getContactById(contactId);
    	this.view.getContactEmailTextField().setText(contact.getEmail());
    }
    
    private void getGBMembers()
    {
    	List<Object[]> GBMembers = gbmModel.getAllGBMembersArray();
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(GBMembers);
		view.getGbMemberComboBox().setModel(lmodel);
		view.getGbMemberComboBox().setEnabled(true);
    }
    
    private void getLevels() {
		String activityId = this.lastSelectedActivity;    	
    	List<Object[]> levelsList = levelsModel.getLevelsListArray(activityId);
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(levelsList);
        view.getLevelsComboBox().setModel(lmodel);
    }

	// ================================================================================

	private void showSubmitDialog() {
		if (!datesFieldsValidation || !activityFieldsValidation) {
			// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"Some of the fields have an incorrect format or have not been provided", // Error message
        		"Agreement Registration Error", // Dialog title
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
        
        int row = this.view.getActivitiesTable().getSelectedRow(); 
        
        String activity = "";
        String idActivity = "";
        
        if (row >= 0)
        {
        	idActivity = activitiesId.get(row);
        	activity = (String) this.view.getActivitiesTable().getModel().getValueAt(row, 2);
        }

        String sponsor = (String) view.getSponsorComboBox().getSelectedItem();
        
        String contact = (String) view.getContactComboBox().getSelectedItem();
        String idContact = SwingUtil.getKeyFromText(contact);
        
        String GBmember = (String) view.getGbMemberComboBox().getSelectedItem();
        String idGBmember = SwingUtil.getKeyFromText(GBmember);

        String amount = this.view.getAmountTextField().getText();
        String agreementStartDate = this.view.getAgreementStartDateTextField().getText();
        String agreementEndDate = this.view.getAgreementEndDateTextField().getText();
		
		String message = "";
        boolean validated = true;
        
        if (!SyntacticValidations.isNotEmpty(amount)) {
        	message = "<html><body>"
                    + "<p>Amount must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(agreementStartDate)) {
        	message = "<html><body>"
                    + "<p>Start Date must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(agreementEndDate)) {
        	message = "<html><body>"
                    + "<p>End Date must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (activitiesSelected.size() <= 0) {
        	message = "<html><body>"
                    + "<p>At least one Activity must be provided</p>"
                    + "</body></html>";
        	validated = false;
        }
        
        if (!validated) {
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + message, // Error message
        		"Agreement Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
        }
        
        double totalAmount = amounts.stream()
                .mapToDouble(Double::parseDouble)
                .sum();

		message = "<html><body>"
                + "<p>You are about to add a Long-Term Sponsorship Agreement</b>.</p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Sponsor:</b></td><td style='padding: 2px 5px;'>" + sponsor + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Contact:</b></td><td style='padding: 2px 5px;'>" + contact + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>GB Member:</b></td><td style='padding: 2px 5px;'>" + GBmember + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + totalAmount + " euros</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Start Date:</b></td><td style='padding: 2px 5px;'>" + agreementStartDate + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>End Date:</b></td><td style='padding: 2px 5px;'>" + agreementEndDate + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this sponsorship agreement?</i></p>"
                + "</body></html>";

		Object[] options = { "Yes", "No" };

		int response = JOptionPane.showOptionDialog(null, message, // The message
				"Confirmation of Agreement Registration", // The title
				JOptionPane.DEFAULT_OPTION, // The option type
				JOptionPane.QUESTION_MESSAGE, // The message type
				null, // Icon (null means default question icon)
				options, // Custom buttons
				options[0]);

		if (response == 1)
			return;

		try {
			this.saModel.insertNewLongTermSponsorshipAgreement(idContact, idGBmember, activitiesSelected, String.valueOf(totalAmount), agreementStartDate, agreementEndDate);
		} catch (Exception e) {
			e.printStackTrace();
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + e.getMessage(), // Error message
        		"Agreement Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
		}
		this.clearActivitiesSelection();
        JOptionPane.showMessageDialog(
    			this.view.getFrame(), "Sponshorship agreement added successfully.",
    			"Operation completed successfully",
    			JOptionPane.INFORMATION_MESSAGE
    	);
    }
}