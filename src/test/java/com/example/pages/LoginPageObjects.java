package com.example.pages;

import com.example.base.PlaywrightFactory;
import com.microsoft.playwright.Page;

public class LoginPageObjects {
    private Page page;
    private String usernameInput = "#user-name";
    private String passwordInput = "#password";
    private String loginButton = "#login-button";
    private String dashboardHeader = ".title";

    public LoginPageObjects(Page page) { this.page = page; }

    public void navigate() {
        String url = PlaywrightFactory.getBaseUrl();
        System.out.println("Navigating to: " + url);
        page.navigate(url);
    }

    public void login(String username, String password) {
        page.fill(usernameInput, username);
        page.fill(passwordInput, password);
        page.click(loginButton);
    }

    public String getDashboardHeaderText() { return page.textContent(dashboardHeader); }
}
