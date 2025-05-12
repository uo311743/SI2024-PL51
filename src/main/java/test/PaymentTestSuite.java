package test;

import org.junit.*;

import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipPaymentsDTO;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipPaymentsModel;
import util.ApplicationException;
import util.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class PaymentTestSuite {
	private static Database db=new Database();
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
				"delete from SponsorOrganizations",
				"insert into SponsorOrganizations (name, address, nif, vat) values ('TechCorp', '123 Tech Street', 'NIF123', 'VAT123')",
				"insert into SponsorOrganizations (name, address, nif, vat) values ('EduFund', '456 Education Avenue', 'NIF456', 'VAT456')",
				"delete from GBmembers",
				"insert into GBmembers (name, role) values ('Alice Johnson', 'Treasurer')",
				"insert into GBmembers (name, role) values ('Bob Smith', 'Secretary')",
				"delete from SponsorContacts",
				"insert into SponsorContacts (idSponsorOrganization, name, email, phone) values (1, 'John Doe', 'john.doe@techcorp.com', '123-456-7890')",
				"insert into SponsorContacts (idSponsorOrganization, name, email, phone) values (2, 'Jane Roe', 'jane.roe@edufund.org', '098-765-4321')",
				"delete from Activities",
				"insert into Activities (name, edition, status, dateStart, dateEnd, place) values ('Olimpics 2023', 1, 'planned', '2023-03-01', '2023-03-03', 'Community Center')",
				"insert into Activities (name, edition, status, dateStart, dateEnd, place) values ('ImpulsoTIC Week 2022', 2, 'closed', '2022-07-10', '2022-07-12', 'Town Square')",
				"delete from SponsorshipAgreements",
				"insert into SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, endDate, status) values (1, 1, 1, 1500.00, '2023-02-01', '2023-12-31', 'signed')",
				"insert into SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, endDate, status) values (2, 2, 2, 800.00, '2022-06-01', '2022-12-31', 'closed')",
				"delete from Invoices",
				"insert into Invoices (id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) values ('INV-103', 1, '2023-02-01', 1500.00, 10, 'issued')",
				"insert into Invoices (id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) values ('INV-104', 2, '2022-06-01', 800.00, 10, 'issued')",
				"delete from SponsorshipPayments"
			});
	}
	
	/**
     * Resets the database by deleting all data from relevant tables.
     * Ensures a clean state for each test by clearing tables in the correct order
     * to respect foreign key constraints.
     * @param db The Database instance to operate on.
     */
    private void resetDatabase(Database db) {
        try {
            db.executeBatch(new String[] {
                    "delete from SponsorshipPayments", // Clear payments first (depends on Invoices)
                    "delete from Invoices",           // Clear invoices (depends on SponsorshipAgreements)
                    "delete from SponsorshipAgreements", // Clear agreements (depends on SponsorContacts, GBMembers, Activities)
                    "delete from SponsorContacts",    // Clear contacts (depends on SponsorOrganizations)
                    "delete from GBmembers",          // Clear members (no dependencies)
                    "delete from SponsorOrganizations", // Clear organizations (no dependencies)
                    "delete from Activities"          // Clear activities (no dependencies)
            });
        } catch (Exception e) {
            System.err.println("Error resetting database: " + e.getMessage());
        }
    }

	/**
     * Tests the list of invoices available for payment registration.
     * Ensures only invoices linked to activities in 'planned' state are shown.
     * Input: None (uses database state).
     * Expected Outcome: Only one invoice ('INV-103') linked to a 'planned' activity is returned.
     */
    @Test
    public void testInvoicesForPaymentRegistrationList() {
        List<SponsorOrganizationsDTO> sponsors = SponsorOrganizationsModel.getSponsorOrganizations();
        InvoicesModel invoicesModel = new InvoicesModel();
        List<InvoicesDTO> invoices = new ArrayList<>();

        for (SponsorOrganizationsDTO sponsor : sponsors) {
            List<InvoicesDTO> sponsorInvoices = invoicesModel.getInvoicesBySponsor(sponsor.getId());
            invoices.addAll(sponsorInvoices);
        }

        assertEquals("Incorrect number of invoices shown", 1, invoices.size());
        InvoicesDTO invoice = invoices.get(0);
        assertEquals("Incorrect invoice ID", "INV-103", invoice.getId());
        assertEquals("Incorrect invoice date", "2023-02-01", invoice.getDateIssued());
        assertEquals("Incorrect invoice amount", 1500.00, invoice.getTotalAmount());
        assertEquals("Incorrect invoice status", "issued", invoice.getStatus());
    }
    
    /**
     * Tests valid payment registration with a single payment.
     * Input Conditions: Existing invoice ID ('INV-103') tied to a 'planned' activity,
     * payment date ≥ invoice date ('2023-02-01'), and amount > 0.0 euros.
     * Expected Outcome: Payment is registered successfully in the database,
     * and the payment is found as the last registered payment.
     */
    @Test
    public void testValidPaymentRegistration() {
        String invoiceId = "INV-103";
        String paymentDate = "2023-02-02";
        double paymentAmount = 100.0;

        // Register the payment
        SponsorshipPaymentsModel model = new SponsorshipPaymentsModel();
        try {
        	model.registerPayment(invoiceId, paymentDate, paymentAmount);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        
        String sql = "SELECT sp.* FROM SponsorshipPayments sp " +
                    "JOIN Invoices i ON sp.idInvoice = i.id " +
                    "WHERE i.id = ? AND sp.date = ? AND sp.amount = ?";
        List<SponsorshipPaymentsDTO> lastPayment = db.executeQueryPojo(
            SponsorshipPaymentsDTO.class, sql, new Object[] { invoiceId, paymentDate, paymentAmount });
        
        // Verify the payment
        //assertNotNull("Last payment record should exist", lastPayment.get(0));
        assertFalse("Last payment record should exist", lastPayment.isEmpty());
        assertEquals("Last payment should be for the correct invoice", invoiceId, lastPayment.get(0).getInvoiceId());
        assertEquals("Last payment date should match input", paymentDate, lastPayment.get(0).getDate());
        assertEquals("Last payment amount should match input", paymentAmount, lastPayment.get(0).getAmount());
    }
    
    /**
     * Tests payment registration of a non-registered invoice.
     * Input Conditions: Invoice ID ('INV-105') does not exist,
     * valid payment date, and amount > 0.0 euros.
     * Expected Outcome: Throws ApplicationException with an appropriate message.
     */
    @Test
    public void testInvalidInvoice() {
        String invoiceId = "INV-105";
        String paymentDate = "2022-06-02";
        double paymentAmount = 100.0;

        ApplicationException exception = null;
        SponsorshipPaymentsModel model = new SponsorshipPaymentsModel();
        try {
            model.registerPayment(invoiceId, paymentDate, paymentAmount);
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate idInvoice does not exist", 
                   exception.getMessage().toLowerCase().contains("idInvoice"));
    }
    
    /**
     * Tests payment registration with a zero payment amount.
     * Input Conditions: Valid invoice ID ('INV-103'), valid payment date,
     * and amount = 0.0 euros.
     * Expected Outcome: Throws ApplicationException with an appropriate message.
     */
    @Test
    public void testInvalidAmount() {
        String invoiceId = "INV-103";
        String paymentDate = "2023-02-02";
        double paymentAmount = 0.0;

        ApplicationException exception = null;
        SponsorshipPaymentsModel model = new SponsorshipPaymentsModel();
        try {
            model.registerPayment(invoiceId, paymentDate, paymentAmount);
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate invalid amount", 
                   exception.getMessage().toLowerCase().contains("amount"));
    }
	
	/**
     * Tests payment registration with a payment date before the invoice date.
     * Input Conditions: Valid invoice ID ('INV-103'), payment date < invoice date ('2023-02-01'),
     * and amount > 0.0 euros.
     * Expected Outcome: Throws ApplicationException with an appropriate message.
     */
	@Test
    public void testInvalidPaymentDate() {
        String invoiceId = "INV-103";
        String paymentDate = "2023-01-31";
        double paymentAmount = 100.0;

        ApplicationException exception = null;
        SponsorshipPaymentsModel model = new SponsorshipPaymentsModel();
        try {
            model.registerPayment(invoiceId, paymentDate, paymentAmount);
        } catch (ApplicationException e) {
            exception = e;
        }
        assertNotNull("Exception should be thrown", exception);
        assertTrue("Exception message should indicate invalid date", 
                   exception.getMessage().toLowerCase().contains("date"));
    }
}