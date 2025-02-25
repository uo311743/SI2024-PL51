package controller;

import model.Model;
import view.AbstractView;
import javax.swing.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class US125Controller {
    
    protected Model model;
    protected AbstractView view; 
    
    public US125Controller(Model m, AbstractView v) { 
        this.model = m;
        this.view = v;
        this.initView();
        this.initController();
    }

    public void initController() {
        view.getButtonLowRight().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipientCountry = model.getInvoiceRecipientCountry(); // Assuming model provides this
                if (isForeignCountry(recipientCountry)) {
                    JOptionPane.showMessageDialog(null, "Sending invoices to foreign countries is restricted.", "Restriction", JOptionPane.WARNING_MESSAGE);
                } else {
                    openOutlook();
                }
            }
        });
        
        view.getButtonLowLeft().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToMainView();
            }
        });
    }
    
    public void initView() {
        view.setVisible();
    }
    
    private void openOutlook() {
        try {
            Desktop.getDesktop().browse(new URI("https://outlook.live.com"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to open Outlook: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBackToMainView() {
        view.disposeView();
    }

    private boolean isForeignCountry(String countryCode) {
        return countryCode != null && !countryCode.equalsIgnoreCase("ES");
    }
}
