package com.Nagarro.nagp.selenium.seleniumSupport;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;



public class Selenium extends SeleniumDriver{

	public static String Browser;
	public static int timeOut;
	
	private static Logger log = Logger.getLogger(Selenium.class.getName());

/*
 * Author:Rahul Gandhi
 * Description: Opens Url
 * 
 */
public void openBrowser(String Url)
{	
	log.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	log.info("Starting selenium with "+ Browser);
	log.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	switch (Browser.toUpperCase()) {
	case "FIREFOX":
		
		System.setProperty("webdriver.gecko.driver", pathOfDrivers +"geckodriver.exe");
		//System.setProperty("webdriver.gecko.driver", "E://Learning//NAGP//New%20folder//NAGP_Selenium_1-RahulGandhi-4470//BrowserServers//geckodriver.exe");
		//DesiredCapabilities capa = DesiredCapabilities.firefox();
		//'capa.setCapability("marionette", true);
		
		driver =  new FirefoxDriver();
		
		break;
	case "CHROME":
		System.setProperty("webdriver.chrome.driver", pathOfDrivers +"chromedriver.exe");
		driver =  new ChromeDriver();
		log.info("Building chrome driver");
		break;
	case "IE":
		System.setProperty("webdriver.ie.driver", pathOfDrivers +"IEDriverServer.exe");
		DesiredCapabilities cap =  DesiredCapabilities.internetExplorer();
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		driver =  new InternetExplorerDriver(cap);
		break;
	default:
		break;
	}
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	driver.get(Url);
}


public void click_webElement(String sXpath)
{
	log.info("Click Web Element "  + sXpath);
	Boolean isElementPresent = isElementPresent(By.xpath(sXpath));
	if (isElementPresent){
	WebElement oWebElement = driver.findElement(By.xpath(sXpath));
	oWebElement.click();
	log.info("Successfully clicked element  " + sXpath);
	}
}

public void enter_Text(String sXpath, String sText)
{
	log.info("trying to enter "+ sText + " at Xpath" + sXpath);
	Boolean isElementPresent = isElementPresent(By.xpath(sXpath));
	if (isElementPresent)
	{
		WebElement oWebElement = driver.findElement(By.xpath(sXpath));
		oWebElement.sendKeys(sText);
		log.info("Successfully entered  "+ sText + " at Xpath" + sXpath);
	}
	
}

public static boolean isElementPresent(By by)
{
try {
            driver.findElement(by);
            log.info("Element found next step would perform action");
            return true;
          } catch (NoSuchElementException ignored) {  
        	  log.info("Element not found test would fail");
            return false;
          }
}

public void selectValueFromDropDown(String sXpath, String value)
{
	Boolean isElementPresent = isElementPresent(By.xpath(sXpath));
	if (isElementPresent)
	{
		log.info("trying to select value from drop down ");
		WebElement oWebElement = driver.findElement(By.xpath(sXpath));
		Select dropdown= new Select(oWebElement);
		//log.info(dropdown.getFirstSelectedOption().);
		dropdown.selectByVisibleText(value);
		log.info("succesfullt selected value "+ value + " from drop down " + sXpath);
		
	}
}


public static void takeScreenShot(String screenShotFileName)
{
	log.info("trying to take screen shot of opened browser at " + screenShotFileName);
	String FileName = screenShotFileName;
	File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	try{
		FileUtils.copyFile(srcFile, new File(FileName));
		log.info("scuccesfully taken screen shot, stored at : " + screenShotFileName);
	} catch (Exception e)
	{
		e.printStackTrace();
	}
}


public static void closeBrowsers()

{	log.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	log.info("Closing all browsers");
	log.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	driver.close();
	driver.quit();
}

	public Selenium(String BrowserName, int timeOutValue ) {
		Browser=BrowserName;
		timeOut=timeOutValue;
		
		// TODO Auto-generated constructor stub
	}
	
}
