package com.comet.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LedgerReportPage {
    private Page page;

    // Constructor
    public LedgerReportPage(Page page) {
        this.page = page;
    }

    // Actions
    public void navigateToLedgerReport() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reports")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Ledger")).click();
    }

    public void searchLedger(String ledgerId) {
        page.getByRole(AriaRole.TEXTBOX).fill(ledgerId);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View")).click();
    }

    public void printLedgerAmounts() {
        String deposit = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Deposit")).textContent();
        System.out.println("Deposit Amount: " + deposit);

        String withdraw = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Withdraw")).textContent();
        System.out.println("Withdraw Amount: " + withdraw);

        String bills = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Bills")).textContent();
        System.out.println("Bills Amount: " + bills);

        String others = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Others")).textContent();
        System.out.println("Others Amount: " + others);

        String virtualBlock = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Virtual Block -")).textContent();
        System.out.println("Virtual Block Amount: " + virtualBlock);
    }
}
