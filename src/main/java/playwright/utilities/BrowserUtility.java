package playwright.utilities;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

public class BrowserUtility {
	private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    // Initialize the Playwright, Browser, and Context
    public static void initializeBrowser(boolean headless) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
    }

    // Get the current Page instance
    public static Page getPage() {
        return page;
    }

    // Close the browser and Playwright instances
    public static void closeBrowser() {
        if (context != null) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}