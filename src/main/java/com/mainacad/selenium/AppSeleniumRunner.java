package com.mainacad.selenium;

import com.mainacad.selenium.driver.WebDriverFactory;
import com.mainacad.selenium.item.Account;
import com.mainacad.selenium.service.PromAccountService;
import com.mainacad.util.Timer;
import org.openqa.selenium.WebDriver;

public class AppSeleniumRunner {

    public static void main(String[] args) {
        WebDriver driver = WebDriverFactory.getChromeDriver();

        Timer.waitSeconds(10);

        Account account = new Account
                ("Pupkina", "0987654321", "Hanna", "Pupkina", "pupkina12345@gmail.com");

        driver = PromAccountService.registerAccount(account, driver);

        driver.quit();
    }
}
