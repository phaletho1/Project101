package gui.Functions;

import org.openqa.selenium.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class UtilityFunctions {
private String processName;
public static  File outputFile;
public static String pathToSubfolder;


	public void ScrollUpandDown(WebDriver driver, String strUporDown) {

		Actions action = new Actions(driver);

		if (strUporDown.toUpperCase().equalsIgnoreCase("DOWN")) {
			action.sendKeys(Keys.PAGE_DOWN).build().perform();

		} else {
			action.sendKeys(Keys.PAGE_UP).build().perform();
		}
	}

	public static boolean isAlertPresent(WebDriver driver) {
		try {

			driver.switchTo().alert();
			return true;

		} catch (NoAlertPresentException ex) {
			return false;
		}

	}

	public void ScrollIntoWebElementView(WebDriver driver, WebElement element) {
		try {


			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", element);

		} catch (Exception e) {
			System.out.print(e.getMessage());

		}
	}
	public WebDriver initializeWedriver(String sdriverName, String sDefaultPath)
	{
		WebDriver driver = null;
		try
			
		{

			 switch (sdriverName.toUpperCase())
			  {
				  case "CHROME":
					  System.setProperty("webdriver.chrome.driver", sDefaultPath+"\\drivers\\ChromeDriver.exe");
					  driver = new ChromeDriver();
					  break;
				  
				  case "FIREFOX":
					  System.setProperty("webdriver.gecko.driver", sDefaultPath+"\\drivers\\geckodriver.exe");
					  driver = new FirefoxDriver();
				      break;
				      
				  case "IE":
					  System.setProperty("webdriver.ie.driver", sDefaultPath+"\\drivers\\IEDriverServer.exe");
					  driver = new InternetExplorerDriver();
				      break;
			  }
			 
		}catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		return driver;
		
	}
	
	  public void WindowsProcess(String processName)
	    {
	        this.processName = processName;
	    }

	    public void CloseRunningProcess() throws Exception
	    {
	        if (isRunning())
	        {
	            getRuntime().exec("taskkill /F /IM " + processName);
	        }
	    }

	    private boolean isRunning() throws Exception
	    {
	        Process listTasksProcess = getRuntime().exec("tasklist");
	        BufferedReader tasksListReader = new BufferedReader(
	                new InputStreamReader(listTasksProcess.getInputStream()));

	        String tasksLine;

	        while ((tasksLine = tasksListReader.readLine()) != null)
	        {
	            if (tasksLine.contains(processName))
	            {
	                return true;
	            }
	        }

	        return false;
	    }

	    private Runtime getRuntime()
	    {
	        return Runtime.getRuntime();
	    }
	    
	    public void navigate(WebDriver driver, String URL)
	    {
	    		driver.manage().deleteAllCookies();
	    	 	driver.get(URL);
			   // driver.manage().window().maximize();
	    }
	    
		public static String getPathToSubfolder()	{
			
			return pathToSubfolder;
		}
	    

	    
		   public String getCurrentTimeStamp()
		   
		   {
			   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			    Timestamp timestamp = new Timestamp(System.currentTimeMillis());  
				return sdf.format(timestamp);
			    
		   }

	   public ExtentReports initializeExtentReports(String sReportName, String sDefaultPath)
	   {
			ExtentHtmlReporter htmlReporter;
			ExtentReports extent;
			htmlReporter = new ExtentHtmlReporter(sDefaultPath+"\\report\\"+sReportName+".html");
			htmlReporter.setAppendExisting(true);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			return extent;

	   }
	  public void ExtentLogPass(WebDriver driver, String sMessagePass, ExtentTest logger, Boolean Screenshot, String sDefaultPath) throws Exception
	  {
		  if (Screenshot)
		  {
			  String fileName=takeScreenShot(driver,"ExtentLogPass"+sMessagePass , sDefaultPath);
			  logger.pass(sMessagePass, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
		  }
		  else
		  {
			  logger.pass(sMessagePass);
		  }
	  }
	  
	  public void ExtentLogFail(WebDriver driver, String sMessageFail, ExtentTest logger, Boolean Screenshot, String sDefaultPath) throws Exception
	  {
		  
		
		  if (Screenshot)
		  {
			  String fileName=takeScreenShot(driver,"ExtentLogFail", sDefaultPath);
			  logger.fail(sMessageFail, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
		  }
		  else
		  {
			  logger.fail(sMessageFail);
		  }
	  }
	  
	  public String takeScreenShot(WebDriver driver,String FileName,String sDefaultPath ) throws Exception {
			 String fileName="Empty";
			 
	       	  try
	       	 {
	           
	           File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

		      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		      fileName =sDefaultPath+"\\screenshots\\"+FileName+timeStamp+".png";
		      
		      // try {
		           FileUtils.copyFile(scrFile, new File(fileName));
		       } catch (Exception e1) {
		           e1.printStackTrace();
		       }
		       
		       return fileName;
		  }


	    public void ClickObject(WebDriver driver, String property, String path) throws SAXException, IOException, ParserConfigurationException
		{
	    	//get object properties from the xml file repository
			String[] element = xmlParser(path, property);
			switch (element[0].toUpperCase())
			  {
				  case "XPATH":
					  driver.findElement(By.xpath(element[1])).click();	
					  break;
				  
				  case "ID":
					  driver.findElement(By.id(element[1])).click();
					  break;
					  
				  case "NAME":
					  driver.findElement(By.name(element[1])).click();	
					  break;
						
				  case "LINKTEXT":
					  driver.findElement(By.linkText(element[1])).click();
					  break;
					  
				  case "CSSSELECTOR":
					  driver.findElement(By.cssSelector(element[1])).click();
					  break;
				
			  }
			
			
		}

		public void EnterText(WebDriver driver, String property, String sText,String path) throws SAXException, IOException, ParserConfigurationException
		{
			//get object properties from the xml file repository
			String[] element = xmlParser(path, property);
			switch (element[0].toUpperCase())
			  {
				  case "XPATH":
					  driver.findElement(By.xpath(element[1])).sendKeys(sText);
					  break;
				  
				  case "ID":
					  driver.findElement(By.id(element[1])).sendKeys(sText);	
					  break;
					  
				  case "NAME":
					  driver.findElement(By.name(element[1])).sendKeys(sText);
					  break;
						
				  case "LINKTEXT":
					  driver.findElement(By.linkText(element[1])).sendKeys(sText);	
					  break;
					  
				  case "CSSSELECTOR":
					  driver.findElement(By.cssSelector(element[1])).sendKeys(sText);	
					  break;
			  }
						
							
		}
		

		public boolean checkIfObjectExists(WebDriver driver, String property, String path)
		{
			boolean exists = false;
			try
			{
				//get object properties from the xml file repository
				String[] element = xmlParser(path, property);
				switch (element[0].toUpperCase())
				  {
					  case "XPATH":
						  if((driver.findElement(By.xpath(element[1]))!= null)||(driver.findElements(By.xpath(element[1])).isEmpty())){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					  
					  case "ID":
						  if((driver.findElement(By.id(element[1]))!= null)||(driver.findElements(By.id(element[1])).isEmpty())){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					      
					  case "NAME":
						  if((driver.findElement(By.name(element[1]))!= null)||(driver.findElements(By.name(element[1])).isEmpty())){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					  case "LINKTEXT":
						  if((driver.findElement(By.linkText(element[1]))!= null)||(driver.findElements(By.linkText(element[1])).isEmpty())){
								exists=true;
								}else{
								exists=false;
								}
						  break;
				  }
			 
			}
			catch(Exception e)
				{
					System.out.println(e.getMessage());
					exists=false;
				}
			return exists;

							
		}
		
		


		/*****************************************************************************
		Function Name: 	checkIfObjectIsDisplayed
		Description:	Checks if an object is displayed using either an xpath, ID or a Name
		Date Created:	13/09/2017
		******************************************************************************/
		public boolean checkIfObjectIsDisplayed(WebDriver driver, String property, String path)
		{
			boolean exists = false;
			try
			{
				//get object properties from the xml file repository
				String[] element = xmlParser(path, property);
				switch (element[0].toUpperCase())
				  {
					  case "XPATH":
						  if(driver.findElement(By.xpath(element[1])).isDisplayed() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					  
					  case "ID":
						  if(driver.findElement(By.id(element[1])).isDisplayed() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					      
					  case "NAME":
						  if(driver.findElement(By.name(element[1])).isDisplayed() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					  case "LINKTEXT":
						  if(driver.findElement(By.linkText(element[1])).isDisplayed() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
				  }
			}
			catch(Exception e)
				{
					System.out.println(e.getMessage());
					exists=false;
				}
			return exists;
							
		}
		
	
		
		
		
/*		*//*****************************************************************************
		Function Name: 	checkIfObjectEnabled
		Description:	Checks if an object is enabled using either an xpath, ID or a Name
		Date Created:	13/09/2017
		 * @param sDefaultPath 
		******************************************************************************//*
		public boolean checkIfObjectEnabled(WebDriver driver, String property, String path)
		{
			
			boolean exists = false;
			try
			{
				String[] element = xmlParser(path, property);
				switch (element[0].toUpperCase())
				  {
					  case "XPATH":
						  if(driver.findElement(By.xpath(element[1])).isEnabled() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					  
					  case "ID":
						  if(driver.findElement(By.id(element[1])).isEnabled() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
					      
					  case "NAME":
						  if(driver.findElement(By.name(element[1])).isEnabled() == true){
								exists=true;
								}else{
								exists=false;
								}
						  break;
				  }
						
			}
			
			catch(Exception e)
				{
					System.out.println(e.getMessage());
					exists=false;
				}
			return exists;

							
		}*/
		
/*
				
			//xmlParser(String xmlPath, String tagName, String fieldName);
		
		*//************************************************************end Selenium***************************************************************************************//*
		
		*//*****************************************************robot******************************************************************************************************//*
		
		//Press Shift and Tab
		public void PressEnter(int iteration) throws InterruptedException, AWTException
		{
			int i=1;
			while(i<=iteration)
			{
				Thread.sleep(1000);
				Robot robot = new Robot();		
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				i++;
			}
		}
		
		//Press Down Key on a page
		public void PressDownKey() throws InterruptedException, AWTException
		{
			Thread.sleep(5000);
			Robot robot = new Robot();		
			robot.keyPress(KeyEvent.VK_PAGE_DOWN);
			robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		}
		
		//Press Down Key on a page
		public void PressUpKey() throws InterruptedException, AWTException
		{
			Thread.sleep(5000);
			Robot robot = new Robot();		
			robot.keyPress(KeyEvent.VK_PAGE_UP);
			robot.keyRelease(KeyEvent.VK_PAGE_UP);
		}
		
		
		//Press Down Key on a page
		public void RefreshPage() throws InterruptedException, AWTException
		{
			Thread.sleep(5000);
			Robot robot = new Robot();		
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
		}
		
		//Press Shift and Tab
		public void PressShiftTab(int iteration) throws InterruptedException, AWTException
		{
			int i=1;
			while(i<=iteration)
			{
				Thread.sleep(1000);
				Robot robot = new Robot();		
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_TAB);
				i++;
			}
		}
		
		//Press Shift and Tab
		public void PressLeftArrow(int iteration) throws InterruptedException, AWTException
		{
			int i=1;
			while(i<=iteration)
			{
				Thread.sleep(1000);
				Robot robot = new Robot();		
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
				i++;
			}
		}
		
		 public void pressTAB()throws AWTException, InterruptedException
		 {
				
			 	Robot r = new Robot();
				r.keyPress(KeyEvent.VK_TAB);
				r.keyRelease(KeyEvent.VK_TAB);
				Thread.sleep(1000);
		 }
		   
		 public void pressF2()throws AWTException, InterruptedException
		 {
				
			 	Robot r = new Robot();
				r.keyPress(KeyEvent.VK_F2);
				r.keyRelease(KeyEvent.VK_F2);
				Thread.sleep(1000);
		 }
		 
		 public void pressA()throws AWTException, InterruptedException
		 {
				
			 	Robot r = new Robot();
				r.keyPress(KeyEvent.VK_A);
				r.keyRelease(KeyEvent.VK_A);
				Thread.sleep(1000);
		 }
			
		  public void pressTAB(int iterations)throws AWTException, InterruptedException
		    {
		
		     int i=1;
		     while(i<=iterations)
		     {
		      Robot r = new Robot();
		     r.keyPress(KeyEvent.VK_TAB);
		     r.keyRelease(KeyEvent.VK_TAB);
		     Thread.sleep(1000);
		     i++;
		     }
		    }
		  
		  //Press Down Key on a page
		    public void pressCtrlShiftA() throws InterruptedException, AWTException
		    {
		      Thread.sleep(5000);
		      Robot robot = new Robot();  
		      robot.keyPress(KeyEvent.VK_CONTROL);
		      robot.keyPress(KeyEvent.VK_SHIFT);
		      robot.keyPress(KeyEvent.VK_A);
		      robot.keyRelease(KeyEvent.VK_CONTROL);
		      robot.keyRelease(KeyEvent.VK_SHIFT);
		      robot.keyRelease(KeyEvent.VK_A);
		     
		    }
		    
		    
		    public void pressCtrlS() throws InterruptedException, AWTException
		    {
		      Thread.sleep(5000);
		      Robot robot = new Robot();  
		      robot.keyPress(KeyEvent.VK_CONTROL);
		      robot.keyPress(KeyEvent.VK_S);
		      robot.keyRelease(KeyEvent.VK_CONTROL);
		      robot.keyRelease(KeyEvent.VK_S);
		     
		    }
		    
		    *//*****************************************************************end robot*************************************************************************************//*
		    */
		    public String[] xmlParser(String xmlPath, String tagName) throws SAXException, IOException, ParserConfigurationException {
				// File fXmlFile = new File();
				//InputStream requestContent = new InputStr();
				//String sLine = outputData;
		        //requestContent.append(sLine);
				//String element = null;
				String[] element2 = new String[2];
				File fXmlFile = new File(xmlPath);
				DocumentBuilderFactory dbFactory =
				DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder =
				dbFactory.newDocumentBuilder();
				
				Document doc = dBuilder.parse(fXmlFile);
				
				/*InputSource is = new InputSource(new
				StringReader(outputData));
				org.w3c.dom.Document doc = dBuilder.parse(is);*/

				
				doc.getDocumentElement().normalize();

			//	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName(tagName);


				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					//System.out.println("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						String element = eElement.getElementsByTagName("identifier").item(0).getTextContent();
						String element1 = eElement.getElementsByTagName("Element").item(0).getTextContent();
						element2[0] = element;
						element2[1] = element1;

				
					} // end if
				} // end for loop
				
				return element2;
			} // end function
		    
/*		    public void VerifyElement(WebDriver driver, String property, String path) throws Exception
		    {
		    	String[] element = xmlParser(path, property);
		    	
		    	WebDriverWait wait = new WebDriverWait(driver,160);
      	     // WebElement WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));
		    	
				switch (element[0].toUpperCase())
				  {
					  case "XPATH":
						  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[1])));	
						  break;
					  
					  case "ID":						 
	            	      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));
						  break;
						  
					  case "NAME":
						  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[1])));	
						  break;
							
					  case "LINKTEXT":						  
						  wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[1])));
						  break;
						  
					  case "CSSSELECTOR":
						  wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[1])));
						  break;
					
				  }
		    }*/
/*
		    public void VerifyElementNotDisplayed(WebDriver driver, String property, String path) throws Exception
		    {
		    	String[] element = xmlParser(path, property);
		    	
		    	WebDriverWait wait = new WebDriverWait(driver,160);
      	     // WebElement WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));
		    	
				switch (element[0].toUpperCase())
				  {
					  case "XPATH":
						  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element[1])));	
						  break;
					  
					  case "ID":						 
	            	      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(element[1])));
						  break;
						  
					  case "NAME":
						  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(element[1])));	
						  break;
							
					  case "LINKTEXT":						  
						  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(element[1])));
						  break;
						  
					  case "CSSSELECTOR":
						  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(element[1])));
						  break;
					
				  }
		    }*/
		    
/*		    public void VerifyWebElementDisplayed(WebDriver driver, WebElement Object) throws Exception
		    {	    	
		    	WebDriverWait wait = new WebDriverWait(driver,500);      	     
				wait.until(ExpectedConditions.visibilityOf(Object));	
	  
		    }
		    
		    public void VerifyWebElementEnabled(WebDriver driver, WebElement Object,String text) throws Exception
		    {	    	
		    	WebDriverWait wait = new WebDriverWait(driver,500);      	     
				wait.until(ExpectedConditions.textToBePresentInElementValue(Object, text));	
	  
		    }
		    
		    public void CompareText(String elementText, String expectedText) throws Exception
		    {	    	
		    	try 
		    	{		    		
		    		Assert.assertEquals(expectedText, elementText);
		    	}
		    	catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
	  
		    }
		    
		    public void VerifyWebElementNotDisplayed(WebDriver driver, WebElement Object) throws Exception
		    {	    	
		    	WebDriverWait wait = new WebDriverWait(driver,160);      	     
				wait.until(ExpectedConditions.invisibilityOf(Object));	
	  
		    }
		    
		    public void ClickWebElement(WebDriver driver, WebElement Object) throws SAXException, IOException, ParserConfigurationException
			{
		    	Object.click();				
			}
		  //click on First Level Bar
		    public List<WebElement> FirstLevelBarOptions(WebDriver driver) {

		     Select FirstLevelSelectClass = new Select(driver.findElement(By.id("j_s_sctrl_tabScreen")));
		     List<WebElement> selectOptions = FirstLevelSelectClass.getOptions();
		     return selectOptions;		   
		     
		    }
		    
		    
		    public void SelectFirstLevelBarOption(WebDriver driver,String optionname) {

			     Select FirstLevelSelectClass = new Select(driver.findElement(By.id("j_s_sctrl_tabScreen")));
			     FirstLevelSelectClass.selectByVisibleText(optionname);			   		  	     
			    }
		    
		  //click on Third Level Bar
		    public List<WebElement> ThirdLevelBarOptions(WebDriver driver) {

		     Select ThirdLevelSelectClass = new Select(driver.findElement(By.id("j_s_vctrl_div_tabScreen")));
		     List<WebElement> selectOptions = ThirdLevelSelectClass.getOptions();
		     return selectOptions;		   
		     
		    }
		    public void SelectFourthLevelBarOption(WebDriver driver,String optionname) {

			     Select FourthLevelSelectClass = new Select(driver.findElement(By.id("j_s_vctrl_div_tabView")));
			     FourthLevelSelectClass.selectByVisibleText(optionname);			   		  	     
			    }
		    
		  //click on Third Level Bar
		    public List<WebElement> FourthLevelBarOptions(WebDriver driver) {

		     Select FourthLevelSelectClass = new Select(driver.findElement(By.id("j_s_vctrl_div_tabView")));
		     List<WebElement> selectOptions = FourthLevelSelectClass.getOptions();
		     return selectOptions;		   
		     
		    }
		    public void SelectThirdLevelBarOption(WebDriver driver,String optionname) {

			     Select ThirdLevelSelectClass = new Select(driver.findElement(By.id("j_s_vctrl_div_tabScreen")));
			     ThirdLevelSelectClass.selectByVisibleText(optionname);			   		  	     
			    }
		    
		    public void generateRandomString(int length) {
		    	boolean useLetters = true;
		    	boolean useNumbers = false;
		    	String generatedString = RandomStringUtils.random(length,useLetters,useNumbers);
		    	System.out.println(generatedString);
		    }
		    public int GetRowCount(WebDriver driver,String summary)
		    {
			    int rowCount = driver.findElements(By.xpath("//table[@summary="+"'"+summary+"'"+"]/tbody/tr")).size();
			       return rowCount;
		
            	   	 
		    }*/

		    public void ScreenshotParentFolder(String packageName, String sDefaultPath)
		    {
		        Path pathParentDirectory = Paths.get(sDefaultPath+"\\screenshots"+"/"+"_"+packageName);
		        
		        outputFile = new File(sDefaultPath+"\\screenshots"+"/"+"_"+packageName);
		        if (Files.notExists(pathParentDirectory,LinkOption.NOFOLLOW_LINKS)) 
		        {
		        	outputFile.mkdir();
		        }
		        
		    }

		    public String subfolderCreation(String className, String timestamp){
		    	
		        String st = outputFile.getAbsolutePath();
		        outputFile = new File(st+"/"+className+timestamp);
		        outputFile.mkdirs();
		        System.out.println(outputFile);
		        pathToSubfolder = outputFile.getPath();
		        return pathToSubfolder;
		    }

		    public String GetCurrentSystemDate() {
		    	
		    	// Create object of SimpleDateFormat class and decide the format
		     	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		     	
		     	//get current date time with Date()
		     	Date date = new Date();
		     	
		       //format the date
		     	String SystemCurrentDate = dateFormat.format(date);
		     	
		       // Print the Date
		     	System.out.println(SystemCurrentDate);
		     	
		     	//Return System Current Date
		     	return SystemCurrentDate;
		    }
		  
}
