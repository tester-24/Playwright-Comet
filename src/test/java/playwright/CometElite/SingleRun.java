package playwright.CometElite;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

public class SingleRun {
	
	private ExtentReports extent;
    private ExtentTest extentTest;

    @Test(priority = 1, enabled=true)
    public void testLoginAndNavigate() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // Time stamp
        String reportFilePath = System.getProperty("user.dir") + "./ExtentReports/ExtentReport_" + timeStamp + ".html";        
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportFilePath);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extentTest = extent.createTest("testLoginAndNavigate");

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));

            // Start tracing for debugging purposes
            context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
            Page page = context.newPage();

            // Perform the entire test in sequence
            navigateToUrl(page);
            if (!login(page)) {
                // If login fails, capture the screenshot and terminate
                captureScreenshot(page);
                extentTest.fail("Login failed.");
                return;
            }

            // If login successful, enter OTP
            enterOTP(page);

            // Navigate to ledger and extract data
            navigateToLedger(page);

            // Stop tracing
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));

            // Close the context and browser
            context.close();
            browser.close();
            extentTest.pass("Test completed successfully.");

        } catch (Exception e) {
            extentTest.fail("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Flush the report
            extent.flush();
            sendEmailWithAttachment(reportFilePath);
        }
    }

    // Navigate to URL
    private void navigateToUrl(Page page) {
    	ExtentTest node = extentTest.createNode("Navigate to URL");
        node.info("Navigating to the URL...");
        page.navigate("https://comet.jainam.in/#/startup");
        node.pass("Navigated successfully to the URL.");
    }

    // Perform Login and return true if successful, false if failed
    private boolean login(Page page) {
    	ExtentTest node = extentTest.createNode("Login");
        node.info("Attempting to log in with credentials.");
        page.getByText("LOGIN WITH SPACE").click();
        page.getByPlaceholder("Enter User ID").fill("1126");
        page.getByPlaceholder("Enter Password").fill("Jainam@123");
        page.getByText("Login Now").click();

        // Wait for the toast message that indicates login failure
        Locator toastMessage = page.locator("#toast-container.toast-top-right.toast-container");
        try {
            toastMessage.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            if (toastMessage.isVisible()) {
                // Login failed
            	node.fail("Login failed: " + toastMessage.textContent());
                return false;
            }
        } catch (TimeoutError e) {
            // Login successful
        	node.pass("Login successful.");
            return true;
        }
        return true; // In case no error appeared
    }

    // Enter OTP and proceed
    private void enterOTP(Page page) {
    	ExtentTest node = extentTest.createNode("Enter OTP");
        node.info("Entering OTP...");
        page.waitForSelector("input[placeholder='-']", new Page.WaitForSelectorOptions().setTimeout(60000));
        page.getByPlaceholder("-").first().fill("1");
        page.getByPlaceholder("-").nth(1).fill("2");
        page.getByPlaceholder("-").nth(2).fill("3");
        page.getByPlaceholder("-").nth(3).fill("4");
        page.getByText("Continue").click();
        
        node.pass("OTP entered and submitted successfully.");
    }

    // Navigate to ledger and extract data
    private void navigateToLedger(Page page) {
    	ExtentTest node = extentTest.createNode("Navigate to Ledger");
        node.info("Navigating to the ledger and extracting data...");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reports")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Ledger")).click();
        page.getByRole(AriaRole.TEXTBOX).click();
        page.getByRole(AriaRole.TEXTBOX).fill("j33");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View")).click();

        // Extracting text from report sections
        String deposit = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Deposit")).textContent();
        extentTest.info("Deposit Amount: " + deposit);
        node.info("Deposit Amount: " + deposit);
        
        String withdraw = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Withdraw")).textContent();
        extentTest.info("Withdraw Amount: " + withdraw);
        node.info("Withdraw Amount: " + withdraw);
        
        String bills = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Bills")).textContent();
        extentTest.info("Bills Amount: " + bills);
        node.info("Bills Amount: " + bills);
        
        String others = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Others")).textContent();
        extentTest.info("Others Amount: " + others);
        node.info("Others Amount: " + others);
        
        String virtualBlock = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Virtual Block -")).textContent();
        extentTest.info("Virtual Block Amount: " + virtualBlock);
        node.info("Virtual Block Amount: " + virtualBlock);
        
        node.pass("Ledger data extracted successfully.");
    }

    // Utility method to capture a screenshot
    private void captureScreenshot(Page page) {
        String screenshotName = "screenshot_" + System.currentTimeMillis();
        String screenshotPath = Paths.get(System.getProperty("user.dir") + "/ExtentReports/screenshots/" + screenshotName + ".png").toString();

        // Capture and save the screenshot to the specified path
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            extentTest.addScreenCaptureFromPath(screenshotPath); 
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }    
    private void sendEmailWithAttachment(String reportFilePath) {

        // Recipient's email ID
        String[] recipients = {
                "ashish.test.p@gmail.com",
                "tester3.elitetechno@gmail.com"
        };

        String from = "tester4.elitetechno@gmail.com";
        String host = "smtp.gmail.com";
        String username = "tester4.elitetechno@gmail.com";
        String password = "cmezcxcglnjpetlo";
        
        // SMTP server configuration
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465"); // SSL port
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Get the Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set the sender's email address
            message.setFrom(new InternetAddress(username));

            // Set the recipient's email address
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", recipients)));
            
            // Set the subject of the email
            message.setSubject("Test Report - Extent Report");

            // Create the body of the email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Please find attached the test report.");

            // Create the attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(reportFilePath);

            // Create a multipart message for email content
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully with report attached.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}