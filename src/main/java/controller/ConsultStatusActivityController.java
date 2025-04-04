package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import DTOs.IncomesExpensesDTO;
import DTOs.MovementsDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipAgreementsDTO;
import model.ActivitiesModel;
import model.MovementsModel;
import model.SponsorOrganizationsModel;
import model.SponsorshipAgreementsModel;
import util.ModelManager;
import util.SwingUtil;
import view.ConsultStatusActivityView;

public class ConsultStatusActivityController {

	protected ActivitiesModel activitiesModel;
	protected SponsorOrganizationsModel soModel;
	protected SponsorshipAgreementsModel saModel;
	protected MovementsModel movementsModel;
	
    protected ConsultStatusActivityView view; 

    private String lastSelectedActivity;
    
    private double estimatedSponsorship, actualSponsorship;
    private double estimatedIncome, actualIncome;
    private double estimatedExpenses, actualExpenses;

    // ================================================================================

    public ConsultStatusActivityController(ConsultStatusActivityView v)
    { 
        this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
        this.soModel = ModelManager.getInstance().getSponsorOrganizationsModel();
        this.saModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
        this.movementsModel = ModelManager.getInstance().getMovementsModel();
        
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
    	List<ActivitiesDTO> activities = activitiesModel.getAllActivities();
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"id", "name", "edition", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
	private void getSponsorships(String idActivity)
	{
		List<SponsorshipAgreementsDTO> sponsorships = saModel.getApplicableSponsorshipAgreementsByActivity(idActivity);
		
		DefaultTableModel tmodel = new DefaultTableModel(new String[] {"amount", "sponsor", "status", "date"}, 0);
		for(SponsorshipAgreementsDTO item : sponsorships)
		{
			SponsorOrganizationsDTO sponsor = this.soModel.getSponsorOrganizationByIdSponsorContact(item.getIdSponsorContact());
			tmodel.addRow(new Object[] {
					item.getAmount(),
					sponsor.getName(),
					item.getStatus(),
					item.getDate()
			});
		}

		this.view.getSponsorshipTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getSponsorshipTable());
		
		this.estimatedSponsorship = this.saModel.getEstimatedSponshorships(idActivity);
		this.view.getSubTotalSponsorshipEstimatedField().setText(String.valueOf(this.estimatedSponsorship));
		this.actualSponsorship = this.saModel.getActualSponshorships(idActivity);
		this.view.getSubTotalSponsorshipActualField().setText(String.valueOf(this.actualSponsorship));

	}
	
	private void getIncome(String idActivity)
	{
		List<IncomesExpensesDTO> income = movementsModel.getIncomeByActivity(idActivity);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(income, new String[] {"id", "idActivity", "type", "status", "amountEstimated", "dateEstimated", "concept"});
		this.view.getIncomeTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getIncomeTable());
		
		this.estimatedIncome = this.movementsModel.getEstimatedIncome(idActivity);
		this.view.getSubTotalIncomeEstimatedField().setText(String.valueOf(this.estimatedIncome));
		this.actualIncome = this.movementsModel.getActualIncome(idActivity);
		this.view.getSubTotalIncomeActualField().setText(String.valueOf(this.actualIncome));
	}
	
	private void getExpenses(String idActivity)
	{
		List<IncomesExpensesDTO> expenses = movementsModel.getExpensesByActivity(idActivity);
		TableModel tmodel = SwingUtil.getTableModelFromPojos(expenses, new String[] {"id", "idActivity", "type", "status", "amountEstimated", "dateEstimated", "concept"});
		this.view.getExpensesTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getExpensesTable());
		
		this.estimatedExpenses = this.movementsModel.getEstimatedExpenses(idActivity);
		this.view.getSubTotalExpensesEstimatedField().setText(String.valueOf(this.estimatedExpenses));
		this.actualExpenses = this.movementsModel.getActualExpenses(idActivity);
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
				String.valueOf(this.actualSponsorship + this.actualIncome - this.actualExpenses)
		);
	}
}