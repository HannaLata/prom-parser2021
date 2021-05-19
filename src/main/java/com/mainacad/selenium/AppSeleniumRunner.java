package com.mainacad.selenium;

import com.mainacad.selenium.driver.WebDriverFactory;
import com.mainacad.selenium.item.Account;
import com.mainacad.selenium.service.PromAccountService;
import com.mainacad.util.Timer;
import org.openqa.selenium.WebDriver;
import java.util.logging.Logger;

public class AppSeleniumRunner {
    private static final Logger LOG = Logger.getLogger(AppSeleniumRunner.class.getName());

    public static void main(String[] args) {
        WebDriver driver = WebDriverFactory.getChromeDriver();

        Timer.waitSeconds(10);

        Account account = new Account
                ("Palahina2", "0987654321", "Hanna", "Palahina", "palahina45678@gmail.com");

        driver = PromAccountService.registerAccount(account, driver);

        driver = PromAccountService.checkRegisteredAccount(account, driver);

        if(driver == null) {
            LOG.warning("Account was not registered!");
        } else {
            LOG.info("Congratulations!");
            driver.quit();
        }
    }
}
