package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import DTOs.ActivitiesDTO;
import DTOs.LevelsDTO;
import model.ActivitiesModel;
import model.ActivityTemplatesModel;
import model.LevelsModel;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterActivityView;

public class RegisterActivityController {
	
	protected ActivityTemplatesModel atModel;
	protected LevelsModel levelsModel;
	protected ActivitiesModel activitiesModel;
		
	protected RegisterActivityView view;
	
	protected LinkedList<LevelsDTO> levels;
	
    public RegisterActivityController(ActivityTemplatesModel atm, LevelsModel lm, ActivitiesModel am, RegisterActivityView v) {
		this.atModel = atm;
		this.levelsModel = lm;
		this.activitiesModel = am;
		
		this.view = v;
		levels = new LinkedList<>();
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
    	
    	// Name CheckBox
    	this.view.getNameCheckBox().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (view.getNameCheckBox().isSelected()) {
    				SwingUtil.exceptionWrapper(() -> configTemplates());
    				SwingUtil.exceptionWrapper(() -> unlockTemplates());
                }
                else {
    				SwingUtil.exceptionWrapper(() -> configTextField());
    				SwingUtil.exceptionWrapper(() -> unlockTemplates());
                }
            }
        });
    	
    	// Name ComboBox
    	this.view.getTemplatesComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}
		});
    	
    	// Name TextField
    	this.view.getNameTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockTemplates());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Edition TextField
    	this.view.getEditionTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockTemplates());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// DateStart TextField
    	this.view.getDateStartTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockTemplates());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// DateEnd TextField
    	this.view.getDateEndTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockTemplates());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Place TextField
    	this.view.getPlaceTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> unlockTemplates());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> unlockTemplates());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	// Add Templates Button
    	this.view.getAddTemplatesButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> insertTemplate());
			}
		});
    	
    	// Add Sponsorship Level Button
    	this.view.getAddSponsorshipLevelsButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> insertDefaultLevelData());
				SwingUtil.exceptionWrapper(() -> getLevels());
				SwingUtil.exceptionWrapper(() -> enableBackButton());
			}
		});
    	
    	// Back Button
    	this.view.getBackButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> initViewBack());
			}
		});
    }
    
    public void initView() {
    	this.restoreDetail();
    	this.loadTemplates();
    	view.setVisible();
    }
    
    public void initViewBack() {
    	this.restoreDetailBack();
    	this.loadTemplates();
    	view.setVisible();
    }
    
    public void enableBackButton() {
    	this.view.getBackButton().setEnabled(true);
    }
    
    public void insertTemplate() {
    	String nameActivity;
    	if (view.getNameCheckBox().isSelected()) {
            nameActivity = String.valueOf(view.getTemplatesComboBox().getSelectedItem());
    	}
    	else {
            nameActivity = view.getNameTextField().getText();
    	}
    	    	
    	String message = "<html><body>"
                + "<p>Add an activity template: </p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + nameActivity + "</td></tr>"
                + "</table>"
                + "<p><i>Proceed with these template?</i></p>"
                + "</body></html>";

        int response = JOptionPane.showConfirmDialog(
            this.view.getFrame(),  message,
            "Confirm Activity Template Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        
        if (response != JOptionPane.YES_OPTION) {
        	return;
        }
        
        int numTemplates = this.atModel.getNumberTemplatesByName(nameActivity);
        
        if (numTemplates == 0) {
        	atModel.insertNewTemplate(nameActivity);
        	
    		JOptionPane.showMessageDialog(
    			this.view.getFrame(), "Activity Template added correctly",
    			"This operation has been succesful",
    			JOptionPane.INFORMATION_MESSAGE
    		);
        }
        else {
        	JOptionPane.showMessageDialog(
            	this.view.getFrame(), "This activity template already exists",
            	"ERROR",
            	JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    public void unlockTemplates() {
		boolean valid = true;
		
		// Validate Name (and Edition)
		if (view.getNameCheckBox().isSelected()) {
			String edition = this.view.getEditionTextField().getText();
    		if(!SyntacticValidations.isNotEmpty(edition)) {
    			this.view.getEditionTextField().setForeground(Color.RED); 
    			valid = false;
    		} 
    		else { 
    			this.view.getEditionTextField().setForeground(Color.BLACK); 
    		}
    	}
    	else {
    		String name = this.view.getNameTextField().getText();
    		if(!SyntacticValidations.isNotEmpty(name)) {
    			this.view.getEditionTextField().setForeground(Color.RED);
    			valid = false;
    		} 
    		else { 
    			this.view.getEditionTextField().setForeground(Color.BLACK); 
    		}
    	}
		
		// Validate DateStart
		String dateStart = this.view.getDateStartTextField().getText();
		if(!SyntacticValidations.isDate(dateStart)) {
			this.view.getDateStartTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getDateStartTextField().setForeground(Color.BLACK); 
		}
		
		// Validate DateEnd
		String dateEnd = this.view.getDateEndTextField().getText();
		if(!SyntacticValidations.isDate(dateEnd)) {
			this.view.getDateEndTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getDateEndTextField().setForeground(Color.BLACK); 
		}
		
		// Validate Place
		String place = this.view.getPlaceTextField().getText();
		if(!SyntacticValidations.isNotEmpty(place)) {
			this.view.getPlaceTextField().setForeground(Color.RED);
			valid = false;
		} 
		else { 
			this.view.getPlaceTextField().setForeground(Color.BLACK); 
		}
		
		// Generate Invoice button
    	this.setInputsEnabled(valid);
    }
    
    public void loadTemplates() {
        List<Object[]> templateList = atModel.getTemplatesListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(templateList);
        view.getTemplatesComboBox().setModel(lmodel);
    }
    
    private void insertDefaultLevelData() {
    	this.view.getNameCheckBox().setEnabled(false);
    	this.view.getTemplatesComboBox().setEnabled(false);
    	this.view.getNameTextField().setEnabled(false);
    	this.view.getEditionTextField().setEnabled(false);
    	this.view.getDateStartTextField().setEnabled(false);
    	this.view.getDateEndTextField().setEnabled(false);
    	this.view.getPlaceTextField().setEnabled(false);
    	this.view.getAddTemplatesButton().setEnabled(false);
    	this.view.getAddSponsorshipLevelsButton().setEnabled(false);

    	this.view.getLevelNameTextField().setText("Default");
    	this.view.getLevelFeeTextField().setText("100.0");
    	
    	this.view.getButtonLowRight().setEnabled(true);
    }
        
	private void getLevels() {
		LevelsDTO element = new LevelsDTO();
		
		element.setName(this.view.getLevelNameTextField().getText());
		element.setFee(this.view.getLevelFeeTextField().getText());
		levels.add(element);
		
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"name", "fee"}, 0);
        for (LevelsDTO level : levels) {
        	tableModel.addRow(new Object[] {
        			level.getName(),
        			level.getFee()
        	});
        }
		this.view.getLevelTable().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getLevelTable());
    }
	
	public void restoreDetail() {
		this.view.getTemplatesComboBox().setEnabled(false);
		this.view.getNameTextField().setEnabled(true);
		this.view.getEditionTextField().setEnabled(false);
		this.view.getEditionTextField().setText("-");
		
		this.view.getAddTemplatesButton().setEnabled(false);
		this.view.getAddSponsorshipLevelsButton().setEnabled(false);
		
		this.view.getLevelNameTextField().setEnabled(false);
		this.view.getLevelFeeTextField().setEnabled(false);
		
		this.view.getBackButton().setEnabled(false);
		
		this.view.getButtonLowRight().setEnabled(false);
	}
	
	public void restoreDetailBack() {
		this.view.getNameCheckBox().setSelected(false);
		this.view.getTemplatesComboBox().setEnabled(true);
		this.view.getNameTextField().setEnabled(true);
		this.view.getEditionTextField().setEnabled(false);
		this.view.getDateStartTextField().setEnabled(true);
		this.view.getDateEndTextField().setEnabled(true);
		this.view.getPlaceTextField().setEnabled(true);
		
		this.view.getNameTextField().setText("");
		this.view.getEditionTextField().setText("-");
		this.view.getDateStartTextField().setText("");
		this.view.getDateEndTextField().setText("");
		this.view.getPlaceTextField().setText("");
		
		this.view.getAddTemplatesButton().setEnabled(false);
		this.view.getAddSponsorshipLevelsButton().setEnabled(false);
		
		this.view.getLevelNameTextField().setEnabled(false);
		this.view.getLevelFeeTextField().setEnabled(false);
		this.view.getLevelNameTextField().setText("");
		this.view.getLevelFeeTextField().setText("");
		
		levels.clear();
		getLevels();
		
		this.view.getBackButton().setEnabled(false);
		
		this.view.getButtonLowRight().setEnabled(false);
	}
	
	public void setInputsEnabled(boolean valid) {
		if(this.view.getNameCheckBox().isSelected()) {
	    	this.view.getAddTemplatesButton().setEnabled(false);
		}
		else {
	    	this.view.getAddTemplatesButton().setEnabled(valid);
		}
		
		this.view.getAddSponsorshipLevelsButton().setEnabled(valid);
    }
	
	public void configTemplates() {
		this.view.getTemplatesComboBox().setEnabled(true);
		this.view.getNameTextField().setEnabled(false);
		this.view.getEditionTextField().setEnabled(true);
		this.view.getEditionTextField().setText("");
    }
	
	public void configTextField() {
		this.view.getTemplatesComboBox().setEnabled(false);
		this.view.getNameTextField().setEnabled(true);
		this.view.getEditionTextField().setEnabled(false);
		this.view.getEditionTextField().setText("-");
    }
    
    private void showSubmitDialog() {
    	String nameActivity;
    	if (view.getNameCheckBox().isSelected()) {
            nameActivity = String.valueOf(view.getTemplatesComboBox().getSelectedItem());
    	}
    	else {
            nameActivity = view.getNameTextField().getText();
    	}
    	String editionActivity = view.getEditionTextField().getText();
        String dateStartActivity = view.getDateStartTextField().getText();
        String dateEndActivity = view.getDateEndTextField().getText();
        String placeActivity = view.getPlaceTextField().getText();
        
        String message = "<html><body>"
                + "<p>Add an activity: </p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + nameActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Edition:</b></td><td style='padding: 2px 5px;'>" + editionActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date Start:</b></td><td style='padding: 2px 5px;'>" + dateStartActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date End:</b></td><td style='padding: 2px 5px;'>" + dateEndActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Place:</b></td><td style='padding: 2px 5px;'>" + placeActivity + "</td></tr>"
                + "</table>"
                + "<p><i>Proceed with these activity?</i></p>"
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

        int numActivities = this.activitiesModel.getNumberActivitiesByFilters(nameActivity, ed);
        
        if (numActivities == 0) {
        	SyntacticValidations.isDate(dateStartActivity);
            SyntacticValidations.isDate(dateEndActivity);
            	
            this.activitiesModel.insertNewActivity(nameActivity, ed, dateStartActivity, dateEndActivity, placeActivity);
    		JOptionPane.showMessageDialog(
    			this.view.getFrame(), "Activity added correctly",
    			"This operation has been succesful",
    			JOptionPane.INFORMATION_MESSAGE
    		);
    		
    		ActivitiesDTO activity = activitiesModel.getActivityByFilters(nameActivity, ed, dateStartActivity, dateEndActivity, placeActivity);
    		this.levelsModel.insertNewLevel(activity.getId(), view.getLevelNameTextField().getText(), view.getLevelFeeTextField().getText());
    		JOptionPane.showMessageDialog(
    			this.view.getFrame(), "Levels added correctly",
    			"This operation has been succesful",
    			JOptionPane.INFORMATION_MESSAGE
    		);

    		this.restoreDetailBack();
        }
        else {
        	JOptionPane.showMessageDialog(
        		this.view.getFrame(), "This activity already exists",
        		"ERROR",
        		JOptionPane.INFORMATION_MESSAGE
        	);
        	
        	this.restoreDetailBack();
        }
    }
}
