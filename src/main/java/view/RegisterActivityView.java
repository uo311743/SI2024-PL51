package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class RegisterActivityView extends AbstractView {
    
    private JCheckBox nameCheckBox;
    private JComboBox<Object> templatesComboBox;
    private JTextField nameTextField;
    private JTextField editionTextField;
    private JTextField dateStartTextField;
    private JTextField dateEndTextField;
    private JTextField placeTextField;
    private JButton addTemplatesButton;
    private JButton addSponsorshipLevelsButton;

    private JTextField levelNameTextField;
    private JTextField levelFeeTextField;
    private JButton backButton;
    private JButton addButton;
    private JTable levelTable;
    
    public RegisterActivityView() {
        super("Register Activity");
    }
    
    @Override
    protected void initialize() {
        this.nameCheckBox = new JCheckBox();
        this.templatesComboBox = new JComboBox<>();
        this.nameTextField = new JTextField(10);
        this.editionTextField = new JTextField(10);
        this.dateStartTextField = new JTextField(10);
        this.dateEndTextField = new JTextField(10);
        this.placeTextField = new JTextField(10);
        this.addTemplatesButton = new JButton("Add Activity Templates");
        this.addSponsorshipLevelsButton = new JButton("Add Sponsorship Levels");
        
        this.levelNameTextField = new JTextField(10);
        this.levelFeeTextField = new JTextField(10);
        this.backButton = new JButton("Back");
        this.addButton = new JButton("Add");
        this.levelTable = new JTable(new DefaultTableModel());
        
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Register Activity");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow 30][grow 70]", "[grow]"));

        // Activity Panel
        JPanel activityPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        activityPanel.setBorder(BorderFactory.createTitledBorder("Activity Details"));
        activityPanel.add(new JLabel("Use Templates?"));
        activityPanel.add(nameCheckBox, "wrap");
        activityPanel.add(new JLabel("Template Name: "));
        activityPanel.add(templatesComboBox, "wrap");
        activityPanel.add(new JLabel("Input Name: "));
        activityPanel.add(nameTextField, "wrap");
        activityPanel.add(new JLabel("Edition: "));
        activityPanel.add(editionTextField, "wrap");
        activityPanel.add(new JLabel("Start Date: "));
        activityPanel.add(dateStartTextField, "wrap");
        activityPanel.add(new JLabel("End Date: "));
        activityPanel.add(dateEndTextField, "wrap");
        activityPanel.add(new JLabel("Place: "));
        activityPanel.add(placeTextField, "wrap");
        activityPanel.add(addTemplatesButton, "span, grow");
        activityPanel.add(addSponsorshipLevelsButton, "span, grow");
        getMainPanel().add(activityPanel, "cell 0 0, grow");
        
        // Level Panel
        JPanel rightPanel = new JPanel(new MigLayout("wrap", "[grow]", "[grow 40][grow 60]"));
        JPanel levelPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        levelPanel.setBorder(BorderFactory.createTitledBorder("Sponsorship Level"));
        levelPanel.add(new JLabel("Sponsorship Level Name: "));
        levelPanel.add(levelNameTextField);
        levelPanel.add(new JLabel("Fee: "));
        levelPanel.add(levelFeeTextField);
        levelPanel.add(backButton, "split 2, align left");
        levelPanel.add(addButton, "align right, wrap");
        rightPanel.add(levelPanel, "grow");
        
        // Level Table
        JScrollPane levelTablePanel = new JScrollPane(levelTable);
        rightPanel.add(levelTablePanel, "grow");
        getMainPanel().add(rightPanel, "cell 1 0, grow");
    }
    
    // Getters and Setters
    public JCheckBox getNameCheckBox() { 
        return nameCheckBox; 
    }
    
    public JComboBox<Object> getTemplatesComboBox() { 
        return templatesComboBox; 
    }
    
    public JTextField getNameTextField() { 
        return nameTextField; 
    }
    
    public JTextField getEditionTextField() { 
        return editionTextField; 
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
    
    public JButton getAddTemplatesButton() { 
        return addTemplatesButton; 
    }
    
    public JButton getAddSponsorshipLevelsButton() { 
        return addSponsorshipLevelsButton; 
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
    
    public JButton getAddButton() { 
        return addButton; 
    }
    
    public JTable getLevelTable() { 
        return levelTable; 
    }
}
