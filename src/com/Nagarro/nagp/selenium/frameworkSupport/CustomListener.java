package com.Nagarro.nagp.selenium.frameworkSupport;

import java.io.File;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.Nagarro.nagp.selenium.seleniumSupport.Selenium;


public class CustomListener extends TestListenerAdapter {

	private static Logger log = Logger.getLogger(CustomListener.class.getName());
	
public void onTestFailure(ITestResult tr) {
	log.info("On Fail");
	String methodName=tr.getName().toString().trim();
	String fileName = methodName+"_element_not_found" +".png";
	File file = new File("");
	Reporter.log("screenshot saved at "+file.getAbsolutePath()+"\\screenshots\\"+fileName);
	
	 Reporter.log("<a href='../"+tr.getName()+".jpg' <img src='../"+tr.getName()+".jpg' hight='100' width='100'/> </a>");
	
	 Reporter.setEscapeHtml(true);
	 
	 
	 Selenium.takeScreenShot( System.getProperty("user.dir") + "\\screenshots\\"+fileName);
	 
	   Reporter.setCurrentTestResult(null);
	 
		
}

public void onTestSuccess(ITestResult tr) {
log.info("On Pass");
}

public void onTestSkipped(ITestResult tr) {
log.info("On Skip");
}

}

