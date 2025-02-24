package controller;

import model.Model;
import view.AbstractView;

public abstract class AbstractController {
	protected Model model;
    protected AbstractView view; 

    // ================================================================================

    public AbstractController(Model m, AbstractView v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }
    
    // ================================================================================

    public abstract void initController();
    public abstract void initView();
}
