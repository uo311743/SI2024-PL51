package controller;

import model.US125Model;
import view.US125View;
import DTOs.InvoicesDTO;
import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;
import java.net.URI;
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
        view.getButtonLowRight().addActionListener(e -> openOutlook());
        view.getButtonLowLeft().addActionListener(e -> view.disposeView());
        view.getFilterButton().addActionListener(e -> updateInvoiceTable());
    }
    
    public void initView() {
        loadSponsors();
        loadActivities();
        view.setVisible();
    }
    
    // Pendiente de arreglo
    public void openOutlook() {
        try {
            Desktop.getDesktop().browse(new URI("https://outlook.live.com"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to open Outlook: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    
    public void updateInvoiceTable() {
        String selectedSponsor = (String) view.getSponsorComboBox().getSelectedItem();
        String selectedActivity = (String) view.getActivityComboBox().getSelectedItem();
        
        List<InvoicesDTO> filteredInvoices = model.getInvoicesBySponsorAndActivity(selectedSponsor, selectedActivity);
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Date", "Amount"}, 0);
        for (InvoicesDTO invoice : filteredInvoices) {
            tableModel.addRow(new Object[]{invoice.getIdInvoices(), invoice.getDateIssueInvoices(), model.getAmountByIdInvoices(invoice.getIdInvoices())});
        }
        
        view.getInvoiceTable().setModel(tableModel);
    }
}
