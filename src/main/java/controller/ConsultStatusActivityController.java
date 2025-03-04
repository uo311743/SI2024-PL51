package controller;

import model.ConsultStatusActivityModel;
import view.ConsultStatusActivityView;

public class ConsultStatusActivityController {

	private final static String DEFAULT_VALUE_COMBOBOX = "--------------------";

	protected ConsultStatusActivityModel model;
    protected ConsultStatusActivityView view; 

    private String lastSelectedActivity;
    private String lastSelectedSponsor;

    // ================================================================================

    public ConsultStatusActivityController(ConsultStatusActivityModel m, ConsultStatusActivityView v)
    { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }

    // ================================================================================

    public void initController()
    {
    	// TODO
    }

    public void initView()
    {
    	this.restartView();
    	view.setVisible();
    }

	public void restoreDetail()
	{
		// TODO
	}

	public void updateDetail()
	{		
		// TODO
	}

	public void restartView()
    {
		// TODO
    }


    // ================================================================================
}