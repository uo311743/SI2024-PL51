package controller;

import model.ActivitiesModel;
import model.MovementsModel;
import util.SemanticValidations;
import util.SwingUtil;
import util.SyntacticValidations;
import view.RegisterMovementsView;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DTOs.ActivitiesDTO;
import DTOs.IncomesExpensesDTO;
import DTOs.MovementsDTO;
import DTOs.SponsorshipPaymentsDTO;

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
		        SwingUtil.exceptionWrapper(() -> typeSelection());
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
		
		this.view.getIncomesExpensesTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> incomeExpenseSelection());
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
		this.view.getStatus().insertItemAt(DEFAULT_VALUE_COMBOBOX, 0);
		this.view.getStatus().setSelectedIndex(0);
		
		view.setVisible();
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
    	
    	// Reset amount field with placeholder effect
    	this.view.getDateTextField().setText("");
    	this.view.getAmountTextField().setText("");
    	this.view.getConceptTextField().setText("");
    	
    	// Reset summary panel
    	this.view.getEstimatedLabel().setText("0.0");
    	this.view.getPaidLabel().setText("0.0");
    	
    	this.setInputsEnabled(false);
    }
	
	public void clear() {
		restartView();
		
		this.view.getCompensationCheckBox().setSelected(false);
		this.view.getType().setSelectedIndex(0);
		this.view.getStatus().setSelectedIndex(0);
	}
	
	private void typeSelection()
	{
		String type = this.view.getType().getSelectedItem().toString();
		if (!type.equals(DEFAULT_VALUE_COMBOBOX)) {
			this.view.getIncomesExpensesLabel().setText("Select " + type + " to see the movements");
			this.view.getMovementLabel().setText("Movements registered for the " + type);
		}
		restartView();
		updateDetail();
	}
	
	private void getActivities()
    {
    	List<ActivitiesDTO> activities = this.activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
    	
    	activities.sort(Comparator.comparing(ActivitiesDTO::getDateStart));
		
		String[] columnNames = {"id", "dateStart", "dateEnd", "name", "status"};
		
		// Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		
	    for (ActivitiesDTO activity : activities) {
	        Object[] rowData = {
	        	activity.getId(),
	        	activity.getDateStart(),
	        	activity.getDateEnd(),
	        	activity.getName(),
	        	activity.getStatus()
	        };
	        model.addRow(rowData);
	    }
	    
	    // Set the model in the JTable
	    this.view.getActivitiesTable().setModel(model);
	    SwingUtil.autoAdjustColumns(this.view.getActivitiesTable());
    }
	
	public void loadTypes() {
        List<Object[]> typesList = this.movementsModel.getUniqueTypes();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(typesList);
        view.getType().setModel(lmodel);
    }
	
	private void getIncomesExpenses()
	{
		int row = this.view.getActivitiesTable().getSelectedRow(); 
        
        String idActivity = "";
        
        if (row >= 0)
        {
        	idActivity = (String) this.view.getActivitiesTable().getModel().getValueAt(row, 0);
        }
        
    	List<IncomesExpensesDTO> incomesExpenses;
    	
    	if (this.view.getType().getSelectedItem().toString().equals("income")) {
    		incomesExpenses = this.movementsModel.getIncomeByActivity(idActivity);
    	} else {
    		incomesExpenses = this.movementsModel.getExpensesByActivity(idActivity);
    	}
    	
    	incomesExpenses.sort(Comparator.comparing(IncomesExpensesDTO::getDateEstimated, Comparator.nullsLast(Comparator.naturalOrder())));
		
		String[] columnNames = {"id", "dateEstimated", "concept", "status", "amountEstimated"};
		
		// Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		
	    for (IncomesExpensesDTO incomeExpense : incomesExpenses) {
	        Object[] rowData = {
	        	incomeExpense.getId(),
	        	incomeExpense.getDateEstimated(),
	        	incomeExpense.getConcept(),
	        	incomeExpense.getStatus(),
	       		incomeExpense.getAmountEstimated()
	        };
	        model.addRow(rowData);
	    }
	    
	    // Set the model in the JTable
	    this.view.getIncomesExpensesTable().setModel(model);
	    SwingUtil.autoAdjustColumns(this.view.getIncomesExpensesTable());
	    this.view.getMovementsTable().clearSelection();
		DefaultTableModel movModel = (DefaultTableModel) this.view.getMovementsTable().getModel();
		movModel.setRowCount(0); // Clears the table
	}
	
	private void getMovements()
	{
		int row = this.view.getIncomesExpensesTable().getSelectedRow(); 
        
        String idType = "";
        
        if (row >= 0)
        {
        	idType = String.valueOf(this.view.getIncomesExpensesTable().getModel().getValueAt(row, 0));
        }
        
    	List<MovementsDTO> movements = this.movementsModel.getMovementsByIncomeExpense(idType);
    	
    	movements.sort(Comparator.comparing(MovementsDTO::getDate));
		
		String[] columnNames = {"date", "concept", "amount"};
		
		// Create table model
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		
	    for (MovementsDTO movement : movements) {
	        Object[] rowData = {
	        	movement.getDate(),
	            movement.getConcept(),
	            movement.getAmount()
	        };
	        model.addRow(rowData);
	    }
	    
	    // Set the model in the JTable
	    this.view.getMovementsTable().setModel(model);
	    SwingUtil.autoAdjustColumns(this.view.getMovementsTable());
	}
	
	private void activitySelection()
	{
		this.lastSelectedActivity = SwingUtil.getSelectedKey(this.view.getActivitiesTable());
		int row = this.view.getActivitiesTable().getSelectedRow();
		
		String idActivity = (String) this.view.getActivitiesTable().getValueAt(row, 0);
		String type = this.view.getType().getSelectedItem().toString();
		
		// Update Labels in the UI
		Double actualAmount = calculateActualAmount(idActivity, type);
		Double estimatedAmount = 0.0;
		this.view.getPaidLabel().setText(String.valueOf(actualAmount));
	    if (type.equals("income")) {
	    	estimatedAmount = calculateEstimatedIncome(idActivity);
	    } else if (type.equals("expense")) {
	    	estimatedAmount = calculateEstimatedExpense(idActivity);
	    }
	    this.view.getEstimatedLabel().setText(String.valueOf(estimatedAmount));
	    
	    if (this.view.getStatus().getSelectedItem().toString().equals("paid")) { 
	    	getIncomesExpenses(); 
	    } else {
	    	restartView();
	    }
		updateDetail();
	}
	
	private Double calculateEstimatedIncome(String idActivity)
	{
		List<IncomesExpensesDTO> incomes = this.movementsModel.getIncomeByActivity(idActivity);
	    
	    double totalEstimated = 0.0;
	    for (IncomesExpensesDTO income : incomes) {
	        totalEstimated += Double.parseDouble(income.getAmountEstimated()); // Accumulate payment total
	    }
	    
	    return totalEstimated;
	}
	
	private Double calculateActualAmount(String idActivity, String type)
	{
		List<MovementsDTO> movements = this.movementsModel.getMovementsByTypeAndActivity(type, idActivity);
	    
	    double totalPaid = 0.0;
	    for (MovementsDTO movement : movements) {
	    	totalPaid += movement.getAmount(); // Accumulate payment total
	    }
	    
	    return totalPaid;
	}
	
	private Double calculateEstimatedExpense(String idActivity)
	{
		List<IncomesExpensesDTO> expenses = this.movementsModel.getExpensesByActivity(idActivity);
	    
	    double totalEstimated = 0.0;
	    for (IncomesExpensesDTO expense : expenses) {
	        totalEstimated += Double.parseDouble(expense.getAmountEstimated()); // Accumulate payment total
	    }
	    
	    return totalEstimated;
	}
	
	private void incomeExpenseSelection()
	{
		int row = this.view.getIncomesExpensesTable().getSelectedRow();
		
		// Fill Default values in the UI
		this.view.getAmountTextField().setText(String.valueOf(this.view.getIncomesExpensesTable().getValueAt(row, 4)));
		this.view.getConceptTextField().setText((String) this.view.getIncomesExpensesTable().getValueAt(row, 2));
	    
		getMovements(); 
		updateDetail();
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
		if(!SyntacticValidations.isDecimal(amount) && !SyntacticValidations.isNotEmpty(amount))
		{
			this.view.getAmountTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getAmountTextField().setForeground(Color.BLACK); }
		
		// Validate date
		String paymentDate = this.view.getDateTextField().getText();
		String status = this.view.getStatus().getSelectedItem().toString();
		if(!SyntacticValidations.isDate(paymentDate) || (status.equals("paid") && !SyntacticValidations.isNotEmpty(paymentDate)))
		{
			this.view.getDateTextField().setForeground(Color.RED);
			valid = false;
		} else { this.view.getDateTextField().setForeground(Color.BLACK); }
		
		// Validate concept
		String concept = this.view.getConceptTextField().getText();
		if (!SyntacticValidations.isNotEmpty(concept)) { 
			valid = false; 
		}
	}
	
	// ================================================================================

    private void showSubmitDialog()
    {
    	// Force UI messages to be in English
        Locale.setDefault(Locale.ENGLISH);
        
        int activityRow = this.view.getActivitiesTable().getSelectedRow();
        int incomeExpenseRow = this.view.getIncomesExpensesTable().getSelectedRow();
        
        String activity = "";
        String idActivity = "";
        
        if (activityRow >= 0)
        {
        	idActivity = String.valueOf(this.view.getActivitiesTable().getModel().getValueAt(activityRow, 0));
        	activity = (String) this.view.getActivitiesTable().getModel().getValueAt(activityRow, 3);
        }
        
        String idType = "";
        
        if (incomeExpenseRow >= 0)
        {
        	idType = String.valueOf(this.view.getIncomesExpensesTable().getModel().getValueAt(incomeExpenseRow, 0));
        }
        
        String amount = this.view.getAmountTextField().getText();
        String type = this.view.getType().getSelectedItem().toString();
        String date = this.view.getDateTextField().getText();
        String concept = this.view.getConceptTextField().getText();
        String status = this.view.getStatus().getSelectedItem().toString();
        
        boolean compensationMovement = this.view.getCompensationCheckBox().isSelected() ? true : false;
        Double estimated = Double.parseDouble(this.view.getEstimatedLabel().getText());
        Double paid = Double.parseDouble(this.view.getPaidLabel().getText());
        
        try {
        	if ("expense".equals(type))
    		{
            	if (compensationMovement) {
            		SemanticValidations.validatePositiveNumber(amount, "Compensation Movements for Expenses must be of positive amounts");
    			} else {
    				SemanticValidations.validateNegativeNumber(amount, "Expenses must be of negative amounts");
    			}
    		} else if ("income".equals(type))
    		{
            	if (compensationMovement) {
            		SemanticValidations.validateNegativeNumber(amount, "Compensation Movements for Incomes must be of negative amounts");
    			} else {
    				SemanticValidations.validatePositiveNumber(amount, "Incomes must be of positive amounts");
    			}
    		}
        	if (paid >= estimated) { 
        		this.movementsModel.setIncomeExpenseStatus(idType, "paid"); 
        	} else {
        		this.movementsModel.setIncomeExpenseStatus(idType, "estimated"); 
        	}
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
        	
        	this.clear();
        	return;
        }

        String message = "<html><body>"
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
        
        if (DEFAULT_VALUE_COMBOBOX.equals(type) || DEFAULT_VALUE_COMBOBOX.equals(status)) {
        	message = "<html><body>"
                    + "<p>Type and Status must be selected</p>"
                    + "</body></html>";
        } 
        
        int response = JOptionPane.showOptionDialog(null,
                message, 		// The message
                "Confirmation of Movement Registration",    // The title
                JOptionPane.DEFAULT_OPTION,          		// The option type
                JOptionPane.QUESTION_MESSAGE,        		// The message type
                null,                               		// Icon (null means default question icon)
                options,                            		// Custom buttons
                options[0]); 
        
        if (response == 1) return;
        
        String idLastIncomeExpense = "";
        
        if ("".equals(concept)) { concept = null; }
        
        try {
        	if ("".equals(idType) && status.equals("paid")) { 
        		SemanticValidations.validateDateInPast(date, true, "Payment cannot be made in the future");
        		idLastIncomeExpense = this.movementsModel.registerIncomeExpense(idActivity, type, status, amount, date, concept);
        		this.movementsModel.registerMovement(idLastIncomeExpense, amount, date, concept);
        	} else if ("".equals(idType) && status.equals("estimated")) {
        		this.movementsModel.registerIncomeExpense(idActivity, type, status, amount, date, concept);
        	} else if (status.equals("paid")) { 
        		SemanticValidations.validateDateInPast(date, true, "Payment cannot be made in the future"); 
        		String incomeExpenseDate = this.movementsModel.getIncomeExpenseById(idType).getDateEstimated();
        		if (SyntacticValidations.isNotNull(incomeExpenseDate)) {
        			SemanticValidations.validateDateAfterTo(date, incomeExpenseDate, true, "Movement cannot be made before Income/Expense registration");
        		}
        		this.movementsModel.registerMovement(idType, amount, date, concept); 
        	}
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
        
        this.clear();
    }
}