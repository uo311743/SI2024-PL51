package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class RegisterSponsorshipView extends AbstractView {
	
	private JTable activityTable;
	private JComboBox<Object> sponsorComboBox;
	private JComboBox<Object> contactComboBox;
	private JComboBox<Object> gbMemberComboBox;
	private JComboBox<Object> levelsComboBox;
	private JTextField amountTextField;
	private JTextField agreementDateTextField;
	private JTextField contactEmailTextField;
	
	public RegisterSponsorshipView() { super("Register Sponshorship"); }
	
	@Override
	protected void initialize()
	{
		this.activityTable = new JTable();
		this.sponsorComboBox = new JComboBox<>();
        this.contactComboBox = new JComboBox<>();
        this.gbMemberComboBox = new JComboBox<>();
        this.levelsComboBox = new JComboBox<>();
        this.amountTextField = new JTextField(10);
        this.agreementDateTextField = new JTextField(10);
		this.contactEmailTextField = new JTextField(10);
		
		super.createButtonLowLeft("Cancel");
		super.createButtonLowMiddle("Reset");
		super.createButtonLowRight("Register");
	}

	@Override
	protected void configMainPanel()
	{
		super.getMainPanel().setLayout(new BorderLayout());
		super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));


		super.getMainPanel().add(createLeftPanel(), BorderLayout.WEST);
		super.getMainPanel().add(createRightPanel(), BorderLayout.EAST);
	}
	
	private JPanel createLeftPanel()
	{
		JLabel activityTableLabel = new JLabel("Please select an Activity:");

		activityTable.setName("activityTable");
		activityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		activityTable.setDefaultEditor(Object.class, null);
		JScrollPane activityTableScroll = new JScrollPane(activityTable);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(activityTableLabel, BorderLayout.NORTH);
		topPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(activityTableScroll, BorderLayout.CENTER);
	
		return panel;
	}
	
	private JPanel createRightPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		// Fields Panel with Border
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		fieldsPanel.setBorder(BorderFactory.createTitledBorder("Agreement Details"));
		GridBagConstraints fieldsGbc = new GridBagConstraints();
		fieldsGbc.insets = new Insets(5, 5, 5, 5);
		fieldsGbc.anchor = GridBagConstraints.WEST;

		// Labels
		JLabel sponsorLabel = new JLabel("Select a sponsor:");
		JLabel contactLabel = new JLabel("Select a contact:");
		JLabel gbMemberLabel = new JLabel("Select a GB member:");
		JLabel levelsLabel = new JLabel("Select a level:");
		JLabel amountLabel = new JLabel("Amount (euro):");
		JLabel agreementDateLabel = new JLabel("Agreement Date (yyyy-MM-dd):");
		JLabel contactEmailLabel = new JLabel("Contact Email:");

		// Fields
		JComponent[][] fields = {
		    {sponsorLabel, sponsorComboBox},
		    {contactLabel, contactComboBox},
		    {contactEmailLabel, contactEmailTextField},
		    {gbMemberLabel, gbMemberComboBox},
		    {levelsLabel, levelsComboBox},
		    {amountLabel, amountTextField},
		    {agreementDateLabel, agreementDateTextField}
		};

		// Add labels above inputs
		for (int i = 0; i < fields.length; i++) {
		    // Set label in the first row
		    fieldsGbc.gridx = 0;
		    fieldsGbc.gridy = i * 2; // position label in even rows
		    fieldsPanel.add(fields[i][0], fieldsGbc);

		    // Set input field in the second row
		    fieldsGbc.gridx = 0;
		    fieldsGbc.gridy = i * 2 + 1; // position input in odd rows
		    fieldsGbc.gridwidth = 2; // input spans two columns
		    fieldsGbc.fill = GridBagConstraints.HORIZONTAL; // Let the field take up horizontal space
		    fieldsGbc.weightx = 1.0; // Allow the input to expand horizontally evenly
		    fieldsPanel.add(fields[i][1], fieldsGbc);
		}

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(fieldsPanel, gbc);

        return panel;
	}
		
	
	
	public JTable getActivityTable() {
	    return activityTable;
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

	public JTextField getAgreementDateTextField() {
	    return agreementDateTextField;
	}

	public JTextField getContactEmailTextField() {
	    return contactEmailTextField;
	}

}
