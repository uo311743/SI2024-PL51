package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.ExampleView;
import view.US125View;
import view.US129View;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * Entry point for the app that includes the buttons to the execution of views
 * and DB inicialization.
 */
public class SwingMain {

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
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    frame = new JFrame();
	    frame.setTitle(APP_NAME);
	    frame.setBounds(0, 0, 300, 300);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

	    // Main panel with BorderLayout
	    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins
	    frame.getContentPane().add(mainPanel);

	    // Title at the top
	    JLabel titleLabel = new JLabel(APP_NAME, SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    mainPanel.add(titleLabel, BorderLayout.NORTH);

	    // Button panel with vertical BoxLayout
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	    /* --------------------------------------------------------------------------------
	     * 
	     *     START ADD BUTTONS
	     */

	    // FIXME Delete this method and create new ones.
	    addButtonToMain(buttonPanel, "Run Example", () -> {
	    	new ExampleView().setVisible();
	    });
	    
	    addButtonToMain(buttonPanel, "Run US125", () -> {
	    	new US125View().setVisible();
	    });
	    
	    addButtonToMain(buttonPanel, "Run US129", () -> {
	    	new US129View().setVisible();
	    });
	    
	    addButtonToMain(buttonPanel, "Initialize Empty Database", () -> {
	        Database db = new Database();
	        db.createDatabase(false);
	    });
	    
	    addButtonToMain(buttonPanel, "Load data for testing", () -> {
	    	Database db=new Database();
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
	
}