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
    private static final String MAIN_URL = "https://prom.ua";

    public static WebDriver registerAccount(Account account, WebDriver driver) {


        driver.get(REG_URL);
        Timer.waitSeconds(2);

//        List<WebElement> regForm = driver.findElements(By.tagName("form")).
//                stream().filter(it -> it.getAttribute("data-qaid") != null && it.getAttribute("data-qaid").equals("register_form")).
//                collect(Collectors.toList());

        List<WebElement> forms = driver.findElements(By.tagName("form"));
        WebElement regForm = null;
        for (WebElement element : forms) {
            if (element.getAttribute("data-qaid") != null && element.getAttribute("data-qaid").equals("register_form")) {
                regForm = element;
                break;
            }
        }
        if (regForm == null) {
            LOG.info("Register form was not found!");
            return driver;
        }

        List<WebElement> inputs = regForm.findElements(By.tagName("input"));

        Timer.waitSeconds(4);

        for (WebElement input : inputs) {
            if (input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("name")) {
                input.sendKeys(account.getFirstName());
            }
            if (input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("email")) {
                input.sendKeys(account.getEmail());
            }
            if (input.getAttribute("data-qaid") != null && input.getAttribute("data-qaid").equals("password")) {
                input.sendKeys(account.getPassword());
            }
        }

        Timer.waitSeconds(2);

        List<WebElement> buttons = regForm.findElements(By.tagName("button"));
        for (WebElement button : buttons) {
            if (button.getAttribute("data-qaid") != null && button.getAttribute("data-qaid").equals("submit")) {
                button.submit();
                break;
            }
        }

        Timer.waitSeconds(3);
        String currentUrl = driver.getCurrentUrl();
        driver.get(currentUrl);
        Timer.waitSeconds(2);

        //confirm user
        List<WebElement> personalFormElements = driver.findElements(By.tagName("input"));

        for (WebElement personalFormElement : personalFormElements) {
            if (personalFormElement.getAttribute("data-qaid") != null
                    && personalFormElement.getAttribute("data-qaid").equals("nickname_input")) {
                personalFormElement.sendKeys(account.getLogin());
                break;
            }
        }
        List<WebElement> personalBLockElements = driver.findElements(By.tagName("div"));
        boolean secondNameConfirmed = false;
        for (WebElement personalBLockElement : personalBLockElements) {
            if (personalBLockElement.getAttribute("data-qaid") != null
                    && personalBLockElement.getAttribute("data-qaid").equals("last_name_block")) {
                List<WebElement> webElements =
                        personalBLockElement.findElements(By.tagName("input"));
                if (!webElements.isEmpty()) {
                    webElements.get(0).sendKeys(account.getSecondName());
                    secondNameConfirmed = true;
                    break;
                }
            }
        }
        if (secondNameConfirmed) {
            List<WebElement> saveProfileButtons = driver.findElements(By.tagName("button"));
            for (WebElement saveProfileButton : saveProfileButtons) {
                if (saveProfileButton.getAttribute("data-qaid") != null
                        && saveProfileButton.getAttribute("data-qaid").equals("save_profile")) {
                    Timer.waitSeconds(3);
                    saveProfileButton.click();
                    break;
                }
            }
        }
        Timer.waitSeconds(2);
        currentUrl = driver.getCurrentUrl();
        driver.get(currentUrl);
        return driver;

    }

    public static WebDriver checkRegisteredAccount(Account account, WebDriver driver) {

        driver.get(MAIN_URL);
        Timer.waitSeconds(2);
        List<WebElement> regElements = driver.findElements(By.tagName("span"));

        for (WebElement regElement : regElements) {
            if (regElement.getAttribute("data-qaid") != null
                    && regElement.getAttribute("data-qaid").equals("reg_elements")) {
                String text = regElement.getText();
                if(text.contains(account.getFirstName()) && text.contains(account.getSecondName())) {
                    return driver;
                }
            }
        }
        driver.quit();
        return null;
    }
}




