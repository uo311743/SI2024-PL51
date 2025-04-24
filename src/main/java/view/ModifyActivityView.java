package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;

public class ModifyActivityView extends AbstractView {
    
    private JTable activitiesTable;
    private JTextField nameTextField;
    private JTextField editionTextField;
    private JComboBox<Object> statusComboBox;
    private JTextField dateStartTextField;
    private JTextField dateEndTextField;
    private JTextField placeTextField;
    private JButton modifySponsorshipLevelsButton;

    private JTextField levelNameTextField;
    private JTextField levelFeeTextField;
    private JButton backButton;
    private JButton addButton;
    private JTable levelTable;
    private JButton removeButton;
    
    public ModifyActivityView() {
        super("Modify Activity");
    }
    
    @Override
    protected void initialize() {
        this.activitiesTable = new JTable(new DefaultTableModel());
        this.nameTextField = new JTextField(20);
        this.editionTextField = new JTextField(20);
        this.statusComboBox = new JComboBox<>(new String[]{"planned", "closed", "cancelled"});
        this.dateStartTextField = new JTextField(20);
        this.dateEndTextField = new JTextField(20);
        this.placeTextField = new JTextField(20);
        this.modifySponsorshipLevelsButton = new JButton("Modify Sponsorship Levels");
        
        this.levelNameTextField = new JTextField(20);
        this.levelFeeTextField = new JTextField(20);
        this.backButton = new JButton("Back");
        this.addButton = new JButton("Add");
        this.levelTable = new JTable(new DefaultTableModel());
        this.removeButton = new JButton("Remove");
        
        Dimension textFieldSize = new Dimension(200, 30);
        nameTextField.setPreferredSize(textFieldSize);
        editionTextField.setPreferredSize(textFieldSize);
        dateStartTextField.setPreferredSize(textFieldSize);
        dateEndTextField.setPreferredSize(textFieldSize);
        placeTextField.setPreferredSize(textFieldSize);
        levelNameTextField.setPreferredSize(textFieldSize);
        levelFeeTextField.setPreferredSize(textFieldSize);
        
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Modify Activity");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow 30][grow 70]", "[grow]"));

        // Activity Panel
        JPanel activityPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        JScrollPane activitiesTablePanel = new JScrollPane(activitiesTable);
        activityPanel.setBorder(BorderFactory.createTitledBorder("Activity Details"));
        activityPanel.add(activitiesTablePanel, "span, grow, wrap");
        activityPanel.add(new JLabel("Name: "));
        activityPanel.add(nameTextField, "wrap");
        activityPanel.add(new JLabel("Edition: "));
        activityPanel.add(editionTextField, "wrap");
        activityPanel.add(new JLabel("Status: "));
        activityPanel.add(statusComboBox, "wrap");
        activityPanel.add(new JLabel("Start Date: "));
        activityPanel.add(dateStartTextField, "wrap");
        activityPanel.add(new JLabel("End Date: "));
        activityPanel.add(dateEndTextField, "wrap");
        activityPanel.add(new JLabel("Place: "));
        activityPanel.add(placeTextField, "wrap");
        activityPanel.add(modifySponsorshipLevelsButton, "span, grow");
        getMainPanel().add(activityPanel, "cell 0 0, grow");
        
        // Level Panel
        JPanel rightPanel = new JPanel(new MigLayout("wrap", "[grow]", "[grow 40][grow 60][]"));
        JPanel levelPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        levelPanel.setBorder(BorderFactory.createTitledBorder("Sponsorship Level"));
        levelPanel.add(new JLabel("Sponsorship Level Name: "));
        levelPanel.add(levelNameTextField);
        levelPanel.add(new JLabel("Fee: "));
        levelPanel.add(levelFeeTextField);
        
        JPanel buttonPanel = new JPanel(new MigLayout("", "[left][right]", ""));
        buttonPanel.add(backButton, "cell 0 0");
        buttonPanel.add(addButton, "cell 1 0");
        levelPanel.add(buttonPanel, "span, grow");
        
        rightPanel.add(levelPanel, "grow");
        
        // Level Table
        JScrollPane levelTablePanel = new JScrollPane(levelTable);
        rightPanel.add(levelTablePanel, "grow");
        
        JPanel removeButtonPanel = new JPanel(new MigLayout("", "[right]", ""));
        removeButtonPanel.add(removeButton, "cell 0 0");
        rightPanel.add(removeButtonPanel, "grow");
        
        getMainPanel().add(rightPanel, "cell 1 0, grow");
    }
    
    // Getters and Setters
    public JTable getActivitiesTable() {
        return activitiesTable;
    }
    
    public JTextField getNameTextField() { 
        return nameTextField; 
    }
    
    public JTextField getEditionTextField() { 
        return editionTextField; 
    }
    
    public JComboBox<Object> getStatusComboBox() {
    	return statusComboBox;
    }
    
    public JTextField getDateStartTextField() { 
        return dateStartTextField; 
    }
    
    public JTextField getDateEndTextField() { 
        return dateEndTextField; 
    }
    
    public JTextField getPlaceTextField() { 
        return placeTextField; 
    }
    
    public JButton getModifySponsorshipLevelsButton() { 
        return modifySponsorshipLevelsButton; 
    }
    
    public JTextField getLevelNameTextField() { 
        return levelNameTextField; 
    }
    
    public JTextField getLevelFeeTextField() { 
        return levelFeeTextField; 
    }
    
    public JButton getBackButton() { 
        return backButton; 
    }
    
    public JButton getRemoveButton() { 
        return removeButton; 
    }
    
    public JButton getAddButton() { 
        return addButton; 
    }
    
    public JTable getLevelTable() { 
        return levelTable; 
    }
}
