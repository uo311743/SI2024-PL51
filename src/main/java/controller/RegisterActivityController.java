package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ActivitiesModel;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterActivityView;

public class RegisterActivityController {
	
	protected ActivityTemplatesModel atModel;
	protected LevelsModel levelsModel;
	protected ActivitiesModel activitiesModel;
		
	protected RegisterActivityView view;
	
    public RegisterActivityController(ActivityTemplatesModel atm, LevelsModel lm, ActivitiesModel am, RegisterActivityView v) {
		this.atModel = atm;
		this.levelsModel = lm;
		this.activitiesModel = am;
		
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
    	
    	// Add Sponsorship Level Button
    	this.view.getAddSponsorshipLevelsButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// Arreglar
				SwingUtil.exceptionWrapper(() -> insertDefaultLevel());
				SwingUtil.exceptionWrapper(() -> getLevels());
			}
		});
    }
    
    public void initView() {
    	this.loadTemplates();
    	view.setVisible();
    }
    
    public void loadTemplates() {
        List<Object[]> templateList = atModel.getTemplatesListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(templateList);
        view.getTemplatesComboBox().setModel(lmodel);
    }
    
    private void setInputsEnabled(boolean enabled) {
    	view.getLevelNameTextField().setEnabled(enabled);
    	view.getLevelAmountTextField().setEnabled(enabled);
    }
    
    private void insertDefaultLevel(String activityId) {
    	this.view.getLevelNameTextField().setText("Default");
    	this.view.getLevelAmountTextField().setText("100.0");
    	
		this.levelsModel.insertNewLevel(view.getLevelNameTextField().getText(), view.getLevelAmountTextField().getText(), activityId);
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
    
    private void showSubmitDialog() {
    	String nameActivity;
    	if (view.getNameCheckBox().isSelected()) {
            nameActivity = String.valueOf(view.getTemplatesComboBox().getSelectedItem());
    	}
    	else {
            nameActivity = view.getNameTextField().getText();
    	}
    	String editionActivity = view.getEditionTextField().getText();
        String statusActivity = view.getStatusTextField().getText();
        String dateStartActivity = view.getDateStartTextField().getText();
        String dateEndActivity = view.getDateEndTextField().getText();
        String placeActivity = view.getPlaceTextField().getText();
        
        String nameLevel = view.getLevelNameTextField().getText();
        String amountLevel = view.getLevelAmountTextField().getText();

        String message = "<html><body>"
                + "<p>Add an activity: </p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + nameActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + editionActivity + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Status:</b></td><td style='padding: 2px 5px;'>" + statusActivity + "</td></tr>"
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
        
        int numActivities = this.activitiesModel.getNumberActivities();
    	
        if(numActivities == 0) {
        	SyntacticValidations.isDate(dateStartActivity);
        	SyntacticValidations.isDate(dateEndActivity);
        
			this.activitiesModel.insertNewActivity(nameActivity, editionActivity, statusActivity, dateStartActivity, dateEndActivity, placeActivity);
	        
			JOptionPane.showMessageDialog(
	    			this.view.getFrame(), "Activity added correctly",
	    			"This operation has been succesful",
	    			JOptionPane.INFORMATION_MESSAGE
	    	);
	        this.restoreDetail();
        }
    	else {
    		message = "It will modify " + numActivities + " activities.";
    		response = JOptionPane.showConfirmDialog(
    	            this.view.getFrame(), message,
    	            "Confirm modification of old activities",
    	            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
	        );
	        
	        if (response == JOptionPane.YES_OPTION) {
	        	SyntacticValidations.isDate(dateStartActivity);
	        	SyntacticValidations.isDate(dateEndActivity);
        		
	        	this.activitiesModel.insertUpdateActivity(nameActivity, editionActivity, statusActivity, dateStartActivity, dateEndActivity, placeActivity);
		        JOptionPane.showMessageDialog(
		    			this.view.getFrame(),
		    			"Activity added correctly",
		    			"This operation has been succesful",
		    			JOptionPane.INFORMATION_MESSAGE
		    	);
		        this.restoreDetail();
	        }
    	}
    }
}
