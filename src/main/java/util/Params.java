package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Params {
    private static final String CONFIG_FILE = "src/main/resources/parameters.properties";
    private Properties properties;

    public Params() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + CONFIG_FILE, e);
        }
    }

    // Get tax info
    public String getTaxName() {
    	String tmp = properties.getProperty("taxinfo.name");
    	if(tmp == null)
    		throw new ApplicationException("Parameter taxinfo.name not found in file: " + CONFIG_FILE);
        return tmp;
    }

    public String getTaxNif() {
        String tmp = properties.getProperty("taxinfo.nif");
    	if(tmp == null)
    		throw new ApplicationException("Parameter taxinfo.nif not found in file: " + CONFIG_FILE);
        return tmp;
    }

    public String getTaxAddress() {
        String tmp = properties.getProperty("taxinfo.address");
    	if(tmp == null)
    		throw new ApplicationException("Parameter taxinfo.address not found in file: " + CONFIG_FILE);
        return tmp;
    }

    // Get email configuration
    public String getSmtpHost() {
        String tmp = properties.getProperty("mail.smtp.host");
    	if(tmp == null)
    		throw new ApplicationException("Parameter mail.smtp.host not found in file: " + CONFIG_FILE);
        return tmp;
    }

    public int getSmtpPort() {
        String tmp_str = properties.getProperty("mail.smtp.port");
    	if(tmp_str == null)
    		throw new ApplicationException("Parameter mail.smtp.port not found in file: " + CONFIG_FILE);
    	
    	int tmp_int;
    	try { tmp_int = Integer.parseInt(tmp_str); }
    	catch (NumberFormatException e)
    	{
    		throw new ApplicationException("Parameter mail.smtp.port must be an int in file: " + CONFIG_FILE);
		}
    	
        return tmp_int;
    }

    public boolean isSmtpAuthEnabled() {        
        String tmp_str = properties.getProperty("mail.smtp.auth");
    	if(tmp_str == null)
    		throw new ApplicationException("Parameter mail.smtp.auth not found in file: " + CONFIG_FILE);
    	
    	boolean tmp_bool;
    	try { tmp_bool = Boolean.parseBoolean(tmp_str); }
    	catch (NumberFormatException e)
    	{
    		throw new ApplicationException("Parameter mail.smtp.auth must be a boolean in file: " + CONFIG_FILE);
		}
    	
        return tmp_bool;
    }

    public boolean isStartTlsEnabled() {
        String tmp_str = properties.getProperty("mail.smtp.starttls.enable");
    	if(tmp_str == null)
    		throw new ApplicationException("Parameter mail.smtp.auth not found in file: " + CONFIG_FILE);
    	
    	boolean tmp_bool;
    	try { tmp_bool = Boolean.parseBoolean(tmp_str); }
    	catch (NumberFormatException e)
    	{
    		throw new ApplicationException("Parameter mail.smtp.auth must be a boolean in file: " + CONFIG_FILE);
		}
    	
        return tmp_bool;
    }

    public String getMailFrom() {
        String tmp = properties.getProperty("mail.from");
    	if(tmp == null)
    		throw new ApplicationException("Parameter mail.from not found in file: " + CONFIG_FILE);
        return tmp;
    }

    public String getApiKey() {
        String tmp = properties.getProperty("mail.smtp.api_key");
    	if(tmp == null)
    		throw new ApplicationException("Parameter mail.smtp.api_key not found in file: " + CONFIG_FILE);
        return tmp;
    }
}