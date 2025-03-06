package controller;

import model.InvoiceManagementModel;
import util.Params;
import util.SwingUtil;
import view.InvoiceManagementView;
import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InvoiceManagementController {
    
    protected InvoiceManagementModel model;
    protected InvoiceManagementView view; 
    
    public InvoiceManagementController(InvoiceManagementModel m, InvoiceManagementView v) { 
        this.model = m;
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
        List<Object[]> sponsorList = model.getSponsorListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(sponsorList);
        view.getSponsorComboBox().setModel(lmodel);
    }
    
    public void loadActivities() {
        List<Object[]> activityList = model.getActivityListArray();
        ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(activityList);
        view.getActivityComboBox().setModel(lmodel);
    }
    
    public void showData() {
        List<InvoicesDTO> data = model.getInvoices();
        
        Params params = new Params();
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Date", "Name", "NIF", "Address", "Amount"}, 0);
        
        for (InvoicesDTO invoice : data) {
            tableModel.addRow(new Object[]{
                invoice.getId(), 
                invoice.getDateIssued(), 
                params.getTaxName(),      
                params.getTaxNif(),     
                params.getTaxAddress(), 
                invoice.getTotalAmount()    
            });
        }
        
        view.getInvoiceTable().setModel(tableModel);
    }
    
    public void updateInvoiceTable() {
    	SponsorOrganizationsDTO selectedSponsor = model.getSponsorByName(String.valueOf(view.getSponsorComboBox().getSelectedItem()));
        ActivitiesDTO selectedActivity = model.getActivityByName(String.valueOf(view.getActivityComboBox().getSelectedItem()));
        
        List<InvoicesDTO> data = model.getInvoicesBySponsorAndActivity(selectedSponsor.getId(), selectedActivity.getId());
        
        Params params = new Params();
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Date", "Name", "NIF", "Address", "Amount"}, 0);
        
        for (InvoicesDTO invoice : data) {
            tableModel.addRow(new Object[]{
                invoice.getId(), 
                invoice.getDateIssued(), 
                params.getTaxName(),      
                params.getTaxNif(),     
                params.getTaxAddress(), 
                invoice.getTotalAmount()    
            });
        }
        
        view.getInvoiceTable().setModel(tableModel);
    }
}
