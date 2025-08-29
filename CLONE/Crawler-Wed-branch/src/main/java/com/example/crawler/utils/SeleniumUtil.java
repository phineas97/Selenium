package com.example.crawler.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class SeleniumUtil {

    private static ChromeOptions createChromeOptions() {
        System.setProperty("webdriver.chrome.driver", "D:\\Drivers\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--incognito");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }

    public static WebDriver createDriver() {
        ChromeOptions options = createChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    public static void loadPage(WebDriver driver, String url) throws InterruptedException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        Thread.sleep(3000);
    }

    public static String getTextByCssSelector(WebDriver driver, String selector) {
        try {
            WebElement element = driver.findElement(By.cssSelector(selector));
            return element != null ? element.getText().trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getTextByCssSelectors(WebDriver driver, String[] selectors) {
        for (String selector : selectors) {
            try {
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element != null && !element.getText().trim().isEmpty()) {
                    return element.getText().trim();
                }
            } catch (Exception e) {
                continue;
            }
        }
        return "";
    }

    public static String getAttributeByCssSelector(WebDriver driver, String selector, String attribute) {
        try {
            WebElement element = driver.findElement(By.cssSelector(selector));
            return element != null ? element.getAttribute(attribute).trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMultipleTextByCssSelector(WebDriver driver, String selector) {
        try {
            List<WebElement> elements = driver.findElements(By.cssSelector(selector));
            StringBuilder content = new StringBuilder();
            for (WebElement element : elements) {
                String text = element.getText().trim();
                if (!text.isEmpty()) {
                    content.append(text).append("\n");
                }
            }
            return content.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String extractNumbersFromText(String text) {
        if (text == null || text.isEmpty()) {
            return "0";
        }
        String numbers = text.replaceAll("[^0-9]", "");
        return numbers.isEmpty() ? "0" : numbers;
    }

    public static void closeDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("关闭浏览器时发生错误：" + e.getMessage());
            }
        }
    }
}
