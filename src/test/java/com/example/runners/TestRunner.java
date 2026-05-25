package com.example.runners;

import com.example.config.DataManager;
import com.example.listeners.RetryListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners(com.example.listeners.RetryListener.class)

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.stepdefs", "com.example.base"},
        plugin = {"pretty", 
                  "html:target/cucumber-reports.html",
                  "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        Object[][] standardScenarios = super.scenarios();
        return standardScenarios; 
    }
    
    @Override
    public void runScenario(Object[] args) throws Throwable {
        if (args.length > 1 && args[1] instanceof Integer) {
            Integer index = (Integer) args[1];
            DataManager.setCurrentDataIndex(index);
        }
        super.runScenario(args);
    }
}
