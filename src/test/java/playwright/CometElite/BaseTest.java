package playwright.CometElite;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;

public class BaseTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		try (Playwright playwright = Playwright.create()) {
		      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
		        .setHeadless(false));
		      BrowserContext context = browser.newContext(new Browser.NewContextOptions()
		                .setViewportSize(1920, 1080));
		      context.tracing().start(new Tracing.StartOptions()
	        		   .setScreenshots(true)
	        		   .setSnapshots(true));
		      Page page = context.newPage();
		      
		      //page.setDefaultTimeout(5000);
		      page.navigate("https://comet.jainam.in/#/startup");
		      page.getByText("LOGIN WITH SPACE").click();
		      page.getByPlaceholder("Enter User ID").click();
		      page.getByPlaceholder("Enter User ID").fill("1126");
		      page.getByPlaceholder("Enter Password").click();
		      page.getByPlaceholder("Enter Password").fill("Jainam@123");
		      page.getByText("Login Now").click();
		      page.getByPlaceholder("-").first().fill("1");
		      page.getByPlaceholder("-").nth(1).fill("2");
		      page.getByPlaceholder("-").nth(2).fill("3");
		      page.getByPlaceholder("-").nth(3).fill("4");
		      page.getByText("Continue").click();
		      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reports")).click();
		      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Ledger")).click();
		      page.getByRole(AriaRole.TEXTBOX).click();
		      page.getByRole(AriaRole.TEXTBOX).fill("j33");
		      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View")).click();
		      
		      String Deposit = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Deposit")).textContent();
		      System.out.println("Deposit Amount: " + Deposit);
		      
		      String Withdraw = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Withdraw")).textContent();
		      System.out.println("Withdraw Amount: " + Withdraw);
		    		  
		      String Bills = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Bills")).textContent();
		      System.out.println("Bills Amount: " + Bills);
		      
		      String Others = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Others")).textContent();
		      System.out.println("Others Amount: " + Others);
		    		  
		      String Virtual_Block = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Virtual Block -")).textContent();
		      System.out.println("Virtual_Block Amount: " + Virtual_Block);
		      
		      context.tracing().stop(new Tracing.StopOptions()
		    		   .setPath(Paths.get("trace.zip")));
		       
		       context.close();
		       browser.close();
		       playwright.close();
		}
		  }
}