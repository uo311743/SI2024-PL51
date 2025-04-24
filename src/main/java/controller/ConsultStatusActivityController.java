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
    
    List<ActivitiesDTO> activities;
    List<IncomesExpensesDTO> income;
    List<IncomesExpensesDTO> expenses;

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
		//TODO
	}

	public void updateDetail()
	{
		this.lastSelectedActivity = this.activities.get(this.view.getActivityTable().getSelectedRow()).getId();
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
    	this.activities = activitiesModel.getAllActivities();
		TableModel tmodel = SwingUtil.getTableModelFromPojos(activities, new String[] {"name", "edition", "status", "dateStart", "dateEnd"});
		this.view.getActivityTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getActivityTable());
    }
    
	private void getSponsorships(String idActivity)
	{
		this.estimatedSponsorship = 0.0;
		this.actualSponsorship = 0.0;
		
		List<SponsorshipAgreementsDTO> sponsorships = saModel.getApplicableSponsorshipAgreementsByActivity(idActivity);
		
		DefaultTableModel tmodel = new DefaultTableModel(new String[] {"Sponsor", "Level", "Estimated", "Paid"}, 0);

		for(SponsorshipAgreementsDTO item : sponsorships)
		{
			SponsorOrganizationsDTO sponsor = this.soModel.getSponsorOrganizationByIdSponsorContact(item.getIdSponsorContact());
			double paidAmount = this.saModel.getSponshorshipPaidAmountByAgreementId(item.getId());
			double estimatedAmount = Double.parseDouble(item.getAmount()) * (1 + this.saModel.getTaxRateByAgreementId(item.getId()) / 100);
			
			tmodel.addRow(new Object[] {
					sponsor.getName(),
					this.saModel.calculateLevelFromSponsorshipAgreementId(item.getId()).getName(),
					estimatedAmount,
					paidAmount

			});
			
			this.estimatedSponsorship += estimatedAmount;
			this.actualSponsorship += paidAmount;
		}

		this.view.getSponsorshipTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getSponsorshipTable());
		
		this.view.getSubTotalSponsorshipEstimatedField().setText(String.valueOf(this.estimatedSponsorship));
		this.view.getSubTotalSponsorshipActualField().setText(String.valueOf(this.actualSponsorship));

	}
	
	private void getIncome(String idActivity) //FIXME
	{
		this.estimatedIncome = 0.0;
		this.actualIncome = 0.0;
		
		this.income = movementsModel.getIncomeByActivity(idActivity);
		
		DefaultTableModel tmodel = new DefaultTableModel(new String[] {"Concept", "Estimated", "Paid"}, 0);
		for(IncomesExpensesDTO item : income)
		{
			double paidAmount = this.movementsModel.getTotalAmountPaid(item.getId());
			double estimatedAmount = Double.parseDouble(item.getAmountEstimated());
			
			tmodel.addRow(new Object[] {
					item.getConcept(), estimatedAmount, paidAmount
			});
			
			this.estimatedIncome += estimatedAmount;
			this.actualIncome += paidAmount;
		}
		
		this.view.getIncomeTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getIncomeTable());
		
		this.view.getSubTotalIncomeEstimatedField().setText(String.valueOf(this.estimatedIncome));
		this.view.getSubTotalIncomeActualField().setText(String.valueOf(this.actualIncome));
	}
	
	private void getExpenses(String idActivity) //FIXME
	{
		this.estimatedExpenses = 0.0;
		this.actualExpenses = 0.0;
		
		expenses = movementsModel.getExpensesByActivity(idActivity);
		
		DefaultTableModel tmodel = new DefaultTableModel(new String[] {"Concept", "Estimated", "Paid"}, 0);
		for(IncomesExpensesDTO item : expenses)
		{
			double paidAmount = this.movementsModel.getTotalAmountPaid(item.getId());
			double estimatedAmount = Double.parseDouble(item.getAmountEstimated());
			
			tmodel.addRow(new Object[] {
					item.getConcept(), estimatedAmount, paidAmount
			});
			
			this.estimatedExpenses += estimatedAmount;
			this.actualExpenses += paidAmount;
		}
		
		this.view.getExpensesTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(this.view.getExpensesTable());
		
		this.view.getSubTotalExpensesEstimatedField().setText(String.valueOf(this.estimatedExpenses));
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
				String.valueOf(this.estimatedSponsorship + this.estimatedIncome + this.estimatedExpenses)
		);
		
		this.view.getTotalActualIncomeField().setText(
				String.valueOf(this.actualSponsorship + this.actualIncome)
		);
		
		this.view.getTotalActualExpensesField().setText(
				String.valueOf(this.actualExpenses)
		);
		
		this.view.getTotalActualBalanceField().setText(
				String.valueOf(this.actualSponsorship + this.actualIncome + this.actualExpenses)
		);
	}
}