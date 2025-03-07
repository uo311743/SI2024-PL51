package controller;

import util.SwingUtil;
import view.InvoiceManagementView;
import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import model.ActivitiesModel;
import model.InvoicesModel;
import model.SponsorOrganizationsModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InvoiceManagementController {
    
    protected SponsorOrganizationsModel soModel;
    protected ActivitiesModel activitiesModel;
    protected InvoicesModel invoicesModel;
    
    protected InvoiceManagementView view; 
    
    public InvoiceManagementController(SponsorOrganizationsModel som, ActivitiesModel am, InvoicesModel im, InvoiceManagementView v) { 
        this.soModel = som;
        this.activitiesModel = am;
        this.invoicesModel = im;
        
        this.view = v;
        this.initView();
        this.initController();
    }

    public void initController() {
        view.getFilterButton().addActionListener(e -> updateInvoiceTable());
        
        this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
        	}
        });
    }
    
    public void initView() {
        loadSponsors();
        loadActivities();
        showData();
        view.setVisible();
    }
    
    public void loadSponsors() {
        List<Object[]> sponsorList = soModel.getSponsorListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponsorList);
        view.getSponsorComboBox().setModel(lmodel);
    }
    
    public void loadActivities() {
        List<Object[]> activityList = activitiesModel.getActivityListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(activityList);
        view.getActivityComboBox().setModel(lmodel);
    }
    
    public void showData() {
        List<InvoicesDTO> data = invoicesModel.getInvoices();
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Date", "Name", "NIF", "Address", "Amount"}, 0);
        
        for (InvoicesDTO invoice : data) {
            tableModel.addRow(new Object[]{
                invoice.getId(), 
                invoice.getDateIssued(), 
                soModel.getSOByInvoiceId(invoice.getId()).getName(),      
                soModel.getSOByInvoiceId(invoice.getId()).getNif(),      
                soModel.getSOByInvoiceId(invoice.getId()).getAddress(),       
                invoice.getTotalAmount()    
            });
        }
        
        view.getInvoiceTable().setModel(tableModel);
    }
    
    public void updateInvoiceTable() {
    	SponsorOrganizationsDTO selectedSponsor = soModel.getSponsorByName(String.valueOf(view.getSponsorComboBox().getSelectedItem()));
        ActivitiesDTO selectedActivity = activitiesModel.getActivityByName(String.valueOf(view.getActivityComboBox().getSelectedItem()));
        
        List<InvoicesDTO> data = invoicesModel.getInvoicesBySponsorAndActivity(selectedSponsor.getId(), selectedActivity.getId());
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Date", "Name", "NIF", "Address", "Amount"}, 0);
        
        for (InvoicesDTO invoice : data) {
            tableModel.addRow(new Object[]{
                invoice.getId(), 
                invoice.getDateIssued(), 
                soModel.getSOByInvoiceId(invoice.getId()).getName(),      
                soModel.getSOByInvoiceId(invoice.getId()).getNif(),      
                soModel.getSOByInvoiceId(invoice.getId()).getAddress(), 
                invoice.getTotalAmount()    
            });
        }
        
        view.getInvoiceTable().setModel(tableModel);
    }
}
