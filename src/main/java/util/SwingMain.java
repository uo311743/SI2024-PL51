package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import controller.RegisterSponsorshipController;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import model.SponsorshipPaymentsModel;
import view.RegisterSponsorshipView;
import controller.RegisterIncomesExpensesController;
import model.InvoicesModel;
import model.MovementsModel;
import controller.RegisterPaymentController;
import view.RegisterPaymentView;
import controller.CloseActivityController;
import controller.ConsultStatusActivityController;
import controller.IncomesExpensesReportController;
import view.IncomesExpensesReportView;
import controller.GenerateInvoicesController;
import model.GBMembersModel;
import view.CloseActivityView;
import view.ConsultStatusActivityView;
import view.GenerateInvoicesView;
import model.ActivitiesModel;
import view.RegisterIncomesExpensesView;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * Entry point for the app that includes the buttons to the execution of views
 * and DB inicialization.
 */
public class SwingMain {
	
	private static Date today;

	private JFrame frame;
	
	private static final String APP_NAME = "Sponsor Management";
	private static final String DISCLAIMER = "<html>"
			+ "<p style='text-align:right;'>"
            + "This application was developed as part of a university project.<br>"
            + "Created by Team SI2024-PL51.<br>"
            + "Information Systems - University of Oviedo (2024-2025)."
            + "</p></html>";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR codigo autogenerado
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR codigo autogenerado
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		SwingMain.today = new Date();
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Database db=new Database();
		
	    frame = new JFrame();
	    frame.setTitle(APP_NAME);
	    frame.setBounds(0, 0, 300, 300);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

	    // Main panel with BorderLayout
	    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins
	    frame.getContentPane().add(mainPanel);

	    // Pannel at the top with title and change date field
	    JPanel topPanel = new JPanel(new BorderLayout());
	    
	    // Creates the title
        JLabel titleLabel = new JLabel(APP_NAME, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Creates the field for changing the date
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JTextField textField = new JTextField(Util.dateToIsoString(SwingMain.today), 15);
        JButton button = new JButton("Change date of today");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(SyntacticValidations.isDate(textField.getText())){
            		Date newDate = Util.isoStringToDate(textField.getText());
                    SwingMain.today = newDate;
                    JOptionPane.showMessageDialog(null, "Date updated to: " + Util.dateToIsoString(newDate));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(textField);
        inputPanel.add(button);
        
        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

	    // Button panel with vertical BoxLayout
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	    /* --------------------------------------------------------------------------------
	     * 
	     *     START ADD BUTTONS
	     */	    
	    // US 29124
	    addButtonToMain(buttonPanel, "Register Sponshorship", () -> {
	    	new RegisterSponsorshipController(new SponsorOrganizationsModel(), new SponsorshipAgreementsModel(), new SponsorContactsModel(), new GBMembersModel(), new ActivitiesModel(), new RegisterSponsorshipView());
	    });
	    
	    // US 29125
	    addButtonToMain(buttonPanel, "Generate Invoices", () -> {
	    	new GenerateInvoicesController(new SponsorshipAgreementsModel(), new InvoicesModel(), new ActivitiesModel(), new SponsorOrganizationsModel(), new GenerateInvoicesView());
	    });
	    
	 	// US 29126
        addButtonToMain(buttonPanel, "Register Payment", () -> {
        	new RegisterPaymentController(new SponsorshipAgreementsModel(), new SponsorshipPaymentsModel(), new ActivitiesModel(), new InvoicesModel(), new RegisterPaymentView());
        });
        
        // US 29127
        addButtonToMain(buttonPanel, "Register Income/Expense", () -> {
        	new RegisterIncomesExpensesController(new ActivitiesModel(), new MovementsModel(), new RegisterIncomesExpensesView());
	    });
        
        // US 29128
	    addButtonToMain(buttonPanel, "Consult Status Activity", () -> {
	    	new ConsultStatusActivityController(new ActivitiesModel(), new SponsorOrganizationsModel(), new SponsorshipAgreementsModel(), new MovementsModel(), new ConsultStatusActivityView());
	    });

	    // US 29129
	    addButtonToMain(buttonPanel, "Incomes/Expenses Report", () -> {
	    	new IncomesExpensesReportController(new MovementsModel(), new ActivitiesModel(), new SponsorshipAgreementsModel(), new IncomesExpensesReportView());
	    });
	    
	    // FIXME
	    addButtonToMain(buttonPanel, "Close Activity", () -> {
	    	new CloseActivityController(new ActivitiesModel(), new SponsorshipAgreementsModel(), new MovementsModel(), new CloseActivityView());
	    });
	    
	    
	    addButtonToMain(buttonPanel, "Initialize Empty Database", () -> {
	        db.createDatabase(false);
	    });
	    
	    addButtonToMain(buttonPanel, "Load data for testing", () -> {
			db.createDatabase(false);
			db.loadDatabase();
	    });

	    
	    /* 
	     *     END ADD BUTTONS
	     * 
	     * -------------------------------------------------------------------------------- */

	    mainPanel.add(buttonPanel, BorderLayout.CENTER);

	    // Disclaimer at the bottom
	    JLabel disclaimerLabel = new JLabel(DISCLAIMER);
	    disclaimerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
	    disclaimerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

	    mainPanel.add(disclaimerLabel, BorderLayout.SOUTH);
	    
	    // Adjust frame size to fit content
	    frame.pack();
	    
	    // Set minimum size to prevent shrinking below initial dimensions
	    frame.setMinimumSize(frame.getSize());
	}

	/**
	 * Creates a button in the main frame
	 * @param text to be displied inside the button
	 * @param action to be performed by the button on-click
	 */
	private void addButtonToMain(JPanel panel, String text, Runnable action) {
	    JButton button = new JButton(text);
	    button.addActionListener(e -> action.run());
	    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(button);
	    panel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
	}


	public JFrame getFrame() { return this.frame; }
	
	public static Date getTodayDate() {  return SwingMain.today; }
	
}