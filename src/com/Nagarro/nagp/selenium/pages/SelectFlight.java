package com.Nagarro.nagp.selenium.pages;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class SelectFlight extends SetupTearDown {

	private static Logger log = Logger.getLogger(SelectFlight.class.getName());
	
	//-----------------------------------------------------------------------------------------------------------
	private String img_selectFlight_xpath = "//img[@src='/images/masts/mast_selectflight.gif']" ;

	private String rbtn_flightSelection_xpath = "//table//tr[td[font[b[contains(text(),'%s')]]]]//input[@type='radio']";
	private String btn_continue_xpath = "//*[@name='reserveFlights']" ;

	private String img_bookFlight_xpath = "//*[@src='/images/masts/mast_book.gif']" ;
	
	@SuppressWarnings("static-access")
	public boolean verifyPagePresent()
	{
		log.info("verifying SelectFlight page is present");
		return selenium.isElementPresent(By.xpath(img_selectFlight_xpath));
	}
	
	public Bookflight flightSelection(Hashtable<String, String> testData)
	{
		String sXpath=rbtn_flightSelection_xpath;
		String departingFromXpath= String.format(sXpath, testData.get("departingFlight"));
		String arrivingAtXpath= String.format(sXpath, testData.get("arrivingAt"));
		log.info(departingFromXpath);
		
		selenium.click_webElement(departingFromXpath);
		selenium.click_webElement(arrivingAtXpath);
		selenium.click_webElement(btn_continue_xpath);
		Bookflight bookflight = new Bookflight();
		return bookflight;
		
	}
}
