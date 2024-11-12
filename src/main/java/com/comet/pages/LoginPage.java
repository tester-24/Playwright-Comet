package com.comet.pages;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

public class LoginPage {

	private Page page;

    // Locators
    private String loginButtonText = "LOGIN WITH SPACE";
    private String userIdPlaceholder = "Enter User ID";
    private String passwordPlaceholder = "Enter Password";
    private String loginNowButtonText = "Login Now";
   // private String otpPlaceholder = "[placeholder='-']";
    
    private String continueButtonText = "Continue";
    private String toastSelector = "#toast-container.toast-top-right.toast-container"; // Class-level variable
    private ExtentTest test;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    // Actions
    public void navigateToLogin() {
        page.navigate("https://comet.jainam.in/#/startup");
    }

    public void login(String userId, String password, String otp) throws Exception {
        page.getByText(loginButtonText).click();
        page.getByPlaceholder(userIdPlaceholder).fill(userId);
        page.getByPlaceholder(passwordPlaceholder).fill(password);
        page.getByText(loginNowButtonText).click();
       // waitForToastAndHandleFailure();
        ////////////////////////////////////
        boolean loginFailed = waitForToastAndHandleFailure();  // Check for failure first
        if (loginFailed) {
            return; // If login failed, return and exit the method
        }
        //////////////////////////////////////
        waitForOtpPage();
        String[] otpArray = otp.split(""); // Splits each character into a separate array element
        fillVerificationCode(otpArray);
        
    }
/*       
private void waitForToastAndHandleFailure() throws Exception {
    try {
        // Wait for the toast message container to appear
        page.locator(toastSelector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        
        // If the toast message is visible, capture screenshot and log failure
        if (page.locator(toastSelector).isVisible()) {
            captureScreenshot(page);
            test.log(Status.FAIL, "Login failed, notification displayed.");
            throw new Exception("Login failed, notification displayed.");
        }
    } catch (TimeoutException e) {
        // If no toast appears, continue (successful login)
    }
} */
    
    private boolean waitForToastAndHandleFailure() throws Exception {
        try {
            // Wait for the toast message container to appear
            page.locator(toastSelector).waitFor(new Locator.WaitForOptions().setTimeout(5000));

            // If the toast message is visible, capture screenshot and log failure
            if (page.locator(toastSelector).isVisible()) {
                captureScreenshot(page);
                test.log(Status.FAIL, "Login failed, notification displayed.");
                return true; // Return true to indicate login failure
            }
        } catch (TimeoutError e) {
            // If no toast appears, login is successful, so return false
            return false;
        }
        return false;  // Default case, if toast not found (shouldn't reach here normally)
    }

// Wait for OTP page to load
private void waitForOtpPage() {
    try {
        page.locator("[placeholder='-']").waitFor(new Locator.WaitForOptions().setTimeout(5000));
    } catch (TimeoutError e) {
        throw new RuntimeException("OTP page did not load within timeout.");
    }
}
// Fill the OTP input fields
public void fillVerificationCode(String[] code) {
    for (int i = 0; i < code.length; i++) {
        page.getByPlaceholder("-").nth(i).fill(code[i]);
    }
    page.getByText("Continue").click();
}


// Capture screenshot in case of failure
private void captureScreenshot(Page page) throws IOException {
    String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
    String filePath = "./reports/screenshots/" + fileName;
    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
    test.addScreenCaptureFromPath(filePath);
}
}
   /*    
private void waitForToastAndHandleFailure() throws Exception {
    try {
        // Wait for the toast message container to appear
        page.locator(toastSelector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        
        // If the toast message is visible, capture screenshot and log failure
        if (page.locator(toastSelector).isVisible()) {
            captureScreenshot(page);
            test.log(Status.FAIL, "Login failed, notification displayed.");
            throw new Exception("Login failed, notification displayed.");
        }
    } catch (TimeoutException e) {
        // If no toast appears, continue (successful login)
    }
}

// Wait for OTP page to load
private void waitForOtpPage() {
    try {
        page.locator(otpPlaceholder).waitFor(new Locator.WaitForOptions().setTimeout(5000));  // Adjust locator if necessary
    } catch (TimeoutError e) {
        throw new RuntimeException("OTP page did not load within timeout.");
    }
}
// Fill the OTP input fields
private void fillOtp(String otp) {
    for (int i = 0; i < otp.length(); i++) {
        page.getByPlaceholder("-").nth(i).fill(String.valueOf(otp.charAt(i)));
    }
    page.getByText(continueButtonText).click();
}

// Capture screenshot in case of failure
private void captureScreenshot(Page page) throws IOException {
    String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
    String filePath = "./reports/screenshots/" + fileName;
    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
    test.addScreenCaptureFromPath(filePath);
}*/
  //  Wait for OTP page to load
    
    