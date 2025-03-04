package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import DTOs.ActivitiesDTO;
import DTOs.MovementsDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipAgreementsDTO;
import model.ConsultStatusActivityModel;
import util.SwingUtil;
import view.ConsultStatusActivityView;

public class ConsultStatusActivityController {

	protected ConsultStatusActivityModel model;
    protected ConsultStatusActivityView view; 

    private String lastSelectedActivity;
    
    private double estimatedSponsorship, actualSponsorship;
    private double estimatedIncome, actualIncome;
    private double estimatedExpenses, actualExpenses;

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
    	this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
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
    	this.restartView();
    	view.setVisible();
    }

	public void restoreDetail()
	{
		this.lastSelectedActivity = SwingUtil.selectAndGetSelectedKey(this.view.getActivityTable(), this.lastSelectedActivity);
		if (!"".equals(this.lastSelectedActivity)) this.updateDetail();
	}

	public void updateDetail()
	{		
		this.lastSelectedActivity = SwingUtil.getSelectedKey(this.view.getActivityTable());
		if("".equals(this.lastSelectedActivity)) {  restartView(); }
		else
		{
			this.getSponsorships(this.lastSelectedActivity);
			this.getIncome(this.lastSelectedActivity);
			this.getExpenses(this.lastSelectedActivity);
			calculateTotals();
		}
	}

	public void restartView()
    {
		this.getActivities();
    }


    // ================================================================================
	
	private void getActivities()
    {
    	List<ActivitiesDTO> activities = model.getAllActivities();
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"id", "name", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
	private void getSponsorships(String idActivity)
	{
		List<SponsorshipAgreementsDTO> sponsorships = model.getApplicableSponsorshipAgreementsByActivity(idActivity);
		
		DefaultTableModel tmodel = new DefaultTableModel(new String[] {"amount", "sponsor", "status", "date"}, 0);
		for(SponsorshipAgreementsDTO item : sponsorships)
		{
			SponsorOrganizationsDTO sponsor = this.model.getSponsorOrganizationByIdSponsorContact(item.getIdSponsorContact());
			tmodel.addRow(new Object[] {
					item.getAmount(),
					sponsor.getName(),
					item.getStatus(),
					item.getDate()
			});
		}

		this.view.getSponsorshipTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getSponsorshipTable());
		
		this.estimatedSponsorship = this.model.getEstimatedSponshorships(idActivity);
		this.view.getSubTotalSponsorshipEstimatedField().setText(String.valueOf(this.estimatedSponsorship));
		this.actualSponsorship = this.model.getActualSponshorships(idActivity);
		this.view.getSubTotalSponsorshipActualField().setText(String.valueOf(this.actualSponsorship));

	}
	
	private void getIncome(String idActivity)
	{
		List<MovementsDTO> income = model.getIncomeByActivity(idActivity);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(income, new String[] {"amount", "concept", "status", "date"});
		this.view.getIncomeTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getIncomeTable());
		
		this.estimatedIncome = this.model.getEstimatedIncome(idActivity);
		this.view.getSubTotalIncomeEstimatedField().setText(String.valueOf(this.estimatedIncome));
		this.actualIncome = this.model.getActualIncome(idActivity);
		this.view.getSubTotalIncomeActualField().setText(String.valueOf(this.actualIncome));
	}
	
	private void getExpenses(String idActivity)
	{
		List<MovementsDTO> expenses = model.getExpensesByActivity(idActivity);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(expenses, new String[] {"amount", "concept", "status", "date"});
		this.view.getExpensesTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getExpensesTable());
		
		this.estimatedExpenses = this.model.getEstimatedExpenses(idActivity);
		this.view.getSubTotalExpensesEstimatedField().setText(String.valueOf(this.estimatedExpenses));
		this.actualExpenses = this.model.getActualExpenses(idActivity);
		this.view.getSubTotalExpensesActualField().setText(String.valueOf(this.actualExpenses));
	}
	
	private void calculateTotals()
	{
		this.view.getTotalEstimatedIncomeField().setText(
				String.valueOf(this.estimatedSponsorship + this.estimatedIncome)
		);
		
		this.view.getTotalEstimatedExpensesField().setText(
				String.valueOf(this.estimatedExpenses)
		);
		
		this.view.getTotalEstimatedBalanceField().setText(
				String.valueOf(this.estimatedSponsorship + this.estimatedIncome - this.estimatedExpenses)
		);
		
		this.view.getTotalActualIncomeField().setText(
				String.valueOf(this.actualSponsorship + this.actualIncome)
		);
		
		this.view.getTotalActualExpensesField().setText(
				String.valueOf(this.actualExpenses)
		);
		
		this.view.getTotalActualBalanceField().setText(
				String.valueOf(this.actualSponsorship + this.estimatedIncome - this.estimatedExpenses)
		);
	}
}