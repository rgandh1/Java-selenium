package com.Nagarro.nagp.selenium.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class Login extends SetupTearDown {
	
	private static Logger log = Logger.getLogger(Login.class.getName());
	
	private String tbx_userName_xpath="//*[@name='userName']" ;
	private String tbx_password_xpath="//*[@name='password']" ;
	private String btn_signin_xpath="//*[@name='login']" ;
	
	public  void signin(String sUserName, String sPassword)
	{
		log.info("applicationLOG :");
		selenium.enter_Text(tbx_userName_xpath, sUserName);
		selenium.enter_Text(tbx_password_xpath, sPassword);
		selenium.click_webElement(btn_signin_xpath);
	
	}
	public boolean verifyPagePresent()
    {
		log.info("applicationLOG :");
		log.error("verifying Login page is opened");
		
    	return selenium.isElementPresent(By.xpath(tbx_userName_xpath));
    	
    }
    
}

