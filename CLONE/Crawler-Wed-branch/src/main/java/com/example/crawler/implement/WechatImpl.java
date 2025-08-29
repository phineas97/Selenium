package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.WechatService;
import com.example.crawler.utils.SeleniumUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class WechatImpl implements WechatService {

    @Override
    public Info ExtractWechat(String url) throws IOException {
        WebDriver driver = null;
        String title = "";
        String maintext = "";
        String k = "";
        String publishTime = "";
        
        try {
            driver = SeleniumUtil.createDriver();
            SeleniumUtil.loadPage(driver, url);

            // 提取标题
            title = SeleniumUtil.getTextByCssSelector(driver, "h1.rich_media_title");
            if (title.isEmpty()) {
                title = SeleniumUtil.getTextByCssSelector(driver, "#activity-name");
            }
            if (title.isEmpty()) {
                title = driver.getTitle();
            }
            if (title.isEmpty()) {
                title = "无标题";
            }

            // 提取正文
            maintext = SeleniumUtil.getMultipleTextByCssSelector(driver, "span[leaf]");
            if (maintext.isEmpty()) {
                String[] contentSelectors = {".rich_media_content", "#js_content", ".rich_media_area_primary", "[id*='content']", ".content"};
                maintext = SeleniumUtil.getTextByCssSelectors(driver, contentSelectors);
            }
            if (maintext.isEmpty()) {
                maintext = "无正文内容";
            }

            // 提取关键词
            k = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
            if (k.isEmpty()) {
                String desc = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='description']", "content");
                if (!desc.isEmpty()) {
                    k = "从描述提取: " + desc.substring(0, Math.min(desc.length(), 50));
                } else {
                    k = "无关键词";
                }
            }

            // 提取发布时间
            publishTime = SeleniumUtil.getTextByCssSelector(driver, "#publish_time");
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
            if (publishTime.isEmpty()) {
                publishTime = "未知时间";
            }

        } catch (Exception e) {
            System.out.println("提取微信公众号内容时发生错误：" + e.getMessage());
            title = "提取失败";
            maintext = "无法获取正文内容，错误：" + e.getMessage();
            k = "无关键词";
            publishTime = "未知时间";
        } finally {
            SeleniumUtil.closeDriver(driver);
        }

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
        info.setPublishTime(publishTime);
        info.setCommentCount("");
        info.setLikeCount("");
        
        return info;
    }
}
