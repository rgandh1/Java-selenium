package com.Nagarro.nagp.selenium.frameworkSupport;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;


import com.Nagarro.nagp.selenium.seleniumSupport.Selenium;
import com.Nagarro.nagp.selenium.seleniumSupport.SeleniumDriver;

/* 
 * Purpose of this class is to setup environment for test execution
 * 	Author : Rahul Gandhi
 * 
 */

public class SetupTearDown extends ConfigVariable {

	private static String projectPath;		//path to Project folder e.g. <Path to Workspace>/NAGP_Selenium_1-RahulGandhi-4470/
	
	private static String browserName;
	private static String autUrl;
	private static String implicitWait;
	
	private static String testRunHistoryFolderPath;
	private static String testDataFolderPath;
	public static String sTestCaseName="";
	public static String skipTestCase="";
	
	public static Properties OR = new Properties();
	private static Properties testConfig = new Properties();
	//public static HashMap<String, Map<String, ArrayList<String>>> testData;
	private static HashMap<String, Integer> rowNum;
	private static HashMap<String, String> testResults=new HashMap<String, String>();
	private static Workbook workbook=null;
	public static Hashtable<String, String> testDataSet = null;
	
	public static Selenium selenium;
	
	 private static Logger log = Logger.getLogger(SetupTearDown.class.getName());
	
	 
	 public static Properties testData = new Properties();
	 
	 /*
	  * Purpose : Purpose of this method is to capture all required paths for the execution
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	 private static void getRequiredPaths()
		{
			log.info("fetching all paths");
			getProjectPath();
			getPathOfDrivers();
			getTestDataPath();
		
		}
	
	 /*
	  * Purpose : Purpose of this method is to capture project path
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	 private static void getProjectPath()
	{
		projectPath = Selenium.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		log.info("projectPath"+projectPath);
		int pos = projectPath.lastIndexOf("bin/");
	    projectPath = projectPath.substring(1,pos);

	}
	
	 /*
	  * Purpose : Purpose of this method is to load configurations for project
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	 
	private static void getTestConfigurations()
	{
		File file = new File(projectPath+testConfigurationsFolder + testConfigurationsFile );
		  
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.toString());
		}
			
		//load properties file
		try {
			testConfig.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		log.info("Reading configuration values from "+projectPath+testConfigurationsFolder + testConfigurationsFile);
	}

				
	/*
	  * Purpose : Purpose of this method is to get path of browser servers
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	
	private static void getPathOfDrivers()
	{
		
	    log.info("loading selenium browser server from --" + browserServerFolder);
		SeleniumDriver.pathOfDrivers = projectPath.concat(browserServerFolder);
	    SeleniumDriver.pathOfDrivers = SeleniumDriver.pathOfDrivers.replace("/", "//");
	    
	}
	/*
	  * Purpose : Purpose of this method is to capture path of testData folder
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	
	private static void getTestDataPath()
	{
		log.info("loading testDatafrom --" + testDataFolder);
		testDataFolderPath = projectPath.concat(testDataFolder);
	    testDataFolderPath = testDataFolderPath.replace("/", "//");
	   // log.info(testDataFolderPath);
		
	}


	/*
	  * Purpose : Purpose of this method is to load testData property file
	  *	@Params: Null
	  * @Returns : Nothing
	  */
	public static void loadTestDataPropertyFile()
	{
		File file = new File(projectPath+ testDataFolder + "testData.properties" );
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.toString());
		}
			
		//load properties file
		try {
			testData.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		log.info("Reading testdata values from "+projectPath+testDataFolder + "testData.properties");
		
	}
	
	
	@BeforeSuite
	public static void setUp() 
	{
		//load all required paths and folders
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		log.info("=======================================================================================");
		log.info("Starting execution of of test suite");
		log.info("=======================================================================================");
		
		getRequiredPaths();		
		getTestConfigurations();
		autUrl=testConfig.getProperty("Url").trim();
		browserName=testConfig.getProperty("browser").trim();
		implicitWait=testConfig.getProperty("globalWaitTime").trim();
		log.info("autUrl  " + autUrl +" browserName   " + browserName + " implicitWait "+ implicitWait);
		
		//initialize webdriver(selenium) for given url of AUT(application under test
		selenium=new Selenium(browserName.trim(), Integer.parseInt(implicitWait.trim()));
		selenium.openBrowser(autUrl);
		
		loadTestDataPropertyFile();
	}
	
	@AfterSuite
	public static void tearDown() {
		log.info("=======================================================================================");
		log.info("completing execution of test suite");
		log.info("=======================================================================================");
		Selenium.closeBrowsers();
		log.info(testResults);
		
	}

	//below function is not used in current setup
	
		 @BeforeMethod(alwaysRun=true)
	 public static void handleTestMethodName(java.lang.reflect.Method method) 
	    {
		 log.info("---------------------------------------------------------------------");
		 log.info("Starting test case " + method);
		 log.info("Value of skip testcase ="+ skipTestCase);
		 
		 //log.info("IN Before test");
		 if (skipTestCase.equalsIgnoreCase("Y"))
		 {
			 
			 log.info("skipping test case as test case is marked not to execute");
			 throw new SkipException("as test case is marked not to execute");
		 }
		 
		  
		 //log.info("----"+sTestCaseName);
	    }
		 
			

}// end of class
	
