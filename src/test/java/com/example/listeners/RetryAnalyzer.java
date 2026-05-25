package com.example.listeners;

import com.example.config.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private int maxRetry = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (maxRetry == 0) {
            try {
                String retryProp = ConfigManager.get("retry.count");
                maxRetry = Integer.parseInt(retryProp);
            } catch (Exception e) { maxRetry = 0; }
        }
        if (count < maxRetry) {
            count++;
            System.out.println("Retrying test " + result.getName() + ", attempt " + count + " of " + maxRetry);
            return true;
        }
        count = 0; 
        return false;
    }
}
