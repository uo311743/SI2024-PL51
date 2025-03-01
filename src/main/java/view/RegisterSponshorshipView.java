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

public class RegisterSponshorshipView extends AbstractView {
	
	private JTable sponshorsTable;
	private JComboBox<Object> sponsorComboBox;
	private JComboBox<Object> contactComboBox;
	private JComboBox<Object> gbMemberComboBox;
	private JTextField amountTextField;
	private JTextField agreementDateTextField;
	
	public RegisterSponshorshipView() { super("Register Sponshorship", false); }
	
	@Override
	protected void initialize()
	{
		this.sponshorsTable = new JTable();
		this.sponsorComboBox = new JComboBox<>();
        this.contactComboBox = new JComboBox<>();
        this.gbMemberComboBox = new JComboBox<>();
        this.amountTextField = new JTextField(10);
        this.agreementDateTextField = new JTextField(10);
		
		super.createButtonLowLeft("Cancel");
		super.createButtonLowRight("Submit");
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

		sponshorsTable.setName("activityTable");
		sponshorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sponshorsTable.setDefaultEditor(Object.class, null);
		JScrollPane activityTable = new JScrollPane(sponshorsTable);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(activityTableLabel, BorderLayout.NORTH);
		topPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(activityTable, BorderLayout.CENTER);
		
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

        JLabel sponsorLabel = new JLabel("Select a sponsor:");
        JLabel contactLabel = new JLabel("Select a contact:");
        JLabel gbMemberLabel = new JLabel("Select a GB member:");
        JLabel amountLabel = new JLabel("Amount (euro):");
        JLabel agreementDateLabel = new JLabel("Agreement Date:");
        
        JComponent[][] fields = {
            {sponsorLabel, sponsorComboBox},
            {contactLabel, contactComboBox},
            {gbMemberLabel, gbMemberComboBox},
            {amountLabel, amountTextField},
            {agreementDateLabel, agreementDateTextField}
        };

        for (int i = 0; i < fields.length; i++) {
            fieldsGbc.gridx = 0;
            fieldsGbc.gridy = i;
            fieldsPanel.add(fields[i][0], fieldsGbc);
            
            fieldsGbc.gridx = 1;
            fieldsPanel.add(fields[i][1], fieldsGbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(fieldsPanel, gbc);

        return panel;
	}
	
	
	
	public JTable sponshorsTable() {
	    return sponshorsTable;
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

	public JTextField getAmountTextField() {
	    return amountTextField;
	}

	public JTextField getAgreementDateTextField() {
	    return agreementDateTextField;
	}

}
