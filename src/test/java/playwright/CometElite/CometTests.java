package playwright.CometElite;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.comet.pages.LedgerReportPage;
import com.comet.pages.LoginPage;
import com.microsoft.playwright.Page;
import playwright.utilities.BrowserUtility;
import playwright.utilities.ReportUtility;

public class CometTests {

    private Page page;
    private LoginPage loginPage;
    private LedgerReportPage ledgerReportPage;

    @BeforeSuite
    public void startReport() {
        ReportUtility.initializeReport();
    }

    @BeforeClass
    public void setup() {
    	BrowserUtility.initializeBrowser(false);
    	page = BrowserUtility.getPage();
    	loginPage = new LoginPage(page);
        ledgerReportPage = new LedgerReportPage(page);
       // ReportUtility.log(Status.PASS, "User logged in successfully.");
    }
    
  @Test(priority = 1)
    public void testLogin() throws Exception {
        // This test will only validate the login process
	  ReportUtility.createTest("Verify Login Test");
	  loginPage.navigateToLogin();
      try {
          loginPage.login("1126", "Jainam@123", "1234");
          ReportUtility.log(Status.PASS, "User logged in successfully.");
      } catch (RuntimeException e) {
          ReportUtility.log(Status.FAIL, "Login Test failed: " + e.getMessage());
          throw e;
      }
    }

    @Test(priority = 2, dependsOnMethods = "testLogin")
    public void testNavigateToLedgerReport() {
    	ReportUtility.createTest("Navigate to Ledger Report Test");
        ledgerReportPage.navigateToLedgerReport();
        ledgerReportPage.searchLedger("j33");
        ledgerReportPage.printLedgerAmounts();
        ReportUtility.log(Status.PASS, "Navigated to ledger report and retrieved data successfully.");
    }
    
    @AfterClass
    public void tearDown() {
    	BrowserUtility.closeBrowser();
    }
    @AfterSuite
    public void endReport() {
        ReportUtility.flushReport();
    }
}