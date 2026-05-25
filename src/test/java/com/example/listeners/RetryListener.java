package com.example.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class RetryListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        testResult.getMethod().setRetryAnalyzer(new RetryAnalyzer());
    }
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {}
}
