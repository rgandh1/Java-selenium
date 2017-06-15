package com.Nagarro.nagp.selenium.testSuites;

/*
 * Purpose of this class is to create test suite
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.InvokedMethod;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;
import com.Nagarro.nagp.selenium.pages.Bookflight;
import com.Nagarro.nagp.selenium.pages.Homepage;
import com.Nagarro.nagp.selenium.pages.Login;
import com.Nagarro.nagp.selenium.pages.SelectFlight;
import com.Nagarro.nagp.selenium.seleniumSupport.Selenium;


public class TestSuite extends SetupTearDown {

	private static Logger log = Logger.getLogger(TestSuite.class.getName());
	
	
	private Login login = new Login();
	private static Homepage homepage = new Homepage();
	private SelectFlight selectflight = new SelectFlight();
	private Bookflight bookflight = new Bookflight();
	
	/*
		Method name:= commonDataProvider
		Author:= Rahul Gandhi
		Purpose:= provides data to calling test method
		Flow:
			- get name of calling Testcase method
			- get test data object from testData(Hashmap which have all test data stored
			- returns object as a dataprovider.
	*/
	@DataProvider(name="dataProvider")
	public static Object[][] commonDataProvider(Method method) throws Exception
	{
		testDataSet = new Hashtable<String, String>();
		sTestCaseName=method.getName();
		Object[][] dataProviderObject=null;
				
		log.info("testCaseName  "+sTestCaseName);
		log.info(testData);
		
		@SuppressWarnings("unchecked")
	    Enumeration<String> enums = (Enumeration<String>) testData.propertyNames();
	    while (enums.hasMoreElements())       
		
		{
		     		  
		     String lKey = enums.nextElement();
		     String lValue = testData.getProperty(lKey);
		     System.out.println(lKey);
		     	
		     String testCaseName = lKey.substring(0, 4);
		     String sParameterValue = lKey.substring(5);
		     log.info(testCaseName);
		     log.info(sParameterValue +"sParameterValue");
		     log.info(lValue);
		     if (sTestCaseName.equalsIgnoreCase(testCaseName))
			     {
		    	 
			     testDataSet.put(sParameterValue, lValue);
			     }
		     
		 }
	    Object[][] dataSetCollection = new Object[1][1];
		dataSetCollection[0][0] = testDataSet;
		return dataSetCollection;
			     
		}
		
		
	
	/*
	Method name:= TC01
	Author:= Rahul Gandhi
	Purpose:= Login to AUT
	Flow:
		- get test data from dataprovider
		- enters username and password into required fields
		- fields are identified through xpath, xpaths are stored in "objectRepository.properties" 
*/
	@SuppressWarnings("static-access")
	@Test(dataProvider = "dataProvider",testName="TC01")
	public void TC01(Hashtable<String, String> testData) 
	{
		
		Assert.assertTrue(login.verifyPagePresent(),"login page is not opened");
		homepage = login.signin(testData);
		Assert.assertTrue(homepage.verifyPagePresent(),"unable to login");
				
	}
	
	/*Method name:= TC02
			Author:= Rahul Gandhi
			Purpose:= select number of passengers, departing location and Arriving location to book a flight
			Flow:
				- get test data from dataprovider
				- enters number of passenegers , arriving from and to
				- fields are identified through xpath, xpaths are stored in "objectRepository.properties" 
		*/			
	@SuppressWarnings("static-access")
	@Test(dataProvider = "dataProvider",testName="TC02", dependsOnMethods="TC01", description="Test case to create a one way trip for given number of passengers and locations")
	public  void TC02(Hashtable<String, String> testData) throws InterruptedException 
	{		
		selectflight = homepage.enterJourneyDetails(testData);
		Assert.assertTrue(selectflight.verifyPagePresent(), "Select flight page not opened");
	
	}
	
	/*Method name:= TC03
	Author:= Rahul Gandhi
	Purpose:= select desired flights
	Flow:
		- get test data from dataprovider
		- select desired flights
		- fields are identified through xpath, xpaths are stored in "objectRepository.properties" 
*/	
	@SuppressWarnings("static-access")
	@Test(dataProvider = "dataProvider",testName="TC03", dependsOnMethods="TC02", description="Test case to verify user is able to select flights for the trip")
	public void TC03(Hashtable<String, String> testData) throws InterruptedException 
	{
		//select departingFlight for trip
		// select arrivingAt for trip
		// Press continue button
		//verify that user is moved to next screen 
		
		bookflight = selectflight.flightSelection(testData);
		Assert.assertTrue(bookflight.verifyPagePresent());
	}
	
	
}
