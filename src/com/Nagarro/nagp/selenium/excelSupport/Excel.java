package com.Nagarro.nagp.selenium.excelSupport;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.Nagarro.nagp.selenium.frameworkSupport.SetupTearDown;

public class Excel {

	private static Sheet sheet=null;
	private static Row row= null;
	private static Cell cell=null;
	private static Map<String,Integer> mapHeaders=null;
	public static Map<String, Object> testData=null;
	
	private static Logger log = Logger.getLogger(Excel.class.getName());
	
 	public static Workbook openWorkBook(String sFilePath)
	{
 		log.info("opening workbook from " + sFilePath);
	 	 //Creating Workbook object
		 Workbook oWorkBook = null;
		 String sErrorMessage = "";
				 String sWorkbookName = null;
		 try
		 {
		 	  int iFileNameIndex = sFilePath.lastIndexOf("\\");
		 	  //Get workbook name
		 	  sWorkbookName = sFilePath.substring(iFileNameIndex + 1);
		 	  
			  File oFile = new File(sFilePath);
			  //Reading file into Input Stream          
			  FileInputStream oInStream = new FileInputStream(oFile);
			  
			  //Getting the Excel file extension
			  int iLast = sFilePath.lastIndexOf(".");  
			  String sType = sFilePath.substring(iLast + 1);		  
			  switch (sType.toUpperCase())
			  {
				   case "XLSX":
					   //creating the XSSFWorkbook object
					   oWorkBook= new XSSFWorkbook(oInStream);             
				        break;
				   case "XLS":
					 //creating the HSSFWorkbook object
					   oWorkBook = new HSSFWorkbook(oInStream);            
				        break;
			  }	   
			  if(oFile.isFile() && oFile.exists())
			  {
			   	log.info("Workbook "+ sWorkbookName + " is opened successfully");
			  }
			  else
			  {
			   	sErrorMessage = "Unable to open the desired workbook "+ sWorkbookName;
			  }
			  ////Closing the Input Stream
			  oInStream.close();
		 
	}
		 catch(Exception e)
		 {
			 log.info( "Exception occurred: " + e.toString());
		 	sErrorMessage = "Exception occurred in opening the desired workbook " + sWorkbookName;
		 }
		  //Return WorkBook object and error message if any
		  return  oWorkBook;
	}

	 public static void GetHeaderList(Workbook oWorkBook,String sSheetName, int iHeaderDepth)
	{
		 log.info("Storing header column indexes");
		 mapHeaders = new HashMap<String, Integer>();
		 int iHeaderOccurence = 1;
		try
		{
			int iIndex = oWorkBook.getSheetIndex(sSheetName);
			//Get the desired sheet object
			Sheet oSheet = oWorkBook.getSheetAt(iIndex);
			int iFirstRow= oSheet.getFirstRowNum();
			
			//Iterating over the rows
			for(int i=iFirstRow;i<iHeaderDepth;i++)
			{
			   Row oRow = oSheet.getRow(i);
			   //Iterating over cells to get the header name
			   Iterator<Cell> cellIterator = oRow.cellIterator();
			   	   
			   while(cellIterator.hasNext())
			   {
				   //Create Cell object
				   Cell oCell = cellIterator.next();
				   //Check whether the header cell is non empty and that the Hash Map does not have the key already
				   if (oCell.getStringCellValue() != "" && !(mapHeaders.containsKey(oCell.getStringCellValue())))
				   {
					   mapHeaders.put(oCell.getStringCellValue(), oCell.getColumnIndex());
				   }
				   else
				   {
					   if (oCell.getStringCellValue() != "" && mapHeaders.containsKey(oCell.getStringCellValue()))
					   {
						   iHeaderOccurence = iHeaderOccurence +1;
						   // Add count to the Key name if it already exists
						   mapHeaders.put(oCell.getStringCellValue()+iHeaderOccurence, oCell.getColumnIndex());
						   iHeaderOccurence = 1;
					   }
				   }
			    }
		       }
		   }
		   catch(Exception e)
	        {
			   e.printStackTrace();
		   }
	     //return mapHeaders;
	 }

	 public static boolean setCellData(Workbook workbook,String sheetName, int colNum, int rowNum, String data) {
			try {


				if (rowNum <= 0)
					return false;

				int index = workbook.getSheetIndex(sheetName);

				if (index == -1)
					return false;

				sheet = workbook.getSheetAt(index);
				 
				sheet.autoSizeColumn(colNum);
			
				row = sheet.getRow(rowNum - 1);
				if (row == null)
					row = sheet.createRow(rowNum - 1);

				cell = row.getCell(colNum);
				if (cell == null)
					cell = row.createCell(colNum-1);

				cell.setCellValue(data);

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	
	 public static String getCellData(Workbook workbook, String sheetName, String ColumnName, int rowNum) {
		 
		 GetHeaderList(workbook, sheetName, 1);
		 int colNum=mapHeaders.get(ColumnName);
		 try {
			if (rowNum <= 0)
				return "";
			
			if (colNum <= 0)
				return "";
			
			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				switch (cell.getCachedFormulaResultType()) {
				case Cell.CELL_TYPE_NUMERIC:
					return String.valueOf(cell.getNumericCellValue());
				case Cell.CELL_TYPE_STRING:
					return cell.getRichStringCellValue().toString();
				}
				return "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR)))
							.substring(2);
					cellText = cal.get(Calendar.MONTH) + 1 + "/"
							+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

					// log.info(cellText);

				}

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum
					+ " does not exist  in xls";
		}
	}

	 public static boolean saveExcelFile(Workbook wb, String path)
	 {
		 log.info("Saving excel file as " + path);
			FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(path);
				//log.info(wb.getSheetAt(0).getRow(0).getCell(1)+"===============");
				//System.out.println(wb.getSheetAt(0).getRow(0).getCell(1)+"===============");
				wb.write(fileOut);
				wb.close();
			fileOut.flush();
			fileOut.close();
			return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	 }
	
	 public static HashMap<String, Map<String, ArrayList<String>>> getTestData(Workbook workbook, String sheetName )
	 {	
		 //	<testcaseName , Map<execute(yes or no), test data
		 Map<String, Map<String, ArrayList<String>>> testData=new HashMap<String, Map<String, ArrayList<String>>>();
		 
		 sheet=workbook.getSheet(sheetName);
		 //GetHeaderList(workbook, sheetName, 1);
		 //mapHeaders.get("TestCaseName");
		 int startingRowNumber=sheet.getFirstRowNum()+1;
		 //log.info("startingRowNumber"+startingRowNumber);
		 int lastRowNumber=sheet.getLastRowNum();
		  for (int rowcntr=startingRowNumber;rowcntr<=lastRowNumber;rowcntr++)
				 {
			  		log.info("reading values from row : " + rowcntr);
					 row=sheet.getRow(rowcntr);
					 int iFirsCell= row.getFirstCellNum();
					 
						int iLastcount= row.getLastCellNum();
						log.info("total cells on this row : " + iLastcount);
						//log.info(iFirsCell + "iFirsCell");
						String testCaseNameKey="";
						String executeKey="";
						ArrayList<String> dataValues = new ArrayList<>();
						Map<String,ArrayList<String>> testCaseData = new HashMap<String, ArrayList<String>>();
							for (int cellCntr=iFirsCell; cellCntr<iLastcount; cellCntr++)
								{			
									
									Cell cell = row.getCell(cellCntr);
									String cellVal =  cell.getStringCellValue();
									
									 if(cellCntr==iFirsCell+1)
									{
										executeKey=cellVal;
									
									}
									else if (cellCntr==iFirsCell)
									{
										testCaseNameKey=cellVal;
									
									}
									else 
									{
										dataValues.add(cellVal);
									}
								}
						testCaseData.put(executeKey, dataValues);
						log.info(rowcntr + "  = rowNumber" + testCaseNameKey + "  " + testCaseData );
						testData.put(testCaseNameKey, testCaseData);
											
				 }
			//log.info(testData);	 
		  	return (HashMap<String, Map<String, ArrayList<String>>>) (testData);
				 
				 }			 
		 
	 
//	 this method stores row number of each test case into a hashmap
	 public static HashMap<String, Integer> getTestCaseRowNum(Workbook workbook, String sheetName )
	 {
		 Map<String,Integer> rowNum=new HashMap<String, Integer>();
		 
		 sheet=workbook.getSheet(sheetName);
		 GetHeaderList(workbook, sheetName, 1);
		 int colNumTestCase=mapHeaders.get("TestCaseName");
		 
		 int startingRowNumber=sheet.getFirstRowNum()+1;
		 log.info("startingRowNumber"+startingRowNumber);
		 int lastRowNumber=sheet.getLastRowNum();
		  for (int rowcntr=startingRowNumber;rowcntr<=lastRowNumber;rowcntr++)
				 {
					 row=sheet.getRow(rowcntr);
					 cell=row.getCell(colNumTestCase);
					 rowNum.put(cell.getStringCellValue(), rowcntr);
						
				 }				
				
		  	log.info(rowNum);
		  	return (HashMap<String, Integer>) rowNum;
				 
				 }
				 
	 }
	 

