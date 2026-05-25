package com.example.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void initReport() {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportPath = "target/ExtentReports/Report_" + timestamp + ".html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Playwright Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("MM/dd/yyyy hh:mm:ss a");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
    }

    public static void createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }

    public static ExtentTest getTest() { return extentTest.get(); }

    public static void flushReport() {
        if (extent != null) { extent.flush(); }
    }
    
    public static void logInfo(String message) {
        if (getTest() != null) { getTest().info(message); }
    }

    public static void logPass(String message) {
        if (getTest() != null) { getTest().pass(message); }
    }
    
    public static void logFail(String message, String base64Screenshot) {
        if (getTest() != null) {
            getTest().fail(message);
            if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                getTest().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            }
        }
    }
}
