package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.ApplicationException;
import util.UnexpectedException;

/**
 * Class that implements some general features for all views.
 */
public abstract class AbstractView {
	
	private JFrame frame; // Frame displayed in the view
	private JPanel mainPanel; // Panel with the contents for each view
	
	// If true a field for changing todays date is displayed.
	private boolean showChangeDate;
	private JButton todaysDateButton;
	
	
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
	
	/**
	 *  Creates and displays a view with the provided name and no
	 *  ability to change todays date.
	 */
	public AbstractView(String viewName) { this(viewName, false); }

	/**
	 * Creates and displays a view.
	 * @param viewName is the text displayed at the frame and window title.
	 * @param showChangeDate indicates if the changeDate option should be displayed.
	 */
	public AbstractView(String viewName, boolean showChangeDate)
	{
		this.showChangeDate = showChangeDate;
		this.frame = new JFrame(viewName); // Creates the frame that will be displayed.
		this.frame.setName(viewName); // Sets the component name.
		
		// On close, the frame is disposed (Other frames still running).
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
<<<<<<< HEAD
		this.initializeAttrib(); // Initializes other possible required attributes.
=======
		this.initialize(); // Inicializes other possible required attributes.
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git
		
		this.createStructureFrame(viewName); // Creates the structure for the frame.
		
		this.frame.setLocationRelativeTo(null); // Centers the frame in the screen.
		this.frame.setVisible(true); // Makes the frame visible.
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
		this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); // Sets the frame dimensions.
		this.frame.setLayout(new BorderLayout(10 ,10)); // Defines the layout for the frame.
		
		JPanel topPanel; // Panel displayed always at the top.
		if (this.showChangeDate)
		{
			topPanel = new JPanel();
	        topPanel.setLayout(new BorderLayout());
	        topPanel.add(createTitlePanel(title), BorderLayout.NORTH);
	        topPanel.add(createTodaysDatePanel(), BorderLayout.CENTER);
	        
		} else {
			topPanel = createTitlePanel(title);
			todaysDateButton = null;
		}

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
        
        
		return;
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
	
	/**
	 * Creates a panel with a date input and a button to change todays date.
	 * @return a JPanel with the ability to change todays date.
	 */
	private final JPanel createTodaysDatePanel()
	{
		// Variables to configure the panel
		Color todaysDatePanelBackground = new Color(0x4A90E2);
		int todaysDatePanelHeight = 40;
		
		// Variables to configure the button
		String todaysDateButtonText = "Set today's date";
		
		// Panel for the date input and the button.
        JPanel todaysDatePanel = new JPanel();
        todaysDatePanel.setBackground(todaysDatePanelBackground);
        todaysDatePanel.setPreferredSize(new Dimension(frame.getWidth(), todaysDatePanelHeight));
        
        // Campo de texto para la fecha
        JTextField dateField = new JTextField(10);
        dateField.setHorizontalAlignment(JTextField.CENTER);
        
        // Botón para establecer la fecha de hoy
        this.todaysDateButton = new JButton(todaysDateButtonText);
        
        // Agregar componentes al panel extra
        todaysDatePanel.add(dateField);
        todaysDatePanel.add(todaysDateButton);
        
        return todaysDatePanel;
	}
	
	/* ================================================================================
     * 
     *     OTHER METHODS.
     * 
     */
	
	 /**
	  * OPTIONAL. Use to initialize class attributes before frame configuration.
	  */
<<<<<<< HEAD
	 protected void initializeAttrib() {}
=======
	 protected void initialize() {}
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git
	 
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
	  * Creates a button in the main frame
	  * @param text to be displayed inside the button
	  * @param action to be performed by the button on-click
	  */
	 protected void addButtonToMain(String text, Runnable action) {
		 JButton button = new JButton(text);
		 button.addActionListener(e -> action.run());
		 button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
		 button.setAlignmentX(Component.CENTER_ALIGNMENT);
		 // Wrap the button in a JPanel with FlowLayout to prevent stretching
	     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the button
	     buttonPanel.add(button);
		 mainPanel.add(buttonPanel);
		 //mainPanel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
	 }
	 
	 /**
	  * Reset the panel that is inserted in the center and varies from view to view.
	  */
	 protected void resetMainPanel()
	 {
		 mainPanel.removeAll();
		 mainPanel.repaint();
		 mainPanel.revalidate();
	 }
	 
	// UNCOMMENT if access to the frame is required from outside this abstract class.
	//protected final JFrame getFrame() { return this.frame; }
	 
	 public final boolean inViewDateChanger() { return this.showChangeDate; }
	 
	 public final JButton getTodaysDateButton()
	 {
		 if (!this.showChangeDate)
			 throw new ApplicationException("ERROR. The view does not have an todaysDateButton. Check view's constructor parms.");
		 
		 if (this.todaysDateButton == null)
			 throw new UnexpectedException("ERROR. Incorrect initialization for todaysDateButton.");
		 
		 return this.todaysDateButton;
	 }
}

