package test;

import org.junit.*;

import DTOs.IncomesExpensesDTO;
import DTOs.MovementsDTO;
import model.MovementsModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class IncomeExpenseTestSuite {
	private static Database db = new Database();
	@Before
	public void setUp() {
		db.createDatabase(true);
		loadCleanDatabase(db);
	}
	@After
	public void tearDown(){
		resetDatabase(db);
	}
	
	public static void loadCleanDatabase(Database db) {
        db.executeBatch(new String[] {
            "DELETE FROM Movements",
            "DELETE FROM IncomesExpenses",
            "DELETE FROM Activities",
            "INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) " +
                "VALUES ('Olimpics 2025', 1, 'planned', '2025-05-10', '2025-05-12', 'Community Center')"
        });
    }
	
	private void resetDatabase(Database db) {
        try {
            db.executeBatch(new String[] {
                "DELETE FROM Movements",
                "DELETE FROM IncomesExpenses",
                "DELETE FROM Activities"
            });
        } catch (Exception e) {
            System.err.println("Error resetting database: " + e.getMessage());
        }
    }
	
	/**
     * Tests registering a new valid estimated income.
     * Input Conditions: activity ID not null associated with registered activity; estimated status; income type; not null concept and amount > 0.00.
     * Expected Outcome: Income is registered without an associated movement (new record in IncomesExpenses table; no record in Movements table).
     * With or without date provided.
     */
	@Test
    public void testRegisterNewEstimatedIncome() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("1");
        income.setType("income");
        income.setStatus("estimated");
        income.setAmountEstimated("1000.0");
        income.setConcept("Room rental estimate");

        try {
        	MovementsModel model = new MovementsModel();
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify IncomesExpenses table
        String sql = "SELECT * FROM IncomesExpenses WHERE id = (SELECT MAX(id) FROM IncomesExpenses)";
        List<IncomesExpensesDTO> lastRecord = db.executeQueryPojo(IncomesExpensesDTO.class, sql, null);
        
        assertNotNull("Record should exist", lastRecord.get(0));
        assertEquals("Concept should match", "Room rental estimate", lastRecord.get(0).getConcept());
        assertEquals("Amount should match", "1000.0", lastRecord.get(0).getAmountEstimated());
        assertEquals("Status should be estimated", "estimated", lastRecord.get(0).getStatus());

        // Verify no movement created
        sql = "SELECT * FROM Movements";
        List<MovementsDTO> count = db.executeQueryPojo(MovementsDTO.class, sql, null);
        assertTrue("Count result should be empty", count.isEmpty());
    }
    
    /**
     * Tests registering a new valid estimated expense.
     * Input Conditions: activity ID not null associated with registered activity; estimated status; expense type; not null concept and amount > 0.00.
     * Expected Outcome: Expense is registered with negative amount without an associated movement (new record in IncomesExpenses table; no record in Movements table).
     * With or without date provided.
     */
	@Test
    public void testRegisterNewEstimatedExpense() {
        IncomesExpensesDTO expense = new IncomesExpensesDTO();
        expense.setIdActivity("1");
        expense.setType("expense");
        expense.setStatus("estimated");
        expense.setAmountEstimated("-1000.0");
        expense.setConcept("Technical Security System Infrastructure");

        try {
        	MovementsModel model = new MovementsModel();
            model.registerIncomeExpense(expense.getIdActivity(), expense.getType(), expense.getStatus(), expense.getAmountEstimated(), expense.getDateEstimated(), expense.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify IncomesExpenses table
        String sql = "SELECT * FROM IncomesExpenses WHERE id = (SELECT MAX(id) FROM IncomesExpenses)";
        List<IncomesExpensesDTO> lastRecord = db.executeQueryPojo(IncomesExpensesDTO.class, sql, null);
        
        assertNotNull("Record should exist", lastRecord.get(0));
        assertEquals("Concept should match", "Technical Security System Infrastructure", lastRecord.get(0).getConcept());
        assertEquals("Amount should match", "-1000.0", lastRecord.get(0).getAmountEstimated());
        assertEquals("Status should be estimated", "estimated", lastRecord.get(0).getStatus());

        // Verify no movement created
        sql = "SELECT * FROM Movements";
        List<MovementsDTO> count = db.executeQueryPojo(MovementsDTO.class, sql, null);
        assertTrue("Count result should be empty", count.isEmpty());
    }
    
    /**
     * Tests registering a new paid income with its associated movement.
     * Input Conditions: Valid activity ID, paid status, income type, valid concept, amount, and payment date.
     * Expected Outcome: Both income and associated movement are registered.
     */
	@Test
    public void testRegisterNewPaidIncomeWithValidMovement() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("1");
        income.setType("income");
        income.setStatus("paid");
        income.setAmountEstimated("1000.0");
        income.setDateEstimated("2025-05-10");
        income.setConcept("Room rental payment");

        MovementsModel model = new MovementsModel();
        try {
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify IncomesExpenses table
        String sql = "SELECT * FROM IncomesExpenses WHERE id = (SELECT MAX(id) FROM IncomesExpenses)";
        List<IncomesExpensesDTO> lastRecord = db.executeQueryPojo(IncomesExpensesDTO.class, sql, null);
        
        assertNotNull("Record should exist", lastRecord.get(0));
        assertEquals("Concept should match", "Room rental payment", lastRecord.get(0).getConcept());
        assertEquals("Amount should match", 1000.0, lastRecord.get(0).getAmountEstimated());
        assertEquals("Status should be paid", "paid", lastRecord.get(0).getStatus());
        String incomeId = lastRecord.get(0).getId();
        
        try {
            model.registerMovement(incomeId, income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify Movements table
        sql = "SELECT * FROM Movements WHERE idType = ?";
        List<MovementsDTO> movement = db.executeQueryPojo(MovementsDTO.class, sql, new Object[] { incomeId });
        
        assertNotNull("Movement should exist", movement.get(0));
        assertEquals("Movement amount should match", "1000.0", movement.get(0).getAmount());
        assertEquals("Movement date should match", "2025-05-10", movement.get(0).getDate());
    }
    
    /**
     * Tests registering a new paid expense with its associated movement.
     * Input Conditions: Valid activity ID, paid status, income type, valid concept, amount, and payment date.
     * Expected Outcome: Both expense and associated movement are registered with negative amount.
     */
	@Test
    public void testRegisterNewPaidExpenseWithValidMovement() {
        IncomesExpensesDTO expense = new IncomesExpensesDTO();
        expense.setIdActivity("1");
        expense.setType("expense");
        expense.setStatus("paid");
        expense.setAmountEstimated("-1000.0");
        expense.setDateEstimated("2025-05-10");
        expense.setConcept("Technical Security System Infrastructure");

        MovementsModel model = new MovementsModel();
        try {
            model.registerIncomeExpense(expense.getIdActivity(), expense.getType(), expense.getStatus(), expense.getAmountEstimated(), expense.getDateEstimated(), expense.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify IncomesExpenses table
        String sql = "SELECT * FROM IncomesExpenses WHERE id = (SELECT MAX(id) FROM IncomesExpenses)";
        List<IncomesExpensesDTO> lastRecord = db.executeQueryPojo(IncomesExpensesDTO.class, sql, null);
        
        assertNotNull("Record should exist", lastRecord.get(0));
        assertEquals("Concept should match", "Technical Security System Infrastructure", lastRecord.get(0).getConcept());
        assertEquals("Amount should match", "-1000.0", lastRecord.get(0).getAmountEstimated());
        assertEquals("Status should be paid", "paid", lastRecord.get(0).getStatus());
        String expenseId = lastRecord.get(0).getId();
        
        try {
            model.registerMovement(expenseId, expense.getAmountEstimated(), expense.getDateEstimated(), expense.getConcept());
        } catch (ApplicationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Verify Movements table
        sql = "SELECT * FROM Movements WHERE idType = ?";
        List<MovementsDTO> movement = db.executeQueryPojo(MovementsDTO.class, sql, new Object[] { expenseId });
        
        assertNotNull("Movement should exist", movement.get(0));
        assertEquals("Movement amount should match", "-1000.0", movement.get(0).getAmount());
        assertEquals("Movement date should match", "2025-05-10", movement.get(0).getDate());
    }
    
    /**
     * Tests registering a new paid income/expense without a payment date.
     * Input Conditions: Valid activity ID, paid status, valid concept and amount (depending on type), no payment date.
     * Expected Outcome: Throws ApplicationException with appropriate message.
     */
	@Test
    public void testRegisterNewPaidIncomeExpenseWithoutPaymentDate() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("1");
        income.setType("income");
        income.setStatus("paid");
        income.setAmountEstimated("1000.0");
        income.setConcept("Room rental payment");
        IncomesExpensesDTO expense = new IncomesExpensesDTO();
        expense.setIdActivity("1");
        expense.setType("expense");
        expense.setStatus("paid");
        expense.setAmountEstimated("-1000.0");
        expense.setConcept("Technical Security System Infrastructure");

        ApplicationException exception = null;
        MovementsModel model = new MovementsModel();
        try {
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate missing payment date",
                   exception.getMessage().toLowerCase().contains("date"));
        
        exception = null;
        try {
            model.registerIncomeExpense(expense.getIdActivity(), expense.getType(), expense.getStatus(), expense.getAmountEstimated(), expense.getDateEstimated(), expense.getConcept());
        } catch (ApplicationException e) {
            exception = e;
        }
        
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate missing payment date",
                   exception.getMessage().toLowerCase().contains("date"));
    }
    
    /**
     * Tests registering a new income/expense without a concept description.
     * Input Conditions: Valid activity ID, estimated status, valid amount, no concept.
     * Expected Outcome: Throws ApplicationException with appropriate message.
     */
	@Test
    public void testRegisterNewIncomeExpenseWithoutConceptDescription() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("1");
        income.setType("income");
        income.setStatus("estimated");
        income.setAmountEstimated("1000.0");

        ApplicationException exception = null;
        try {
        	MovementsModel model = new MovementsModel();
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), null, "");
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate missing concept",
                   exception.getMessage().toLowerCase().contains("concept"));
    }
    
    /**
     * Tests registering a new income/expense with an invalid amount.
     * Input Conditions: Valid activity ID, estimated status, valid concept, zero amount.
     * Expected Outcome: Throws ApplicationException with appropriate message.
     */
	@Test
    public void testRegisterNewIncomeExpenseWithInvalidAmount() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("1");
        income.setType("income");
        income.setStatus("estimated");
        income.setAmountEstimated("0.00");
        income.setConcept("Room rental estimate");

        ApplicationException exception = null;
        try {
        	MovementsModel model = new MovementsModel();
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate invalid amount",
                   exception.getMessage().toLowerCase().contains("amount"));
    }
    
    /**
     * Tests registering a new income/expense without selecting an activity.
     * Input Conditions: No activity ID, estimated status, valid concept and amount.
     * Expected Outcome: Throws ApplicationException with appropriate message.
     */
	@Test
    public void testRegisterNewIncomeExpenseWithoutActivity() {
        IncomesExpensesDTO income = new IncomesExpensesDTO();
        income.setIdActivity("0");
        income.setType("income");
        income.setStatus("estimated");
        income.setAmountEstimated("1000.0");
        income.setConcept("Room rental estimate");

        ApplicationException exception = null;
        try {
        	MovementsModel model = new MovementsModel();
            model.registerIncomeExpense(income.getIdActivity(), income.getType(), income.getStatus(), income.getAmountEstimated(), income.getDateEstimated(), income.getConcept());
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate idActivity does not exist",
                   exception.getMessage().toLowerCase().contains("idActivity"));
    }
}