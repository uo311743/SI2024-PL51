package controller;

import model.RegisterSponshorshipModel;
import view.RegisterSponshorshipView;

public class RegisterSponshorshipController {
	protected RegisterSponshorshipModel model;
    protected RegisterSponshorshipView view; 

    // ================================================================================

    public RegisterSponshorshipController(
    		RegisterSponshorshipModel m,
    		RegisterSponshorshipView v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }
    
    // ================================================================================

    public void initController() {
    	
    }
    
    public void initView() {
    	view.setVisible();
    }
}
