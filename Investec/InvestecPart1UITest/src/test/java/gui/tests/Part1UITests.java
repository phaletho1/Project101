package gui.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.codoid.products.fillo.Recordset;
import gui.Functions.ApplicationSpecificFunction;
import gui.Functions.DataFunctions;
import gui.Functions.UtilityFunctions;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.sql.ResultSet;

public class Part1UITests {

	static UtilityFunctions utils = new UtilityFunctions();
	static DataFunctions data = new DataFunctions();
	static String[] env=new String[10];
	ExtentTest logger;
	static  ExtentReports extent;
	int iRow;
	Sheet sheet;
	Recordset record;
	static String sDefaultPath;
	ResultSet resultset;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		sDefaultPath = System.getProperty("user.dir");
		sDefaultPath = sDefaultPath.replace("batch", "");
		utils.WindowsProcess("chromedriver.exe");
		utils.CloseRunningProcess();
		data.GetEnvironmentVariables(sDefaultPath);
		extent = utils.initializeExtentReports("InvestecProject101", sDefaultPath);
		
	}

	@SuppressWarnings("deprecation")
	@Test
	//test build1
	public void test() {
		try
		{
		logger = extent.createTest("InvestecProject101");
		logger.assignCategory("RegressionTesting");
		logger.assignAuthor("Thomas Phale");
		ApplicationSpecificFunction appspecific = new ApplicationSpecificFunction();
		String strSearchRecord = "Understanding cash investment interest rates";
		String strSearchResultsURL = "https://www.investec.com/en_za/focus/money/understanding-interest-rates.html";

		//
		String strSignUpName = "Thomas";
		String strSignUpSurname = "Phale";
		String strSignUpEmail = "test@investec.co.za";

		//in a project framework working with multiple tests I would actually include this as part of the initialisation module and each script can call that function
		WebDriver driver = utils.initializeWedriver("Chrome", sDefaultPath);
		driver.manage().window().maximize();
		//Navigate to https://www.investec.com/
		utils.navigate(driver, data.getWebURL());
		//Search the browser
		appspecific.SearchTheBrowser(driver, strSearchRecord, strSearchResultsURL,logger,true, sDefaultPath);
		//Sign Up
		appspecific.SignUp(driver, strSignUpName,strSignUpSurname,strSignUpEmail, logger,true,sDefaultPath);
		extent.flush();
		driver.quit();
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
			logger.fail(e);
			System.out.print(e.getMessage());

		}
	}

}
