package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.ToutiaoService;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToutiaoImpl implements ToutiaoService {

    String k = "";
    String maintext = "";
    String title = "";

    private ChromeOptions createChromeOptions() {
        // 设置WebDriver路径
        System.setProperty("webdriver.chrome.driver", "D:\\Drivers\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        
        // 基本设置
        options.addArguments("--disable-extensions");  // 禁用浏览器扩展
        options.addArguments("--headless");  // 启用无头模式（不显示浏览器界面）
        options.addArguments("--window-size=1920,1080");    // 指定浏览器分辨率
        options.addArguments("--disable-gpu");    // 禁用GPU硬件加速
        options.addArguments("--disable-popup-blocking");    // 禁用弹出窗口拦截器
        options.addArguments("--no-sandbox");    // 彻底停用沙箱
        options.addArguments("--disable-web-security");    // 禁用网页安全性功能
        options.addArguments("--ignore-certificate-errors");    // 忽略SSL证书错误
        options.addArguments("--incognito");    // 以隐身模式启动浏览器
        options.addArguments("--disable-plugins");    // 禁用插件
        options.addArguments("--disable-images");    // 禁用图像
        
        // 反检测设置
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // 禁用自动化标识
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        return options;
    }

    @Override
    public Info ExtractToutiao(String url) throws IOException {
        WebDriver driver = null;
        
        try {
            // 使用配置好的ChromeOptions创建WebDriver
            ChromeOptions options = createChromeOptions();
            
            // 如果需要代理，可以在这里配置
            // Proxy proxy = new Proxy();
            // proxy.setSocksProxy("proxy-server:port");
            // options.setProxy(proxy);
            
            driver = new ChromeDriver(options);
            
            // 设置页面加载超时
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // 打开目标页面
            driver.get(url);

            // 显式等待页面完全加载
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            // 额外等待，允许JavaScript执行和动态内容加载
            Thread.sleep(3000);

            // 提取内容
            ExtractTitle(driver);
            ExtractMaintext(driver, wait);
            ExtractKeywords(driver);

        } catch (Exception e) {
            System.out.println("提取今日头条内容时发生错误：" + e.getMessage());
            e.printStackTrace();
            
            // 设置默认值
            title = "提取失败";
            maintext = "无法获取正文内容，错误：" + e.getMessage();
            k = "无关键词";
            
        } finally {
            // 确保关闭浏览器
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    System.out.println("关闭浏览器时发生错误：" + e.getMessage());
                }
            }
        }

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
        return info;
    }
    private void ExtractTitle(WebDriver driver) {
        title = driver.getTitle();
        if (title == null || title.isEmpty()) {
            title = "无标题";
        }
    }

    private void ExtractMaintext(WebDriver driver, WebDriverWait wait) {
        try {
            // 尝试多种选择器来找到文章内容
            WebElement articleElement = null;
            String[] selectors = {
                "article.syl-article-base.syl-page-article.tt-article-content.syl-device-pc",
                "article.article-content",
                ".article-content",
                ".content",
                "[class*='article-content']",
                "[class*='content']"
            };
            
            for (String selector : selectors) {
                try {
                    articleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
                    break;
                } catch (Exception e) {
                    // 尝试下一个选择器
                    continue;
                }
            }
            
            if (articleElement != null) {
                List<WebElement> paragraphs = articleElement.findElements(By.tagName("p"));
                List<String> content = new ArrayList<>();
                
                for (WebElement p : paragraphs) {
                    String text = p.getText().trim();
                    if (!text.isEmpty()) {
                        content.add(text);
                    }
                }
                
                maintext = String.join("\n", content);
                if (maintext.isEmpty()) {
                    // 如果p标签没有内容，尝试获取整个文章的文本
                    maintext = articleElement.getText().trim();
                }
            }
            
            if (maintext == null || maintext.isEmpty()) {
                maintext = "无正文内容";
            }
            
        } catch (Exception e) {
            System.out.println("提取正文内容失败：" + e.getMessage());
            maintext = "无法获取正文内容";
        }
    }

    private void ExtractKeywords(WebDriver driver) {
        try {
            WebElement keywordElement = driver.findElement(By.cssSelector("meta[name='keywords']"));
            if (keywordElement != null) {
                k = keywordElement.getAttribute("content").trim();
            }
            
            if (k == null || k.isEmpty()) {
                // 尝试从description中获取关键词
                try {
                    WebElement descElement = driver.findElement(By.cssSelector("meta[name='description']"));
                    if (descElement != null) {
                        String desc = descElement.getAttribute("content").trim();
                        if (!desc.isEmpty()) {
                            k = "从描述提取: " + desc.substring(0, Math.min(desc.length(), 100));
                        }
                    }
                } catch (Exception e) {
                    // 忽略
                }
            }
            
            if (k == null || k.isEmpty()) {
                k = "无关键词";
            }
            
        } catch (Exception e) {
            System.out.println("提取关键词失败：" + e.getMessage());
            k = "无关键词";
        }
    }

}