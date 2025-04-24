package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Params
{
    private static final String CONFIG_FILE = "src/main/resources/parameters.properties";
    private Properties properties;

    public Params()
    {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) { properties.load(fis); }
        catch (IOException e)
        {
            throw new RuntimeException("Error loading properties file: " + CONFIG_FILE, e);
        }
    }

    // Get tax info
    public String getTaxName() { return this.getPropertyString("taxinfo.name"); }
    public String getTaxNif() { return this.getPropertyString("taxinfo.nif"); }
    public String getTaxAddress() { return this.getPropertyString("taxinfo.address"); }
    
    public int getTaxExpDays()
    {
    	int tmp;
    	try { tmp = Integer.parseInt(this.getPropertyString("taxinfo.expdays")); }
    	catch (NumberFormatException e)
    	{
    		throw new ApplicationException("Parameter taxinfo.expdays must be an int in file: " + CONFIG_FILE);
		}
    	
        return tmp;
    }
    
    public double getTaxVAT() {
    	double tmp;
    	try { tmp = Double.parseDouble(this.getPropertyString("taxinfo.vat")); }
    	catch (NumberFormatException e)
    	{
    		throw new ApplicationException("Parameter taxinfo.vat must be a double in file: " + CONFIG_FILE);
		}
    	
        return tmp;
    }

    // Get email configuration
    public String get_mail_smtp_host() { return this.getPropertyString("mail.smtp.host"); }
    public String get_mail_smtp_port() { return this.getPropertyString("mail.smtp.port"); }
    public String get_mail_smtp_auth() { return this.getPropertyString("mail.smtp.auth"); }
    public String get_mail_smtp_starttls() { return this.getPropertyString("mail.smtp.starttls"); }
    
    public String get_mail_auth_username() { return this.getEnvVar("COIIPA_SMTP_AUTH_username"); }
    public String get_mail_auth_passwd() { return this.getEnvVar("COIIPA_SMTP_AUTH_passd"); }

    public String get_mail_from() { return this.getPropertyString("mail.from"); }
    
    // ---------------------------------------------------------------------------------------------------
    
    private String getPropertyString(String name) {
    	String tmp = properties.getProperty(name);
    	if(tmp == null)
    		throw new ApplicationException("Parameter " + name + " not found in file: " + CONFIG_FILE);
        return tmp;
    }
    
    private String getEnvVar(String name) {
    	String tmp = System.getenv(name);
    	if(tmp == null)
    		throw new ApplicationException("Env variable " + name + " does not exist.");
        return tmp;
    }
}