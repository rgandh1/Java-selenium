package com.Nagarro.nagp.selenium.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class Bookflight extends SetupTearDown{
	
	private static Logger log = Logger.getLogger(Bookflight.class.getName());
	
	private static String img_bookFlight_xpath = "//*[@src='/images/masts/mast_book.gif']" ;
	
	@SuppressWarnings("static-access")
	public boolean verifyPagePresent()
	{
		log.info("applicationLOG :");
    	log.info("verifying bookflight page is opened");
    	return selenium.isElementPresent(By.xpath(img_bookFlight_xpath));
    	
    }
}
