package com.example.stepdefs;

import com.example.base.PlaywrightFactory;
import com.example.config.DataManager;
import com.example.listeners.ExtentReportListener;
import com.example.pages.LoginPageObjects;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import java.util.Map;

public class LoginPageStepDefs {
    private Page page;
    private LoginPageObjects loginPage;

    public LoginPageStepDefs() {
        this.page = PlaywrightFactory.getPage();
        this.loginPage = new LoginPageObjects(page);
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        ExtentReportListener.logInfo("Navigating to Login Page");
        loginPage.navigate();
    }

    @When("the user logs in with username \"\" and password \"\"")
    public void theUserLogsInWithUsernameAndPassword(String rawUsername, String rawPassword) {
        String actualUsername = DataManager.resolveValue(rawUsername);
        String actualPassword = DataManager.resolveValue(rawPassword);
        ExtentReportListener.logInfo("Attempting login with user: " + actualUsername);
        loginPage.login(actualUsername, actualPassword);
    }

    @Then("user should see the dashboard header text {string}")
    public void userShouldSeeTheDashboardHeaderText(String expectedText) {
        String actualText = loginPage.getDashboardHeaderText();
        Assert.assertEquals(actualText, expectedText, "Dashboard header mismatch");
        ExtentReportListener.logPass("Verified dashboard header");
    }

    @Then("user print address information from \"\" section of the data file")
    public void userPrintAddressInformationFromSectionOfTheDataFile(String rawAddressKey) {
        String addressKey = DataManager.resolveValue(rawAddressKey);
        Object addressObj = DataManager.getValue(addressKey);
        System.out.println("Thread [" + Thread.currentThread().getId() + "] ---------- Address Information ----------");
        if (addressObj instanceof Map) {
            Map<String, String> addressMap = (Map<String, String>) addressObj;
            addressMap.forEach((k, v) -> System.out.println(k + ": " + v));
        } else { System.out.println(addressObj); }
        System.out.println("--------------------------------------------------------");
    }
}
