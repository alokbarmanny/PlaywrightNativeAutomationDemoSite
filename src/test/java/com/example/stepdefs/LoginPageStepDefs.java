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

    @When("the user logs in with username {string} and password {string}")
    public void the_user_logs_in_with_username_and_password(String username, String password) {
        String actualUsername = DataManager.resolveValue(username);
        String actualPassword = DataManager.resolveValue(password);
        ExtentReportListener.logInfo("Attempting login with user: " + actualUsername);
        loginPage.login(actualUsername, actualPassword);
    }

    @Then("user should see the dashboard header text {string}")
    public void userShouldSeeTheDashboardHeaderText(String expectedText) {
        String actualText = loginPage.getDashboardHeaderText();
        Assert.assertEquals(actualText, expectedText, "Dashboard header mismatch");
        ExtentReportListener.logPass("Verified dashboard header");
    }

    @Then("user print address information from {string} section of the data file")
    @SuppressWarnings("unchecked")
    public void user_print_address_information_from_section_of_the_data_file(String addressSection) {
        String addressKey = DataManager.resolveValue(addressSection);
        Object addressObj = DataManager.getValue(addressKey);
        System.out.println("Thread [" + Thread.currentThread().getId() + "] ---------- Address Information ----------");
        if (addressObj instanceof Map) {
            Map<String, String> addressMap = (Map<String, String>) addressObj;
            addressMap.forEach((k, v) -> System.out.println(k + ": " + v));
        } else {
            System.out.println(addressObj);
        }
        System.out.println("--------------------------------------------------------");
    }
}
