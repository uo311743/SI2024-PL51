package controller;

import model.Payment;
import view.PaymentView;

public class PaymentController {
	// Create an Object of Payment type for the Model
	// Create View Object
	private Payment model;
	private PaymentView view;
	
	public PaymentController(Payment model, PaymentView view) {
		this.model = model;
		this.view = view;
	}

	public Payment getModel() {
		return model;
	}

	public void setModel(Payment model) {
		this.model = model;
	}

	public PaymentView getView() {
		return view;
	}

	public void setView(PaymentView view) {
		this.view = view;
	}
	
	// Control Model Object
	// Interact with the items inside the Model Class
	// Methods to set the properties in the actual Model
	
	// Control View Object
	public void updateView()
	{
		view.main(null);
	}
}