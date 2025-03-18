package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
	
    public RegisterActivityController(ActivityTemplatesModel atm, LevelsModel lm, ActivitiesModel am, RegisterActivityView v) {
		this.atModel = atm;
		this.levelsModel = lm;
		this.activitiesModel = am;
		
		this.view = v;
        this.initView();
        this.initController();
    }
    
    public void initController() {
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
    				SwingUtil.exceptionWrapper(() -> restoreDetailEnable());
                }
                else {
    				SwingUtil.exceptionWrapper(() -> restoreDetailDisable());
                }
            }
        });
    	
    	// Add Templates Button
    	this.view.getAddTemplatesButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> atModel.insertNewTemplate(nameActivity));
			}
		});
    	
    	// Add Sponsorship Level Button
    	this.view.getAddSponsorshipLevelsButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> insertDefaultLevel(activitiesModel.getActivityByFilters(nameActivity, editionActivity, dateStartActivity, dateEndActivity, placeActivity).getId()));
				SwingUtil.exceptionWrapper(() -> getLevels());
			}
		});
    }
    
    public void initView() {
    	this.restoreDetailDisable();
    	this.loadTemplates();
    	view.setVisible();
    }
    
    public void loadTemplates() {
        List<Object[]> templateList = atModel.getTemplatesListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(templateList);
        view.getTemplatesComboBox().setModel(lmodel);
    }
    
    private void insertDefaultLevel(String activityId) {
    	this.view.getLevelNameTextField().setText("Default");
    	this.view.getLevelFeeTextField().setText("100.0");
    	
		this.levelsModel.insertNewLevel(activityId, view.getLevelNameTextField().getText(), view.getLevelFeeTextField().getText());
    }
        
	private void getLevels() {
		LinkedList<LevelsDTO> levels = new LinkedList<>();
		LevelsDTO element = new LevelsDTO();
		
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
		
		element.setIdActivity(activitiesModel.getActivityByFilters(nameActivity, editionActivity, dateStartActivity, dateEndActivity, placeActivity).getId());
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
	
	public void restoreDetailEnable() {
		this.view.getTemplatesComboBox().setEnabled(true);
		this.view.getNameTextField().setEnabled(false);
		this.view.getEditionTextField().setEnabled(true);
    }
	
	public void restoreDetailDisable() {
		this.view.getTemplatesComboBox().setEnabled(false);
		this.view.getNameTextField().setEnabled(true);
		this.view.getEditionTextField().setEnabled(false);
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
                + "<tr><td style='padding: 2px 5px;'><b>Name:</b></td><td style='padding: 2px 5px;'>" + editionActivity + "</td></tr>"
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
        
        SyntacticValidations.isDate(dateStartActivity);
        SyntacticValidations.isDate(dateEndActivity);
        	
        this.activitiesModel.insertNewActivity(nameActivity, editionActivity, dateStartActivity, dateEndActivity, placeActivity);
	        
		JOptionPane.showMessageDialog(
			this.view.getFrame(), "Activity added correctly",
			"This operation has been succesful",
			JOptionPane.INFORMATION_MESSAGE
		);
		this.restoreDetailDisable();
    }
}
