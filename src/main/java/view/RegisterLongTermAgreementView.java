package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class RegisterLongTermAgreementView extends AbstractView {
	private JTable activitiesTable;
	private JTable activitySelectionTable;
	private JComboBox<Object> sponsorComboBox;
	private JComboBox<Object> contactComboBox;
	private JComboBox<Object> gbMemberComboBox;
	private JComboBox<Object> levelsComboBox;
	private JTextField amountTextField;
	private JTextField agreementStartDateTextField;
	private JTextField agreementEndDateTextField;
	private JTextField contactEmailTextField;
	private JButton showActivities;
	private JButton addActivity;
	private JButton removeActivity;
	private JLabel amountLabel;
    
    public RegisterLongTermAgreementView() { super("Register Long-Term Agreement"); }
    
    @Override
    protected void initialize()
    {
    	this.activitiesTable = new JTable();
    	this.activitySelectionTable = new JTable();
		this.sponsorComboBox = new JComboBox<>();
        this.contactComboBox = new JComboBox<>();
        this.gbMemberComboBox = new JComboBox<>();
        this.levelsComboBox = new JComboBox<>();
        this.amountTextField = new JTextField(10);
        this.agreementStartDateTextField = new JTextField(10);
        this.agreementEndDateTextField = new JTextField(10);
		this.contactEmailTextField = new JTextField(10);
		this.showActivities = new JButton("Show Activities");
		this.addActivity = new JButton("Add Activity");
		this.removeActivity = new JButton("Remove Activity");
		this.removeActivity.setVisible(false);
		this.amountLabel = new JLabel("Amount (€):");
    	
    	super.createButtonLowLeft("Cancel");
    	super.createButtonLowMiddle("Reset");
        super.createButtonLowRight("Register");
    }
    
    @Override
    protected void configMainPanel()
    {
    	super.getMainPanel().setLayout(new BorderLayout());
    	super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));

    	JPanel topPanel = createTopPanel();
    	JPanel bottomPanel = createBottomPanel();

    	// Main panel setup with vertical stacking
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	panel.add(addActivityPanel());
    	panel.add(Box.createVerticalStrut(10)); // Space between panels
    	panel.add(bottomPanel);

    	// Create the split pane
    	JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, panel);
    	splitPane.setResizeWeight(0.5); // Equal distribution of extra space
    	splitPane.setDividerLocation(0.5); // Start at 50% position
    	splitPane.setBorder(null);
    	splitPane.setContinuousLayout(true); // Smooth resizing
    	// COMPLETELY REMOVE THE DIVIDER VISUALLY
    	splitPane.setDividerSize(0);  // This is the key line
    	splitPane.setBorder(null);

    	// Ensure panels take all available space
    	topPanel.setMinimumSize(new Dimension(100, 100)); // Minimum sizes
    	panel.setMinimumSize(new Dimension(100, 100));
    	topPanel.setPreferredSize(new Dimension(300, 300)); // Initial sizes
    	panel.setPreferredSize(new Dimension(300, 300));

    	// Add to parent container
    	super.getMainPanel().add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel()
	{
        UIManager.put("TitledBorder.font", new Font("Arial", Font.PLAIN, 14));

        // Set table properties
        JLabel activitiesTableLabel = new JLabel("Activities");
        activitiesTableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        activitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activitiesTable.setDefaultEditor(Object.class, null);
        // Visual improvements
	    activitiesTable.setRowHeight(25); // Bigger rows
	    activitiesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
	    activitiesTable.setIntercellSpacing(new Dimension(10, 5));
	    
	    // Optional: Grid lines
	    activitiesTable.setShowGrid(true);
	    activitiesTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane activitiesTableScroll = new JScrollPane(activitiesTable);
        
        // Set table properties
        JLabel activitySelectionTableLabel = new JLabel("Activities included in the Agreement");
        activitySelectionTableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        activitySelectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activitySelectionTable.setDefaultEditor(Object.class, null);
        // Visual improvements
        activitySelectionTable.setRowHeight(25); // Bigger rows
        activitySelectionTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Bigger font
	    
	    // Optional: Intercell spacing for better readability
        activitySelectionTable.setIntercellSpacing(new Dimension(10, 5));
	    
	    // Optional: Grid lines
        activitySelectionTable.setShowGrid(true);
        activitySelectionTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane activitySelectionTableScroll = new JScrollPane(activitySelectionTable);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Left/right padding

        // Activity Panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel activitiesLabelPanel = new JPanel(new BorderLayout());
        activitiesLabelPanel.add(activitiesTableLabel, BorderLayout.CENTER);
        activitiesLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Bottom padding

        activitiesPanel.add(activitiesLabelPanel, BorderLayout.NORTH);
        activitiesPanel.add(activitiesTableScroll, BorderLayout.CENTER); 
        
        // Activity Selection Panel
        JPanel activitySelectionPanel = new JPanel(new BorderLayout());
        // Create a container panel for the label with empty border
        JPanel activitySelectionLabelPanel = new JPanel(new BorderLayout());
        activitySelectionLabelPanel.add(activitySelectionTableLabel, BorderLayout.CENTER);
        activitySelectionLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Bottom padding

        activitySelectionPanel.add(activitySelectionLabelPanel, BorderLayout.NORTH);
        activitySelectionPanel.add(activitySelectionTableScroll, BorderLayout.CENTER);
        
        // Main panel with vertical layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand
        gbc.insets = new Insets(0, 6, 0, 6); // Horizontal spacing only
        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 10, 10, 10); // Left/right padding

        // Add first table
        gbc.gridx = 0;
        gbc.gridy = 0;
        tablesPanel.add(activitiesPanel, gbc);

        // Add second table
        gbc.gridx = 1;
        gbc.gridy = 0;
        tablesPanel.add(activitySelectionPanel, gbc);

        JLabel activitiesLabel = new JLabel("*Select Activities to include in the Agreement");
        activitiesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(activitiesLabel);
        
        panel.add(createDatesPanel());
        panel.add(tablesPanel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(labelPanel);
	
		return panel;
	}
    
    public void adjustColumns() {
    	activitiesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	activitySelectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    private JPanel createDatesPanel() {
    	// Main panel setup with centered content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center container for all components
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(Box.createHorizontalGlue()); // Left spacer

        // Left side - Start Date
        JPanel startDatePanel = new JPanel();
        startDatePanel.setLayout(new BoxLayout(startDatePanel, BoxLayout.Y_AXIS));
        
        JLabel startDateLabel = new JLabel("Start Date (yyyy-mm-dd):");
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        startDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startDatePanel.add(startDateLabel);
        startDatePanel.add(Box.createVerticalStrut(5)); // Space between label and field
        
        agreementStartDateTextField.setMaximumSize(new Dimension(200, 25));
        agreementStartDateTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        startDatePanel.add(agreementStartDateTextField);
        
        centerPanel.add(startDatePanel);
        centerPanel.add(Box.createHorizontalStrut(20)); // Space between date groups

        // Middle - End Date
        JPanel endDatePanel = new JPanel();
        endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.Y_AXIS));
        
        JLabel endDateLabel = new JLabel("End Date (yyyy-mm-dd):");
        endDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        endDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        endDatePanel.add(endDateLabel);
        endDatePanel.add(Box.createVerticalStrut(5)); // Space between label and field
        
        agreementEndDateTextField.setMaximumSize(new Dimension(200, 25));
        agreementEndDateTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        endDatePanel.add(agreementEndDateTextField);
        
        centerPanel.add(endDatePanel);
        centerPanel.add(Box.createHorizontalStrut(20)); // Space before button

        // Right side - Show Button
        showActivities.setAlignmentY(Component.CENTER_ALIGNMENT);
        centerPanel.add(showActivities);
        
        centerPanel.add(Box.createHorizontalGlue()); // Right spacer

        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel addActivityPanel() {
    	// Main panel setup with centered content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center container for all components
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(Box.createHorizontalGlue()); // Left spacer

        // Left side - Level
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        
        JLabel levelLabel = new JLabel("Level:");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelPanel.add(levelLabel);
        levelPanel.add(Box.createVerticalStrut(5)); // Space between label and field
        
        levelsComboBox.setMaximumSize(new Dimension(200, 25));
        levelsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelPanel.add(levelsComboBox);
        
        centerPanel.add(levelPanel);
        centerPanel.add(Box.createHorizontalStrut(20)); // Space between date groups

        // Middle - Amount
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountPanel.add(amountLabel);
        amountPanel.add(Box.createVerticalStrut(5)); // Space between label and field
        
        amountTextField.setMaximumSize(new Dimension(200, 25));
        amountTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountPanel.add(amountTextField);
        
        centerPanel.add(amountPanel);
        centerPanel.add(Box.createHorizontalStrut(20)); // Space before button

        // Right side - Add Activity Button
        addActivity.setAlignmentY(Component.CENTER_ALIGNMENT);
        centerPanel.add(addActivity);
        // Right side - Remove Activity Button
        removeActivity.setAlignmentY(Component.CENTER_ALIGNMENT);
        centerPanel.add(removeActivity);
        
        centerPanel.add(Box.createHorizontalGlue()); // Right spacer

        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void setRemoveActivityButtonVisibility(boolean visible) {
    	removeActivity.setVisible(visible);
    }

    private JPanel createBottomPanel() {
    	// Main panel setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fields Panel with Border
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Agreement Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);  // Consistent padding
        gbc.weightx = 1.0;

        // Labels
     	JLabel sponsorLabel = new JLabel("Select a Sponsor:");
     	JLabel contactLabel = new JLabel("Select a Contact:");
     	JLabel gbMemberLabel = new JLabel("Select a GB Member:");
     	JLabel contactEmailLabel = new JLabel("Contact Email:");

        // Create labels with consistent font
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        
        sponsorLabel.setFont(labelFont);
        contactLabel.setFont(labelFont);
        gbMemberLabel.setFont(labelFont);
        contactEmailLabel.setFont(labelFont);

        // Add components in a clean grid
        int row = 0;
        
        // Sponsor row
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(sponsorLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(sponsorComboBox, gbc);
        
        // Contact row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(contactLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(contactComboBox, gbc);
        
        // Email row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(contactEmailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(contactEmailTextField, gbc);
        
        // GB Member row
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(gbMemberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(gbMemberComboBox, gbc);
        
        // Add glue to push components to top
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        fieldsPanel.add(Box.createGlue(), gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        return panel;
    }

	public JTable getActivitiesTable() {
		return this.activitiesTable;
	}
	
	public JComboBox<Object> getSponsorComboBox() {
		return sponsorComboBox;
	}

	public JComboBox<Object> getContactComboBox() {
		return contactComboBox;
	}

	public JComboBox<Object> getGbMemberComboBox() {
		return gbMemberComboBox;
	}

	public JComboBox<Object> getLevelsComboBox() {
		return levelsComboBox;
	}

	public JTextField getAmountTextField() {
		return amountTextField;
	}

	public JTextField getAgreementStartDateTextField() {
		return agreementStartDateTextField;
	}

	public JTextField getAgreementEndDateTextField() {
		return agreementEndDateTextField;
	}

	public JTextField getContactEmailTextField() {
		return contactEmailTextField;
	}
	
	public JButton getShowActivitiesButton() {
		return showActivities;
	}
	
	public JButton getAddActivityButton() {
		return addActivity;
	}
	
	public JTable getActivitySelectionTable() {
		return activitySelectionTable;
	}
	
	public JLabel getAmountLabel() {
		return amountLabel;
	}
	
	public JButton getRemoveActivityButton() {
		return removeActivity;
	}
}