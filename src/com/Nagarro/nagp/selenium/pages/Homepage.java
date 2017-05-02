package com.Nagarro.nagp.selenium.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class Homepage extends SetupTearDown{

	private static Logger log = Logger.getLogger(Homepage.class.getName());
	
	private String img_flightFinder_xpath="//*[@src='/images/masts/mast_flightfinder.gif']";
    private String rbtn_roundtrip_xpath="//*[@name='tripType' and @value='roundtrip']";
    private String rbtn_oneway_xpath="//*[@name='tripType' and @value='oneway']";
    private String drp_passengers_xpath = "//select[@name='passCount']" ;
	private String drp_departingfrom_xpath = "//select[@name='fromPort']" ; 
	private String drp_fromMonth_xpath = "//select[@name='fromMonth']" ;
	private String drp_fromDay_xpath = "//select[@name='fromDay']" ;
	private String drp_arrivingIn_xpath = "//select[@name='toPort']" ;
	private String btn_contibue_xpath = "//input[@name='findFlights']" ;
    
    @SuppressWarnings("static-access")
	public boolean verifyPagePresent()
	{
    	log.info("applicationLOG :");
    	log.info("verifying Home page is opened");
    	return selenium.isElementPresent(By.xpath(img_flightFinder_xpath));
    	
    }
    public void enterJourneyDetails(String Passengers, String departingFrom, String arrivingTo)
    {
    	//select oneway trip
    	//select number of passengers as given in parameters
    	// select arriving from and to as given in parameters
    log.info("applicationLOG :");
    log.info("entering passenger details" + Passengers);
    log.info("entering departingFrom details" + departingFrom);
    log.info("entering arrivingTo details" + arrivingTo);
    selenium.click_webElement(rbtn_oneway_xpath);
	selenium.selectValueFromDropDown(drp_passengers_xpath,Passengers);
	selenium.selectValueFromDropDown(drp_departingfrom_xpath, departingFrom);
	selenium.selectValueFromDropDown(drp_arrivingIn_xpath, arrivingTo);
	selenium.click_webElement(btn_contibue_xpath);
	
    }
	
}
