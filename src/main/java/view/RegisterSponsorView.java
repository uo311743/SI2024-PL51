package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class RegisterSponsorView extends AbstractView {
	
	private JTextField sponsorNameField;
	private JTextField sponsorAddressField;
	private JTextField sponsorVatField;
    
	private JTable contactsTable;
    
	private JTextField contactNameField;
	private JTextField contactEmailField;
	private JTextField contactPhoneField;
    
	private JButton buttonAddContact;
	
	
	public RegisterSponsorView() { super("Register Sponsor"); }
	
	@Override
	protected void initialize()
	{
		this.sponsorNameField = new JTextField(40);
		this.sponsorAddressField = new JTextField(40);
		this.sponsorVatField = new JTextField(40);
		
	    this.contactNameField = new JTextField(30);
	    this.contactEmailField = new JTextField(30);
	    this.contactPhoneField = new JTextField(30);

	    String[] columnNames = {"Name", "Email", "Phone"};
	    DefaultTableModel contactsTableModel = new DefaultTableModel(columnNames, 0);
        contactsTable = new JTable(contactsTableModel);
	    
	    this.buttonAddContact = new JButton("Add Contact");
		
		super.createButtonLowLeft("Cancel");
		super.createButtonLowMiddle("Reset");
		super.createButtonLowRight("Register");
	}

	@Override
	protected void configMainPanel()
	{
		super.getMainPanel().setLayout(new BorderLayout());
		super.getMainPanel().setBorder(new EmptyBorder(10, 20, 10, 20));


		super.getMainPanel().add(this.createTopPanel(), BorderLayout.NORTH);
		super.getMainPanel().add(createCenterPanel(), BorderLayout.CENTER);
	}
	
	private JPanel createTopPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5)); // 3 rows, 2 columns
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sponsor Information", TitledBorder.LEFT, TitledBorder.TOP));

        // Labels
        JLabel nameLabel = new JLabel("Organization Name:");
        JLabel addressLabel = new JLabel("Billing Address:");
        JLabel vatLabel = new JLabel("NIF / VAT number:");

        // Adding components to panel
        panel.add(nameLabel);
        panel.add(this.sponsorNameField);
        panel.add(addressLabel);
        panel.add(this.sponsorAddressField);
        panel.add(vatLabel);
        panel.add(this.sponsorVatField);
		return panel;
	}
	
	private JPanel createCenterPanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Contacts", TitledBorder.LEADING, TitledBorder.TOP));

	    // Table on the left
	    JScrollPane tableScrollPane = new JScrollPane(contactsTable);
	    tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // Form on the right
	    JPanel formPanel = new JPanel();
	    formPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better spacing

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.insets = new Insets(5, 5, 5, 5); // Add padding

	    formPanel.add(new JLabel("Complete name:"), gbc);
	    gbc.gridy++;
	    formPanel.add(contactNameField, gbc);

	    gbc.gridy++;
	    formPanel.add(new JLabel("Email:"), gbc);
	    gbc.gridy++;
	    formPanel.add(contactEmailField, gbc);

	    gbc.gridy++;
	    formPanel.add(new JLabel("Phone (optional):"), gbc);
	    gbc.gridy++;
	    formPanel.add(contactPhoneField, gbc);

	    // Add padding before the button
	    gbc.gridy++;
	    gbc.weighty = 1; // Push button to bottom
	    formPanel.add(Box.createVerticalStrut(10), gbc);
	    
	    // Add Contact Button
	    gbc.gridy++;
	    gbc.weighty = 0;
	    gbc.anchor = GridBagConstraints.CENTER;
	    formPanel.add(this.buttonAddContact, gbc);

	    // Container for table and form to balance sizes
	    JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
	    contentPanel.add(tableScrollPane);
	    contentPanel.add(formPanel);

	    // Main layout
	    panel.add(contentPanel, BorderLayout.CENTER);

	    return panel;
	}
	
	
    public JTextField getSponsorNameField() {
        return this.sponsorNameField;
    }

    public JTextField getSponsorAddressField() {
        return this.sponsorAddressField;
    }

    public JTextField getSponsorVatField() {
        return this.sponsorVatField;
    }

    public JTable getContactsTable() {
        return this.contactsTable;
    }

    public JTextField getContactNameField() {
        return this.contactNameField;
    }

    public JTextField getContactEmailField() {
        return this.contactEmailField;
    }

    public JTextField getContactPhoneField() {
        return this.contactPhoneField;
    }
    
    public JButton getButtonAddContact() {
        return this.buttonAddContact;
    }
}
