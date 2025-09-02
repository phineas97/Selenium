package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.ToutiaoService;
import com.example.crawler.utils.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ToutiaoImpl extends AbstractCrawlerImpl implements ToutiaoService {

    @Override
    public Info ExtractToutiao(String url) throws IOException {
        return extractContent(url);
    }

    // extractContent方法已在抽象基类中实现，无需重复实现

    @Override
    public String getPlatformName() {
        return "今日头条";
    }

    @Override
    protected String extractTitle(WebDriver driver) {
        String title = driver.getTitle();
        return safeExtract(title, "无标题");
    }

    @Override
    protected String extractMaintext(WebDriver driver) {
        String[] contentSelectors = {
            "article.syl-article-base.syl-page-article.tt-article-content.syl-device-pc",
            "article.article-content",
            ".article-content",
            ".content",
            "[class*='article-content']",
            "[class*='content']"
        };
        
        String maintext = "";
        for (String selector : contentSelectors) {
            maintext = SeleniumUtil.getMultipleTextByCssSelector(driver, selector + " p");
            if (!maintext.isEmpty()) {
                break;
            }
            // 如果没有p标签，尝试获取整个元素文本
            maintext = SeleniumUtil.getTextByCssSelector(driver, selector);
            if (!maintext.isEmpty()) {
                break;
            }
        }
        
        return safeExtract(maintext, "无正文内容");
    }

    @Override
    protected String extractKeywords(WebDriver driver) {
        String keywords = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
        if (keywords.isEmpty()) {
            String desc = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='description']", "content");
            if (!desc.isEmpty()) {
                keywords = "从描述提取: " + desc.substring(0, Math.min(desc.length(), 100));
            }
        }
        return safeExtract(keywords, "无关键词");
    }

    @Override
    protected String extractPublishTime(WebDriver driver) {
        String[] timeSelectors = {
            ".article-meta span", 
            ".article-meta span:first-child", 
            "[class*='time']", 
            "[class*='date']", 
            ".publish-time", 
            ".time"
        };
        String publishTime = SeleniumUtil.getTextByCssSelectors(driver, timeSelectors);
        return safeExtract(publishTime, "未知时间");
    }

    @Override
    protected String extractCommentCount(WebDriver driver) {
        String commentText = SeleniumUtil.getTextByCssSelector(driver, ".title span");
        if (commentText.matches("\\d+")) {
            return commentText;
        }
        
        String[] commentSelectors = {".comment-count", "[class*='comment'] span", "[aria-label*='评论']"};
        for (String selector : commentSelectors) {
            String text = SeleniumUtil.getTextByCssSelector(driver, selector);
            String numbers = SeleniumUtil.extractNumbersFromText(text);
            if (!numbers.equals("0")) {
                return numbers;
            }
        }
        return "0";
    }

    @Override
    protected String extractLikeCount(WebDriver driver) {
        String likeText = SeleniumUtil.getTextByCssSelector(driver, ".detail-like span");
        if (likeText.matches("\\d+")) {
            return likeText;
        }
        
        // 尝试通过aria-label获取
        String ariaLabel = SeleniumUtil.getAttributeByCssSelector(driver, "[aria-label*='点赞']", "aria-label");
        if (!ariaLabel.isEmpty()) {
            return SeleniumUtil.extractNumbersFromText(ariaLabel);
        }
        
        String[] likeSelectors = {".like-count", "[class*='like'] span", "[class*='digg'] span", ".digg-count"};
        for (String selector : likeSelectors) {
            String text = SeleniumUtil.getTextByCssSelector(driver, selector);
            String numbers = SeleniumUtil.extractNumbersFromText(text);
            if (!numbers.equals("0")) {
                return numbers;
            }
        }
        return "0";
    }
}