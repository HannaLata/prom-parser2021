package com.mainacad.selenium.service;

import com.mainacad.selenium.item.Account;
import com.mainacad.util.Timer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Logger;

public class PromAccountService {

    private static final Logger LOG = Logger.getLogger(PromAccountService.class.getName());

    private static final String REG_URL = "https://prom.ua/join-customer";

    public static WebDriver registerAccount(Account account, WebDriver driver) {


        driver.get(REG_URL);
        Timer.waitSeconds(2);

//        List<WebElement> regForm = driver.findElements(By.tagName("form")).
//                stream().filter(it -> it.getAttribute("data-qaid") != null && it.getAttribute("data-qaid").equals("register_form")).
//                collect(Collectors.toList());

        List<WebElement> forms = driver.findElements(By.tagName("form"));
        WebElement regForm = null;
        for (WebElement element : forms) {
            if(element.getAttribute("data-qaid") != null && element.getAttribute("data-qaid").equals("register_form")) {
            regForm = element;
            break;
            }
        }
        if(regForm == null) {
            LOG.info("Register form was not found!");
            return driver;
        }

        List<WebElement> inputs = regForm.findElements(By.tagName("input"));

        Timer.waitSeconds(4);

        for (WebElement input: inputs) {
            if(input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("name")) {
                input.sendKeys(account.getFirstName());
            }
            if(input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("email")) {
                input.sendKeys(account.getEmail());
            }
            if(input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("password")) {
                input.sendKeys(account.getPassword());
            }
        }

        Timer.waitSeconds(2);

        List<WebElement> buttons = regForm.findElements(By.tagName("button"));
        for (WebElement button: buttons) {
            if (button.getAttribute("data-qaid") != null && button.getAttribute("data-qaid").equals("submit")) {
                button.submit();
                break;
            }
        }

        Timer.waitSeconds(3);
        String currentUrl = driver.getCurrentUrl();
        driver.get(currentUrl);
        Timer.waitSeconds(2);

        return driver;
    }
}
