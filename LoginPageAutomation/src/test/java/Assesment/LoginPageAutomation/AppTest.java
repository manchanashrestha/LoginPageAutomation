package Assesment.LoginPageAutomation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(Assesment.LoginPageAutomation.EventListner.class)
public class AppTest {
	WebDriver driver;

	@Parameters({ "browser" })
	@BeforeTest
	public void setUp(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			// Enable headless mode
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");

			// Initialize WebDriver with headless mode
			driver = new ChromeDriver(options);
		} else if (browser.equalsIgnoreCase("firefox")) {
			// Enable headless mode
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");

			// Initialize WebDriver with headless mode
			driver = new FirefoxDriver(options);
		} else {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless");
			driver = new EdgeDriver(options);
		}
		driver.get("https://rahulshettyacademy.com/loginpagePractise/");
		driver.manage().window().maximize();
	}

	@Parameters({ "usernameData", "passwordData", "userType" })
	@Test
	public void adminLoginTest(String username, String password, String userRole) throws InterruptedException {
		SoftAssert ObjSoftAssert = new SoftAssert();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.findElement(By.id("username")).sendKeys(username);
		Reporter.log("Entered username");
		driver.findElement(By.id("password")).sendKeys(password);
		Reporter.log("Entered password");

		List<WebElement> userType = driver.findElements(By.className("customradio"));
		Actions action = new Actions(driver);
		if (userRole.equals("admin")) {
			action.moveToElement(userType.get(0)).click().perform();
		} else if (userRole.equals("user")) {
			action.moveToElement(userType.get(1)).click().perform();

			WebElement alertBox = driver.findElement(By.xpath("//*[@id=\"myModal\"]/div/div/div[2]"));
			wait.until(ExpectedConditions.visibilityOf(alertBox));
			Reporter.log("Alert box opened for user role" + alertBox.getText());
			ObjSoftAssert.assertTrue(alertBox.getText().contains("Okay"),
					"Alert text does not contain the expected substring: " + "Cancel or Okay");

			driver.findElement(By.id("okayBtn")).click();
			Reporter.log("Clicked on Okay button");
			wait.until(ExpectedConditions.invisibilityOf(alertBox));
			Reporter.log("Alert box closed");
		}
		Reporter.log("Selected " + userRole + " as a user role");

		// Select teacher from dropdown
		AppTest test = new AppTest();
		test.performDropdown(driver, "teach");
		Reporter.log("Selected teacher from the dropdown");

		// Select terms and condition checkbox
		driver.findElement(By.id("terms")).click();
		Reporter.log("Checked the terms and condition checked box.");

		// Click on submit button
		WebElement signIn = driver.findElement(By.id("signInBtn"));
		signIn.click();
		Reporter.log("Clicked on sign in button");

		if (username.equals("rahulshettyacademy") && password.equals("learning")) {
			// Wait until the URL changes
			String expectedUrl = "https://rahulshettyacademy.com/angularpractice/shop";

			wait.until(ExpectedConditions.urlToBe(expectedUrl));
			Reporter.log("Waiting until the page is redirected to the url: " + expectedUrl);

			ObjSoftAssert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Landing page url mis-matched!");
			WebElement photoCommerceLink = driver.findElement(By.xpath("/html/body/app-root/app-navbar/div/nav/a"));
			Reporter.log("Verified the expected URL and title of the link of the page");

			ObjSoftAssert.assertEquals(photoCommerceLink.getText(), "ProtoCommerce",
					"Landing page title ProtoCommerce mis-matched!");
		} else {
			Thread.sleep(100);
			Reporter.log("Reached at incorect username or password condition.");

			// Find the element corresponding to the alert div
			WebElement alertDiv = driver.findElement(By.className("alert-danger"));

			// Execute JavaScript to display the alert div
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.display='block';", alertDiv);

			ObjSoftAssert.assertEquals(alertDiv.getText(), "Incorrect username/password.",
					"Error message mis-matched!");
			Reporter.log("Alert message is " + alertDiv.getText());

		}
		ObjSoftAssert.assertAll();

	}

	public void performDropdown(WebDriver driver, String value) {
		WebElement designationDropDown = driver.findElement(By.tagName("select"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(designationDropDown));
		Reporter.log("Performing dropdown selection ... ");
		if (value.equals("stud")) {
			Select designation = new Select(designationDropDown);
			designation.selectByValue("stud");
		} else if (value.equals("teach")) {
			Select designation = new Select(designationDropDown);
			designation.selectByValue("teach");
		}
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}
