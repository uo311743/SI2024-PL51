package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import util.UnexpectedException;

/**
 * Class that implements some general features for all views.
 */
public abstract class AbstractView {
	
	private JFrame frame; // Frame displayed in the view
	private JPanel mainPanel; // Panel with the contents for each view
	
	// Buttons for low part of the view
	JButton btnLowLeft, btnLowMiddle, btnLowRight;
	
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private static final String DEFAULT_TITLE = "Default View Name";

	
	/* ================================================================================
     * 
     *     CONSTRUCTORS
     * 
     */
	
	/**
	 *  Creates and displays a view with the default name and no
	 *  ability to change todays date.
	 */
	public AbstractView() { this(DEFAULT_TITLE); }
	
	public AbstractView(String viewName) { this(viewName, DEFAULT_WIDTH, DEFAULT_HEIGHT); }

	/**
	 * Creates and displays a view.
	 * @param viewName is the text displayed at the frame and window title.
	 * @param showChangeDate indicates if the changeDate option should be displayed.
	 */
	public AbstractView(String viewName, int width, int height)
	{
		// Some variables require initialization
		this.btnLowLeft = null;
		this.btnLowMiddle = null;
		this.btnLowRight = null;
		
		
		this.frame = new JFrame(viewName); // Creates the frame that will be displayed.
		this.frame.setName(viewName); // Sets the component name.
		
		// On close, the frame is disposed (Other frames still running).
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.frame.setSize(width, height); // Sets the frame dimensions.
		
		this.initialize(); // Inicializes other possible required attributes.
		
		this.createStructureFrame(viewName); // Creates the structure for the frame.
	 }
	
	
	
	
	/* ================================================================================
     * 
     *     PRIVATE METHODS.
     * 
     *  Warning. Modification affectas all classes extending this one.
     * 
     */
	
	/**
	 * Creates the frame used for every view.
	 * @param title is the text displayed at title.
	 */
	private final void createStructureFrame(String title)
	{
		this.frame.setLayout(new BorderLayout(10 ,10)); // Defines the layout for the frame.
		
		JPanel topPanel = createTitlePanel(title);

        // Agregar el panel contenedor al frame en la parte superior
        frame.add(topPanel, BorderLayout.NORTH);

        
        /* --------------------------------------------------------------------------------
	     * 
	     *     HERE WILL THE CODE IN configMainPanel() BE INSERTED. DO NOT MODIFY
	     */
        
        mainPanel = new JPanel(); // Creates the main panel.
        this.configMainPanel(); // Adds contents to the main panel.
        frame.add(mainPanel, BorderLayout.CENTER); // Adds the mainPanel to the frame.
        
        /* 
	     *     END CONFIGURATION
	     * 
	     * -------------------------------------------------------------------------------- */
        
        // If any button is initialized it creates the low panel with it.
        if(this.btnLowLeft != null || this.btnLowMiddle != null || this.btnLowRight != null)
        	frame.add(createLowButtonsPanel(), BorderLayout.SOUTH);
        
		return;
	}
	
	protected final void setFrameTitle(String title) {
		JPanel topPanel = createTitlePanel(title);

        // Agregar el panel contenedor al frame en la parte superior
        frame.add(topPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Creates a panel with a title.
	 * @param title is the text to be displayed.
	 * @return a JPanel with the title.
	 */
	private final JPanel createTitlePanel(String title)
	{
		// Variables to configure the panel
		Color titlePanelBackground = new Color(0x1E3A5F);
		int titlePanelHeight = 30;
		
		// Variables to configure the label
		String titleLabelFontName = "Arial";
		int titleLabelFontStyle = Font.BOLD;
		int titleLabelFontSize = 16;
		Color titleLabelForeground = Color.WHITE;
		
		// Label creation. AVOID MODIFICATION
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabelFontName, titleLabelFontStyle, titleLabelFontSize));
        titleLabel.setForeground(titleLabelForeground);
        
		// Pannel creation. AVOID MODIFICATION
		JPanel titlePanel = new JPanel();
        titlePanel.setBackground(titlePanelBackground);
        titlePanel.setPreferredSize(new Dimension(frame.getWidth(), titlePanelHeight));
        titlePanel.add(titleLabel);
        
        return titlePanel;
	}
	
	private final JPanel createLowButtonsPanel() {
	    Color lowButtonsPanelBackground = new Color(0xD3D3D3);
	    int lowButtonsPanelHeight = 40;
	    int panelMargin = 5;

	    // Create the panel with GridBagLayout
	    JPanel lowButtonsPanel = new JPanel(new GridBagLayout());
	    lowButtonsPanel.setBackground(lowButtonsPanelBackground);
	    lowButtonsPanel.setPreferredSize(new Dimension(frame.getWidth(), lowButtonsPanelHeight));
	    lowButtonsPanel.setBorder(BorderFactory.createEmptyBorder(panelMargin, panelMargin, panelMargin, panelMargin));

	    // GridBagLayout configuration
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridy = 0;
	    gbc.weighty = 1.0; // Prevent buttons from collapsing vertically
	    gbc.insets = new Insets(0, 5, 0, 5); // Spacing between buttons

	    // Spacers to maintain fixed button positions
	    gbc.weightx = 1.0;
	    
	    // Left button or empty space
	    gbc.gridx = 0;
	    if (this.btnLowLeft != null) {
	        lowButtonsPanel.add(this.btnLowLeft, gbc);
	    } else {
	        lowButtonsPanel.add(Box.createHorizontalGlue(), gbc);
	    }

	    // Middle button or empty space
	    gbc.gridx = 1;
	    if (this.btnLowMiddle != null) {
	        lowButtonsPanel.add(this.btnLowMiddle, gbc);
	    } else {
	        lowButtonsPanel.add(Box.createHorizontalGlue(), gbc);
	    }

	    // Right button or empty space
	    gbc.gridx = 2;
	    if (this.btnLowRight != null) {
	        lowButtonsPanel.add(this.btnLowRight, gbc);
	    } else {
	        lowButtonsPanel.add(Box.createHorizontalGlue(), gbc);
	    }

	    return lowButtonsPanel;
	}

	
	
	/* ================================================================================
     * 
     *     CONFIGURATION METHODS.
     * 
     */
	
	 /**
	  * OPTIONAL. Use to initialize class atributes before frame configuration.
	  * This includes the calls to create lowPannel buttons.
	  */
	 protected void initialize() {}
	 
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
	 
	 /**
	  * Sets the frame dimensions.
	  * @param width of the frame in pixels
	  * @param height of the frame in pixels
	  */
	 protected void setDimensions(int width, int height)
	 {
		 this.frame.setSize(width, height);
	 }
	 
	 /**
	  * Reset the panel that is inserted in the center of the frame, leaving it blank.
	  * Used for changing its distribution.
	  */
	 public final void resetMainPanel()
	 {
		 mainPanel.removeAll();
		 mainPanel.repaint();
		 mainPanel.revalidate();
	 }
	 
	 /**
	  * Makes the view visible in the center of the screen.
	  */
	 public final void setVisible()
	 {
		 this.frame.setLocationRelativeTo(null); // Centers the frame in the screen.
		 this.frame.setVisible(true); // Makes the frame visible. 
	 }
	 
	 /**
	  * Closes this view. It does not end the application.
	  * Override to add extra functionalities.
	  */
	 public void disposeView()
	 {
		 this.frame.dispose();
	 }
	 
	 
	 // UNCOMMENT if access to the frame is required from outside this abstract class.
	 public final JFrame getFrame() { return this.frame; }
	 
	 
	 
	/* ================================================================================
     * 
     *    LOW PANEL BUTTON METHODS.
     * 
     */
	 
	 /**
	  * Checks if the low left button exists.
	  * @return true if low left button exists.
	  */
	 protected final boolean existsButtonLowLeft() { return this.btnLowLeft != null; };
	 
	 /**
	  * Checks if the low middle button exists.
	  * @return true if low middle button exists.
	  */
	 protected final boolean existsButtonLowMiddle() { return this.btnLowMiddle != null; };
	 
	 /**
	  * Checks if the low right button exists.
	  * @return true if low right button exists.
	  */
	 protected final boolean existsButtonLowRight() { return this.btnLowRight != null; };
	 
	 /**
	  * Creates low left button with given text.
	  * @param text to be displyed inside the button.
	  */
	 protected final void createButtonLowLeft(String text) { this.btnLowLeft = new JButton(text); };
	 
	 /**
	  * Creates low middle button with given text.
	  * @param text to be displyed inside the button.
	  */
	 protected final void createButtonLowMiddle(String text) { this.btnLowMiddle = new JButton(text); };
	 
	 /**
	  * Creates low right button with given text.
	  * @param text to be displyed inside the button.
	  */
	 protected final void createButtonLowRight(String text) { this.btnLowRight = new JButton(text); };
	 
	 /**
	  * Returns the button on the low left corner of the frame.
	  * @return a JButton placed in the low left of the frame
	  */
	 public final JButton getButtonLowLeft()
	 {
		 if (this.btnLowLeft == null)
			 throw new UnexpectedException("ERROR. Button lowLeft does not exist.");
		 return this.btnLowLeft;
	 }
	 
	 /**
	  * Returns the button on the low middel of the frame.
	  * @return a JButton placed in the low middel of the frame
	  */
	 public final JButton getButtonLowMiddle()
	 {
		 if (this.btnLowMiddle == null)
			 throw new UnexpectedException("ERROR. Button lowMiddle does not exist.");
		 return this.btnLowMiddle;
	 }
	 
	 /**
	  * Returns the button on the low right corner of the frame.
	  * @return a JButton placed in the low right of the frame
	  */
	 public final JButton getButtonLowRight()
	 {
		 if (this.btnLowRight == null)
			 throw new UnexpectedException("ERROR. Button lowRight does not exist.");
		 return this.btnLowRight;
	 }
}

