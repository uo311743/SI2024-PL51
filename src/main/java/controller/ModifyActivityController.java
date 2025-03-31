package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import DTOs.ActivitiesDTO;
import DTOs.LevelsDTO;
import model.ActivitiesModel;
import model.ActivityTemplatesModel;
import model.LevelsModel;
import util.ModelManager;
import util.SwingUtil;
import util.SyntacticValidations;
import view.ModifyActivityView;

public class ModifyActivityController {
	
	protected ActivityTemplatesModel atModel;
	protected LevelsModel levelsModel;
	protected ActivitiesModel activitiesModel;
		
	protected ModifyActivityView view;
	
    protected String lastSelectedActivity;
    protected String lastSelectedLevel;
	protected List<LevelsDTO> levels;
	protected List<LevelsDTO> newLevels;
    protected DefaultTableModel tableModelLevels;
	
    public ModifyActivityController(ModifyActivityView v) {
		this.atModel = ModelManager.getInstance().getActivityTemplatesModel();
		this.levelsModel = ModelManager.getInstance().getLevelsModel();
		this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
		
		this.view = v;
		levels = new LinkedList<>();
		newLevels = new LinkedList<>();
        tableModelLevels = new DefaultTableModel(new String[]{"name", "fee"}, 0);
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
    	
    	// Activities Table
    	this.view.getActivitiesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetailLeftPanel());
			}
		});
    	
    	// Name TextField
    	this.view.getNameTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockRightPanel());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockRightPanel());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Edition TextField
    	this.view.getEditionTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockRightPanel());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockRightPanel());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// DateStart TextField
    	this.view.getDateStartTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockRightPanel());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockRightPanel());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// DateEnd TextField
    	this.view.getDateEndTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockRightPanel());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockRightPanel());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Place TextField
    	this.view.getPlaceTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockRightPanel());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockRightPanel());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Modify Sponsorship Level Button
    	this.view.getModifySponsorshipLevelsButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> enableLevelInputs());
				SwingUtil.exceptionWrapper(() -> getLevels());
			}
		});
    	
    	// Name Level TextField
    	this.view.getLevelNameTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockLevels());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockLevels());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Fee Level TextField
    	this.view.getLevelFeeTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockLevels());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockLevels());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Back Button
    	this.view.getBackButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> initViewBack());
			}
		});
    	
    	// Add Button
    	this.view.getAddButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> addLevel());
			}
		});
    	
    	// Levels Table
    	this.view.getLevelTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetailRightPanel());
			}
		});
    	
    	// Remove Button
    	this.view.getRemoveButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> removeLevel());
			}
		});
    }
    
    public void initView() {
    	this.restoreDetail();
    	this.getActivities();
    	view.setVisible();
    }
    
    public void initViewBack() {
    	this.restoreDetailBack();
    	this.getActivities();
    	view.setVisible();
    }
    
    public void getActivities() {
    	List<ActivitiesDTO> activities = activitiesModel.getAllActivities();
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"id", "name", "edition", "status", "dateStart", "dateEnd", "place"}, 0);
        for (ActivitiesDTO activity : activities) {
        	tableModel.addRow(new Object[] {
        			activity.getId(),
        			activity.getName(),
        			activity.getEdition(),
        			activity.getStatus(),
        			activity.getDateStart(),
        			activity.getDateEnd(),
        			activity.getPlace()
        	});
        }
		this.view.getActivitiesTable().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getActivitiesTable());
    }
    
    public void updateDetailLeftPanel() {
    	this.lastSelectedActivity = SwingUtil.getSelectedKey(this.view.getActivitiesTable());
		if("".equals(this.lastSelectedActivity)) {  
			restoreDetail();
		}
		else {
			String nameActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 1);
			String editionActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 2);
			Object statusActivity = this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 3);
			String dateStartActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 4);
			String dateEndActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 5);
			String placeActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 6);

			this.view.getNameTextField().setText(nameActivity);
			this.view.getEditionTextField().setText(editionActivity);
			this.view.getStatusComboBox().setSelectedItem(statusActivity);;
			this.view.getDateStartTextField().setText(dateStartActivity);
			this.view.getDateEndTextField().setText(dateEndActivity);
			this.view.getPlaceTextField().setText(placeActivity);
			
			this.setInputsEnabled(true);
			this.setModifySponsorshipLevelsButtonEnabled(true);
		}
    }
    
    public void unlockRightPanel() {
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
    	
    	// Validate Edition
    	String edition = this.view.getEditionTextField().getText();
		if(!SyntacticValidations.isNotEmpty(edition)) {
			this.view.getEditionTextField().setForeground(Color.RED); 
			valid = false;
		} 
		else { 
			this.view.getEditionTextField().setForeground(Color.BLACK); 
		}
		
		// Validate DateStart
		String dateStart = this.view.getDateStartTextField().getText();
		if (!dateStart.isBlank()) {
			if(!SyntacticValidations.isDate(dateStart)) {
				this.view.getDateStartTextField().setForeground(Color.RED);
				valid = valid && false;
			} 
			else { 
				this.view.getDateStartTextField().setForeground(Color.BLACK); 
			}
		}
		
		// Validate DateEnd
		String dateEnd = this.view.getDateEndTextField().getText();
		if (!dateEnd.isBlank()) {
			if(!SyntacticValidations.isDate(dateEnd)) {
				this.view.getDateEndTextField().setForeground(Color.RED);
				valid = valid && false;
			} 
			else { 
				this.view.getDateEndTextField().setForeground(Color.BLACK); 
			}
		}
		
		// Validate Place
		String place = this.view.getPlaceTextField().getText();
		if (!place.isBlank()) {
			if(!SyntacticValidations.isNotEmpty(place)) {
				this.view.getPlaceTextField().setForeground(Color.RED);
				valid = valid && false;
			} 
			else { 
				this.view.getPlaceTextField().setForeground(Color.BLACK); 
			}
		}
		
		// Modify Sponsorship Button
    	this.setModifySponsorshipLevelsButtonEnabled(valid);
    }
    
    public void unlockLevels() {
		boolean valid = true;
		
		// Validate Name
		String name = this.view.getLevelNameTextField().getText();
		if(!SyntacticValidations.isNotEmpty(name)) {
			this.view.getLevelNameTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getLevelNameTextField().setForeground(Color.BLACK); 
		}
		
		// Validate Fee
		String fee = this.view.getLevelFeeTextField().getText();
		if(!SyntacticValidations.isDecimal(fee)) {
			this.view.getLevelFeeTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getLevelFeeTextField().setForeground(Color.BLACK); 
		}
		
		// Generate Levels button
    	this.setInputsEnabledLevels(valid);
    }
    
    public void getLevels() {
    	String idActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 0);
    	List<LevelsDTO> preLevels = levelsModel.getLevelsByActivityId(idActivity);
        for (LevelsDTO level : preLevels) {
        	levels.add(level);
        	tableModelLevels.addRow(new Object[] {
        			level.getName(),
        			level.getFee()
        	});
        }
		this.view.getLevelTable().setModel(tableModelLevels);
		SwingUtil.autoAdjustColumns(view.getLevelTable());
    }
    
    public void updateDetailRightPanel() {
    	this.lastSelectedLevel = SwingUtil.getSelectedKey(this.view.getLevelTable());
		if("".equals(this.lastSelectedLevel)) {  
			setRemoveEnabledLevels(false);
		}
		else {
			this.setRemoveEnabledLevels(true);
		}
    }
    
    // RESTORE METHODS
    
    public void restoreDetail() {
		this.view.getNameTextField().setEnabled(false);
		this.view.getEditionTextField().setEnabled(false);
		this.view.getStatusComboBox().setEnabled(false);
		this.view.getDateStartTextField().setEnabled(false);
		this.view.getDateEndTextField().setEnabled(false);
		this.view.getPlaceTextField().setEnabled(false);
		
		this.view.getModifySponsorshipLevelsButton().setEnabled(false);
		
		this.view.getLevelNameTextField().setEnabled(false);
		this.view.getLevelFeeTextField().setEnabled(false);
		
		this.view.getBackButton().setEnabled(false);
		this.view.getAddButton().setEnabled(false);
		
		this.view.getRemoveButton().setEnabled(false);
		
		this.view.getButtonLowRight().setEnabled(false);
	}
	
	public void restoreDetailBack() {
		this.setInputsEnabled(false);
		
		this.view.getNameTextField().setText("");
		this.view.getEditionTextField().setText("");
		this.view.getStatusComboBox().setSelectedIndex(0);
		this.view.getDateStartTextField().setText("");
		this.view.getDateEndTextField().setText("");
		this.view.getPlaceTextField().setText("");
		
		this.view.getModifySponsorshipLevelsButton().setEnabled(false);
		
		this.view.getLevelNameTextField().setEnabled(false);
		this.view.getLevelFeeTextField().setEnabled(false);
		this.view.getLevelNameTextField().setText("");
		this.view.getLevelFeeTextField().setText("");
		
		levels = new LinkedList<>();
		restoreLevelTable();
		
		this.view.getBackButton().setEnabled(false);
		this.view.getAddButton().setEnabled(false);
		
		this.view.getRemoveButton().setEnabled(false);
		
		this.view.getButtonLowRight().setEnabled(false);
	}
	
	public void restoreLevelTable() {
		this.view.getLevelTable().setModel(new DefaultTableModel());
	}
	
	// ENABLE METHODS
	
	public void setInputsEnabled(boolean valid) {
		this.view.getNameTextField().setEnabled(valid);
		this.view.getEditionTextField().setEnabled(valid);
		this.view.getStatusComboBox().setEnabled(valid);
		this.view.getDateStartTextField().setEnabled(valid);
		this.view.getDateEndTextField().setEnabled(valid);
		this.view.getPlaceTextField().setEnabled(valid);
	}
	
	public void setModifySponsorshipLevelsButtonEnabled(boolean valid) {
		this.view.getModifySponsorshipLevelsButton().setEnabled(valid);
    }
	
	public void enableLevelInputs() {
    	this.view.getModifySponsorshipLevelsButton().setEnabled(false);

    	this.view.getLevelNameTextField().setEnabled(true);
    	this.view.getLevelFeeTextField().setEnabled(true);
    	
    	this.view.getBackButton().setEnabled(true);
    	
    	this.view.getButtonLowRight().setEnabled(true);
    }
	
	public void setInputsEnabledLevels(boolean valid) {
		this.view.getAddButton().setEnabled(valid);
    }
	
	public void setRemoveEnabledLevels(boolean valid) {
		this.view.getRemoveButton().setEnabled(valid);
    }
	
	// OTHER METHODS
	
	public void addLevel() {
		LevelsDTO element = new LevelsDTO();
		
		element.setName(this.view.getLevelNameTextField().getText());
		element.setFee(this.view.getLevelFeeTextField().getText());
		levels.add(element);
		
		newLevels.clear();
		newLevels.add(element);
		
        for (LevelsDTO level : newLevels) {
        	tableModelLevels.addRow(new Object[] {
        			level.getName(),
        			level.getFee()
        	});
        }
		this.view.getLevelTable().setModel(tableModelLevels);
		SwingUtil.autoAdjustColumns(view.getLevelTable());
		
		this.view.getLevelNameTextField().setText("");
		this.view.getLevelFeeTextField().setText("");
		
		this.view.getButtonLowRight().setEnabled(true);
	}
	
	public void removeLevel() {
		levels.remove(this.view.getLevelTable().getSelectedRow());
		
        tableModelLevels.removeRow(this.view.getLevelTable().getSelectedRow());
        	
		this.view.getLevelTable().setModel(tableModelLevels);
		SwingUtil.autoAdjustColumns(view.getLevelTable());
		
		this.view.getRemoveButton().setEnabled(false);
		
		if (levels.isEmpty()) {
			this.view.getButtonLowRight().setEnabled(false);
		}
		else {
			this.view.getButtonLowRight().setEnabled(true);
		}
	}
    
    private void showSubmitDialog() {
    	String idActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(this.view.getActivitiesTable().getSelectedRow(), 0);
    	String nameActivity = view.getNameTextField().getText();
    	String editionActivity = view.getEditionTextField().getText();
    	String statusActivity = (String) view.getStatusComboBox().getSelectedItem();
        String dateStartActivity = view.getDateStartTextField().getText();
        String dateEndActivity = view.getDateEndTextField().getText();
        String placeActivity = view.getPlaceTextField().getText();
        
        String message = "<html><body>"
                + "<p>Modify an activity: </p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Activity ID:</b></td><td style='padding: 2px 5px;'>" + idActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + nameActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Edition:</b></td><td style='padding: 2px 5px;'>" + editionActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Status:</b></td><td style='padding: 2px 5px;'>" + statusActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date Start:</b></td><td style='padding: 2px 5px;'>" + dateStartActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date End:</b></td><td style='padding: 2px 5px;'>" + dateEndActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Place:</b></td><td style='padding: 2px 5px;'>" + placeActivity + "</td></tr>"
                + "</table>"
                + "<p><i>Proceed with these changes for the selected activity?</i></p>"
                + "</body></html>";

        int response = JOptionPane.showConfirmDialog(
            this.view.getFrame(),  message,
            "Confirm Activity Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        
        if (response != JOptionPane.YES_OPTION) {
        	return;
        }
        
        String ed = editionActivity.equals("-") ? "0" : editionActivity.trim();

        if (!dateStartActivity.isBlank()) {
        	SyntacticValidations.isDate(dateStartActivity);
    	}
    	if (!dateEndActivity.isBlank()) {
            SyntacticValidations.isDate(dateEndActivity);
    	}
        	
        this.activitiesModel.updateActivity(idActivity, nameActivity, ed, statusActivity, dateStartActivity, dateEndActivity, placeActivity);
		JOptionPane.showMessageDialog(
			this.view.getFrame(), "Activity updated correctly",
			"This operation has been succesful",
			JOptionPane.INFORMATION_MESSAGE
		);
		
		levelsModel.removeLevels(idActivity);
		for (LevelsDTO level : levels) {
    		this.levelsModel.insertNewLevel(idActivity, level.getName(), level.getFee());
		}
		
		JOptionPane.showMessageDialog(
			this.view.getFrame(), "Levels updated correctly",
			"This operation has been succesful",
			JOptionPane.INFORMATION_MESSAGE
		);

		this.restoreDetailBack();
    }
}
