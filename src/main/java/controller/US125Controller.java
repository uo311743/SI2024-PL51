package controller;

import model.US125Model;
import util.Params;
import view.US125View;
import DTOs.InvoicesDTO;
import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class US125Controller {
    
    protected US125Model model;
    protected US125View view; 
    
    public US125Controller(US125Model m, US125View v) { 
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
    	String selectedSponsor = model.getSponsorIdByName(String.valueOf(view.getSponsorComboBox().getSelectedItem()));
        String selectedActivity = model.getActivityIdByName(String.valueOf(view.getActivityComboBox().getSelectedItem()));
        
    	List<InvoicesDTO> data = model.getInvoicesBySponsorAndActivity(selectedSponsor, selectedActivity);
        
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
