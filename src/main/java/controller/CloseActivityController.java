package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import model.ActivitiesModel;
import model.MovementsModel;
import model.SponsorContactsModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import util.SwingUtil;
import view.CloseActivityView;

public class CloseActivityController {
		
	protected SponsorOrganizationsModel soModel;
	protected SponsorContactsModel scModel;
	protected ActivitiesModel activitiesModel;
	protected MovementsModel movementsModel;
	protected SponsorshipAgreementsModel saModel;

	
    protected CloseActivityView view; 
    private List<ActivitiesDTO> activities;
    
    private int lastSelectedActivity;
    

    // ================================================================================

    public CloseActivityController(ActivitiesModel am, SponsorOrganizationsModel som, SponsorshipAgreementsModel sam, MovementsModel mm, CloseActivityView v)
    { 
        this.activitiesModel = am;
        this.soModel = som;
        this.saModel = sam;
        this.movementsModel = mm;
        
        this.view = v;
        this.initView();
        this.initController();
    }
    
    // ================================================================================

    public void initController()
    {
    	this.view.getButtonLowLeft().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
			}
		});
    	
    	
    	this.view.getButtonLowRight().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent  e) {
				SwingUtil.exceptionWrapper(() -> showCloseDialog());
			}
		});
    	
    	this.view.getActivityTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> updateDetail());
			}
		});
    }
    
    public void initView()
    {
    	this.restoreDetail();
    	view.setVisible();
    }
        
	public void restoreDetail()
	{
		this.getActivities();
		
		this.view.getButtonLowRight().setEnabled(false);
		this.view.getStatusSponsorTextField().setEditable(false);
		this.view.getStatusIncomesTextField().setEditable(false);
		this.view.getStatusExpensesTextField().setEditable(false);
		
		this.lastSelectedActivity = this.view.getActivityTable().getSelectedRow();
		if (this.lastSelectedActivity != -1) this.updateDetail();
	}
	
	public void updateDetail()
	{		
		// ------------------------------------------------------------
		// If an activity is selected in the table do:
		
		this.lastSelectedActivity = this.view.getActivityTable().getSelectedRow();
		if(this.lastSelectedActivity != -1)
		{
			String idLlastSelectedActivity = this.activities.get(
				this.lastSelectedActivity
			).getId();
						
			double estimatedSponsorship = this.saModel.getEstimatedSponshorships(idLlastSelectedActivity);
			double actualSponsorship = this.saModel.getActualSponshorships(idLlastSelectedActivity);

			this.view.getStatusSponsorTextField().setText(
					String.format("%.2f paid of %.2f", actualSponsorship, estimatedSponsorship)
			);
			
			double estimatedIncome = this.movementsModel.getEstimatedIncome(idLlastSelectedActivity);
			double actualIncome = this.movementsModel.getActualIncome(idLlastSelectedActivity);
			
			this.view.getStatusIncomesTextField().setText(
					String.format("%.2f paid of %.2f", actualIncome, estimatedIncome)
			);

			double estimatedExpenses = this.movementsModel.getEstimatedExpenses(idLlastSelectedActivity);
			double actualExpenses = this.movementsModel.getActualExpenses(idLlastSelectedActivity);

			this.view.getStatusExpensesTextField().setText(
					String.format("%.2f paid of %.2f", actualExpenses, estimatedExpenses)
			);
			
			// FIXME Add colour red or green depending on values
			
			this.view.getButtonLowRight().setEnabled(true);
		}
	}
    
    
    // ================================================================================
    
    private void getActivities()
    {
    	activities = activitiesModel.getActivitiesbyStatus("registered", "planned", "done");
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"name", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
    // ================================================================================

    private void showCloseDialog() // FIXME
    {
    	return;
    }

}
