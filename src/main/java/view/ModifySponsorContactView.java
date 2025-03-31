package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;

public class ModifySponsorContactView extends AbstractView {
    
    private JTable contactsTable;
    private JLabel idSOLabel;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    
    public ModifySponsorContactView() {
        super("Modify Sponsor Contact");
    }
    
    @Override
    protected void initialize() {
        this.contactsTable = new JTable(new DefaultTableModel());
        this.idSOLabel = new JLabel("Sponsor Organization ID: ");
        this.nameTextField = new JTextField(20);
        this.emailTextField = new JTextField(20);
        this.phoneTextField = new JTextField(20);
        
        Dimension textFieldSize = new Dimension(200, 30);
        nameTextField.setPreferredSize(textFieldSize);
        emailTextField.setPreferredSize(textFieldSize);
        phoneTextField.setPreferredSize(textFieldSize);
        
        super.createButtonLowLeft("Cancel");
        super.createButtonLowRight("Modify Sponsor Contact");
    }
    
    @Override
    protected void configMainPanel() {
        getMainPanel().setLayout(new MigLayout("", "[grow 30][grow 70]", "[grow]"));
        
        // Contacts Table Panel
        JPanel leftPanel = new JPanel(new MigLayout("wrap", "[grow]", "[grow][]"));
        JScrollPane contactsTableScrollPane = new JScrollPane(contactsTable);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Contacts"));
        leftPanel.add(contactsTableScrollPane, "grow");
        getMainPanel().add(leftPanel, "cell 0 0, grow");
        
        // Contact Details Panel
        JPanel rightPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", ""));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Sponsor Contact Details"));
        rightPanel.add(idSOLabel, "wrap");
        rightPanel.add(new JLabel("Name:"));
        rightPanel.add(nameTextField, "wrap");
        rightPanel.add(new JLabel("Email:"));
        rightPanel.add(emailTextField, "wrap");
        rightPanel.add(new JLabel("Phone:"));
        rightPanel.add(phoneTextField, "wrap");
        getMainPanel().add(rightPanel, "cell 1 0, grow");
    }
    
    // Getters and Setters
    public JTable getContactsTable() {
        return contactsTable;
    }
    
    public JLabel getIdSOLabel() {
    	return idSOLabel;
    }
    
    public JTextField getNameTextField() { 
        return nameTextField; 
    }
    
    public JTextField getEmailTextField() { 
        return emailTextField; 
    }
    
    public JTextField getPhoneTextField() { 
        return phoneTextField; 
    }
}
