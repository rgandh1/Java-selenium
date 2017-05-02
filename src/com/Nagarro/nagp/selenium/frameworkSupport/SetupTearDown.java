package com.Nagarro.nagp.selenium.frameworkSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.Nagarro.nagp.selenium.excelSupport.Excel;
import com.Nagarro.nagp.selenium.seleniumSupport.Selenium;
import com.Nagarro.nagp.selenium.seleniumSupport.SeleniumDriver;

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
	public static HashMap<String, Map<String, ArrayList<String>>> testData;
	private static HashMap<String, Integer> rowNum;
	private static HashMap<String, String> testResults=new HashMap<String, String>();
	private static Workbook workbook=null;
	
	public static Selenium selenium;
	
	 private static Logger log = Logger.getLogger(SetupTearDown.class.getName());
	 
	 private static void getRequiredPaths()
	{
		log.info("fetching all paths");
		getProjectPath();
		getPathOfDrivers();
		getTestDataPath();
		getTestRunHistoryFolderPath();
	}
	
	
	 private static void getProjectPath()
	{
		projectPath = Selenium.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		log.info("projectPath"+projectPath);
		int pos = projectPath.lastIndexOf("bin/");
	    projectPath = projectPath.substring(1,pos);
		//return projectPath;
		
	}
	
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

				
	
	
	private static void getPathOfDrivers()
	{
		
	    log.info("loading selenium browser server from --" + browserServerFolder);
		SeleniumDriver.pathOfDrivers = projectPath.concat(browserServerFolder);
	    SeleniumDriver.pathOfDrivers = SeleniumDriver.pathOfDrivers.replace("/", "//");
	    
	}

	private static void getTestDataPath()
	{
		log.info("loading testDatafrom --" + testDataFolder);
		testDataFolderPath = projectPath.concat(testDataFolder);
	    testDataFolderPath = testDataFolderPath.replace("/", "//");
	   // log.info(testDataFolderPath);
		
	}


	private static void getTestRunHistoryFolderPath()
	{
		log.info("test will store excel results in : " + testRunHistoryFolder);
		testRunHistoryFolderPath = projectPath.concat(testRunHistoryFolder);
		testRunHistoryFolderPath = testRunHistoryFolderPath.replace("/", "//");
	   // log.info(testRunHistoryFolderPath);
		
	}

	@BeforeSuite
	public static void setUp() {
		//load all required paths and folders
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		log.info("=======================================================================================");
		log.info("Starting execution of of test suite");
		log.info("=======================================================================================");
		
		getProjectPath();
		getPathOfDrivers();
		getTestDataPath();
		getTestRunHistoryFolderPath();
		//load object repository
		
		//loadObjectRepository();
		
		workbook=Excel.openWorkBook(testDataFolderPath + testDataFilename );
		getTestConfigurations();
		autUrl=testConfig.getProperty("Url").trim();
		browserName=testConfig.getProperty("browser").trim();
		implicitWait=testConfig.getProperty("globalWaitTime").trim();
		log.info("autUrl  " + autUrl +" browserName   " + browserName + " implicitWait "+ implicitWait);
		//initialize webdriver(selenium) for given url of AUT(application under test
		selenium=new Selenium(browserName.trim(), Integer.parseInt(implicitWait.trim()));
		selenium.openBrowser(autUrl);
		
		testData=Excel.getTestData(workbook, testDataSheetName);
		rowNum=Excel.getTestCaseRowNum(workbook, testDataSheetName);
		
	}
	
	@AfterSuite
	public static void tearDown() {
		log.info("=======================================================================================");
		log.info("completing execution of test suite");
		log.info("=======================================================================================");
		Selenium.closeBrowsers();
		log.info(testResults);
		testResultHandler();
	}

	//below function is not used in current setup
	private static void loadTestData() {
		
		Workbook workbook=Excel.openWorkBook(testDataFolderPath + testDataFilename );
		
		autUrl=Excel.getCellData(workbook, testDataSheetName, "Url", 2);
		browserName=Excel.getCellData(workbook, testDataConfigSheetName, "Browser", 2);
		implicitWait=Excel.getCellData(workbook, testDataConfigSheetName, "Global WaitTime", 2);
		
		log.info("browserName: " + browserName);
		log.info("autUrl: " + autUrl);
		log.info("Global WaitTime: " + implicitWait);
	}
	//below function is not used in current setup
	private static void loadObjectRepository(){
	
		File file = new File(projectPath+ORFilename);
	  
	FileInputStream fileInput = null;
	try {
		fileInput = new FileInputStream(file);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
		
	//load properties file
	try {
		OR.load(fileInput);
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

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
	
	 @AfterMethod
public void afterMethod(ITestResult result, java.lang.reflect.Method method )
	 {
		 log.info("---------------------------------------------------------------------");
		 log.info("ending test case " + method);
		 
	     try
	  {
	     if(result.getStatus() == ITestResult.SUCCESS)
	     {

	    	 testResults.put(method.getName(), "Pass");
	         log.info("passed **********");
	     }

	     else if(result.getStatus() == ITestResult.FAILURE)

	     {
	          //Do something here
	    	 testResults.put(method.getName(), "Fail");
	    	 log.info("Failed ***********");

	     }

	      else if(result.getStatus() == ITestResult.SKIP ){
	    	  
	    	  testResults.put(method.getName(), "SKIPPED");
	         log.info("Skiped***********");

	     }
	 }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }

	 }
	 
	 public static void testResultHandler()
	 {	
		 //log.info(testResults);
		 
		 for (String key: testResults.keySet())
		 {
			 int rowNumber=rowNum.get(key)+1;
			 String data=testResults.get(key);
			 log.info(data + "----");
			 Excel.setCellData(workbook, testDataSheetName, 6, rowNumber, data);
			 
		 }
		 log.info("saving excel file");
		 String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
		 
		 Excel.saveExcelFile(workbook, testRunHistoryFolder+"//" + timeStamp + ".xlsx");
	 }

}// end of class
	


