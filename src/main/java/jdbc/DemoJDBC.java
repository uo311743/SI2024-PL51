package jdbc;

import model.Payment;

public class DemoJDBC {

	public static void main(String[] args) {
		Payment model = retrievePaymentFromDatabase();
	}

	// Populate Model Object
	private static Payment retrievePaymentFromDatabase()
	{
		Payment payment = new Payment();
		return payment;
	}
}
