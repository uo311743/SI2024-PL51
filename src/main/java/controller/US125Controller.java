package controller;

import model.Model;
import view.AbstractView;
import view.US125View;
import javax.swing.*;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class US125Controller extends AbstractController {
    
    public US125Controller(Model m, AbstractView v) {
        super(m, v);
    }

    @Override
    public void initController() {
        ((AbstractView) super.view).getButtonLowRight().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendInvoiceEmail();
            }
        });
        
        ((AbstractView) super.view).getButtonLowLeft().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToMainView();
            }
        });
    }
    
    @Override
    public void initView() {
        ((AbstractView) super.view).getFrame().setVisible(true);
    }
    
    private void sendInvoiceEmail() {
        String recipientEmail = ((US125View) view).getEmailTextField().getText();
        if (recipientEmail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String invoiceData = extractInvoiceData();
        String subject = "Invoice Details";
        String body = "Here are the details of your invoice:\n\n" + invoiceData;

        try {
            sendEmail(recipientEmail, subject, body);
            JOptionPane.showMessageDialog(null, "Invoice sent successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to send invoice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String extractInvoiceData() {
        StringBuilder sb = new StringBuilder();
        JTable table = ((US125View) view).getInvoiceTable();
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                sb.append(table.getValueAt(i, j)).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private void sendEmail(String to, String subject, String body) throws Exception {
        final String from = "your-email@example.com";
        final String password = "your-email-password";
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        
        Transport.send(message);
    }
    
    private void goBackToMainView() {
        view.getFrame().dispose();
    }
}
