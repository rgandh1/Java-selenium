package com.Nagarro.nagp.selenium.pages;
/* 
 * Purpose of this class is to store all objects and methods related to Login page
 * 	Author : Rahul Gandhi
 * 
 */


import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.net.NetworkInterface;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class Login extends SetupTearDown {
	
	private static Logger log = Logger.getLogger(Login.class.getName());
	
	// ----------------Object repository for Login Page--------------------------
	private String tbx_userName_xpath="//*[@name='userName']" ;
	private String tbx_password_xpath="//*[@name='password']" ;
	private String btn_signin_xpath="//*[@name='login']" ;
	
	/* 
	 * Purpose of this method is to signin on login page
	 * 	Author : Rahul Gandhi
	 * 
	 */

	
	public  Homepage signin(Hashtable<String, String> testData)
	{
		log.info("applicationLOG :");
		selenium.enter_Text(tbx_userName_xpath, testData.get("userName"));
		selenium.enter_Text(tbx_password_xpath, testData.get("password"));
		selenium.click_webElement(btn_signin_xpath);
		
		Homepage homepage = new Homepage();
		return homepage;
	
	}
	public boolean verifyPagePresent()
    {
		log.info("applicationLOG :");
		log.error("verifying Login page is opened");
		
    	return selenium.isElementPresent(By.xpath(tbx_userName_xpath));
    	
    }
    
}

