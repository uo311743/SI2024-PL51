package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;

public class ModifySponsorContactView extends AbstractView {

    private JTable contactsTable;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    private JComboBox<Object> sponsorComboBox;

    public ModifySponsorContactView() {
        super("Modify Sponsor Contact");
    }

    @Override
    protected void initialize() {
        this.contactsTable = new JTable(new DefaultTableModel());
        this.nameTextField = new JTextField(20);
        this.emailTextField = new JTextField(20);
        this.phoneTextField = new JTextField(20);
        this.sponsorComboBox = new JComboBox<>();

        Dimension textFieldSize = new Dimension(200, 30);
        nameTextField.setPreferredSize(textFieldSize);
        emailTextField.setPreferredSize(textFieldSize);
        phoneTextField.setPreferredSize(textFieldSize);
        sponsorComboBox.setPreferredSize(textFieldSize);

        super.createButtonLowLeft("Cancel");
        super.createButtonLowMiddle("Remove Contact");
        super.createButtonLowRight("Modify Sponsor Contact");
    }

    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow 30][grow 70]", "[grow]"));

        // Left Panel - Contacts Table and ComboBox
        JPanel leftPanel = new JPanel(new MigLayout("wrap", "[grow]", "[][][grow]"));
        JScrollPane contactsTableScrollPane = new JScrollPane(contactsTable);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Contacts"));
        leftPanel.add(new JLabel("Select Sponsor:"), "wrap");
        leftPanel.add(sponsorComboBox, "wrap");
        leftPanel.add(contactsTableScrollPane, "grow");
        getMainPanel().add(leftPanel, "cell 0 0, grow");

        // Right Panel - Contact Details
        JPanel rightPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Sponsor Contact Details"));
        rightPanel.add(new JLabel("Name:"));
        rightPanel.add(nameTextField, "wrap");
        rightPanel.add(new JLabel("Email:"));
        rightPanel.add(emailTextField, "wrap");
        rightPanel.add(new JLabel("Phone:"));
        rightPanel.add(phoneTextField, "wrap");
        getMainPanel().add(rightPanel, "cell 1 0, grow");
    }

    // Getters
    public JTable getContactsTable() {
        return contactsTable;
    }
    
    public JTextField getNameTextField() { 
        return nameTextField; 

    public JComboBox<Object> getSponsorComboBox() {
        return sponsorComboBox;
    }

    public JLabel getIdSOLabel() {
        return idSOLabel;
    }

    public JTextField getEmailTextField() {
        return emailTextField;
    }

    public JTextField getPhoneTextField() {
        return phoneTextField;
    }
}
