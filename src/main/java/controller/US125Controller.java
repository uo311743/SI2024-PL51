package controller;

import model.Model;
import view.US125View;
import javax.swing.*;
import javax.swing.table.TableModel;
import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
import homework.run.view.ViewAthletes;
import homework.utils.CompetitionDTO;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

public class US125Controller {
    
    protected Model model;
    protected US125View view; 
    
    public US125Controller(Model m, US125View v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }

    public void initController() {
        view.getButtonLowRight().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	/*
                String recipientCountry = model.getInvoiceRecipientCountry(); // Assuming model provides this
                if (isForeignCountry(recipientCountry)) {
                    JOptionPane.showMessageDialog(null, "Sending invoices to foreign countries is restricted.", "Restriction", JOptionPane.WARNING_MESSAGE);
                } 
                else {
                    openOutlook();
                }
                */
            }
        });
        
        view.getButtonLowLeft().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.disposeView();
            }
        });
    }
    
    public void initView() {
        loadSponsors();
        loadActivities();
        getInvoiceDetails();
        view.setVisible();
    }
    
    public void openOutlook() {
        try {
            Desktop.getDesktop().browse(new URI("https://outlook.live.com"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to open Outlook: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isForeignCountry(String countryCode) {
        return countryCode != null && !countryCode.equalsIgnoreCase("ES");
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
    
    public void getInvoiceDetails() {
        List<InvoicesDTO> date = model.getDateIssueInvoices();
        List<InvoicesDTO> invoiceId = model.getIdInvoices();
        
        // CONSTANTS NOT DONE
        String recipientName = model.getRecipientName(); // Assuming this method exists in Model
        String recipientFiscalNumber = model.getRecipientFiscalNumber(); // Assuming this method exists in Model
        String recipientAddress = model.getRecipientAddress(); // Assuming this method exists in Model
        
        String invoiceDetails = "Invoice Date: " + date + "\n" +
                                "Invoice ID: " + invoiceId + "\n" +
                                "Recipient Name: " + recipientName + "\n" +
                                "Fiscal Number: " + recipientFiscalNumber + "\n" +
                                "Address: " + recipientAddress;
        
        // view.getInvoice().setText(invoiceDetails); // Assuming setText method exists
        view.getInvoiceTable().setToolTipText(invoiceDetails);
    }
}
