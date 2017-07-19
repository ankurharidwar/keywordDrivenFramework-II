/**
 * Summary: This class is written for keyword definitions
 * Last Changes Done on Feb 2, 2015 3:56:34 PM
 * 
 * Change made in Vdopia_Automation
 * Purpose of change: Added keywords to be used while executing test cases.
 */


package projects.portal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import projects.TestSuiteClass;import org.apache.log4j.Logger; 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;


import vlib.CaptureScreenShotLib;
import vlib.CustomException;
import vlib.CustomExceptionStopExecution;
import vlib.DBLib;
import vlib.FileLib;
import vlib.IntegerLib;
import vlib.KeyBoardActionsUsingRobotLib;
import vlib.GenericMethodsLib;


// TODO: Auto-generated Javadoc
/**
 * @author Pankaj Katiyar
 *
 */

public class Keywords {

	Logger logger = Logger.getLogger(Keywords.class.getName());

	String passed_status;
	String failed_status;

	/**
	 * Defining Variables
	 */
	String warning_status;
	WebDriver driver;
	WebElement webelement;
	String noObjectSuppliedMessage;
	String noDataSuppliedMessage;

	GetObjects getObject = new GetObjects();
	HandlerLib handler = new HandlerLib();
	ApplyRules applyRules = new ApplyRules();
	String locationToSaveSceenShot;

	JSONObject jsonObjectRepo;
	static Connection connection;

	/**
	 * Constructor initialization.
	 */
	public Keywords(Connection connection, JSONObject jsonObjectRepo)
	{
		this.passed_status = "Pass: ";
		this.failed_status = "Fail: ";
		this.warning_status = "Warning: ";
		this.noObjectSuppliedMessage = failed_status + "Please supply the desired object from object repository.";
		this.noDataSuppliedMessage = failed_status + "Please supply the desired test data.";
		this.locationToSaveSceenShot = TestSuiteClass.AUTOMATION_HOME.concat("/screenshots/").concat("ErrorKeywords").concat("/");

		this.jsonObjectRepo = jsonObjectRepo;
		Keywords.connection = connection;
	}


	/**
	 * This keyword launches browser.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String launchbrowser  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received browser name is: "+data);

			if(data.isEmpty())
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Empty browser is received. ");
				result = failed_status + "Browser type: "+data +" can't be empty, please supply the supported browser: chrome or firefox.";
			}
			else if(data.equalsIgnoreCase("chrome") || data.equalsIgnoreCase("firefox"))
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Setting up browser: "+data);

				driver = GenericMethodsLib.WebDriverSetUp(data, null);
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Launched browser name is: "+data + " browsername "+ driver );			

				//Bring browser in focus, normally chrome opens in background.
				handler.getBrowserInFocus(driver);

				result = passed_status+ "Browser launched successfully";
			}
			else
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Supplied browser: "+data + " is not supported. ");
				result = failed_status + "Supplied browser type: "+data +" is not supported, supported ones are chrome and firefox.";
			}
		}catch(Exception e)
		{
			result = failed_status + "Couldn't launch browser";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while launching browser: "+data, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword closes the browsers opened by automation code.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String closebrowser  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Closing browser. ");

			driver.quit();

			//			for(String window : driver.getWindowHandles())
			//			{
			//				driver.switchTo().window(window).close();
			//			}

			result = passed_status+ "Browser closed successfully.";
		}catch(Exception e)
		{
			result = failed_status + "Couldn't close browser. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while closing browser. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword navigates to a URL.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String navigateurl  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{

			/** putting a random delay to avoid simultaneous load on server */
			int delay = IntegerLib.GetRandomNumber(5000, 1000);
			Thread.sleep(delay);
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : random delay of: "+delay);

			if(driver != null)
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received url is: "+data+ " and browser is: " +driver);
				try{driver.get(data);}catch(Exception e)
				{
					Thread.sleep(IntegerLib.GetRandomNumber(1500, 500));
					driver.navigate().to(data);
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred, reloading again : "+data);
				}

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Navigated url is: "+data);
				result = passed_status+ "Navigated url successfully.";
			}
			else
			{
				result = failed_status + "Couldn't navigate to url. ";
			}
		}catch(Exception e)
		{
			result = failed_status + "Couldn't navigate to url";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while navigating url : "+data+ "at browser : " +driver, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will move the driver to new browser window.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String movetonewbrowserwindow  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moving to new browser window" );

			String currentState = driver.getWindowHandle().toString();

			/** This code will explicitly wait for max 5 sec to appear multiple window 
			 * for driver to switch on */
			int i=0;
			while(driver.getWindowHandles().size()<2){
				Thread.sleep(3000);
				i++;

				if(i==5){
					break;
				}
			}

			for(String handles : driver.getWindowHandles())
			{
				if(!handles.equalsIgnoreCase(currentState))
				{
					driver.switchTo().window(handles);
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Switched window has title: "+driver.getTitle());
				}
			}
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moved to new window: "+driver.getCurrentUrl());
			result = passed_status+ "Moved to new window successfully";
		}catch(Exception e)
		{
			result = failed_status + "Couldn't moved to new window";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while moving to new window ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword is used to upload the creative.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String uploadimage  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{

			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received file name with location is: " +data);

			/**
			 * Getting relative image file
			 */
			data = handler.getUploadImageLocation(data);
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Uploading image file: "+data);

			Thread.sleep(500);

			WebElement upload = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			upload.click();

			Thread.sleep(1500);

			KeyBoardActionsUsingRobotLib.ChooseFileToUpload(data, driver);

			/**
			 * Wait until image is uploaded successfully. Currently hardcoding this 
			 * later on need to get from OR
			 */
			By byLocator = By.xpath("//div[@class='ui-progressbar ui-widget ui-widget-content ui-corner-all'][@aria-valuenow='100']");
			handler.applyExplicitWait(driver, byLocator, new NoSuchElementException(""));

			result = passed_status+ "Creative uploaded successfully";
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't upload creative";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while uploading file : "+data, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will wait until all conversions: Video Dimension, FPS, Video Duration, Ratio, .VDO Conversion are completed
	 * and for a specific message: "Please enter the name, url and click on save ad to continue" appears as go ahead signal to fill further details,
	 * Subsequent validation will be performed only previous element appears. 
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public  String verifyandwaitforvideoconversion  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		boolean flag;

		try{
			By bylocator;

			bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_VideoDimension_Indicator", driver, jsonObjectRepo);
			flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			if(!flag){
				result = result + "Video Dimension conversion failed. ";
			}


			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_FPS_Indicator", driver, jsonObjectRepo);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + "FPS conversion failed. "; 
			}

			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_VideoDuration_Indicator", driver, jsonObjectRepo);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + "Video Duration conversion failed. ";
			}

			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_Ratio_Indicator", driver, jsonObjectRepo);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));

			}else{
				result = result + "Ratio conversion failed. ";
			}

			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_VDOConversion_Indicator", driver, jsonObjectRepo);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + ".VDO Conversion failed. ";
			}

			/* Commenting this code as these text box will not be available on ui from tag: vt0.6.3-0
			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_VDOURL_TextBox", driver);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + ".VDO URL wasn't found. ";
			}

			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_DemoURL_TextBox", driver);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + "Demo URL wasn't found. ";
			}

			if(flag){
				bylocator = getObject.getByLocatorFromRepository("Create_Sub_placement_DemoURL_QRCode", driver);
				flag = handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""));
			}else{
				result = result + "QR Code wasn't found. ";
			}
			 */
			if(flag){
				String msg = "Please enter the name, url and click on save ad to continue";
				By byelement = getObject.getByLocatorFromRepository("Create_Sub_placement_UploadVideo_GoAheadMessage", driver, jsonObjectRepo);
				result = result + handler.waitForUploadVideoText(driver, byelement, msg);
			}
		}
		catch(Exception e)
		{
			flag = false;
			result = failed_status + "Error occurred while checking video conversion. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while waiting for conversion to be completed. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		if(flag)
		{
			result = passed_status + "Video Converted Successfully. ";	
		}
		else
		{
			result = failed_status + result; 
		}
		return result;
	}


	/**
	 * This keyword will select the date.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String selectdate  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received date is: " +data);

			//finding the calendar icon
			webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);

			//Selecting the dates from calendar
			handler.selectDateFromCalendar(driver, webelement, data, jsonObjectRepo);

			result = passed_status+ "Selected date successfully";
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't selected date";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while selecting date : "+data, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will select the date range in Placements / Sub-placement screen,
	 * This keyword accepts input data in comma separated or semicolon separated format,
	 * If desired date range is select, then input can be like select, Date1, Date2 or Date1, Date2
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String selectdaterange  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			data = data.trim().toLowerCase();
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received date range is: " +data);

			/** finding the calendar icon */
			webelement = getObject.getWebElementFromRepository("Placements_DateRange", "name", driver, jsonObjectRepo);

			/** 
			 * Putting sleep for 1 sec to sync the application.
			Thread.sleep(1000);
			webelement.click();
			Thread.sleep(1000);
			 */

			String javaScript = "arguments[0].click()";
			handler.executeJavaScript(driver, javaScript, webelement);

			if(data.matches("^today.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Today", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^yesterday.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Yesterday", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^last 7 days.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Last7days", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^last 30 days.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Last30days", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^this month.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_ThisMonth", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^last month.*"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_LastMonth", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else if(data.matches("^select.*") || data.contains(",") || data.contains(";"))
			{
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Select", "name", driver, jsonObjectRepo);
				webelement.click();

				//Pick calendar date, here data has to be splitted 

				List<String> inputdata = new ArrayList<String>();

				if(data.contains(";"))
				{
					inputdata = Arrays.asList(data.split(";"));
				}
				else if(data.contains(","))
				{
					inputdata = Arrays.asList(data.split(","));
				}

				/** Keeping a check if some one forgets to enter the data like select, <left date>, < right date>
				 * and enters data like <left date>, < right date> then consider this case.
				 */

				//String option = "";
				String leftDate = "";
				String rightDate = "";

				if(inputdata.size() == 2)
				{
					leftDate = inputdata.get(0).trim();
					rightDate = inputdata.get(1).trim();
				}
				else if(inputdata.size() == 3)
				{
					leftDate = inputdata.get(1).trim();
					rightDate = inputdata.get(2).trim();
				}

				//Select dates from left and right calendar
				handler.selectDateRangeFromCalendar(driver, leftDate, rightDate, jsonObjectRepo);

				//Click Submit button finally
				webelement = getObject.getWebElementFromRepository("Placement_DateRange_Submit_Button", "name", driver, jsonObjectRepo);
				webelement.click();
			}
			else
			{
				throw new CustomException("Invalid date supplied. ");
			}

			result = passed_status+ "Selected date range successfully";
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't selected date range";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while selecting date range : "+data, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword is for click on button.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String clickbutton  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		By byLocator = null;
		try{
			if(!objectName.isEmpty())
			{
				try{
					/** create dynamic element */
					byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
					webelement = driver.findElement(byLocator);
				}catch(CustomException e)
				{
					/** create element normally */
					webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);

				}
				if(!webelement.isEnabled())
				{
					By bylocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);

					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Element is not clickable");
					handler.applyExplicitWait(driver, bylocator, new WebDriverException());
				}

				/**
				 * Apply rules to handle certain specific web element clicks.
				 */
				if(!applyRules.applyRule(driver, objectName, webelement, data, jsonObjectRepo))
				{
					/** temp fix to handle stale exception */
					try{webelement.click();}catch(StaleElementReferenceException w){System.out.println("Nothing to worry - ");}
				}

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicking element :" + webelement );
				result = passed_status+ "Button clicked successfully";

			}
			else
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No Object received to click.");
				result = failed_status+ "No object was provided to click. ";
			}

		}

		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click on button";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while clicking button element: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword is used to click link.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String clicklink  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			boolean staleExceptionHandleFlag = true;
			int staleExceptionAttempt=0;

			/** Adding a check on staleExceptionHandleFlag exception, in case this occurs then find the element again until the max attempt = 5.
			 */
			while(staleExceptionHandleFlag)
			{
				try
				{					
					/** First check if this a dynamic element, if not then catch customexception and find element conventionally --> 
					 * Now putting condition on data, if data is empty then get element from object repository using objectName 
					 * else find element using objectLabel --> to be used in keyword clickmenu.
					 */
					try{
						webelement = handler.createDynamicWebElement(driver, objectName, data, jsonObjectRepo);
					}catch(CustomException c)
					{
						if(objectName.isEmpty())
						{
							webelement = getObject.getWebElementFromRepository(data, "label", driver, jsonObjectRepo);
						}
						else
						{
							webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
						}
					}

					/** Wait until link is visible and clickable, if its not enabled.*/
					By bylocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);

					if(!webelement.isEnabled() && webelement != null)
					{
						handler.applyExplicitWait(driver, bylocator, new WebDriverException());
					}

					if(!applyRules.applyRule(driver, objectName, webelement, data, jsonObjectRepo))
					{
						/** if there is any exception thrown while clicking link, then reattempt after catching that exception */
						try{
							webelement.click();
						}catch(WebDriverException w){
							handler.applyExplicitWait(driver, bylocator, new WebDriverException());
							webelement.click();
						}
					}

					staleExceptionHandleFlag = false;

				}catch(StaleElementReferenceException e){
					staleExceptionAttempt ++;
				}

				if(staleExceptionAttempt ==5){
					break;
				}
			}

			Thread.sleep(2500);
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicking element: " + webelement );
			result = passed_status+ "Clicked link successfully";

		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click link";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking link: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword is to select radio button.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String selectradiobutton  (WebDriver driver, String objectName, String data)
	{
		String result;
		By byLocator = null;

		try{
			/** create dynamic element */
			byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
			webelement = driver.findElement(byLocator);
		}catch(CustomException e)
		{
			/** create element normally */
			try {
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			} catch (CustomException e1) { logger.error(e.getMessage()); }
		}

		try{

			/** apply explicit wait */
			WebDriverWait wait = new WebDriverWait(driver, 45);
			wait.until(ExpectedConditions.elementToBeClickable(webelement));

			if(!webelement.isSelected())
			{
				webelement.click();
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Selected radio button option is: " +webelement);
				result = passed_status+ "Selected radio button successfully";
			}
			else
			{
				result = passed_status+ "Desired radio button was already selected. ";
			}
		}
		catch(Exception e)
		{
			//Get by locator and apply external wait
			try {
				byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
			} catch (CustomException e2) {
				try {
					byLocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);
				} catch (CustomException e3) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			try{


				handler.applyExplicitWait(driver, byLocator, e);
				webelement.click();

				result = passed_status+ "Selected radio button successfully";
			}
			catch(Exception ex)
			{
				result = failed_status + "Couldn't select radio button";
				logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while selecting radio button option : " +webelement, e);

				/** Taking screenshot during exception */
				CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
			}
		}

		return result;
	}


	/** This keyword clicks any check box if its not already checked. this keyword supports multiple check box selection.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String selectcheckbox  (WebDriver driver, String objectName, String data)
	{
		String result = "";

		/** if object is supplied by comma separated then select every check box */
		if(objectName.contains(","))
		{
			List<String> objectList = new ArrayList<>(Arrays.asList(objectName.split(",")));
			for(int i=0; i<objectList.size(); i++)
			{
				result = result + "\n" + handler.selectCheckbox(driver, objectList.get(i).trim(), webelement, data, 
						locationToSaveSceenShot, passed_status, failed_status, jsonObjectRepo);
			}
		}
		else if(data.contains(","))
		{
			List<String> dataList = new ArrayList<>(Arrays.asList(data.split(",")));
			for(int i=0; i<dataList.size(); i++)
			{
				result = result + "\n" + handler.selectCheckbox(driver, objectName, webelement, dataList.get(i).trim(), 
						locationToSaveSceenShot, passed_status, failed_status, jsonObjectRepo);
			}
		}
		else
		{
			result = handler.selectCheckbox(driver, objectName, webelement, data, locationToSaveSceenShot, passed_status, failed_status, jsonObjectRepo);
		}

		return result;
	}


	/** This keyword unselect any selected check box.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String unselectcheckbox  (WebDriver driver, String objectName, String data)
	{
		String result;
		By byLocator = null;
		try{
			/** create dynamic element */
			byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
			webelement = driver.findElement(byLocator);
		}catch(CustomException e)
		{
			/** create element normally */
			try {
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			} catch (CustomException e1) 
			{ logger.error(e.getMessage()); 
			result = failed_status + e.getMessage();
			}
		}
		try{


			if(webelement.isSelected()){
				webelement.click();
			}

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Checkbox is cleared. ");
			result = passed_status+ "Checkbox cleared successfully";
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't clear checkbox. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clearing checkbox : " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword verifies if supplied check box is selected.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String ischeckboxselected  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			if(webelement.isSelected())
			{
				return passed_status + "Checkbox is selected. "; 
			}
			else
			{
				return failed_status + "Checkbox is not selected. ";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't check the supplied checkbox selection. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while checking selection of checkbox : " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

		}
		return result;
	}


	/**
	 * This keyword is for select an option from radio button.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String selectdropdownvalue  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		/** if object is supplied by comma separated then select every check box */
		try{
			if(objectName.contains(","))
			{
				List<String> objectList = new ArrayList<>(Arrays.asList(objectName.split(",")));
				for(int i=0; i<objectList.size(); i++)
				{
					result = result + "\n" + selectdropdownvalue(driver, objectList.get(i).trim(), data);
				}
			}
			else if(data.contains(","))
			{
				List<String> dataList = new ArrayList<>(Arrays.asList(data.split(",")));
				for(int i=0; i<dataList.size(); i++)
				{
					result = result + "\n" + selectdropdownvalue(driver, objectName, dataList.get(i).trim());
				}
			}
			else{
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
				Select select = new Select(webelement);
				select.selectByVisibleText(data.trim());

				result = passed_status+ "Selected dropdown value successfully";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Selected drop down value: " +data);
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Exception occurred while selecting drop down option. "+data;
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while selecting drop down option : " +data, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will be used to select the desired value(s) out of pre populated list by searched records based on 
	 *  user input, like Select Placement in Assign Placement to Client screen / select channel in Generate Tag screen etc.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String chooseinlist  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{
			if(!objectName.isEmpty() && !data.isEmpty())
			{
				/** In case of stale element exception, find element again to do operation.
				 *  repeatAction parameter will keep the code in loop and attemptCount parameter will limit the 
				 *  number of attempt to 5 to avoid infinite loop.
				 */
				boolean repeatAction = true;
				int attemptCount = 0;

				while(repeatAction)
				{
					try{
						Thread.sleep(1000);
						webelement = handler.createDynamicWebElement(driver, objectName, data, jsonObjectRepo);
						webelement.click();
						Thread.sleep(1000);

						repeatAction = false;
					}catch(StaleElementReferenceException e){
						repeatAction = true;
						attemptCount++;
					}

					if(attemptCount ==5){
						break;
					}
				}

				result = passed_status+ "Selected value: "+data+" successfully";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Selected value: "+data+" successfully");
			}
			else
			{
				result = noDataSuppliedMessage + "   " + noObjectSuppliedMessage;
			}
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't select "+data +" from list. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while selecting value: "+data +" from list. ");

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword is for type the value in text field/area.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String typevalue  (WebDriver driver, String objectName, String data)
	{
		String result;
		try{

			if(!objectName.isEmpty())
			{
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);

				//Clearing element before typing, if exception occurs while clearing then ignore this and proceed.
				try{
					if(objectName.equalsIgnoreCase("EditApplication_MarketingName_TextBox"))
					{
						Thread.sleep(1000);

						/** Clearing the text of text box */
						webelement.sendKeys(Keys.CONTROL + "a");
						webelement.sendKeys(Keys.DELETE);
					}
					else
					{
						Thread.sleep(2000);
						webelement.clear();
					}

					/** executing js to clear text if not cleared by above code */
					handler.executeJavaScript(driver, " arguments[0].value=\"\" ", webelement);

				}catch(Exception e){
					/** executing js to clear text if not cleared by above code */
					handler.executeJavaScript(driver, " arguments[0].value=\"\" ", webelement);

					logger.error(e.getMessage());
				}

				//This code will type the text in search box -- slowly key by key
				if(objectName.equalsIgnoreCase("Create_Sub_placement_SearchApplication_textBox") || 
						objectName.equalsIgnoreCase("GenerateTag_SearchChannel_TextBox")
						|| objectName.equalsIgnoreCase("AddNewAppOrSite_AddResultGoogle_WebResult_DropDown")
						|| objectName.equalsIgnoreCase("CreateANewDeal_Package_Chip"))
				{
					handler.typeSlowly(webelement, data);
					Thread.sleep(1000);
				}
				else
				{
					Thread.sleep(250);
					webelement.sendKeys(data);
					Thread.sleep(250);
				}
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Typing the value : " + data + " in the element: " + webelement );
				result = passed_status+ "Value typed successfully";
			}
			else
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No object was provided to type value. " );
				result = failed_status+ "No object was provided to type value. ";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't type value";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while Typing the value : " + data + " in the element: " + webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}

		return result;
	}


	/** This keyword is to upload video, automation code will directly type the received file location in choose file input field.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String choosefile  (WebDriver driver, String objectName, String data)
	{		
		String result;
		try{
			webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);

			/**
			 * Get video location relative to Automation_Home,
			 * in case of banner sub placement, choosefile keyword is used but image is uploaded 
			 */
			String uploadFile = data;
			uploadFile = handler.getUploadVideoLocation(data);

			/** If video file is empty then check if image file needs to be uploaded. */
			if(uploadFile.isEmpty())
			{
				uploadFile = handler.getUploadImageLocation(data);
			}

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Uploading media file: "+uploadFile);

			//webelement.sendKeys(uploadFile);
			webelement.click();
			Thread.sleep(1500);
			KeyBoardActionsUsingRobotLib.ChooseFileToUpload(uploadFile, driver);
			Thread.sleep(1000);

			//Get progress bar webelement 
			webelement = getObject.getWebElementFromRepository("Create_Sub_placement_Video_Upload_ProgressBar","name" ,driver, jsonObjectRepo);

			//wait until progress is 100, max time for wait = 600*1000 mili sec.
			boolean status = false;
			for(int i=0; i<600; i++)
			{
				//get the progress
				Thread.sleep(2000);
				String progress = webelement.getAttribute("aria-valuenow").toString().trim();

				if(progress.equalsIgnoreCase("100") || data.contains("jpg"))
				{
					status = true;
					break;
				}
				else
				{
					/** handling banner upload case where progress attribute becomes 0.1 uploading image */
					if(i==5 && progress.equalsIgnoreCase("0.1")) 
					{
						break;
					}
					continue;
				}
			}

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Uploading media file: " + data + " in the element: " + webelement );

			if(status){
				result = passed_status+ "Media was uploaded successfully. ";
			}else{
				result = failed_status+ "Media wasn't uploaded. ";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't upload media.";			
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while uploading video located at: " + data + " in the element: " + webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword is for verify the text of any supplied object.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 * @throws CustomExceptionStopExecution 
	 */	
	public String verifytext  (WebDriver driver, String objectName, String data) throws CustomExceptionStopExecution
	{
		String result = "";

		try{

			if(objectName.isEmpty())
			{
				result = failed_status + "Supplied object name is empty. ";
			}

			else
			{
				//Getting data after removing any flag like "must flag"
				if(data.contains(";")){
					data = data.split(";")[0].trim();
				}
				else if(data.contains(",")){
					data = data.split(",")[0].trim();
				}

				String actualValue = "";

				/** Handling stale element reference exception, in this exception retry to find the element, max attempt is 5
				 */
				boolean staleElementReferenceException = true;
				int staleElementReferenceExceptionCount = 0;
				By bylocator = null;

				while(staleElementReferenceException)
				{
					try
					{
						/** First the check if the supplied element is a dynamic object which needs data to create element definition, 
						 * if no, then createDynamicWebElement method will throw CustomExceptionsLib exception, and then find the element using 
						 * method: getWebElementFromRepository
						 */
						try{
							bylocator = getObject.getByLocatorFromRepository(objectName, data, driver, jsonObjectRepo);
						}catch(CustomException c){
							bylocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);
						}

						/** apply explicit wait of 45 sec before finding the element */
						handler.applyExplicitWait(driver, bylocator, new NoSuchElementException(""), 45);
						webelement = driver.findElement(bylocator);

						for (int i=0; i<5; i++)
						{
							actualValue = driver.findElement(bylocator).getText().trim(); 
							logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : actual text of element: " + actualValue);

							if(actualValue.equalsIgnoreCase(data))
							{
								break;
							}
							else
							{
								Thread.sleep(1000);
							}
						}

						staleElementReferenceException = false;
					}
					catch(StaleElementReferenceException s)
					{
						staleElementReferenceException = true;
						staleElementReferenceExceptionCount++;
					}

					if(staleElementReferenceExceptionCount == 5)
					{
						break;
					}
				}

				if(actualValue.equalsIgnoreCase(data))
				{
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Matched. ");
					result = passed_status + "Text is as expected.";
				}
				else 
				{
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Doesn't Match. ");
					result = failed_status + "The actual value is: " + actualValue + ", the expected value is: " + data;
				}
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch (TimeoutException e) 
		{
			result = failed_status + "Could not retrieve the text."; 
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Timed out while waiting for text to be present: "+data);
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ " : " + failed_status + "Exception occurred while verifying the text: " + data + " of the element: " +webelement, e);
			result = failed_status + "Could not retrieve the text."; 

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Current url: "+driver.getCurrentUrl());
		return result;
	}


	/**
	 * This keyword will verify the details from mysql database, user has to
	 * supply the db query in objectName and expected comma separated result(s)
	 * in data column, usage example: [objectName = select ABC from campaign
	 * where id = "XYZ"][input = xyz].
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */	
	public String verifydbdetails  (WebDriver driver, String objectName, String data) 
	{
		String result = "";
		try{
			/** 
			 * Do not proceed if there is no query supplied.
			 */
			if(objectName.isEmpty())
			{
				result = failed_status + "No query was supplied. ";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ ": " + result);
			}
			else
			{
				/** parse received data */
				data = handler.dataParser(data, connection);

				/** Parsing the supplied sql query. */
				String sqlQuery = objectName.replace("\"", "'");
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Executing supplied query: "+sqlQuery);

				String [] records = GenericMethodsLib.ExecuteMySQLQueryReturns1DArray(connection, sqlQuery);

				/** proceed to test only if received records is not null */
				if(records != null)
				{
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received Number Of Records: "+records.length);

					boolean dataListFlag = false;
					List<String> dataList = new ArrayList<>();

					/**
					 * This is for a special case If there is only one supplied data but containing ',' into it.
					 */
					if(data.contains("Vpaid") || data.contains("Mraid"))
					{
						/**
						 *  Replace the provided data as per the saved data in database
						 */
						data.replace("Vpaid", "2");
						data.replace("Mraid1", "3");
						data.replace("Mraid2", "5");

						if(records[0].trim().equalsIgnoreCase(data.trim()))
						{
							result = passed_status + "Actual value is same as expected. ";
						}
						else
						{
							result = failed_status + "Expected value= "+data + " whereas actual value saved in db = "+records[0];
						}
					}

					/**
					 * Converting the comma / semi colon separated supplied data into a list 
					 * only when --> data contains must pass flag separated by , or ;
					 */

					else if(data.contains(";"))
					{
						if(data.toLowerCase().contains("must pass"))
						{
							dataListFlag = true;	

						}
						else
						{
							dataListFlag = false;
						}

						/** Recasting the splitted string as list to avoid unsupported operation exception. */
						dataList = new ArrayList<>(Arrays.asList(data.split(";")));
					}
					else if(data.contains(","))
					{
						if(data.toLowerCase().contains("must pass"))
						{
							dataListFlag = true;
						}
						else
						{
							dataListFlag = false;
						}
						/** Recasting the splitted string as list to avoid unsupported operation exception. */
						dataList = new ArrayList<>(Arrays.asList(data.split(",")));
					}
					else
					{
						/** If there is only one supplied data::: 
						 */
						if(records[0].trim().equalsIgnoreCase(data.trim()))
						{
							result = passed_status + "Actual value is same as expected. ";
						}
						else
						{
							result = failed_status + "Expected value= "+data + " whereas actual value saved in db = "+records[0];
						}
					}

					/** If the supplied data is a list then iterating it: */
					if(dataListFlag)
					{
						/** Remove any must pass flag from the supplied user data list, checking the only last item
						 * coz must pass can be used only at the last place. 
						 */
						if(dataList.get(dataList.size()-1).trim().equalsIgnoreCase("must pass"))
						{
							dataList.remove(dataList.size()-1);
						}
					}	
					if(!dataList.isEmpty())
					{
						/** This failFlag will be used to verify if there is any case of data mismatch */
						boolean failFlag = true;

						/** Iterating the supplied data list. */
						for(int i=0; i<dataList.size(); i++)
						{
							String suppliedExpectedValue = dataList.get(i).trim();
							String actualDBValue = records[i];

							/** Compare each supplied data with the retrieved value from database */
							if(!suppliedExpectedValue.equalsIgnoreCase(actualDBValue))
							{
								result = result + "Expected value= "+dataList.get(i) +" whereas actual value saved in db= "+records[i] + "  ";

								/** If there is even a single mismatch failFlag = false, later on to be determined if there was any mismatch. */
								if(failFlag)
								{
									failFlag = false;
								}
							}

							/** Check if the whole list is iterated yet, if yes then check the failFlag, if failFlag is true 
							 * then there was no mismatch else there was a mismatch in data.  
							 */
							if(i==dataList.size()-1)
							{
								if(failFlag)
								{
									result = passed_status + "All values are saved as expected in database.";
								}
								else
								{
									result = failed_status + result;
								}
							}
						}

					}
				}
				else
				{
					result = failed_status + "Received null in database. ";
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No Records Received ... ");
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ " : " + failed_status + "Exception occurred while verifying the database details." , e);
			result = failed_status + "Could not get database details. "; 

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}

	/**
	 * This keyword will execute the supplied insert / update query in mysql database, user has to
	 * supply the db query in objectName , usage example: [objectName = update abc where a = c].
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */	
	public String executedbquery  (WebDriver driver, String objectName, String data) 
	{
		String result = "";
		try{
			/** 
			 * Do not proceed if there is no query supplied.
			 */
			if(objectName.isEmpty())
			{
				result = failed_status + "No query was supplied. ";
			}
			else
			{
				/** Parsing the supplied sql query. */
				String sqlQuery = objectName.replace("\"", "'");
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Executing supplied query: "+sqlQuery);

				boolean flag = new DBLib().executeUpdateInsertQuery(connection, sqlQuery);
				if(flag)
				{
					result = passed_status + "Query was executed.";
				}
				else {
					result = failed_status + "Query was not executed.";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ " : " + failed_status + "Exception occurred while verifying the database details." , e);
			result = failed_status + "Could not get database details. "; 

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/**
	 * This keyword is little special, it will be used to verify the text of
	 * desired parameter corresponding to the given searched value. For example,
	 * in Marketplace Connections screen, user searches a connection by giving
	 * Connection Identifier = "SearchOnly_DontDelete" and verifies Connection
	 * Name = "searchonly_dontdelete_both_video_rtb22_all" corresponding to
	 * given Connection Identifier, then this keyword verifytextofsearchedrecord
	 * will be used with the below values in input data along with the object Name of Object to be verified
	 * (in this case objectName of Connection Name)
	 * INPUT DATA: SearchOnly_DontDelete, searchonly_dontdelete_both_video_rtb22_all
	 * 
	 * First Parameter is the Value Used To Perform Search, Second is the
	 * expected value of desired parameter which will be matched with the actual
	 * value. This is required to maintain the relation.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String verifytextofsearchedrecord  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			if(objectName.isEmpty() || data.isEmpty())
			{
				result = failed_status + "Both object name and desired test in data should be supplied to use this keyword. ";
			}
			else
			{
				String searchParam = "";
				String expectedValue = "";

				/** Getting searched parameter and expected value from the supplied data. 
				 * First parameter will always be the data to be used in finding element and second one will be 
				 * the expected data.
				 */

				if(data.contains(";"))
				{
					searchParam = data.split(";")[0].trim();
					expectedValue = data.split(";")[1].trim();
				}
				else if(data.contains(","))
				{
					searchParam = data.split(",")[0].trim();
					expectedValue = data.split(",")[1].trim();
				}

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Search parameter is: " + searchParam);

				/** Create the dynamic element using searchParam, putting sleep to handle sync
				 */
				Thread.sleep(2000);
				webelement = handler.createDynamicWebElement(driver, objectName, searchParam, jsonObjectRepo);
				if(webelement != null)
				{
					/** Wait until the expected text is present in the web element.  
					 */
					try{
						WebDriverWait wait = new WebDriverWait(driver, 2);
						wait.until(ExpectedConditions.textToBePresentInElement(webelement, expectedValue));		
					}catch(Exception e){
					}
					String actualValue = webelement.getText().trim();
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : actual text of element : " + actualValue);

					/** Matching expected and actual values */
					if(actualValue.equals(expectedValue))
					{
						logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Matched. ");
						result = passed_status + "Text is as expected.";
					}
					else 
					{
						logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Doesn't Match. ");
						result = failed_status + "The actual value is: " + actualValue + ", the expected value is: " + expectedValue;
					}
				}
				else
				{
					result = "FAIL: Couldn't find the supplied webelement therefore text couldn't be verified.";
				}
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the text: " + data + " of the element: " +webelement, e);
			result = failed_status + "Could not retrieve the text."; 

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will be used to verify the generated tags like reporting tag, advertiser tag or channel tag, 
	 * User has to supply the type of tag in data like: reporting tag or channel tag or advertiser tag etc,
	 * The generated tag is verified by executing java script which is used on portal also. 
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String verifygeneratedtag  (WebDriver driver, String objectName, String data)
	{
		try{

			/** Splitting the must flag from the supplied data and getting the input type for which tag has to verified.
			 */
			if(data.contains(";"))
			{
				data = data.split(";")[0].toLowerCase().trim();
			}
			else if(data.contains(","))
			{
				data = data.split(",")[0].toLowerCase().trim();
			}

			/** Creating java script based on supplied input data
			 */
			String javaScript = "";

			if(data.contains("reporting tag") || data.matches("^reporting.*"))
			{
				javaScript = "return $('#tag').val()";
			}
			else if(data.contains("advertiser tag") || data.matches("^advertiser tag.*"))
			{
				javaScript = "return $('#adtag').val()";
			}

			/**
			 * Executing java script and getting output, proceed only if java script is formed based on supplied data.
			 */
			if(javaScript.isEmpty())
			{
				return failed_status + "Please supply the either of these type of tags: reporting tag or advertiser tag. ";
			}
			else
			{
				/** getting output of executed java script in a loop until 5 attempts to get the generated tag
				 */
				Object result = "";
				int count = 0;

				while(((String)result).isEmpty())
				{
					result = handler.executeJavaScript(driver, javaScript, null);

					if(count==5)
					{
						break;
					}

					Thread.sleep(1000);
					count++;
				}


				/** if there is no exception, then get result
				 */
				if(result != null)
				{
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Retrieved Generated Tag: "+result);

					if(result.toString().isEmpty())
					{
						return failed_status + "No Tag was generated.";
					}
					else
					{
						return passed_status + "Tag: "+result +" was generated. ";	
					}
				}
				else
				{
					return failed_status + "Couldn't retrieve the generated tag. ";
				}
			}
		}
		catch(Exception e)
		{

			logger.error(failed_status + "Exception occurred while verifying the text: " + data + " of the element: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Couldn't retrieve the generated reporting tag. "; 
		}
	}


	/** This keyword will be used to verify all the error elements/texts present on any screen.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String geterrormessages  (WebDriver driver, String objectName, String data)
	{

		/**
		 * //span[class='text-error'] and //label[class='text-error']
		 */
		try{
			//First check if there is any alert found.
			String message = "";
			message = message + getalerttext(driver,objectName, data);

			//Secondly get any error message present on screen.
			List<WebElement> errorElementList = new ArrayList<WebElement>();

			//Get all the error text defined by tag label
			By labelerrorMessage = getObject.getByLocatorFromRepository("ScreenErrorMessages_DefinedByLabel_Text", driver, jsonObjectRepo);

			errorElementList = driver.findElements(labelerrorMessage);			

			//Get all the error text defined by tag span if no error found by tag label
			if(errorElementList == null)
			{
				By spanerrorMessage = getObject.getByLocatorFromRepository("ScreenErrorMessages_DefinedBySpan_Text", driver, jsonObjectRepo);
				errorElementList = driver.findElements(spanerrorMessage);
			}

			if(errorElementList != null)
			{
				for(WebElement element : errorElementList)
				{
					message = message + element.getText() +"\n";
				}
				return message;
			}
			else
			{
				return "No error message found. ";
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while verifying the error messages.", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "could not retrieve the error message."; 


		}
	}


	/**
	 * This keyword is for verify the title of the browser.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String verifybrowsertitle  (WebDriver driver, String objectName, String data)
	{
		try{
			//Getting data after removing any flag like "must pass flag"
			if(data.contains(";")){
				data = data.split(";")[0].trim();
			}
			else if(data.contains(",")){
				data = data.split(",")[0].trim();
			}

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Verifying the title of browser : " + driver );
			String actualValue = driver.getTitle().trim();
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Title of browser is: "+actualValue);

			if(actualValue.equalsIgnoreCase(data))
			{
				return passed_status + " Title is as expected.";
			}
			else 
			{
				return failed_status + " the actual title is : " + actualValue + " but the expected title is : " + data;
			}
		}catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the title: " + data + " in the element: " +driver, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + ": could not retrive the browser title."; 
		}
	}


	/**
	 * This keyword is used to get the text of any element.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String gettext  (WebDriver driver, String objectName, String data)
	{
		try 
		{
			/** create dynamic element first */
			webelement = handler.createDynamicWebElement(driver, objectName, data, jsonObjectRepo);
		} 
		catch (CustomException e1) 
		{
			/** if not dynamic element then create it normally */
			try {
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			} catch (CustomException e) {
				logger.error(e.getMessage());
			}
		}

		try{

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Getting the text of element : " + webelement );
			String actualValue = webelement.getText().trim();
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Text of element is: " +actualValue);

			return actualValue;
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred  "+e+" while getting the text from the element: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + " : could not retrive the text";
		}
	}


	/** This keyword will get the text of alert, if any.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String getalerttext  (WebDriver driver, String objectName, String data)
	{
		try{
			String actualText = driver.switchTo().alert().getText().toString();

			//Accepting alert
			acceptalert(driver,objectName, data);
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : actual text present in alert: " + actualText );
			return actualText;
		}
		catch(NoAlertPresentException e)
		{
			logger.warn("No Alert found.");
			return "No alert found.";
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the alert text. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "could not retrieve the alert text."; 
		}
	}


	/**
	 * This keyword is for get the title of browser.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String getbrowsertitle  (WebDriver driver, String objectName, String data)
	{
		try{			
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Title of browser : " + driver );
			String actualValue = driver.getTitle().trim();
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Title of browser is: "+actualValue);
			return actualValue;
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred  "+e+" while getting the browser title from the element: " +driver, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + " : could not retrive the page title"; 
		}
	}


	/** This keyword will be used to click on searched advertiser email .
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickadvertiseremail  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			if(!objectName.isEmpty())
			{
				/** added code to handle stale element exception until max attempt=5 */
				boolean flag = true;
				int maxCount = 0;
				while(flag)
				{
					try{
						webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
						webelement.click();
						flag = false;

					}catch(StaleElementReferenceException s)
					{
						logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Stale element exception is being handled ... attempt: "+maxCount);
						flag = true;
						maxCount++;
					}

					if(maxCount ==5)
					{
						break;
					}
				}

				/** proceed only if there is no stale exception while clicking advertiser email */
				if(!flag)
				{
					movetonewbrowserwindow(driver,objectName, data);
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Switched to new window successfully");

					//By by = getObject.getByLocatorFromRepository("placements_iframe", driver);		
					handler.moveToFrame(driver, "ifrm");
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moved to placements iframe.");

					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Current URL after clicking advertiser email: "+driver.getCurrentUrl());
					result = passed_status + "Clicked on advertiser email.";
				}
				else
				{
					result = failed_status + "Couldn't click advertiser email. ";
					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : result = "+result + " possible reason is : stale element exception. ");
				}
			}
			else
			{
				result = noObjectSuppliedMessage;
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click on advertiser email. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while clicking on link element: " +webelement,e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will be used to click on searched publisher email .
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickpublisheremail  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{

			if(data.isEmpty())
			{
				result = failed_status + "Please supply the publisher email id. ";
			}
			else
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicking publisher email: " + data);

				/**Wait until link is clickable */
				By byLocator = getObject.getByLocatorFromRepository(objectName, data, driver, jsonObjectRepo);
				handler.applyExplicitWait(driver, byLocator, new WebDriverException());

				/**Create dynamic element - based on supplied publisher email*/
				webelement = driver.findElement(byLocator);				

				webelement.click();
				movetonewbrowserwindow(driver, objectName, data);
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Switched to new window successfully");

				handler.moveToFrame(driver, "ifrm");
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moved to iframe.");

				result = passed_status + "Clicked on publisher email.";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click on searched publisher email link";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on searched publisher email link element: " +webelement,e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will be used to click the Menu Options located at the Left side of Portal,
	 * After clicking the menu option, driver focus will be moved to right side of page which is a frame.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickmenu  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			driver.switchTo().defaultContent();

			if(data.equalsIgnoreCase("advertisers"))
			{
				result = clicklink(driver, "advertisers_menu", data);
			}
			else if(data.equalsIgnoreCase("advertiser"))
			{
				result = clicklink(driver, "advertiser_menu", data);
			}
			else
			{
				result = clicklink(driver, objectName, data);
			}


			//Moving the focus on advertiser/publisher frame
			movetopage(driver, objectName, data);
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click on Menu Option - "+data;
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking dashboard menu. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}

		return result;
	}


	/**
	 * This keyword will move the driver focus to the iframe located at the right side of screen.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String movetopage  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			//By by = getObject.getByLocatorFromRepository("advertiser_iframe", driver);		
			if(handler.moveToFrame(driver, "ifrm"))
			{
				Thread.sleep(1000);
				result = passed_status+ "Moved to page.";
			}
			else
			{
				result = failed_status+ "Couldn't move to page.";
			}
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't move to page.";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while switching to iframe. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** this keyword is generic, will be used to move the driver focus to a window and handle the frame internally, 
	 * just pass the desired window name: 
	 * These screens are covered by this keyword
	 * Move to create/edit placement screen
	 * Move to Add Credit screen
	 * Move to Set Date screen
	 * Move to Create a New Marketplace Connection / Edit Connection screen
	 * Move to Assign Network screen
	 * Move to Clone Campaign screen
	 * Move to Edit Revenue screen
	 * Move to Alternate Ads / Upload Creative screen
	 * Move to Create New Account screen / Edit Account screen
	 * Move to Assign placements to Clients screen
	 * Move to Generate Reporting API screen
	 * Move to default content / move out of current iframe / frame
	 * Move to Generate Tag screen
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String movetowindow  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			data = data.toLowerCase();

			if((data.contains("create placement") || data.contains("edit placement") || data.matches("^placement.*"))
					|| (data.contains("add credit")) 
					|| (data.contains("set date")) 
					|| (data.contains("create a new marketplace connection") || data.contains("edit connection") || data.matches("^edit connection.*"))
					|| (data.contains("assign network") || data.matches("^assign network.*"))
					|| (data.contains("clone campaign") || data.matches("^clone campaign.*"))
					|| (data.contains("edit revenue") || data.matches("^edit revenue.*"))
					|| (data.contains("create new account") || data.matches("^create new account.*") || data.contains("edit account") || data.matches("^edit account.*"))
					|| (data.contains("assign placements to clients".toLowerCase()) || data.matches("^assign placements.*"))
					|| (data.contains("Generate Reporting API".toLowerCase()) || data.matches("^generate reporting api.*"))
					|| (data.contains("generate tag") || data.matches("^generate tag.*"))
					|| (data.contains("media (whitelist)") || data.matches("^whitelist.*"))
					|| (data.contains("media (blacklist)") || data.matches("^blacklist.*"))
					|| (data.contains("add site list")) || (data.contains("tag generator")||data.contains("add app list"))
					)
			{
				//By by = getObject.getByLocatorFromRepository("create_placements_iframe", driver);
				handler.moveToFrame(driver, "modalIfrm");

				result = passed_status + "Moved to " +data + " window successfully. ";
			}
			else if(data.contains("alternate ads") || data.contains("upload creative"))
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moved to "+data + " screen. ");
				result = passed_status + "Moved to " +data + " window successfully. ";
			}
			else if(data.equalsIgnoreCase("new browser window"))
			{
				result = movetonewbrowserwindow(driver, objectName, data);
			}
			else if(data.contains("default content") || data.matches("^default content.*") 
					|| data.contains("default screen") || data.matches("^default screen.*")
					|| data.contains("no frame") || data.matches("^no frame.*")
					|| data.contains("no iframe") || data.matches("^no iframe.*")
					|| data.startsWith("default") || data.isEmpty()
					|| data.contains("move out of window"))
			{
				driver.switchTo().defaultContent();
				result = passed_status + "Moved out of frame and switched to default content. ";
			}
			else
			{
				result = warning_status + "This screen wasn't found, please check again. ";
			}

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Moved to iframe corresponding to supplied window: "+data);
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't move to "+ data +" window. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while moving to "+data + " window. " ,e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will verify the existence of supplied web element(s), multiple elements 
	 * can be supplied separated by comma(,) or semicolon(;). 
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String verifyelementpresent  (WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			/** checking if supplied data is q query, if yes then execute it and get the data, create webelement using it
			 * and call recursively verifyelementpresent */
			if(data.toLowerCase().trim().startsWith("select") && data.toLowerCase().trim().contains("from"))
			{
				//Connection con = MobileTestClass_Methods.CreateSQLConnection();
				String [] arrayData = GenericMethodsLib.ExecuteMySQLQueryReturns1DArray(connection, data);
				//con.close();

				/** iterating the received data */
				for(int i=0; i<arrayData.length; i++)
				{
					/** recursive call to keywords.verifyelementpresent method */
					String dataToBeUsed = arrayData[i].trim();
					result = result + "\n" + verifyelementpresent(driver, objectName, dataToBeUsed);
				}
			}
			else
			{
				result = handler.verifyElementPresent(objectName, data, driver, webelement, getObject, 
						handler, applyRules, passed_status, failed_status, locationToSaveSceenShot, jsonObjectRepo);
			}
		}catch(Exception e)
		{
			if(e instanceof CustomException)
			{
				result = e.getMessage();
			}
			else
			{
				result = failed_status + "Couldn't check the presence of element. ";
				logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while checking the presence of element. ", e);

				/** Taking screenshot during exception */
				CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
			}
		}

		return result;
	}


	/** This keyword will verify the existence of supplied web element, if element not present then result is pass.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String verifyelementnotpresent  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		List<String> notPresentObjectList = new ArrayList<String>();

		try{
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Checking presence of supplied element: "+objectName);

			boolean listFlag = false;
			List<String> suppliedObjectList = new ArrayList<String>();

			/** if comma separated objects are supplied then splitting them into a list. */
			if(objectName.contains(","))
			{
				listFlag = true;
				suppliedObjectList = new ArrayList<String>(Arrays.asList(objectName.split(",")));
			}
			else if(objectName.contains(";"))
			{
				listFlag = true;
				suppliedObjectList = new ArrayList<String>(Arrays.asList(objectName.split(";")));
			}
			else
			{
				listFlag = false;

				boolean iselementDisplayed = false;

				/** if data is supplied with or without comma / semi colon then convert the data into list and get the first string 
				 * as data input to create the dynamic element, multiple data can be supplied like: xyz ; must pass 
				 * then last value needs to be separated out, other than this data can't be supplied.
				 */
				webelement = handler.parseObject_GetWebElement(driver, objectName, data, jsonObjectRepo);

				if(webelement == null){
					iselementDisplayed = false;
				}else
				{
					//Checking if element is displayed
					iselementDisplayed = webelement.isDisplayed();
				}

				if(iselementDisplayed)
				{
					result = failed_status + "Element is present. ";
				}
				else
				{
					result = passed_status + "Element is not present. ";
				}
			}

			/** Iterating list and collecting not present objects into notPresentObjectList list, in case of InvocationTargetException exception
			 * also, adding object into  notPresentObjectList list
			 */
			if(listFlag)
			{
				for(int i=0; i<suppliedObjectList.size(); i++)
				{
					/** Catching InvocationTargetException exception in case webelement is not found on web page.
					 */
					try{
						webelement = getObject.getWebElementFromRepository(suppliedObjectList.get(i), "name", driver, jsonObjectRepo);

						if(!webelement.isDisplayed())
						{
							notPresentObjectList.add(suppliedObjectList.get(i));
						}
					}catch(NullPointerException e)
					{
						notPresentObjectList.add(suppliedObjectList.get(i));
						logger.info(suppliedObjectList.get(i) + " wasn't found on web page. ");
					}
				}

				/** checking if notPresentObjectList's size, if this is equal to supplied one then pass
				 * else fail it.
				 */

				if(notPresentObjectList.size() == suppliedObjectList.size())
				{
					result = passed_status + "All supplied elements were not present. ";
				}
				else
				{
					/**
					 * Removing not present objects from supplied list and display only present elements in results.
					 */
					suppliedObjectList.removeAll(notPresentObjectList);
					result = failed_status + "Element(s): "+suppliedObjectList +" was(were) present. ";
				}
			}
		}
		catch(Exception e)
		{
			if(e instanceof CustomException)
			{
				result = e.getMessage();
			}
			else
			{
				result = failed_status + "Couldn't check the presence of element. ";
				logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while checking the presence of element. ", e);

				/** Taking screenshot during exception */
				CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
			}
		}

		return result;
	}


	/** This keyword will move the driver focus to alert, if any and accept it.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String acceptalert  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{

			if(handler.waitForAlert(driver))
			{
				driver.switchTo().alert().accept();
				result = passed_status+ "Alert accepted.";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Accepted alert. ");
			}
			else
			{
				return warning_status + "No alert found.";
			}
		}
		catch(NoAlertPresentException e)
		{
			logger.warn("No Alert found.");
			return warning_status + "No alert found.";
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't move to alert.";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while accepting alert. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will move the driver focus to alert (if any) and dismiss it.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String dismissalert  (WebDriver driver, String objectName, String data)
	{
		String result;

		try{

			if(handler.waitForAlert(driver))
			{
				driver.switchTo().alert().dismiss();
				result = passed_status+ "Alert dismissed.";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Alert dismissed. ");
			}
			else
			{
				return warning_status + "No alert found.";
			}
		}
		catch(NoAlertPresentException e)
		{
			logger.warn("No Alert found.");
			return warning_status + "No alert found.";
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't move to alert.";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while dismissing alert. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword is to verify the text of any alert.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String verifyalerttext  (WebDriver driver, String objectName, String data)
	{
		try{

			//Getting data after removing any flag like "must flag"
			if(data.contains(";")){
				data = data.split(";")[0].trim();
			}
			else if(data.contains(",")){
				data = data.split(",")[0].trim();
			}

			String actualText = driver.switchTo().alert().getText().toString();

			//Accepting alert
			acceptalert(driver,objectName, data);

			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : actual text present in alert: " + actualText );

			if(actualText.equals(data))
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Matched. ");
				return passed_status;
			}
			else 
			{
				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Text Doesn't Match. ");
				return failed_status + "The actual value is: " + actualText + " but the expected value is: " + data;
			}
		}
		catch(NoAlertPresentException e)
		{
			logger.warn("No Alert found.");
			return failed_status + "No alert found.";
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the alert text. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "could not retrieve the alert text."; 


		}
	}


	/** This keyword will click the searched placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickplacementname  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "placement");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't click the searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on seached placement. ", e);
			return failed_status + "Error occurred while clicking the searched placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickplacementsettings  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which row setting has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "rowsetting");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked row setting of searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't click the row setting of searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking row setting of searched placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking row setting of the searched placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched placement and then click on edit.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickeditplacementoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which edit has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "editPlacement");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on edit option of searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't on edit option of searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while editing searched placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while editing the searched placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched placement and then click on view-subplacement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickviewsubplacementsoption  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which view sub-placement has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "viewsubplacements");

				if(webelement != null)
				{
					String javaScript = "arguments[0].scrollIntoView();";
					handler.executeJavaScript(driver, javaScript, webelement);
					webelement.click();

					return passed_status + "Clicked on view option of searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't on view option of searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while viewing searched placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while viewing the searched placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched placement and then click on create sub-placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickcreatesubplacementoption  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which sub-placement has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "createsubplacement");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on create-subplacement option of searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't on create-subplacement option of searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on create-subplacement of searched placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking on create-subplacement option of the searched placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched placement and then click on view report.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickviewreport_placementoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the placement name for which view report has to be clicked.";
			}
			else
			{
				webelement = handler.getSearchedPlacementElements(driver, data, "viewreport");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on viewreport option of searched placement. ";
				}
				else
				{
					return failed_status + "Couldn't on viewreport option of searched placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on viewreport of searched placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking on viewreport option of the searched placement. ";
		}
	}


	/** This keyword will click the check box located in front of searched sub-placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String selectsubplacement  (WebDriver driver, String objectName, String data)
	{
		String xpath ="";
		try{

			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name which has to be selected.";
			}
			else
			{
				/** remove must pass flag if supplied */
				data = handler.parseMustPassFlag(data);

				xpath = handler.getSearchedSubPlacementElements(driver, data, "checkbox");
				Thread.sleep(2000);
				webelement = driver.findElement(By.xpath(xpath));

				if(webelement != null)
				{

					webelement.click();
					return passed_status + "Clicked searched sub-placement checkbox. ";
				}
				else
				{
					return failed_status + "Couldn't click the searched sub-placement checkbox. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on searched sub-placement checkbox using xpath: "+xpath, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking the searched sub-placement checkbox. ";
		}
	}


	/** This keyword will click the check box located in front of the desired ad in Campaign Creative screen.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String selectad  (WebDriver driver, String objectName, String data)
	{
		try{

			if(data.isEmpty())
			{
				return failed_status + "Please supply the Ad Name which has to be selected.";
			}
			else
			{
				/** remove must pass flag if supplied */
				data = handler.parseMustPassFlag(data);

				By byLocator = handler.getSearchedAdElement(driver, data, "searchedAdCheckBox");
				webelement = driver.findElement(byLocator);

				if(webelement != null)
				{	
					webelement.click();
					return passed_status + "Clicked searched ad check box. ";
				}
				else
				{
					return failed_status + "Couldn't click the searched ad check box. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on searched ad check box. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking the searched ad check box. ";
		}
	}


	/** This keyword will click the Set Date button corresponding to supplied Ad Name in Campaign Creative screen. 
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String setdateforad  (WebDriver driver, String objectName, String data)
	{
		try{

			Thread.sleep(2000);
			if(data.isEmpty())
			{
				return failed_status + "Please supply the Ad Name for which Set Date button has to be clicked.";
			}
			else
			{
				By bylocator = handler.getSearchedAdElement(driver, data, "searchedAdSetDate");				
				webelement = driver.findElement(bylocator);

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked Set Date button. ";
				}
				else
				{
					return failed_status + "Couldn't click the Set Date button. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking Set Date button. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking the Set Date button. ";
		}
	}


	/** This keyword will click the check box located in front of searched campaign.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String selectcampaign  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			if(data.isEmpty())
			{
				result = failed_status + "Please supply the campaign name for which has to be selected.";
			}
			else
			{
				/**
				 * Handling stale element reference exception, max attempt = 5
				 */
				int staleElementReferenceExceptionCount = 0;
				boolean staleElementReferenceException = true;

				while(staleElementReferenceException)
				{
					try{
						webelement = handler.getSearchedCampaignElement(driver, data);

						if(webelement != null)
						{
							webelement.click();
							result = passed_status + "Clicked checkbox of searched campaign. ";
						}
						else
						{
							result = failed_status + "Couldn't find the checkbox of searched campaign. ";
						}

						staleElementReferenceException = false;

					}catch(StaleElementReferenceException s)
					{
						staleElementReferenceException = true;
						staleElementReferenceExceptionCount ++;
					}

					if(staleElementReferenceExceptionCount == 5)
					{
						break;
					}
				}
			}

			return result;
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking checkbox of searched campaign. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking checkbox of the searched campaign. ";
		}
	}


	/** This keyword will click the searched sub-placement link.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clicksubplacementname  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name for which has to be clicked.";
			}
			else
			{
				String xpath = handler.getSearchedSubPlacementElements(driver, data, "subplacement");
				webelement = driver.findElement(By.xpath(xpath));

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked searched sub-placement. ";
				}
				else
				{
					return failed_status + "Couldn't click the searched sub-placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on searched sub-placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking the searched sub-placement. ";
		}

	}


	/** This keyword will click the row setting of the respective searched sub-placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clicksubplacementsettings  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name for which row setting has to be clicked.";
			}
			else
			{
				String xpath = handler.getSearchedSubPlacementElements(driver, data, "rowsetting");
				By bylocator = By.xpath(xpath);

				webelement = driver.findElement(bylocator);

				/** scrolling element before clicking it */
				try{
					handler.executeJavaScript(driver, "arguments[0].scrollIntoView()", webelement);}catch(Exception e){
						logger.debug(e.getMessage());
					}

				if(webelement != null)
				{					
					handler.applyExplicitWait(driver, bylocator, new WebDriverException());

					Thread.sleep(2500);

					try{
						webelement.click();
					}catch(StaleElementReferenceException s){
						webelement = driver.findElement(bylocator);
						webelement.click();
					}
					return passed_status + "Clicked row setting of searched sub-placement. ";
				}
				else
				{
					return failed_status + "Couldn't click the row setting of searched sub-placement. ";
				}
			}
		}
		catch(NoSuchElementException e1)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking row setting of searched sub-placement. ", e1);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Couldn't find the searched sub-placement. ";
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking row setting of searched sub-placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking row setting of the searched sub-placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched connection.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickconnectionsettings  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the connection name for which row setting has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedConnectionElements(driver, data, "rowsetting");

				if(webelement != null)
				{
					Thread.sleep(2000);
					try{
						webelement.click();
					}catch (StaleElementReferenceException e) {
						webelement = handler.getSearchedConnectionElements(driver, data, "rowsetting");
						webelement.click();
					}
					return passed_status + "Clicked row setting of searched connection. ";
				}
				else
				{
					return failed_status + "Couldn't click the row setting of searched connection. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking row setting of searched connection. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking row setting of the searched connection. ";
		}
	}


	/** This keyword will click the row setting of the respective searched client detail.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickclientsettings  (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the client name for which row setting has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedClientElements(driver, data, "rowsetting");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked row setting of searched client. ";
				}
				else
				{
					return failed_status + "Couldn't click the row setting of searched client. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking row setting of searched client. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clicking row setting of the searched client. ";
		}
	}


	/** This keyword will click the row setting of the respective searched sub-placement and then click on edit.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickeditsubplacementoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name for which edit option has to be selected.";
			}
			else
			{
				String xpath = handler.getSearchedSubPlacementElements(driver, data, "editsubplacement");
				webelement = driver.findElement(By.xpath(xpath));

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on edit option of searched sub-placement. ";
				}
				else
				{
					return failed_status + "Couldn't on edit option of searched sub-placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while editing searched sub-placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while editing the searched sub-placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched sub-placement and then click on clone.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickclonesubplacementoption   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name for which clone has to be selected.";
			}
			else
			{
				String xpath = handler.getSearchedSubPlacementElements(driver, data, "clonesubplacement");
				webelement = driver.findElement(By.xpath(xpath));

				if(webelement != null)
				{
					webelement.click();
					result = passed_status + "Clicked on clone option of searched sub-placement. ";
				}
				else
				{
					result = failed_status + "Couldn't on clone option of searched sub-placement. ";
				}

				//Moving to clone campaign window
				movetowindow(driver, objectName, "clone campaign");
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while cloning the searched sub-placement. ", e);
			result = failed_status + "Error occurred while cloning the searched sub-placement. ";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

		}
		return result;
	}


	/** This keyword will click the row setting of the respective searched client in Client Details screen and then click Edit.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickeditclientoption   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the client name for which has edit to be selected.";
			}
			else
			{
				webelement = handler.getSearchedClientElements(driver, data, "edit");

				if(webelement != null)
				{
					webelement.click();
					result = passed_status + "Clicked on edit option of searched client. ";
				}
				else
				{
					result = failed_status + "Couldn't on edit option of searched client. ";
				}

				//Moving to edit account window
				movetowindow(driver, objectName, "edit account");
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while editing the searched client. ", e);
			result = failed_status + "Error occurred while editing the searched client. ";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

		}
		return result;
	}


	/** This keyword will click the row setting of the respective searched client in Client Details screen and then click Delete.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickdeleteclientoption   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{

			if(data.isEmpty())
			{
				return failed_status + "Please supply the client name for which delete option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedClientElements(driver, data, "delete");

				if(webelement != null)
				{
					webelement.click();
					result = passed_status + "Clicked on delete option of searched client. ";
				}
				else
				{
					result = failed_status + "Couldn't delete the searched client. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while deleting the searched client. ", e);
			result = failed_status + "Error occurred while deleting the searched client. ";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will click the row setting of the respective searched client in Client Details screen and then click Generate Tag.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickclientgeneratetagoption   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the client name for which generate tag option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedClientElements(driver, data, "generatetag");

				if(webelement != null)
				{
					webelement.click();
					result = passed_status + "Clicked on generate tag option of searched client. ";
				}
				else
				{
					result = failed_status + "Couldn't click on generate tag option of searched client. ";
				}

				//Move to generate reporting api screen
				movetowindow(driver, objectName, "generate reporting api");
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while generating tag for the searched client. ", e);
			result = failed_status + "Error occurred while generating tag for the searched client. ";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will click the row setting of the respective searched client in Client Details screen and then click Assign Placement.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickclientassignplacementoption   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the client name for which assign placement option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedClientElements(driver, data, "assignplacement");

				if(webelement != null)
				{
					webelement.click();
					result = passed_status + "Clicked on assign placement option of searched client. ";
				}
				else
				{
					result = failed_status + "Couldn't click on assign placement option of searched client. ";
				}

				//Move to Assign Placement Screen
				movetowindow(driver, objectName, "assign placements");
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while assigning placement for the searched client. ", e);
			result = failed_status + "Error occurred while assigning placement for the searched client. ";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		} 
		return result;
	}


	/** This keyword will click the row setting of the respective searched sub-placement and then click on view tags.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clicksubplacementviewtagsoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the sub-placement name for which view tag option has to be selected.";
			}
			else
			{
				String xpath = handler.getSearchedSubPlacementElements(driver, data, "viewtags");
				webelement = driver.findElement(By.xpath(xpath));

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on view tags option of searched sub-placement. ";
				}
				else
				{
					return failed_status + "Couldn't on view tags option of searched sub-placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking view tags option of the searched sub-placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clikcing view tags option of the searched sub-placement. ";
		}
	}


	/** This keyword will click the row setting of the respective searched connection and then click edit.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickeditconnectionoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the connection name for which edit option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedConnectionElements(driver, data, "editconnection");

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Clicked on edit option of searched connection. ";
				}
				else
				{
					return failed_status + "Couldn't on edit option of searched connection. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking edit option of the searched connection. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clikcing edit option of the searched connection. ";
		}
	}


	/** This keyword will click the row setting of the respective searched connection and then click Enable.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickenableconnectionoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the connection name for which enable connection option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedConnectionElements(driver, data, "enableconnection");

				if(webelement != null)
				{
					webelement.click();
					Thread.sleep(3000);
					return passed_status + "Clicked on enable option of searched connection. ";
				}
				else
				{
					return failed_status + "Couldn't on enable option of searched connection. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking enable option of the searched connection. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clikcing enable option of the searched connection. ";
		}
	}


	/** This keyword will click the row setting of the respective searched connection and then click disable.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clickdisableconnectionoption   (WebDriver driver, String objectName, String data)
	{
		try{
			if(data.isEmpty())
			{
				return failed_status + "Please supply the connection name for which disable connection option has to be selected.";
			}
			else
			{
				webelement = handler.getSearchedConnectionElements(driver, data, "disableconnection");

				if(webelement != null)
				{
					webelement.click();
					Thread.sleep(3000);
					return passed_status + "Clicked on disable option of searched connection. ";
				}
				else
				{
					return failed_status + "Couldn't on disable option of searched connection. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking disable option of the searched connection. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clikcing disabling option of the searched connection. ";
		}
	}


	/** This keyword will update the values of Pricing Model, Budget, Daily Limit in search results screen 
	 * upon searching the sub-placement, 
	 * This keyword accepts three input separated by comma or semicolon, 
	 * Format of input data: Sub-Placement Name, WhatToUpdate (Pricing Model, Budget, Daily Limit ), Value
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String editvalueinplace   (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{

			boolean flag = true;

			//Converting input to a list by splitting string by comma first if no comma is provided then split by semicolon.
			List<String> inputList = new ArrayList<String>();

			if(data.contains(";"))
			{
				inputList = Arrays.asList(data.split(";"));
			}
			else if(data.contains(","))
			{
				inputList = Arrays.asList(data.split(","));
			}
			else
			{
				flag = false;
				result = failed_status + "Please provide data separated by comma or semicolon in sequence: Sub-Placement Name, PricingModel/Budget/DailyLimit, Value ";
			}

			//If correct values are not provided then don't proceed.
			if(flag)
			{

				String subplacement = inputList.get(0).trim();
				String whatToUpdate = inputList.get(1).trim();
				String value = inputList.get(2).trim();

				if(whatToUpdate.toLowerCase().trim().matches("^pricing.*"))
				{
					whatToUpdate = "pricingmodel";
				}
				else if(whatToUpdate.toLowerCase().trim().matches("^budget.*"))
				{
					whatToUpdate = "budget";
				}
				else if(whatToUpdate.toLowerCase().trim().matches("^daily.*"))
				{
					whatToUpdate = "dailylimit";
				}

				//Get the relevant element 
				String xpath = handler.getSearchedSubPlacementElements(driver, subplacement, whatToUpdate);
				By bylocator = By.xpath(xpath);
				webelement = driver.findElement(bylocator);

				if(webelement != null)
				{
					Thread.sleep(1500);
					handler.applyExplicitWait(driver, bylocator, new WebDriverException());

					try{
						webelement.clear();
						webelement.sendKeys(value);
					}catch(StaleElementReferenceException s){
						webelement = driver.findElement(bylocator);
						webelement.clear();
						webelement.sendKeys(value);
					}

					Thread.sleep(2000);

					result = passed_status + "Clicked on clone option of searched sub-placement. ";
				}
				else
				{
					result = failed_status + "Couldn't click on clone option of searched sub-placement. ";
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while updating inline values.", e);
			result = failed_status + "Exception occurred while updating inline values.";

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}


	/** This keyword will add the single or multiple campaigns to client on screen View Client --> Create New Client --> Create New Account
	 * Multiple campaign can be supplied separated with comma or semicolon.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String addcampaigntoclient   (WebDriver driver, String objectName, String data)
	{
		String result ="";
		try{

			boolean dataListFlag = false;
			List<String> datatList = new ArrayList<String>();

			//getting a list of supplied data 
			if(data.contains(";"))
			{
				dataListFlag = true;
				datatList = Arrays.asList(data.split(";"));
			}
			else if(data.contains(","))
			{
				dataListFlag = true;
				datatList = Arrays.asList(data.split(","));
			}
			else
			{
				webelement = handler.getSearchedCampaignForClient(driver, data);

				if(webelement != null)
				{
					webelement.click();
					return passed_status + "Selected campaign: "+data;
				}
				else
				{
					return failed_status + "Couldn't select campaign: "+data;
				}
			}

			//Iterating the supplied data list and getting elements to click them
			if(dataListFlag)
			{
				for(String campaignName : datatList)
				{
					webelement = handler.getSearchedCampaignForClient(driver, campaignName);

					if(webelement != null)
					{
						webelement.click();
						result = result + passed_status + "Selected campaign: "+campaignName + "  ";
					}
					else
					{
						result = result + failed_status + "Couldn't select campaign: "+campaignName + "  ";
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking view tags option of the searched sub-placement. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "Error occurred while clikcing view tags option of the searched sub-placement. ";
		}
		return result;
	}


	/** This keyword is to verify if any alert is present.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String verifyalertpresent  (WebDriver driver, String objectName, String data)
	{
		try
		{
			/** Get alert text, if no alert then move to NoAlertPresentException exception. */

			String alertText = driver.switchTo().alert().getText().toString();

			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : An alert was present having text = "+alertText);
			driver.switchTo().alert().accept();

			return passed_status + "An alert is present having text = "+alertText;
		}
		catch(NoAlertPresentException a)
		{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No Alert Was Found.");
			return failed_status + "No alert was present."; 
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the presence of alert. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "could not verify the presence of alert.";
		}
	}


	/** This keyword is to verify if alert is not present.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public String verifyalertnotpresent  (WebDriver driver, String objectName, String data)
	{
		try
		{
			/** Get alert text, if no alert then move to NoAlertPresentException exception. */

			String alertText = driver.switchTo().alert().getText().toString();

			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : An alert was present having text = "+alertText);
			driver.switchTo().alert().accept();

			return failed_status + "An alert is present having text = "+alertText;
		}
		catch(NoAlertPresentException a)
		{
			logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No Alert Was Found.");
			return passed_status + "No alert was present."; 
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the presence of alert. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

			return failed_status + "could not verify the presence of alert.";
		}
	}


	/** This keyword is to verify the value of supplied element, it checks the text present in VALUE attribute of
	 * supplied element.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String verifyelementvalue  (WebDriver driver, String objectName, String data)throws CustomExceptionStopExecution
	{
		String result = "";
		try
		{
			if(objectName.isEmpty())
			{
				result = failed_status + "Supplied object name is empty. ";
			}
			else
			{
				//Getting data after removing any flag like "must pass"
				if(data.contains(";")){
					data = data.split(";")[0].trim();
				}
				else if(data.contains(",")){
					data = data.split(",")[0].trim();
				}

				/** First the check if the supplied element is a dynamic object which needs data to create element definition, 
				 * if no, then createDynamicWebElement method will throw CustomExceptionsLib exception, and then find the element using 
				 * method: getWebElementFromRepository
				 */
				try{
					webelement = handler.createDynamicWebElement(driver, objectName, data, jsonObjectRepo);
				}catch(CustomException c){
					webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
				}

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Verifying the value of element: " + webelement );
				String actualValue = webelement.getAttribute("value").trim();

				logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : actual value of element : " + actualValue);

				if(actualValue.equals(data))
				{
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Value Matched. ");
					result = passed_status + "Value is as expected.";
				}
				else 
				{
					logger.debug(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Actual and Expected Value Doesn't Match. ");
					result = failed_status + "The actual value is: " + actualValue + ", the expected value is: " + data;
				}
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			logger.error(failed_status + "Exception occurred while verifying the value: " + data + " of the element: " +webelement, e);
			result = failed_status + "Could not retrieve the value."; 

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

		}
		return result;
	}


	/**
	 * This keyword is completely responsible for login by google interface
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String googlelogin  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		String username = "";
		String password = "";
		try{

			if(!data.isEmpty())
			{
				//Getting data after removing any flag like "must pass"
				if(data.contains(";")){
					username = data.split(";")[0].trim();
					password = data.split(";")[1].trim();	
				}
				else if(data.contains(",")){
					username = data.split(",")[0].trim();
					password = data.split(",")[1].trim();
				}

				// Entering the username
				webelement = getObject.getWebElementFromRepository("gmail_username", "name", driver, jsonObjectRepo);
				webelement.sendKeys(username);
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Typing the value : " + username + " in the element: " + webelement );

				// Clicking on Next button
				webelement = getObject.getWebElementFromRepository("gmail_next_button", "name", driver, jsonObjectRepo);
				webelement.click();
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicked on the next button: " + webelement );

				/** dynamic wait unitl password text is present */
				By byLocator = getObject.getByLocatorFromRepository("gmail_password", driver, jsonObjectRepo);
				handler.applyExplicitWait(driver, byLocator, new ElementNotVisibleException(""), 120);

				// Entering the password
				webelement = getObject.getWebElementFromRepository("gmail_password", "name", driver, jsonObjectRepo);
				webelement.sendKeys(password);
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Typing the value : " + password + " in the element: " + webelement );

				// Getting the URL before login  
				String urlBeforeLogin = driver.getCurrentUrl();

				webelement = getObject.getWebElementFromRepository("gmail_next_button", "name", driver, jsonObjectRepo);
				webelement.click();

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicked on the sign button: " + webelement );

				/** putting a dynamic wait until browser'title is not vdopia */
				try{
					int count = 0;
					while(!driver.getTitle().contains("Vdopia") && count < 90)
					{
						Thread.sleep(1000);
						count ++;
					}}catch(TimeoutException t){driver.navigate().refresh();}

				// Getting the URL after successful login
				String urlAfterLogin = driver.getCurrentUrl();

				if (!urlBeforeLogin.equalsIgnoreCase(urlAfterLogin))
				{
					result = passed_status+ "user logged in successfully";
					//logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" Page Source: "+driver.getPageSource());
				}
				else
				{
					result = failed_status + "Google login was unsuccessful. ";
				}
			}

			else
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No login credentials were provided. " );
				result = failed_status+ "No login credentials were provided. ";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Google login was unsuccessful. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while google sign in. ", e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);

		}
		return result;
	}


	/** This keyword will be used to scroll objects.
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String scrollobject  (WebDriver driver, String objectName, String data) 
	{
		String result = "";
		By byLocator = null;
		try
		{	
			try{
				/** create dynamic locator */
				byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
			}catch(CustomException e)
			{
				/** create locator normally */
				byLocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);
			}

			//apply explicit wait
			handler.applyExplicitWait(driver, byLocator, new ElementNotVisibleException(""), 7);
			webelement = driver.findElement(byLocator);

			//execute js to bring element into view
			String javaScript = "arguments[0].scrollIntoView(false);";
			handler.executeJavaScript(driver, javaScript, webelement);

			result = passed_status+ "Scrolled the bar successfully";
		}
		catch(Exception e){
			result = failed_status + e.getMessage();
		}
		return result;
	}


	/** This is used to escape the auto fill --- pages like package and deal screen
	 * 
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String escapeautofill  (WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{	Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
		result = passed_status+"Escaped the object successfully";
		}
		catch(Exception e){
			result = failed_status + e.getMessage();
		}
		return result;	
	}


	/** This keyword is for create html page with channel tag
	 * 
	 * @param objectName
	 * @param data
	 */
	public String savehtmltag  (WebDriver driver, String objectName, String data) 
	{
		String result = "";

		try{
			String template = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\" \"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>";
			template = template + "\n" + "$body" + "</body></html>";

			String tag = FileLib.getClipBoardText();
			String htmlString = template.replace("$body", tag);

			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received channel tag: "+tag);
			FileLib.WriteFile(htmlString, TestSuiteClass.AUTOMATION_HOME.toString()+"/tpt", "tag.html");

			result = passed_status+"HTML page created sccessfully at: "+ TestSuiteClass.AUTOMATION_HOME.toString()+"/tpt/tag.html";
		}
		catch(Exception e)
		{
			result = failed_status+"HTML page wasn't created. ";
			logger.error(e.getMessage(), e);
		}

		return result;
	}


	/** This keyword is for create html page with channel tag
	 * 
	 * @param objectName
	 * @param data
	 */
	public String savetag  (WebDriver driver, String objectName, String data) 
	{
		String result = "";

		try{

			String tag = FileLib.getClipBoardText();
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Received channel tag: "+tag);

			FileLib.WriteFile(tag, TestSuiteClass.AUTOMATION_HOME.toString()+"/tpt", "tag.txt");
			result = passed_status+"Tag saved at: "+ TestSuiteClass.AUTOMATION_HOME.toString()+"/tpt/tag.txt";

		}
		catch(Exception e)
		{
			result = failed_status+"Tag is not saved successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occured when writting the tag.", e);

		}

		return result;
	}


	/**
	 * This keyword will apply the targeting as per the provided data, received data will be in json format containing the
	 * desired targeting and respective values. Add multiple targeting in targeting json object as json object only not as json array.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String applytargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);

			//---> TO BE DONE LATER 
			/** clear existing targeting if the received json data has flag clearExistingTargeting = yes -- by deleting it from db in qa */
			if(jsonData.getString("clearExistingTargeting").trim().equalsIgnoreCase("Yes"))
			{
				String campaignid = jsonData.getString("campaignid").trim();
				//handler.clearTargeting(campaignid);
				clearcampaigntargeting(driver, objectName, campaignid);
			}

			/** parsing targeting node and apply respective targeting */
			JSONObject targetingJsonData = jsonData.getJSONObject("targeting");

			Iterator<?> targetingKeys = targetingJsonData.keys();
			while(targetingKeys.hasNext())
			{
				String targetingName = (String) targetingKeys.next();
				String targetingData = targetingJsonData.getString(targetingName);
				result = new PerformAction().performAction(driver, targetingName, objectName, targetingData, connection, jsonObjectRepo);
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Targeting is not applied successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying targeting ", e);
		}
		return result;
	}


	/** This keyword sets the app / site targeting, received data will be in json format containing
	 * include and exclude values. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String appsitetargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();

				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){
					result = result +selectradiobutton(driver, "Create_Sub_placement_SiteDomainandAppBundleTargeting_Include/Exclude_RadioButton",key);

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Add app/site list based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("site") || targetingkey.equalsIgnoreCase("app"))
						{
							if(targetingkey.equalsIgnoreCase("site"))
							{
								result = result +"\n"+ clickbutton(driver, "Create_Sub_placement_SiteDomainandAppBundleTargeting_AddSiteList_Button", "");
								result = result +"\n"+ movetowindow(driver, objectName, "Add Site List");
							}

							else if(targetingkey.equalsIgnoreCase("app"))
							{
								result = result +"\n"+ clickbutton(driver, "Create_Sub_placement_SiteDomainandAppBundleTargeting_AddAppList_Button", "");
								result = result +"\n"+ movetowindow(driver, objectName, "Add App List");
							}

							JSONArray actualValues = jsonTargetingData.getJSONArray(targetingkey);
							for(int i = 0; i < actualValues.length(); i++)
							{
								String actualValue = actualValues.getString(i);
								result=result +"\n"+ typevalue(driver, "Create_Sub_placement_SiteDomainandAppBundleTargeting_AddSiteAppList_Textarea", actualValue);
							}
							result = result +"\n"+ clickbutton(driver, "Create_Sub_placement_SiteDomainandAppBundleTargeting_AddSiteAppList_Continue_Button", "");

							result = result +"\n"+ movetopage(driver, "", "");
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"App/Site Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying App/Site Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the deal targeting, received data will be in json format containing
	 * include and exclude values. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String dealtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		String bidderType = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			JSONObject targetingData = (JSONObject) jsonData.get("include");
			String dealID = targetingData.getString("dealID");
			String bidderID = targetingData.getString("bidderID");
			String sqlQuery = "select BidderType from hudsonBidder where bidderId = '"+bidderID+"'"; 
			ResultSet rs = GenericMethodsLib.ExecuteMySQLQueryReturnsResultSet(connection, sqlQuery);
			rs.beforeFirst();	// Setting the cursor at first line	
			while (rs.next())
			{
				bidderType = rs.getString(1).toString();
			}

			result = result +clickexpandcollapseicon(driver,"Create_Sub_placement_DealTargeting_PlusIcon", "");
			result = result +typevalue(driver, "Create_Sub_placement_DealTargeting_SearchDeals_Textbox",dealID);
			result = result +clicklink(driver, "Create_Sub_placement_DealTargeting_SearchDeals_value",dealID);
			String query = "insert into vanilla_bidder_mapping (bidder_id,bidder_type,status) values ('"+bidderID+"','"+bidderType+"','active')";
			executedbquery(driver, query, null);

		}
		catch(Exception e)
		{
			result = failed_status+"Deal Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Deal Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the language targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String languagetargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				//movetopage(driver, "", "");
				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on Language Targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_LanguageTargeting_PlusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the language based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("languageName"))
						{
							JSONArray languages = jsonTargetingData.getJSONArray("languageName");

							for(int i = 0 ; i < languages.length(); i ++)
							{
								String language = languages.getString(i);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_LanguageTargeting_Options_Checkbox", language);
							}

						}
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_LanguageTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_LanguageTargeting_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Language Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Language Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the os targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String ostargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				//movetopage(driver, "", "");
				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on Operating System Targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_OSTargeting_PlusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the os based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("OSName"))
						{
							JSONArray osNames = jsonTargetingData.getJSONArray("OSName");

							for(int i = 0 ; i < osNames.length(); i ++)
							{
								String osName = osNames.getString(i);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_OSTargeting_Options_Checkbox", osName);
							}

						}
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_OSTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_OSTargeting_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"OS Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying OS Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the os version targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String osversiontargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				//movetopage(driver, "", "");
				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on os version targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_OSVersionTargeting_PlusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the os version based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("OSVersionName"))
						{
							JSONArray OSVersionNames = jsonTargetingData.getJSONArray("OSVersionName");

							for(int i = 0 ; i < OSVersionNames.length(); i ++)
							{
								String OSVersionName = OSVersionNames.getString(i);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_OSVersionTargeting_Options_Checkbox", OSVersionName);
							}

						}
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_OSVersionTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_OSVersionTargeting_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"OS version targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying OS version targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Mobile Device Type targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String mobiledevicetypetargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on Mobile Device Type targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_MobileDeviceTypeTargeting_PlusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the os version based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("MobileDeviceType"))
						{
							JSONArray MobileDeviceTypes = jsonTargetingData.getJSONArray("MobileDeviceType");

							for(int i = 0 ; i < MobileDeviceTypes.length(); i ++)
							{
								String MobileDeviceType = MobileDeviceTypes.getString(i);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_MobileDeviceTypeTargeting_Options_List", MobileDeviceType);
							}

						}
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_MobileDeviceTypeTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_MobileDeviceTypeTargeting_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Mobile Device Type targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Mobile Device Type targeting ", e);
		}
		return result;
	}


	/**This keyword sets the location targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String locationtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();

				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on location targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_LocationTargeting_PlusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the location based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("lat"))
						{
							int latitute  = jsonTargetingData.getInt("lat");
							int MinLat = latitute - 10;
							int MaxLat = latitute + 10;
							String MinLatString = String.valueOf(MinLat);
							String MaxLatString = String.valueOf(MaxLat);

							result = result +"\n"+ typevalue(driver, "Create_Sub_placement_LocationTargeting_MinLat_textfield", MinLatString);
							result = result +"\n"+ typevalue(driver, "Create_Sub_placement_LocationTargeting_MaxLat_textfield", MaxLatString);

						}
						else if(targetingkey.equalsIgnoreCase("long"))
						{
							int longitute = jsonTargetingData.getInt("long");
							int MinLong = longitute - 10;
							int MaxLong = longitute + 10;
							String MinLongString = String.valueOf(MinLong);
							String MaxLongString = String.valueOf(MaxLong);

							result = result +"\n"+ typevalue(driver, "Create_Sub_placement_LocationTargeting_MinLong_textfield", MinLongString);
							result = result +"\n"+ typevalue(driver, "Create_Sub_placement_LocationTargeting_MaxLong_textfield", MaxLongString);
						}
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_LocationTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_LocationTargeting_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Location Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Location Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the category targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String categorytargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				//movetopage(driver, "", "");
				if(key.equalsIgnoreCase("include")){

					/** Select on 'Select specific categories' radio button */
					result = result + selectradiobutton(driver, "Create_Sub_placement_CategoryTargeting_SpecificCategories_RadioButton", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the Category based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("Category"))
						{
							JSONArray CategoryNames = jsonTargetingData.getJSONArray("Category");

							for(int i = 0 ; i < CategoryNames.length(); i++)
							{
								String CategoryName = CategoryNames.getString(i);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_CategoryTargeting_Categories_CheckBoxes", CategoryName);
							}

						}
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Category targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Category targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Geographies targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String geographiestargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include")){

					/** Select on 'Select specific countries' radio button */
					result = result + selectradiobutton(driver, "Create_Sub_placement_GeographiesTargeting_SpecificCountries_RadioButton", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the Country based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("Country"))
						{
							JSONArray CountryNames = jsonTargetingData.getJSONArray("Country");

							for(int i = 0 ; i < CountryNames.length(); i ++)
							{
								String CountryName = CountryNames.getString(i);
								result = result +"\n"+ clicklink(driver, "Create_Sub_placement_GeographiesTargeting_Countrieslist", CountryName);
							}

						}
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Geographies targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Geographies targeting ", e);
		}
		return result;
	}


	/** This keyword sets the DMA codes targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String dmacodestargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include")){

					/** Select on 'Select specific DMA codes' radio button */
					result = result + selectradiobutton(driver, "Create_Sub_placement_DMACodesTargeting_SpecificDMACodes_RadioButton", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the DMA codes based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("DMA codes"))
						{
							JSONArray CountryNames = jsonTargetingData.getJSONArray("DMA codes");

							for(int i = 0 ; i < CountryNames.length(); i ++)
							{
								String CountryName = CountryNames.getString(i);
								result = result +"\n"+ typevalue(driver, "Create_Sub_placement_DMACodesTargeting_SearchDMA_testbox", CountryName);
								result = result +"\n"+ clicklink(driver, "Create_Sub_placement_DMACodesTargeting_DMACodeslist", CountryName);
							}

						}
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"DMA codes targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying DMA codes targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Publisher Integration Direct vs. Indirect targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String directindirecttargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			JSONObject jsonTargetingData;

			try{
				jsonTargetingData = jsonData.getJSONObject("include");
			}catch(JSONException j){
				jsonTargetingData = jsonData.getJSONObject("exclude");
			}
			/** Select the DMA codes based on provided data in include/exclude */
			String publisherIntegration = jsonTargetingData.getString("Publisher Integration");

			result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_PublisherIntegrationDirectvs.Indirect_dropdown", publisherIntegration);

		}
		catch(Exception e)
		{
			result = failed_status+"Publisher Integration Direct vs. Indirect targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Publisher Integration Direct vs. Indirect targeting ", e);
		}
		return result;
	}


	/** This keyword sets the ron setting targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String ronsettingtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include")){

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the DMA codes based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("ronsetting"))
						{
							String ronsetting = jsonTargetingData.getString("ronsetting");
							if(!ronsetting.isEmpty())
							{
								result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_RonSetting_dropdown", ronsetting);
							}
						}

					}
				}
			}

		}
		catch(Exception e)
		{
			result = failed_status+"Publisher Integration Direct vs. Indirect targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Publisher Integration Direct vs. Indirect targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Retargeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String retargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include")){

					/** Clicked on retargeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_Retargeting_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the retargeting based on provided data in include */

					String group = jsonTargetingData.getString("group");
					result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_Retargeting_Group_Dropdown", group);

					String condition1 = jsonTargetingData.getString("condition1");
					result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_Retargeting_ConditionOne_Dropdown", condition1);

					String condition2 = jsonTargetingData.getString("condition2");
					result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_Retargeting_ConditionTwo_Dropdown", condition2);

					String withID = jsonTargetingData.getString("withID");
					result = result +"\n"+ typevalue(driver, "Create_Sub_placement_Retargeting_WithID_textBox", withID);

					String atLeast = jsonTargetingData.getString("atLeast");
					result = result +"\n"+ typevalue(driver, "Create_Sub_placement_Retargeting_atleast_textBox", atLeast);
					result = result +"\n"+ clickbutton(driver, "Create_Sub_placement_Retargeting_ADD_button", "");
					result = result +"\n"+selectcheckbox(driver, "Create_Sub_placement_Retargeting_shortTerm_Checkbox", "");

					String shortTerm = jsonTargetingData.getString("shortTerm");
					result = result +"\n"+ selectradiobutton(driver, "Create_Sub_placement_Retargeting_ShortTermRetargeting_RadioButton", shortTerm);
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Retargeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Retargeting ", e);
		}
		return result;
	}


	/** This keyword sets the App vs. Mobile Web targeting , received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String appvsmobilewebtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			/** get all the keys in include json object and these keys are the fields which contains the values */
			JSONObject jsonTargetingData;

			try{
				jsonTargetingData = jsonData.getJSONObject("include");
			}catch(JSONException j){
				jsonTargetingData = jsonData.getJSONObject("exclude");
			}

			/** Select the App vs. Mobile Web targeting based on provided data in include */
			String AppVsWeb = jsonTargetingData.getString("AppVsWeb");

			result = result +"\n"+ selectdropdownvalue(driver, "Create_Sub_placement_AppvsMobileWeb_Targeting_dropdown", AppVsWeb);
		}
		catch(Exception e)
		{
			result = failed_status+"App vs. Mobile Web is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying App vs. Mobile Web ", e);
		}
		return result;
	}


	/** This keyword sets the Rotate Reach targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String rotatereachtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include/exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include") || key.equalsIgnoreCase("exclude")){

					/** Clicked on Rotate Reach plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_RotateReach_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the Rotate Reach targeting based on provided data in include */

					String selection = jsonTargetingData.getString("selection");
					result = result +"\n"+ selectradiobutton(driver, "Create_Sub_placement_RotateReach_selection_redioButton", selection);

					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_RotateReach_filter_dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_RotateReach_filter_dropdown", "Filter Out");
					}

				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Rotate Reach targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Rotate Reach targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Adult Content targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String adultcontenttargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include/exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include") || key.equalsIgnoreCase("exclude")){

					/** Clicked on Adult Content plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_AdultContent_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the Adult Content targeting based on provided data in include */

					String NotAllowed = jsonTargetingData.getString("NotAllowed");
					result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_AdultContent_NotAllowed_checkbox", NotAllowed);

					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_AdultContent_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_AdultContent_Filter_Dropdown", "Filter Out");
					}

				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Adult Content targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Adult Content targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Keyword Targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String keywordtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include/exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include") || key.equalsIgnoreCase("exclude")){

					/** Clicked on Keyword Targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_KeywordTargeting_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the Keyword Targeting based on provided data in include */

					String keywords = jsonTargetingData.getString("keywords");
					result = result +"\n"+ typevalue(driver, "Create_Sub_placement_KeywordTargeting_Keywords_Textbox", keywords);

					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_KeywordTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_KeywordTargeting_Filter_Dropdown", "Filter Out");
					}

				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Keyword Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Keyword Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Hudson targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String hudsontargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include/exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include") || key.equalsIgnoreCase("exclude")){

					/** Clicked on hudson targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_HudsonTargeting_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the hudson targeting based on provided data in include */

					String Allowed = jsonTargetingData.getString("Allowed");
					result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_HudsonTargeting_Allowed_checkbox", Allowed);

					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_HudsonTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_HudsonTargeting_Filter_Dropdown", "Filter Out");
					}

				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Hudson targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Hudson targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Connection type targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String connectiontypetargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include/exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include") || key.equalsIgnoreCase("exclude")){

					/** Clicked on connection type targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_ConnectionTypeTargeting_plusSign", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);

					/** Select the connection type targeting based on provided data in include */

					JSONArray connectionTypes = jsonTargetingData.getJSONArray("ConnectionTypes");
					for(int i=0; i < connectionTypes.length(); i++)
					{
						String connectionType = (String) connectionTypes.get(i);
						result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_ConnectionTypeTargeting_input_checkbox", connectionType);
					}
					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_ConnectionTypeTargeting_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_ConnectionTypeTargeting_Filter_Dropdown", "Filter Out");
					}

				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Connection type targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Connection type targeting ", e);
		}
		return result;
	}


	/** This keyword sets the Device Capabilities targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String devicecapabilitytargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("exclude") || key.equalsIgnoreCase("include") ){

					/** Clicked on Device Capabilities Targeting plus sign */
					result = result + clickbutton(driver, "Create_Sub_placement_DeviceCapabilities_PlusSign", "");
					result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_DeviceCapabilities_Options_Checkbox", "");

					if(key.equalsIgnoreCase("include"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_DeviceCapabilities_Filter_Dropdown", "Filter In");
					}
					else if (key.equalsIgnoreCase("exclude"))
					{
						result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_DeviceCapabilities_Filter_Dropdown", "Filter Out");
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"Device Capabilities Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying Device Capabilities Targeting ", e);
		}
		return result;
	}

	/** This keyword sets the Weekday targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String weekdaytargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();

				/** Clicked on Weekday plus sign */
				result = result + clickbutton(driver, "Create_Sub_placement_WeekDayTargeting_PlusSign", "");
				JSONObject includeExcludeJson = jsonData.getJSONObject(key);
				String day = includeExcludeJson.getString("day");
				String NewSqlQuery = "";
				if(day.equalsIgnoreCase("today"))
				{
					NewSqlQuery = "select dayname(now()) as weekday;";

				}
				else if(day.equalsIgnoreCase("notToday"))
				{
					NewSqlQuery = "select dayname(now() + INTERVAL 1 DAY) as weekday;";

				}	

				Statement NewStmt = (Statement) connection.createStatement();
				ResultSet NewRs = (ResultSet) NewStmt.executeQuery(NewSqlQuery);
				NewRs.next();
				String weekday = NewRs.getString("weekday");
				result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_WeekDayTargeting_Options_Checkbox", weekday);

				if(key.equalsIgnoreCase("include"))
				{
					result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_WeekDayTargeting_Filter_Dropdown", "Filter In");
				}
				else if (key.equalsIgnoreCase("exclude"))
				{
					result = result+ "\n" + selectdropdownvalue(driver, "Create_Sub_placement_WeekDayTargeting_Filter_Dropdown", "Filter Out");
				}
			}

		}
		catch(Exception e)
		{
			result = failed_status+"Weekday Targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying weekday Targeting ", e);
		}
		return result;
	}


	/** This keyword sets the channel targeting, received data will be in json format containing
	 * include values
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String channeltargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try
		{
			JSONObject jsonData = new JSONObject(data);
			Iterator<?> keys = jsonData.keys();
			while(keys.hasNext())
			{
				/** getting include and exclude key and based on that performing action */
				String key = (String) keys.next();
				if(key.equalsIgnoreCase("include")){

					/** Select on 'Select specific channel' radio button */
					result = result + selectradiobutton(driver, "Create_Sub_placement_SelectSpecificApplications_RadioButton", "");

					/** get all the keys in include / exclude json object and these keys are the fields which contains the values */
					JSONObject jsonTargetingData = jsonData.getJSONObject(key);
					Iterator<?> targetingkeys = jsonTargetingData.keys();

					/** Select the channels based on provided data in include/exclude */
					while(targetingkeys.hasNext())
					{						
						String targetingkey = (String) targetingkeys.next();
						if(targetingkey.equalsIgnoreCase("channel"))
						{
							String channel = jsonTargetingData.getString("channel");
							if(!channel.isEmpty())
							{
								result = result +"\n"+ typevalue(driver, "Create_Sub_placement_SearchApplication_textBox", channel);
								result = result +"\n"+ selectcheckbox(driver, "Create_Sub_placement_channel_selectcheckBox", channel);
								result = result +"\n"+ clickbutton(driver, "Create_Sub_placement_channel_add", "");
								result = result +"\n"+ acceptalert(driver, "", "");
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			result = failed_status+"DMA codes targeting is not applying successfully";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred when applying DMA codes targeting ", e);
		}
		return result;
	}


	/** This keyword will delete all campaign targeting from db. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clearcampaigntargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			data = data.trim();

			String sql = "delete from campaign_target where cid = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from domain_bundle_targetting where campaign_id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from campaign_retargeting where campaign_id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from campaign_targeting_info where campaign_id = "+data;
			//new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from geography where campaign_id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "update campaign set review_status = 'Pending Review', integration_type = 2, "
					+ "  platform = 'any', status = 'active' ,channel_set = NULL, channel_choice = NULL, deals = '' where id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			/** setting up limit in rt_campaign_matrix table to maintain serving */
			sql = "update rt_campaign_matrix set short_limit_to_serve = 100, daily_limit_to_serve = 100, total_limit_to_serve = 100 where campaign_id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			result = passed_status + " Targetings are cleared from db. ";
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Existing targeting is cleared from DB for: "+data);
		}
		catch(Exception e)
		{
			result = failed_status + " Targetings are not cleared from db. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : " +e.getMessage(), e);
		}

		return result;
	}

	/** This keyword will clear all provided channel targeting from db. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clearchanneltargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			data = data.trim();

			String sql = "update channels set ApiFramework = '' where id="+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "update channels set integration_type = 2 where id = "+data;
			new DBLib().executeUpdateInsertQuery(connection, sql);

			result = passed_status + " Channel targeting are cleared from db. ";
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Existing channel targeting are cleared from DB for: "+ data);
		}
		catch(Exception e)
		{
			result = failed_status + "Channel targeting are not cleared from db. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : " + e.getMessage(), e);
		}

		return result;
	}


	/** This method will clear all the values of supplied package id from db.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clearpackagetargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			data = data.trim();
			String sql = "update packages set " + 
					//					"definition = '{\"inventoryFilters\":[],\"apporwebtargetting\":\"-1\",\"categories\":[],\"categoriesinclude\":1,\"domainbundleinclude\":"
					//					+ "1,\"adultcontent\":1,\"deviceOS\":[],\"deviceOSinclude\":1,\"deviceID\":[],\"deviceCategory\":\"-1\",\"deviceMake\":[],\"carrierwhitelist\""
					//					+ ":[],\"ispwhitelist\":[],\"wifitargeting\":\"-1\",\"country\":\"-1\",\"states\":[],\"cities\":[],\"site\":\"\",\"packagename\":\"clear\","
					//					+ "\"dp_expression\":\"\"}', "+
					"video_inventory_type_expr = '-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1', "+
					"app_web_expr = '-1VDO_&-1VDO_&-1VDO_&1', "+
					"devices_expr = '-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1', "+
					"carriers_network_expr = '-1VDO_&-1VDO_&-1', "+
					"location_expr = '-1VDO_&-1VDO_&-1VDO_&-1VDO_&-1', "+
					"data_provider_expr = '-1VDO_&-1VDO_&-1', "+
					"design_id = NULL, " +
					"status = 'inactive' "+
					"where id = "+data + ";";

			if(new DBLib().executeUpdateInsertQuery(connection, sql)){
				result = passed_status + " Package targeting are cleared from db. ";
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Existing package targeting are cleared from DB for: "+data);
			}
		}
		catch(Exception e)
		{
			result = failed_status + "Package targeting are not cleared from db. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+e.getMessage(), e);
		}

		return result;
	}


	/** This method will clear all the values of supplied deal id from db.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String cleardealtargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			data = data.trim();

			String deal = data.trim().split(":")[0];
			String bidderName = data.trim().split(":")[1];

			String sql = "update deals set status = 'pause' where deal_id = '"+deal+"'";
			new DBLib().executeUpdateInsertQuery(connection, sql);

			/** pause all the deals which are applicable to all bidders */
			sql = "update deals set status = 'pause' where definition like '%\"bidders\":\"\"%' OR definition like '%"+bidderName+"%' ;";
			new DBLib().executeUpdateInsertQuery(connection, sql);

			result = passed_status + " Deal targeting is cleared from db for: "+deal;
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Existing Deal targeting are cleared from DB for: "+deal);
		}
		catch(Exception e)
		{
			result = failed_status + "Deal targeting is not cleared from db. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+e.getMessage(), e);
		}

		return result;
	}


	/** This method will clear all the bidders from Deal UI.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String removebiddersfromdealui(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			data = data.trim();

			/** getting all x Remove icon Object Name from OR to delete the selected bidders in UI by clicking on all of them */
			By byLocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);

			List<WebElement> webElements = null;
			try{
				webElements = driver.findElements(byLocator);
			}catch(Exception e)
			{logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ " Exception occurred while getting Bidders from Deal UI: ", e);}

			if(webElements != null){

				for(WebElement removeButton :  webElements)
				{
					removeButton.click();
				}
			}

			result = passed_status + " Bidder were removed from Deal UI. ";
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Bidder are removed from Deal UI." );
		}
		catch(Exception e)
		{
			result = failed_status + "Some unknown error occurred while removing Bidders from Deal UI. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+ " Some unknown error occurred while removing Bidders from Deal UI.", e);
		}

		return result;
	}


	/** This keyword will clear all provided bidder targeting from db. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String clearbiddertargeting(WebDriver driver, String objectName, String data)
	{
		String result = "";

		try
		{
			String channelId = data.trim().split(":")[1];
			data = data.trim().split(":")[0];

			/** Before starting the Bidder Test Case, bidder has to Approved and at Test Cases level, explicitly 
			 * bidder will Disapproved before executing test case */

			String sql = "update hudsonBidder set data_provider_expr = '', BidderStatus = 'Disapproved', "
					+ " channel_choice = '"+channelId+"', ClientSideUnwrapping = '0', ApiFramework = 'NULL', "
					+ " BidderAdditionalSettings = '{\"filters\":{},\"companionResourceType\":{\"static\":\"1\"}}', "
					+ " BidderCountryCode = '' " + ", only_deal_impression = 0 "
					+ " where BidderId ='"+data+"'";
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from hudson_bidder_whitelist_blacklist where bidder_id='"+data+"'"; 
			new DBLib().executeUpdateInsertQuery(connection, sql);

			sql = "delete from targeting_package where package_id = '"+data+"'";
			new DBLib().executeUpdateInsertQuery(connection, sql);

			/** This is required when a campaign is mapped to a deal - still putting it here */
			sql = "delete from vanilla_bidder_mapping where bidder_id = '"+data+"'";
			new DBLib().executeUpdateInsertQuery(connection, sql);
			
			/** This is required to remove deal with bidders */
			sql = "delete from deal_bidder where bidder_id = '"+data+"'";
			new DBLib().executeUpdateInsertQuery(connection, sql);

			result = passed_status + " Bidder targeting are cleared from db. ";
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Existing Bidder targeting are cleared from DB for: "+data);
		}
		catch(Exception e)
		{
			result = failed_status + "Bidder targeting are not cleared from db. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : " + e.getMessage(), e);
		}

		return result;
	}


	/** This keyword will bring the desired element in focus.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String getfocusonelement(WebDriver driver, String objectName, String data)
	{
		String result = "";
		By byLocator = null;

		try{
			if(!objectName.isEmpty())
			{
				try{
					/** create dynamic element */
					byLocator = handler.createDynamicByLocator(driver, objectName, data, jsonObjectRepo);
					webelement = driver.findElement(byLocator);
				}catch(CustomException e)
				{
					/** create element normally */
					webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
					if(webelement.equals(null))
					{
						Thread.sleep(5000);
						webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
					}
				}
				if(!webelement.isEnabled())
				{
					By bylocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);

					logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Element is not enabled");
					handler.applyExplicitWait(driver, bylocator, new WebDriverException());
				}

				Thread.sleep(1000);
				String javaScript = "arguments[0].scrollIntoView(false);";
				Thread.sleep(1000);

				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Executing java script: " +javaScript);
				handler.executeJavaScript(driver, javaScript, webelement);

				result = passed_status+ "script executed successfully";
			}
			else
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : No Object received to bring into focus.");
				result = failed_status+ "No Object received to bring into focus. ";
			}
		}
		catch(CustomException e)
		{
			result = failed_status + e.getMessage();
		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't bring the element in focus. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred: "+e+" while moving foucs on element: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}

		return result;
	}


	/** This keyword has no use in this class, it is just a flag which is read by ReadTestCases class even before coming to this class,
	 * this is declared here because, if its not declared here then Test Case will try to find this keyword here and that would throw
	 * an error saying this - on_error_resume_next doesn't exist. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String on_error_resume_next(WebDriver driver, String objectName, String data)
	{
		if(data.equalsIgnoreCase("no") || data.equalsIgnoreCase("false"))
		{
			return passed_status+"After encountering first failure, subsequent steps won't be executed.";
		}
		else
		{
			return passed_status +"After encountering first failure, subsequent steps will still be executed.";
		}
	}

	/** This keyword is used to click expand/collapse icon.
	 *
	 * @param objectName 
	 * @param data 
	 * @return 
	 */	
	public  String clickexpandcollapseicon(WebDriver driver, String objectName, String data)
	{
		String result;

		try{
			if(objectName.isEmpty())
			{
				webelement = getObject.getWebElementFromRepository(data, "label", driver, jsonObjectRepo);
			}
			else
			{
				webelement = getObject.getWebElementFromRepository(objectName, "name", driver, jsonObjectRepo);
			}

			if(!webelement.isDisplayed())
			{
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Eleemtnt is already expanded");
			}
			else
			{
				webelement.click();
				logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Clicking element: " + webelement );
			}

			result = passed_status+ "Clicked element successfully";

		}
		catch(Exception e)
		{
			result = failed_status + "Couldn't click on element";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Exception occurred while clicking on element: " +webelement, e);

			/** Taking screenshot during exception */
			CaptureScreenShotLib.captureScreenShot(driver, locationToSaveSceenShot.concat(GenericMethodsLib.DateTimeStamp("MMdd_hhmmss") + ".png"));
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" : Screenshot is captured at: "+locationToSaveSceenShot);
		}
		return result;
	}

	/**
	 * This keyword is responsible to search, select and add the bluekai/liveRamp category into package and bidder  
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String selectcategory(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{

			String[] categories = data.split("&");
			data = data.toLowerCase();
			for(int i=0;i<categories.length;i++)
			{
				String[] category = categories[i].split("\\|");
				for(int j = 0;j<category.length;j++)
				{
					category[j] = category[j].replace("lr_", "").replace("bk_", "").trim();
					/** Enter the category for search */
					objectName = "CreateANewPackage_DataProvider_Search_TextBox";
					result = typevalue(driver, objectName, category[j]);
					Thread.sleep(1000);

					/** Clicked on Search button */
					objectName = "CreateANewPackage_DataProvider_Search_Button";
					result = result + clicklink(driver, objectName, "");
					Thread.sleep(1000);

					/** Select the searched value */
					objectName = "CreateANewPackage_DataProvider_SearchedDataCheckbox";
					result = result + selectcheckbox(driver, objectName, category[j]);
					Thread.sleep(1000);
				}

				/** Clicked on And button if '&' is available in provided expression */
				if(i+1<categories.length)
				{				
					objectName = "CreateANewPackage_DataProvider_CreateNewInclusion_button";
					result = result + clickbutton(driver, objectName, "");
				}
			}

		}
		catch(Exception e)
		{
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" :Exception occured when select category"+e);
		}
		return result;
	}


	/** this keyword will wait until the supplied element is disappeared.
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String waitforelementtodisappear(WebDriver driver, String objectName, String data)
	{
		String result = "";
		try{
			By bylocator = getObject.getByLocatorFromRepository(objectName, driver, jsonObjectRepo);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(bylocator));

			result = passed_status +" Success. ";
		}
		catch(Exception e)
		{
			result = failed_status +" error occurred while waiting for disappearance of supplied object. ";
			logger.error(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" :Exception occured when select category"+e);
		}
		return result;

	}


	/** This keyword is created specifically for e2e tests where we need the test data in test cases for further usage and the test data is supplied 
	 * after replacing the desired macros. 
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String donothing(WebDriver driver, String objectName, String data)
	{
		return "PASS: NOTHING WAS DONE HERE. ";
	}


	/** apply delay in seconds
	 * 
	 * @param driver
	 * @param objectName
	 * @param data
	 * @return
	 */
	public String applyrandomdelaysec(WebDriver driver, String objectName, String data)
	{
		int delay = 0;
		try
		{
			delay = IntegerLib.GetRandomNumber(6000, 3000);
			Thread.sleep(delay);
			logger.info(TestSuiteClass.UNIQ_EXECUTION_ID.get()+" Applied Random Delay: "+delay);
		}catch(Exception e){}
		return "PASS: Applied Random Delay of: "+delay;
	}

}


