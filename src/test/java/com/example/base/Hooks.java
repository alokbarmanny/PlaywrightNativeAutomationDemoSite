package com.example.base;

import com.example.config.ConfigManager;
import com.example.config.DataManager;
import com.example.listeners.ExtentReportListener;
import com.microsoft.playwright.Page;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before(order = 0)
    public void setUpConfig(Scenario scenario) {
        String env = System.getProperty("env", "dev");
        ConfigManager.loadConfig(env);
    }

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        ExtentReportListener.initReport();
        ExtentReportListener.createTest(scenario.getName());
        PlaywrightFactory.initPage();
        
        scenario.getSourceTagNames().stream().filter(tag -> tag.startsWith("@dataFile:")).findFirst().ifPresent(tag -> {
            String filePath = tag.split("@dataFile:")[1];
            ExtentReportListener.logInfo("Loading data file: " + filePath);
            DataManager.loadDataFile(filePath);
        });
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotPath = captureScreenshot(scenario.getName());
            ExtentReportListener.logFail("Scenario Failed", screenshotPath);
        } else {
            ExtentReportListener.logPass("Scenario Passed");
        }
        DataManager.clearData();
        ConfigManager.clear();
        PlaywrightFactory.closeBrowser();
    }

    @After(order = 0)
    public void flushReport() {
        ExtentReportListener.flushReport();
    }

    private String captureScreenshot(String scenarioName) {
        try {
            Page page = PlaywrightFactory.getPage();
            byte[] buffer = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            return java.util.Base64.getEncoder().encodeToString(buffer);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return "";
        }
    }
}
