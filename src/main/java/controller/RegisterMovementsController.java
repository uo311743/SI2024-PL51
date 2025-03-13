package controller;

import model.ActivitiesModel;
import model.MovementsModel;
import util.SemanticValidations;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterMovementsView;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
import DTOs.MovementsDTO;

public class RegisterMovementsController {
	private static Object DEFAULT_VALUE_COMBOBOX = "--------";
	
	protected ActivitiesModel activitiesModel;
	protected MovementsModel movementsModel;
	private RegisterMovementsView view;
	
    private String lastSelectedActivity;
	
	public RegisterMovementsController(ActivitiesModel am, MovementsModel mm, RegisterMovementsView view) {
		this.activitiesModel = am;
		this.movementsModel = mm;
		
		this.view = view;
		
		this.initController();
		this.initView();
	}
	
	public void initController() {
		this.view.getType().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
		        SwingUtil.exceptionWrapper(() -> restartView());
		    }
		});
		
		this.view.getStatus().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
		        SwingUtil.exceptionWrapper(() -> restartView());
		    }
		});
		
		this.view.getActivitiesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> activitySelection());
			}
		});
		
		this.view.getMovementsTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
		
		this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {view.disposeView();});}
		});
		
		this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {clear();});}
		});
		
		this.view.getButtonLowRight().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> showSubmitDialog());
			}
		});
		
		this.view.getAmountTextField().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> updateDetail());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	
    	this.view.getDateTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	this.view.getConceptTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
	}
	
	public void initView() {
		this.restartView();
		
		this.loadTypes();
		this.view.getType().insertItemAt(DEFAULT_VALUE_COMBOBOX, 0);
		this.view.getType().setSelectedIndex(0);
		this.loadStatus();
		this.view.getStatus().insertItemAt(DEFAULT_VALUE_COMBOBOX, 0);
		this.view.getStatus().setSelectedIndex(0);
		
		view.setVisible();
	}
	
	public void restartView()
    {
		this.view.getButtonLowRight().setEnabled(false);
		
		this.getActivities();
		
		this.view.getActivitiesTable().clearSelection();
		this.view.getMovementsTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getMovementsTable().getModel();
		model.setRowCount(0); // Clears the table
    	
    	// Reset amount field with placeholder effect
    	this.view.getDateTextField().setText("");
    	this.view.getAmountTextField().setText("");
    	this.view.getConceptTextField().setText("");
    	
    	this.setInputsEnabled(false);
    }
	
	public void clear() {
		restartView();
		
		this.view.getType().setSelectedIndex(0);
		this.view.getStatus().setSelectedIndex(0);
	}
	
	private void getActivities()
    {
    	List<ActivitiesDTO> activities = this.activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"id", "name", "status", "dateStart", "dateEnd"});
		this.view.getActivitiesTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivitiesTable());
    }
	
	public void loadTypes() {
        List<Object[]> typesList = this.movementsModel.getUniqueTypes();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(typesList);
        view.getType().setModel(lmodel);
    }
	
	public void loadStatus() {
        List<Object[]> statusList = this.movementsModel.getUniqueStatus();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(statusList);
        view.getStatus().setModel(lmodel);
    }
	
	private void getMovements()
	{
		int row = this.view.getActivitiesTable().getSelectedRow(); 
        
        String idActivity = "";
        
        if (row >= 0)
        {
        	idActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(row, 0);
        }
        
        if (!this.view.getStatus().getSelectedItem().toString().equals("paid")) { this.view.getMovementsTable().removeAll(); return; };
    	List<MovementsDTO> movements = this.movementsModel.getEstimatedMovementsbyTypeAndActivity(this.view.getType().getSelectedItem().toString(), idActivity);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(movements, new String[] {"id", "idActivity", "type", "concept", "amount", "date", "status"});
		this.view.getMovementsTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getMovementsTable());
	}
	
	private void activitySelection()
	{
		if (this.view.getStatus().getSelectedItem().toString().equals("paid"))
		{  
			getMovements(); 
			updateDetail();
		} else {
			updateDetail();
		}
	}
	
	private void setInputsEnabled(boolean enabled)
    {
    	this.view.getDateTextField().setEnabled(enabled);
    	this.view.getAmountTextField().setEnabled(enabled);
    	this.view.getConceptTextField().setEnabled(enabled);
    }
	
	public void updateDetail()
	{	
		// ------------------------------------------------------------
		// If a movement is selected in the table do:
		
		this.lastSelectedActivity = SwingUtil.getSelectedKey(this.view.getActivitiesTable());
		
		if("".equals(this.lastSelectedActivity))
		{
			restartView(); 
		}
		else
		{
			this.setInputsEnabled(true);
		}
				
		// ------------------------------------------------------------
		// Check JTextField inputs:
		boolean valid = true;
		
		// Validate amount
		String amount = this.view.getAmountTextField().getText();
		if(!SyntacticValidations.isDecimal(amount))
		{
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
		
		// Validate date
		String paymentDate = this.view.getDateTextField().getText();
		if(!SyntacticValidations.isDate(paymentDate))
		{
			this.view.getDateTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getDateTextField().setForeground(Color.BLACK); }
		
		// Validate concept
		String concept = this.view.getConceptTextField().getText();
		String status = this.view.getStatus().getSelectedItem().toString();
		if(status.equals("paid") && !SyntacticValidations.isNotEmpty(concept)) { valid = false; }
		
		// Activate/Deactivate the submit button
		this.view.getButtonLowRight().setEnabled(valid);
	}
	
	// ================================================================================

    private void showSubmitDialog()
    {
    	// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
        int activityRow = this.view.getActivitiesTable().getSelectedRow(); 
        
        String activity = "";
        String idActivity = "";
        
        if (activityRow >= 0)
        {
        	idActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(activityRow, 0);
        	activity = (String) this.view.getActivitiesTable().getModel().getValueAt(activityRow, 1);
        }
        
        int movementRow = this.view.getMovementsTable().getSelectedRow(); 
        
        String movement = "";
        String idMovement = "";
        
        if (movementRow >= 0)
        {
        	idMovement = (String) this.view.getMovementsTable().getModel().getValueAt(movementRow, 0);
        	movement = (String) this.view.getMovementsTable().getModel().getValueAt(movementRow, 2);
        }

        String amount = this.view.getAmountTextField().getText();
        String date = this.view.getDateTextField().getText();
        String concept = this.view.getConceptTextField().getText();

        String message = "<html><body>"
                + "<p>You are about to add a movement for the activity: <b>" + activity + "</b>.</p>"
                + "<p><b>Details:</b></p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Type:</b></td><td style='padding: 2px 5px;'>" + movement + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + amount + " euros</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date:</b></td><td style='padding: 2px 5px;'>" + date + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Concept:</b></td><td style='padding: 2px 5px;'>" + concept + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this movement?</i></p>"
                + "</body></html>";
        
        Object[] options = {"Yes", "No"};
        
        int response = JOptionPane.showOptionDialog(null,
                "Are you sure you want to continue?", 		// The message
                "Confirmation of Movement Registration",    // The title
                JOptionPane.DEFAULT_OPTION,          		// The option type
                JOptionPane.QUESTION_MESSAGE,        		// The message type
                null,                               		// Icon (null means default question icon)
                options,                            		// Custom buttons
                options[0]); 
        
        if (response == 1) return;

        String status = this.view.getStatus().getSelectedItem().toString();
        
        try {
        	if (status.equals("paid")) { SemanticValidations.validateDateInFuture(date, true, "Payment cannot be made in the future"); }
        	this.movementsModel.registerMovement(idActivity, this.view.getType().getSelectedItem().toString(), concept, amount, date, this.view.getStatus().getSelectedItem().toString());
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        this.clear();
    }
}