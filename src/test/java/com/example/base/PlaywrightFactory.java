﻿package com.example.base;

import com.example.config.ConfigManager;
import com.microsoft.playwright.*;

public class PlaywrightFactory {
    private static ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> browserContextThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static Page initPage() {
        if (playwrightThreadLocal.get() == null) { playwrightThreadLocal.set(Playwright.create()); }

        // Use System Property (e.g. from GHA matrix) first, then config, then default to chromium
        String browserName = System.getProperty("browser", ConfigManager.get("browser.name"));
        if (browserName == null) browserName = "chromium";

        boolean isCI = System.getenv("CI") != null;
        // Always headless in CI; locally, respect system property or config (defaults to false/headed)
        boolean isHeadless = isCI || Boolean.parseBoolean(System.getProperty("headless", ConfigManager.get("execution.headless")));

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setSlowMo(50);

        Browser browser;
        switch (browserName.toLowerCase()) {
            case "firefox": browser = playwrightThreadLocal.get().firefox().launch(options); break;
            case "webkit": browser = playwrightThreadLocal.get().webkit().launch(options); break;
            case "chrome": browser = playwrightThreadLocal.get().chromium().launch(options.setChannel("chrome")); break;
            case "edge": browser = playwrightThreadLocal.get().chromium().launch(options.setChannel("msedge")); break;
            case "chromium": default: browser = playwrightThreadLocal.get().chromium().launch(options); break;
        }

        browserThreadLocal.set(browser);
        BrowserContext context = browser.newContext();
        browserContextThreadLocal.set(context);
        Page page = context.newPage();
        pageThreadLocal.set(page);

        System.out.println("Browser: " + browserName + " | Headless: " + isHeadless + " | Thread: " + Thread.currentThread().getId());
        return page;
    }

    public static Page getPage() { return pageThreadLocal.get(); }

    public static String getBaseUrl() {
        String url = ConfigManager.get("base.url");
        if (url == null) throw new RuntimeException("Base URL is not defined!");
        return url;
    }

    public static void closeBrowser() {
        if (browserContextThreadLocal.get() != null) { browserContextThreadLocal.get().close(); }
        if (browserThreadLocal.get() != null) { browserThreadLocal.get().close(); }
        if (playwrightThreadLocal.get() != null) { playwrightThreadLocal.get().close(); }
        playwrightThreadLocal.remove(); browserThreadLocal.remove(); browserContextThreadLocal.remove(); pageThreadLocal.remove();
    }
}
