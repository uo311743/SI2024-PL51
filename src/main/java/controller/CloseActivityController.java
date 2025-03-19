package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import model.ActivitiesModel;
import model.MovementsModel;
import model.SponsorshipAgreementsModel;
import util.SwingUtil;
import view.CloseActivityView;

public class CloseActivityController {
		

	private ActivitiesModel activitiesModel;
	private MovementsModel movementsModel;
	private SponsorshipAgreementsModel saModel;
	
	private double estimatedSponsorship;
	private double actualSponsorship;
	private double estimatedIncome;
	private double actualIncome;
	private double estimatedExpenses;
	private double actualExpenses;

	
	private CloseActivityView view; 
    private List<ActivitiesDTO> activities;
    
    private int lastSelectedActivity;
    

    // ================================================================================

    public CloseActivityController(ActivitiesModel am, SponsorshipAgreementsModel sam, MovementsModel mm, CloseActivityView v)
    { 
        this.activitiesModel = am;
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
						
			estimatedSponsorship = this.saModel.getEstimatedSponshorships(idLlastSelectedActivity);
			actualSponsorship = this.saModel.getActualSponshorships(idLlastSelectedActivity);

			this.view.getStatusSponsorTextField().setText(
					String.format("%.2f paid of %.2f", actualSponsorship, estimatedSponsorship)
			);
			
			estimatedIncome = this.movementsModel.getEstimatedIncome(idLlastSelectedActivity);
			actualIncome = this.movementsModel.getActualIncome(idLlastSelectedActivity);
			
			this.view.getStatusIncomesTextField().setText(
					String.format("%.2f paid of %.2f", actualIncome, estimatedIncome)
			);

			estimatedExpenses = this.movementsModel.getEstimatedExpenses(idLlastSelectedActivity);
			actualExpenses = this.movementsModel.getActualExpenses(idLlastSelectedActivity);

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
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"name", "edition", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
    // ================================================================================

    private void showCloseDialog() // FIXME
    {
    	ActivitiesDTO DTOlastSelectedActivity = this.activities.get(
				this.lastSelectedActivity
			);
    	
    	boolean isPaidSponsorship = (estimatedSponsorship - actualSponsorship) == 0;
    	boolean isPaidIncome      = (estimatedIncome      - actualIncome     ) == 0;
    	boolean isPaidExpenses    = (estimatedExpenses    - actualExpenses   ) == 0;
    	
    	if(!isPaidSponsorship || !isPaidIncome || !isPaidExpenses)
    	{
    		String message = "<html><body>"
	                + "<p>Are you sure you want to close the activity: "
    				+"<b>" + DTOlastSelectedActivity.getName() + " " + DTOlastSelectedActivity.getEdition() +"</b>"
					+ "</p>"
	                + "<p><b>Details:</b></p>"
	                + "<table style='margin: 10px auto; font-size: 8px; border-collapse: collapse;'>";
    		
    		if(!isPaidSponsorship)
    			message +="<tr><td style='padding: 2px 5px;'><b>Pending sponsorships:</b></td><td style='padding: 2px 5px;'>"
    					+ String.valueOf(estimatedSponsorship - actualSponsorship) + " €</td></tr>";
    		
    		if(!isPaidIncome)
    			message +="<tr><td style='padding: 2px 5px;'><b>Pending income:</b></td><td style='padding: 2px 5px;'>"
    					+ String.valueOf(estimatedIncome - actualIncome) + " €</td></tr>";
    		
    		if(!isPaidExpenses)
    			message +="<tr><td style='padding: 2px 5px;'><b>Pending Expenses:</b></td><td style='padding: 2px 5px;'>"
    					+ String.valueOf(estimatedExpenses - actualExpenses) + " €</td></tr>";

    		message += "</table></body></html>";

	        int response = JOptionPane.showConfirmDialog(
	            this.view.getFrame(),  message,
	            "Confirm Sponsorship Agreement Details",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
	        );
	        
	        if(response != JOptionPane.YES_OPTION) return;
    	}
	
    	this.activitiesModel.closeActivityById(DTOlastSelectedActivity.getId());
    	
    	JOptionPane.showMessageDialog(
    			this.view.getFrame(), "Activity closed successfully.",
    			"Operation completed successfully",
    			JOptionPane.INFORMATION_MESSAGE
    	);
    	
    	this.restoreDetail();
    	return;
    }

}
