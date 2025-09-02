package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.BaijiahaoService;
import com.example.crawler.utils.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class BaijiahaoImpl extends AbstractCrawlerImpl implements BaijiahaoService {

    @Override
    public Info ExtractBaijiahao(String url) throws IOException {
        return extractContent(url);
    }

    // extractContent方法已在抽象基类中实现，无需重复实现

    @Override
    public String getPlatformName() {
        return "百家号";
    }

    @Override
    protected String extractTitle(WebDriver driver) {
        String title = SeleniumUtil.getTextByCssSelector(driver, ".sKHSJ");
        if (title.isEmpty()) {
            title = driver.getTitle();
        }
        return safeExtract(title, "无标题");
    }

    @Override
    protected String extractMaintext(WebDriver driver) {
        String maintext = SeleniumUtil.getMultipleTextByCssSelector(driver, ".bjh-p") +
                         SeleniumUtil.getMultipleTextByCssSelector(driver, "div.dpu8C._2kCxD");

        if (maintext.isEmpty()) {
            String[] contentSelectors = {".content", "[class*='content']", ".article-content", ".text-content"};
            maintext = SeleniumUtil.getTextByCssSelectors(driver, contentSelectors);
        }
        return safeExtract(maintext, "无正文内容");
    }

    @Override
    protected String extractKeywords(WebDriver driver) {
        String keywords = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
        return safeExtract(keywords, "无关键词");
    }

    @Override
    protected String extractPublishTime(WebDriver driver) {
        String publishTime = SeleniumUtil.getTextByCssSelector(driver, "._2sjh9[data-testid='updatetime']");
        if (publishTime.isEmpty()) {
            String[] timeSelectors = {"._2sjh9", "[data-testid='updatetime']", ".publish-time", ".time", "[class*='time']", "[class*='date']"};
            publishTime = SeleniumUtil.getTextByCssSelectors(driver, timeSelectors);
        }
        return safeExtract(publishTime, "未知时间");
    }

    @Override
    protected String extractCommentCount(WebDriver driver) {
        String interactText = SeleniumUtil.getMultipleTextByCssSelector(driver, ".interact-desc");
        String[] lines = interactText.split("\n");
        if (lines.length >= 2) {
            return SeleniumUtil.extractNumbersFromText(lines[1]);
        }
        return "0";
    }

    @Override
    protected String extractLikeCount(WebDriver driver) {
        String interactText = SeleniumUtil.getMultipleTextByCssSelector(driver, ".interact-desc");
        String[] lines = interactText.split("\n");
        if (lines.length >= 2) {
            return SeleniumUtil.extractNumbersFromText(lines[0]);
        }
        return "0";
    }
}
