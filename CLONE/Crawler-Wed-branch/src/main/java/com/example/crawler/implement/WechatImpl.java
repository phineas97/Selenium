package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.WechatService;
import com.example.crawler.utils.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class WechatImpl extends AbstractCrawlerImpl implements WechatService {

    @Override
    public Info ExtractWechat(String url) throws IOException {
        return extractContent(url);
    }

    // extractContent方法已在抽象基类中实现，无需重复实现

    @Override
    public String getPlatformName() {
        return "微信公众号";
    }

    @Override
    protected String extractTitle(WebDriver driver) {
        String title = SeleniumUtil.getTextByCssSelector(driver, "h1.rich_media_title");
        if (title.isEmpty()) {
            title = SeleniumUtil.getTextByCssSelector(driver, "#activity-name");
        }
        if (title.isEmpty()) {
            title = driver.getTitle();
        }
        return safeExtract(title, "无标题");
    }

    @Override
    protected String extractMaintext(WebDriver driver) {
        String maintext = SeleniumUtil.getMultipleTextByCssSelector(driver, "span[leaf]");
        if (maintext.isEmpty()) {
            String[] contentSelectors = {".rich_media_content", "#js_content", ".rich_media_area_primary", "[id*='content']", ".content"};
            maintext = SeleniumUtil.getTextByCssSelectors(driver, contentSelectors);
        }
        return safeExtract(maintext, "无正文内容");
    }

    @Override
    protected String extractKeywords(WebDriver driver) {
        String keywords = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
        if (keywords.isEmpty()) {
            String desc = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='description']", "content");
            if (!desc.isEmpty()) {
                keywords = "从描述提取: " + desc.substring(0, Math.min(desc.length(), 50));
            }
        }
        return safeExtract(keywords, "无关键词");
    }

    @Override
    protected String extractPublishTime(WebDriver driver) {
        String publishTime = SeleniumUtil.getTextByCssSelector(driver, "#publish_time");
        if (publishTime.isEmpty()) {
            String[] timeSelectors = {".rich_media_meta_text", ".rich_media_meta", "[class*='publish']", "[class*='time']", "[class*='date']", ".time", ".date"};
            for (String selector : timeSelectors) {
                String text = SeleniumUtil.getTextByCssSelector(driver, selector);
                if (!text.isEmpty() && (text.matches(".*\\d{4}.*\\d{1,2}.*\\d{1,2}.*") || text.matches(".*\\d{1,2}.*\\d{1,2}.*"))) {
                    publishTime = text;
                    break;
                }
            }
        }
        return safeExtract(publishTime, "未知时间");
    }

    @Override
    protected String extractCommentCount(WebDriver driver) {
        // 微信公众号不支持评论数
        return "0";
    }

    @Override
    protected String extractLikeCount(WebDriver driver) {
        // 微信公众号不支持点赞数
        return "0";
    }
}
