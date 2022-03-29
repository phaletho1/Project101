package gui.Functions;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Sheet;
import com.aventstack.extentreports.ExtentTest;
import com.codoid.products.fillo.Recordset;



public class ApplicationSpecificFunction {
	UtilityFunctions utils = new UtilityFunctions(); 
	static DataFunctions data = new DataFunctions();

	/*****************************************************************************
	 Function Name: 	NavigateToSearchResults
	 Description:	NavigateToSearchResults
	 Author: Thomas Phale
	 Date Created:	29/03/2022
	 * @param sheet
	 * @param sheet
	 * @param record
	 * @param Type
	 * @param b
	 * @param sDefaultPath
	 * @throws Exception
	 ******************************************************************************/
	public void NavigateToSearchResults(WebDriver driver,String strReturnedSearchResults, String strSearchResultsURL,  ExtentTest logger, boolean b, String sDefaultPath)
	{
		try
		{
			if (driver.getPageSource().contains(strSearchResultsURL))
			{
				Thread.sleep(2000);
				utils.navigate(driver, strSearchResultsURL);

				WebElement elmSearchedContent = driver.findElement(By.xpath("//*[@id=\"content\"]/div[6]/div/div[4]/div/div/div/div[1]"));
				String elmReturnedSearchedContent = elmSearchedContent.getAttribute("value");

				if (driver.getPageSource().contains(strReturnedSearchResults)){
					utils.ExtentLogPass(driver, "Navigation to understanding cash investment interest rates is Successful", logger, true, sDefaultPath);
				}
				else {
					Thread.sleep(3000);
					utils.ExtentLogFail(driver, "Navigation to search results was not successful",logger,true, sDefaultPath);
					Assert.fail();
				}
			}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			driver.quit();

		}

	}

	/*****************************************************************************
	 Function Name: 	SignUp
	 Description:	SignUp
	 Author: Thomas Phale
	 Date Created:	29/03/2022
	 * @param sheet
	 * @param sheet
	 * @param record
	 * @param Type
	 * @param b
	 * @param sDefaultPath
	 * @throws Exception
	 ******************************************************************************/
	public void SignUp(WebDriver driver, String strName, String strSurname, String strEmail, ExtentTest logger, boolean b, String sDefaultPath) throws Exception
	{
		try {
			if (utils.checkIfObjectIsDisplayed(driver, "SignUpButton", sDefaultPath+"\\Repository\\SignUp.xml")) {


				if (utils.isAlertPresent(driver)) {

					Alert alert = driver.switchTo().alert();
					System.out.println(alert.getText());
					alert.accept();
				}
				/*utils.ClickObject(driver, "SignUpButton", sDefaultPath + "\\Repository\\SignUp.xml");
				TimeUnit.SECONDS.sleep(3);*/
				WebElement elmSignUp = driver.findElement(By.xpath("//button[contains(text(),'Sign up')]"));
				//Scroll down before clickin on the sign up button
				utils.ScrollUpandDown(driver, "DOWN");
				JavascriptExecutor executor = (JavascriptExecutor)driver;
				executor.executeScript("arguments[0].click();", elmSignUp);

				//Capture the sign up details
				utils.EnterText(driver, "SignUpName", strName, sDefaultPath + "\\Repository\\SignUp.xml");
				edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(5);
				utils.EnterText(driver, "SignUpsurnameName", strSurname, sDefaultPath + "\\Repository\\SignUp.xml");
				edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(5);
				utils.EnterText(driver, "SignUpemail", strEmail, sDefaultPath + "\\Repository\\SignUp.xml");
				edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(5);

				//Click submit button
				WebElement inputCheckBox = driver.findElement(By.xpath("//*[@id=\"content\"]/div[7]/div[2]/div/div/div/div/div[2]/div/div/div/div/div[1]/div/div/div/div/div/form/div[1]/section/fieldset[2]/div[4]/checkbox-input/div/div/div[1]/button"));
				//Scroll down before clickin on the sign up button
				edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(5);
				utils.ScrollIntoWebElementView(driver, inputCheckBox);
				executor.executeScript("arguments[0].click();", inputCheckBox);

				//utils.ClickObject(driver, "InputCheckBox", sDefaultPath + "\\Repository\\SignUp.xml");

				//Capture screenshot
				utils.ExtentLogPass(driver, "Sign up detailscaptured successfuly", logger, true, sDefaultPath);

				//Click submit button
				WebElement elmSubmit = driver.findElement(By.xpath("//button[contains(text(),'Submit')]"));
				//Scroll down before clickin on the sign up button
				edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(5);
				utils.ScrollIntoWebElementView(driver, elmSubmit);
				executor.executeScript("arguments[0].click();", elmSubmit);


				if (utils.checkIfObjectExists(driver,"ThankYouDataComp", sDefaultPath + "\\Repository\\SignUp.xml"))
				{
					//Check if the page source containts the html url
					if (driver.getPageSource().contains("We look forward to sharing out of the ordinary insights with you")) {
						//Navigate to search results
						edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(15);
						//Capture screenshot
						utils.ExtentLogPass(driver, "Sign up is successfuly", logger, true, sDefaultPath);
					}
					else {
						Assert.fail("Sign up failed");
						driver.quit();
					}
				}
				else {
					Assert.fail("Success sign up page is not loaded");
					driver.quit();
				}

			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			driver.close();
		}
	}


	/*****************************************************************************
	 Function Name: 	SearchTheBrowser
	 Description:	SearchTheBrowserAuthor: Thomas Phale
	 Date Created:	29/03/2022
	 * @param sheet
	 * @param sheet
	 * @param record
	 * @param Type
	 * @param b
	 * @param sDefaultPath
	 * @throws Exception
	 ******************************************************************************/
	public void SearchTheBrowser(WebDriver driver, String strSearchRecord, String strSearchResultsURL, ExtentTest logger, boolean b, String sDefaultPath) throws Exception
	{
		try {

			//check if the home page is loaded sucessfully

			if (utils.checkIfObjectIsDisplayed(driver, "HomePageSearch", sDefaultPath+"\\Repository\\VerifyObjects.xml")) {

				utils.ClickObject(driver, "HomePageSearch", sDefaultPath + "\\Repository\\VerifyObjects.xml");
				TimeUnit.SECONDS.sleep(3);
				//Capture the text to be searched
				utils.EnterText(driver, "txtSearchBar", strSearchRecord, sDefaultPath + "\\Repository\\SearchBrowser.xml");
				utils.ClickObject(driver, "txtSearchBarButton", sDefaultPath + "\\Repository\\SearchBrowser.xml");

				//Check if the page source containts the html url
				if (driver.getPageSource().contains(strSearchResultsURL)) {
					//Navigate to search results
					NavigateToSearchResults(driver, strSearchRecord, strSearchResultsURL, logger, true, sDefaultPath);
					edu.emory.mathcs.backport.java.util.concurrent.TimeUnit.SECONDS.sleep(3);
				}
				else {
					Assert.fail("Search results not found");
					driver.quit();
				}
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			driver.close();
		}
	}
}



