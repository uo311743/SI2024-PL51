package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Class that implements some general features for all views.
 */
public abstract class AbstractView {
	
	private JFrame frame; // Frame displayed in the view
	private JPanel mainPanel; // Panel with the contents for each view

	
	/* ================================================================================
     * 
     *     CONSTRUCTORS
     * 
     */
	
	/**
	 *  Creates and displays a view with the default name.
	 */
	public AbstractView() { this("Default View Name"); }

	/**
	 * Creates and displays a view with the provided name.
	 * @param viewName is the text displayed at the frame and window title.
	 */
	public AbstractView(String viewName)
	{
		
		this.frame = new JFrame(viewName); // Creates the frame that will be displayed.
		this.frame.setName(viewName); // Sets the component name.
		
		// On close, the frame is disposed (Other frames still running).
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.initializeAtrib(); // Inicializes other possible required attributes.
		
		this.createStructureFrame(viewName); // Creates the structure for the frame.
		
		this.frame.setLocationRelativeTo(null); // Centers the frame in the screen.
		this.frame.setVisible(true); // Makes the frame visible.
	 }
	
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	/**
	 * Creates the frame used for every view.
	 * @param title is the text displayed at title.
	 */
	private final void createStructureFrame(String title)
	{
		this.frame.setSize(400, 300); // Sets the frame dimensions.
		this.frame.setLayout(new BorderLayout(10 ,10)); // Defines the layout for the frame.
		
		// Creates a title at the top
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(0x1E3A5F));
		titlePanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
        JLabel labelTitulo = new JLabel(title, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setForeground(Color.WHITE);
        titlePanel.add(labelTitulo);
        frame.add(titlePanel, BorderLayout.NORTH);
        
        /* --------------------------------------------------------------------------------
	     * 
	     *     HERE WILL THE CODE IN configMainPanel() BE INSERTED.
	     */
        
        mainPanel = new JPanel(); // Creates the main panel.
        this.configMainPanel(); // Adds contents to the main panel.
        frame.add(mainPanel, BorderLayout.CENTER); // Adds the mainPanel to the frame.
        
        /* 
	     *     END ADD BUTTONS
	     * 
	     * -------------------------------------------------------------------------------- */
        
		return;
	}
	
	 /**
	  * OPTIONAL. Use to initialize class atributes before frame configuration.
	  */
	 protected void initializeAtrib() {}
	 
	 /**
	  * Configures the main panel that can be accessed with getMainPanel().
	  * PLEASE DO NOT TRY TO MODIFY THE FRAME
	  */
	 protected abstract void configMainPanel();
	 
	 /**
	  * Obtain the panel that is inserted in the center and varies from view to view.
	  * @return a panel of the class JPanel.
	  */
	 protected final JPanel getMainPanel() { return this.mainPanel; }
	 
	// UNCOMMENT if access to the frame is required from outside this abstract class.
	//protected final JFrame getFrame() { return this.frame; }
}

