package controller;

import model.Model;
import model.Payment;
import view.PaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class PaymentController {
	// Create an Object of Payment type for the Model
	// Create View Object
	private Model model;
	private PaymentView view;
	
	public PaymentController(Model model, PaymentView view) {
		this.model = model;
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
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
	
	// Control View Object to be updated any time there is a change in the Model related to Payments
	public void updateView()
	{
		
	}
}