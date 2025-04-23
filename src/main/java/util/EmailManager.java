package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.util.Properties;

import DTOs.ActivitiesDTO;
import DTOs.SponsorContactsDTO;
import DTOs.SponsorOrganizationsDTO;


public class EmailManager {
	
	private String SMTP_auth, SMTP_starttls, SMTP_host, SMTP_port;
	private String AUTH_username, AUTH_passd;
	private String emailFrom, emailTo;
	private String subject, body, filePath;
	
	private static String FILENAME = "Invoice.pdf";
	
	public EmailManager(SponsorContactsDTO contact, SponsorOrganizationsDTO sponsor, ActivitiesDTO activity, String filepath)
	{
		// Class for reading params
		Params params = new Params();
		
		// Connection details
		this.SMTP_auth = params.get_mail_smtp_auth();
		this.SMTP_starttls = params.get_mail_smtp_starttls();
		this.SMTP_host = params.get_mail_smtp_host();
		this.SMTP_port = params.get_mail_smtp_port();
		
		// Session authentification details
		this.AUTH_username = params.get_mail_auth_username();
		this.AUTH_passd = params.get_mail_auth_passwd();
		
		// Email data
		this.emailFrom = params.get_mail_from();
		this.emailTo = contact.getEmail();
		
		// Email contents
		this.subject = "Invoice for " + activity.getName();
		this.body = ""
				+ "<p>Dear " + contact.getName() + ",</p>"
				+ "<p>I hope this message finds you well. "
				+ "Please find attached the invoice related to our sponsorship agreement with <strong>"
				+ sponsor.getName() + "</strong> for the activity "
				+ activity.getName() + " Edition " + activity.getEdition()+ "</strong>.</p>"
				+ "The activity is scheduled to take place between " + activity.getDateStart() + " and "
				+ activity.getDateEnd() + " in " + activity.getPlace() + "</p>"
				+ "<p>Best wishes,</p><p>" + params.getTaxName() + "</p>";
		
		// Path to the PDF to send
		this.filePath = filepath;
	}
	
	public boolean sendEmail()
	{
		// SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.auth"				, this.SMTP_auth);
        props.put("mail.smtp.starttls.enable"	, this.SMTP_starttls);
        props.put("mail.smtp.host"				, this.SMTP_host);
        props.put("mail.smtp.port"				, this.SMTP_port);

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AUTH_username, AUTH_passd);
            }
        });

        try {
            // Create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            // Email body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html; charset=utf-8");

            // Create multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // PDF attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(FILENAME);

            // Add attachment
            multipart.addBodyPart(attachmentPart);

            // Combine parts
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            
            System.out.println("Email sent: " + emailFrom + " -> " + emailTo);
            
        } catch (MessagingException e) {
        	e.printStackTrace();
            return false;
        }
        
        return true;
	}
}
