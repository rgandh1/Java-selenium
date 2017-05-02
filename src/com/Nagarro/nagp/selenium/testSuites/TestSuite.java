package com.Nagarro.nagp.selenium.testSuites;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

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

	//Page objects 
	
	
	//Page objects ends
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
		
		sTestCaseName=method.getName();
		Object[][] dataProviderObject=null;
				
		log.info("testCaseName  "+sTestCaseName);
		log.info(testData);
		Map<String, ArrayList<String>> testCaseData=testData.get(sTestCaseName);
		
		ArrayList<String> arrayList;
		
		if (testData.containsKey("N"))
		{
			skipTestCase="Y";
			arrayList=testCaseData.get("N");
		}
		else
		{
			skipTestCase="N";
			arrayList=testCaseData.get("Y");
			log.info(arrayList + "test data for testcase");
		}
		dataProviderObject = new String[1][arrayList.size()];
				
		for (int i = 0; i < arrayList.size(); i++) {
		    ArrayList<String> row=new ArrayList<String>();
		    row.add(arrayList.get(i));
		    //log.info(arrayList.get(i));
		    dataProviderObject[0][i] = arrayList.get(i).toString();
		}
		//log.info("------------------------------------"+dataProviderObject);
		return (dataProviderObject);
				
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
	public void TC01(String sUserName, String sPassword) 
	{
		
		if (skipTestCase=="Y")
		{
			
			log.info("testcase skipped as runMode = N");
			throw new SkipException("tescase skipped");
		}
		Assert.assertTrue(login.verifyPagePresent(),"login page is not opened");
		
		login.signin(sUserName, sPassword);
		
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
	public  void TC02(String Passengers, String departingFrom, String arrivingTo) throws InterruptedException 
	{
		
		if (skipTestCase=="Y") 
			{
			log.info("testcase skipped as runMode = N");
			throw new SkipException("tescase skipped");
			}
		
		homepage.enterJourneyDetails(Passengers, departingFrom, arrivingTo);
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
	public void TC03(String departingFlight, String arrivingAt) throws InterruptedException 
	{
		//select departingFlight for trip
		// select arrivingAt for trip
		// Press continue button
		//verify that user is moved to next screen 
		
		if (skipTestCase=="Y") {
		throw new SkipException("tescase skipped");}
		selectflight.flightSelection(departingFlight, arrivingAt);
		Assert.assertTrue(bookflight.verifyPagePresent());
	}
	
	
}
