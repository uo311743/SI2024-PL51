package controller;

import model.ActivitiesModel;
import model.MovementsModel;
import util.ModelManager;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterMovementsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
import DTOs.IncomesExpensesDTO;
import DTOs.MovementsDTO;

public class RegisterMovementsController {
	private static Object DEFAULT_VALUE_COMBOBOX = "Select...";
	
	protected ActivitiesModel activitiesModel;
	protected MovementsModel movementsModel;
	private RegisterMovementsView view;
	
    private String lastSelectedActivity;
    private String lastSelectedIncomeExpense;
    
    private List<String> activitiesId;
    private List<String> incomeExpenseId;
    
    private boolean submit;
	
	public RegisterMovementsController(RegisterMovementsView view) {
		this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
		this.movementsModel = ModelManager.getInstance().getMovementsModel();
		
		this.view = view;
		
		activitiesId = new ArrayList<String>();
		incomeExpenseId = new ArrayList<String>();
		
		this.submit = false;
		
		this.initController();
		this.initView();
	}
	
	public void initController() {
		this.view.getActivitiesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> activitySelection());
			}
		});
		
		this.view.getIncomesExpensesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> incomeExpenseSelection());
			}
		});
		
		this.view.getMovementsTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}
		});
		
		this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {view.disposeView();});}
		});
		
		this.view.getButtonLowMiddle().addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) { SwingUtil.exceptionWrapper( () -> {restartView();});}
		});
		
		this.view.getBtnIncomesExpenses().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> submitIncomeExpense());
			}
		});
		
		this.view.getBtnMovements().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> submitMovement());
			}
		});
		
		this.view.getAmountTextField1().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> updateDetail1());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail1());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	
    	this.view.getDateTextField1().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail1());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail1());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	this.view.getConceptTextField1().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail1());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail1());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	this.view.getAmountTextField2().getDocument().addDocumentListener(new DocumentListener() {
    		@Override
			public void insertUpdate(DocumentEvent e) {
    			SwingUtil.exceptionWrapper(() -> updateDetail2());
    		}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	
    	this.view.getDateTextField2().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
    	
    	this.view.getConceptTextField2().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail2());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
    	});
	}
	
	public void initView() {
		this.loadTypes();
		this.view.getType().insertItemAt(DEFAULT_VALUE_COMBOBOX, 0);
		this.view.getType().setSelectedIndex(0);
		this.view.getStatus().insertItemAt(DEFAULT_VALUE_COMBOBOX, 0);
		this.view.getStatus().setSelectedIndex(0);
		
		this.restartView();
		view.setVisible();
	}
	
	public void clearFields() {
		// Reset amount field with placeholder effect
    	this.view.getDateTextField1().setText("");
    	this.view.getAmountTextField1().setText("");
    	this.view.getConceptTextField1().setText("");
    	this.view.getDateTextField2().setText("");
    	this.view.getAmountTextField2().setText("");
    	this.view.getConceptTextField2().setText("");
    	
    	this.view.getCompensationCheckBox().setSelected(false);
		this.view.getType().setSelectedIndex(0);
		this.view.getStatus().setSelectedIndex(0);
	}
	
	public void restartView()
    {
		this.getActivities();
		
		this.view.getActivitiesTable().clearSelection();
		this.view.getMovementsTable().clearSelection();
		DefaultTableModel model = (DefaultTableModel) this.view.getMovementsTable().getModel();
		model.setRowCount(0); // Clears the table
		this.view.getIncomesExpensesTable().clearSelection();
		DefaultTableModel imodel = (DefaultTableModel) this.view.getIncomesExpensesTable().getModel();
		imodel.setRowCount(0); // Clears the table
    	
    	this.clearFields();
    	
    	// Reset summary panel
    	this.view.getEstimatedIncomesLabel().setText("0.0");
    	this.view.getPaidIncomesLabel().setText("0.0");
    	this.view.getEstimatedExpensesLabel().setText("0.0");
    	this.view.getPaidExpensesLabel().setText("0.0");
    	this.view.getRemainingBalanceLabel().setText("0.0");
    	
    	this.setInputsEnabled1(false);
    	this.setInputsEnabled2(false);
    }
	
	public void updateView() {
		this.setInputsEnabled1(false);
    	this.setInputsEnabled2(false);
		this.getIncomesExpenses();
		getMovements();
		calculateTotals();
	}
	
	private void getActivities()
    {
    	List<ActivitiesDTO> activities = this.activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
    	activities.sort(Comparator.comparing(ActivitiesDTO::getDateStart));
    	
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"dateStart", "dateEnd", "name", "status"});
		activitiesId.clear();
		for (ActivitiesDTO activity : activities) {
	    	activitiesId.add(activity.getId());
	    }
		
		// Create larger font
		Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
		
		// Set the model in the JTable
	    this.view.getActivitiesTable().setModel(tmodel);
	    JTableHeader header = this.view.getActivitiesTable().getTableHeader();
	    header.setFont(largerFont);
	    this.view.adjustColumns();
	    
	    calculateTotals();
    }
	
	public void loadTypes() {
        List<Object[]> typesList = this.movementsModel.getUniqueTypes();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(typesList);
        view.getType().setModel(lmodel);
    }
	
	private void getIncomesExpenses()
	{
    	List<IncomesExpensesDTO> incomesExpenses = this.movementsModel.getIncomeExpenseByActivity(lastSelectedActivity);
    	
    	incomesExpenses.sort(Comparator.comparing(IncomesExpensesDTO::getDateEstimated, Comparator.nullsLast(Comparator.naturalOrder())));
		
		TableModel tmodel = SwingUtil.getTableModelFromPojos(incomesExpenses, new String[] {"dateEstimated", "type", "concept", "status", "amountEstimated"});
	    
	    incomeExpenseId.clear();
	    for (IncomesExpensesDTO incomeExpense : incomesExpenses) {
	    	incomeExpenseId.add(incomeExpense.getId());
	    	if (incomeExpense.getId() == this.lastSelectedIncomeExpense) {
	    		this.view.getIncomesExpensesTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    		this.view.getIncomesExpensesTable().setRowSelectionInterval(incomeExpenseId.size()-1, incomeExpenseId.size()-1);
	    		this.view.getIncomesExpensesTable().scrollRectToVisible(this.view.getIncomesExpensesTable().getCellRect(incomeExpenseId.size()-1, 0, true));
	    	}
	    }
	    
	    // Set the model in the JTable
	    this.view.getIncomesExpensesTable().setModel(tmodel);
	    this.view.adjustColumns();
	    // Create larger font
	 	Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
 	    JTableHeader header = this.view.getIncomesExpensesTable().getTableHeader();
 	    header.setFont(largerFont); 
 	    
	    this.view.getMovementsTable().clearSelection();
		DefaultTableModel movModel = (DefaultTableModel) this.view.getMovementsTable().getModel();
		movModel.setRowCount(0); // Clears the table
	}
	
	private void getMovements()
	{
    	List<MovementsDTO> movements = this.movementsModel.getMovementsByIncomeExpense(lastSelectedIncomeExpense);
    	
    	movements.sort(Comparator.comparing(MovementsDTO::getDate));
		
		// Create table model
		TableModel tmodel = SwingUtil.getTableModelFromPojos(movements, new String[] {"date", "concept", "amount"});
	    
	    // Set the model in the JTable
	    this.view.getMovementsTable().setModel(tmodel);
	    Font largerFont = new Font("Arial", Font.PLAIN, 14); // Adjust size as needed
 	    JTableHeader header = this.view.getMovementsTable().getTableHeader();
 	    header.setFont(largerFont); 
	}
	
	private void activitySelection()
	{
		int row = this.view.getActivitiesTable().getSelectedRow();
		this.lastSelectedActivity = activitiesId.get(row);
		
		calculateTotals();
	    getIncomesExpenses();
		updateDetail1();
	}
	
	private void calculateTotals()
	{
		this.view.getEstimatedIncomesLabel().setText(String.valueOf(this.movementsModel.getEstimatedIncome(lastSelectedActivity)));
		this.view.getPaidIncomesLabel().setText(String.valueOf(String.valueOf(this.movementsModel.getActualIncome(lastSelectedActivity))));
		this.view.getEstimatedExpensesLabel().setText(String.valueOf(this.movementsModel.getEstimatedExpenses(lastSelectedActivity)));
		this.view.getPaidExpensesLabel().setText(String.valueOf(this.movementsModel.getActualExpenses(lastSelectedActivity)));
		
		this.view.getRemainingBalanceLabel().setText(
				String.valueOf(this.movementsModel.getTotalAmountPaidForActivity(lastSelectedActivity))
		);
	}
	
	private void incomeExpenseSelection()
	{
		this.clearFields();
		
		int row = this.view.getIncomesExpensesTable().getSelectedRow();
		this.lastSelectedIncomeExpense = incomeExpenseId.get(row);
		
		getMovements(); 
		updateDetail2();
	}
	
	private void setInputsEnabled1(boolean enabled)
    {
    	this.view.getDateTextField1().setEnabled(enabled);
    	this.view.getAmountTextField1().setEnabled(enabled);
    	this.view.getConceptTextField1().setEnabled(enabled);
    }
	
	private void setInputsEnabled2(boolean enabled)
    {
    	this.view.getDateTextField2().setEnabled(enabled);
    	this.view.getAmountTextField2().setEnabled(enabled);
    	this.view.getConceptTextField2().setEnabled(enabled);
    }
	
	public void updateDetail1()
	{	
		// ------------------------------------------------------------
		// If an Income/Expense is selected in the table do:
		if (this.view.getIncomesExpensesTable().getSelectedRow() >= 0) {
			return;
		}
		
		if("".equals(this.lastSelectedActivity))
		{
			restartView(); 
		}
		else
		{
			this.setInputsEnabled1(true);
		}
				
		// ------------------------------------------------------------
		// Check JTextField inputs:
		this.submit = true;
		
		// Validate amount
		String amount = this.view.getAmountTextField1().getText();
		if(!SyntacticValidations.isDecimal(amount) || !SyntacticValidations.isNotEmpty(amount) || !SyntacticValidations.isPositiveNumber(amount, true))
		{
			this.view.getAmountTextField1().setForeground(Color.RED);
			this.submit = false;
		} else { this.view.getAmountTextField1().setForeground(Color.BLACK); }
		
		// Validate date
		String paymentDate = this.view.getDateTextField1().getText();
		String status = this.view.getStatus().getSelectedItem().toString();
		if(!SyntacticValidations.isDate(paymentDate) || (status.equals("paid") && !SyntacticValidations.isNotEmpty(paymentDate)))
		{
			this.view.getDateTextField1().setForeground(Color.RED);
			this.submit = false;
		} else { this.view.getDateTextField1().setForeground(Color.BLACK); }
		
		// Validate concept
		String concept = this.view.getConceptTextField1().getText();
		if (!SyntacticValidations.isNotEmpty(concept)) { 
			this.submit = false; 
		}
	}
	
	public void updateDetail2()
	{	
		// ------------------------------------------------------------
		// If a movement is selected in the table do:
		
		if("".equals(this.lastSelectedIncomeExpense))
		{
			restartView(); 
		}
		else
		{
			this.setInputsEnabled2(true);
		}
				
		// ------------------------------------------------------------
		// Check JTextField inputs:
		this.submit = true;
		
		// Validate amount
		String amount = this.view.getAmountTextField2().getText();
		
		if(!SyntacticValidations.isDecimal(amount) || !SyntacticValidations.isNotEmpty(amount) || !SyntacticValidations.isPositiveNumber(amount, true))
		{
			this.view.getAmountTextField2().setForeground(Color.RED);
			this.submit = false;
		} else { this.view.getAmountTextField2().setForeground(Color.BLACK); }
		
		// Validate date
		String paymentDate = this.view.getDateTextField2().getText();
		String status = this.view.getStatus().getSelectedItem().toString();
		if(!SyntacticValidations.isDate(paymentDate) || !SyntacticValidations.isNotEmpty(paymentDate))
		{
			this.view.getDateTextField2().setForeground(Color.RED);
			this.submit = false;
		} else { this.view.getDateTextField2().setForeground(Color.BLACK); }
		
		// Validate concept
		String concept = this.view.getConceptTextField2().getText();
		if (!SyntacticValidations.isNotEmpty(concept)) { 
			this.submit = false; 
		}
	}
	
	// ================================================================================
	private void submitIncomeExpense()
	{
		if (!submit) {
			// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"Some of the fields have an incorrect format", // Error message
        		"Income/Expense Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
		}
		
		// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
        int activityRow = this.view.getActivitiesTable().getSelectedRow();
        String activity = "";
        String idActivity = "";
        
        if (activityRow >= 0)
        {
        	idActivity = activitiesId.get(activityRow);
        	activity = (String) this.view.getActivitiesTable().getModel().getValueAt(activityRow, 2);
        }
        
        String amount = this.view.getAmountTextField1().getText();
        String type = this.view.getType().getSelectedItem().toString();
        String date = this.view.getDateTextField1().getText();
        String concept = this.view.getConceptTextField1().getText();
        String status = this.view.getStatus().getSelectedItem().toString();
        
        String message = "";
        boolean validated = true;
        
        if (!SyntacticValidations.isNotEmpty(amount)) {
        	message = "<html><body>"
                    + "<p>Amount must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(concept)) {
        	message = "<html><body>"
                    + "<p>Concept must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (DEFAULT_VALUE_COMBOBOX.equals(type) || DEFAULT_VALUE_COMBOBOX.equals(status)) {
        	message = "<html><body>"
                    + "<p>Type and Status must be selected</p>"
                    + "</body></html>";
        	validated = false;
        } else if ("paid".equals(status) && !SyntacticValidations.isNotEmpty(date)) {
        	message = "<html><body>"
                    + "<p>Date must be provided</p>"
                    + "</body></html>";
        	validated = false;
        }
        
        if (!validated) {
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + message, // Error message
        		"Income/Expense Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
        }
        
        if ("expense".equals(type))
		{
    		Double aux = Double.parseDouble(amount);
			aux = (-1)*aux;
			amount = String.valueOf(aux);
		}
        
        message = "<html><body>"
                + "<p>You are about to add a Movement for the Activity: <b>" + activity + "</b>.</p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Type:</b></td><td style='padding: 2px 5px;'>" + type + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + amount + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date:</b></td><td style='padding: 2px 5px;'>" + date + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Concept:</b></td><td style='padding: 2px 5px;'>" + concept + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this Movement?</i></p>"
                + "</body></html>";
        
        Object[] options = {"Yes", "No"};
        
        int response = JOptionPane.showOptionDialog(null,
                message, 		// The message
                "Confirmation of Income/Expense Registration",    // The title
                JOptionPane.DEFAULT_OPTION,          		// The option type
                JOptionPane.QUESTION_MESSAGE,        		// The message type
                null,                               		// Icon (null means default question icon)
                options,                            		// Custom buttons
                options[0]); 
        
        if (response == 1) return;
        
        String idLastIncomeExpense = "";
        
        try {
        	if (status.equals("paid")) {
        		idLastIncomeExpense = this.movementsModel.registerIncomeExpense(idActivity, type, status, amount, date, concept);
        		this.movementsModel.registerMovement(idLastIncomeExpense, amount, date, concept);
        	} else if (status.equals("estimated")) {
        		this.movementsModel.registerIncomeExpense(idActivity, type, status, amount, date, concept);
        	}
        	this.updateView();
        } catch (Exception e) {
        	e.printStackTrace();
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + e.getMessage(), // Error message
        		"Income/Expense Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        }
	}
	
	private void submitMovement()
	{
		if (!submit) {
			// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"Some of the fields have an incorrect format", // Error message
        		"Movement Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
		}
		
		// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
        int activityRow = this.view.getActivitiesTable().getSelectedRow();
        int incomeExpenseRow = this.view.getIncomesExpensesTable().getSelectedRow();
        
        String activity = "";
        String idActivity = "";
        
        if (activityRow >= 0)
        {
        	idActivity = activitiesId.get(activityRow);
        	activity = (String) this.view.getActivitiesTable().getModel().getValueAt(activityRow, 2);
        }
        
        String idType = "";
        String type = "";
        
        if (incomeExpenseRow >= 0)
        {
        	idType = incomeExpenseId.get(incomeExpenseRow);
        	type = (String) this.view.getIncomesExpensesTable().getModel().getValueAt(incomeExpenseRow, 1);
        	
        }
        
        String amount = this.view.getAmountTextField2().getText();
        String date = this.view.getDateTextField2().getText();
        String concept = this.view.getConceptTextField2().getText();
        
        boolean compensationMovement = this.view.getCompensationCheckBox().isSelected() ? true : false;
        
        String message = "";
        boolean validated = true;
        
        if (!SyntacticValidations.isNotEmpty(amount)) {
        	message = "<html><body>"
                    + "<p>Amount must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(concept)) {
        	message = "<html><body>"
                    + "<p>Concept must be provided</p>"
                    + "</body></html>";
        	validated = false;
        } else if (!SyntacticValidations.isNotEmpty(date)) {
        	message = "<html><body>"
                    + "<p>Date must be provided</p>"
                    + "</body></html>";
        	validated = false;
        }
        
        if (!validated) {
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + message, // Error message
        		"Movement Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        	return;
        }
        
        if ("expense".equals(type) && !compensationMovement)
		{
    		Double aux = Double.parseDouble(amount);
			aux = (-1)*aux;
			amount = String.valueOf(aux);
		} else if ("income".equals(type) && compensationMovement)
		{
			Double aux = Double.parseDouble(amount);
			aux = (-1)*aux;
			amount = String.valueOf(aux);
		}
        
        message = "<html><body>"
                + "<p>You are about to add a Movement for the Activity: <b>" + activity + "</b>.</p>"
                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>"
                + "<tr><td style='padding: 2px 5px;'><b>Type:</b></td><td style='padding: 2px 5px;'>" + type + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Amount:</b></td><td style='padding: 2px 5px;'>" + amount + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Date:</b></td><td style='padding: 2px 5px;'>" + date + "</td></tr>"
                + "<tr><td style='padding: 2px 5px;'><b>Concept:</b></td><td style='padding: 2px 5px;'>" + concept + "</td></tr>"
                + "</table>"
                + "<p><i>Do you want to proceed with adding this Movement?</i></p>"
                + "</body></html>";
        
        Object[] options = {"Yes", "No"};
        
        int response = JOptionPane.showOptionDialog(null,
                message, 		// The message
                "Confirmation of Movement Registration",    // The title
                JOptionPane.DEFAULT_OPTION,          		// The option type
                JOptionPane.QUESTION_MESSAGE,        		// The message type
                null,                               		// Icon (null means default question icon)
                options,                            		// Custom buttons
                options[0]); 
        
        if (response == 1) return;
        
        try {
        	this.movementsModel.registerMovement(idType, amount, date, concept);
        	this.updateView();
        } catch (Exception e) {
        	e.printStackTrace();
        	// Show an error dialog
        	JOptionPane.showOptionDialog(
        		null, 
        		"An error occurred: " + e.getMessage(), // Error message
        		"Movement Registration Error", // Dialog title
        		JOptionPane.DEFAULT_OPTION, 
        		JOptionPane.ERROR_MESSAGE, 
        		null, 
        		new Object[]{"OK"}, // Force "OK" button in English
        		"OK" // Default selected option
        	);
        }
	}
}