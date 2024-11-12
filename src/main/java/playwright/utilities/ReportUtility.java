package playwright.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;

public class ReportUtility {
	private static ExtentReports extent;
    private static ExtentSparkReporter spark;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    

    
    // Initialize the Extent Report
    public static void initializeReport() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // Time stamp
        String reportFilePath = System.getProperty("user.dir") + "./ExtentReports/ExtentReport_" + timeStamp + ".html";
        
        // Create ExtentReports and attach reporter
        extent = new ExtentReports();
        spark = new ExtentSparkReporter(reportFilePath);
        
        // Configuration for the report
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Test Execution Report");
        spark.config().setTheme(Theme.STANDARD);
        
        extent.attachReporter(spark);
    }

    // Create a test node in the report
    public static void createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }

    // Log information to the current test
    public static void log(Status status, String details) {
        extentTest.get().log(status, details);
    }
    
    public static String captureScreenshot(Page page) {
        // Generate the screenshot name with timestamp
    	String screenshotsDir = System.getProperty("user.dir") + "/ExtentReports/screenshots";
        
        // Ensure the screenshots directory exists
        File directory = new File(screenshotsDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directories if they do not exist
        }
        String screenshotName = "screenshot_" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".png";
        String screenshotPath = screenshotsDir + "/" + screenshotName;

        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(screenshotPath)));
        } catch (Exception e) {
            System.out.println("Error while capturing screenshot: " + e.getMessage());
        }

        return screenshotPath;
    }
    
    public static void logScreenshot(String message, String screenshotPath) {
        try {
            extentTest.get().log(Status.INFO, message,
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (Exception e) {
            extentTest.get().log(Status.WARNING, "Failed to attach screenshot to report: " + e.getMessage());
        }
    }

    public static void captureToastMessage(Page page, String toastSelector) {
        try {
            // Check if the toast message is visible
            if (page.locator(toastSelector).isVisible()) {
                // Capture a screenshot if the toast message is displayed
                String screenshotPath = captureScreenshot(page);
                logScreenshot("Toast Message displayed", screenshotPath);
            }
        } catch (Exception e) {
            System.out.println("Toast message not found: " + e.getMessage());
        }
    }

    // Finalize the report
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
} 
