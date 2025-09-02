package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.XHSService;
import com.example.crawler.utils.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class XhsImpl extends AbstractCrawlerImpl implements XHSService {

    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public Info ExtractXHS(String url) throws IOException {
        return extractContent(url);
    }

    // extractContent方法已在抽象基类中实现，无需重复实现

    @Override
    public String getPlatformName() {
        return "小红书";
    }

    @Override
    protected String extractTitle(WebDriver driver) {
        String title = driver.getTitle();
        return safeExtract(title, "无标题");
    }

    @Override
    protected String extractMaintext(WebDriver driver) {
        String maintext = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name=description]", "content");
        return safeExtract(maintext, "无正文内容");
    }

    @Override
    protected String extractKeywords(WebDriver driver) {
        String keywords = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name=keywords]", "content");
        return safeExtract(keywords, "无关键词");
    }

    @Override
    protected String extractPublishTime(WebDriver driver) {
        // 小红书发布时间选择器
        String[] timeSelectors = {
            ".date",
            ".bottom-container .date",
            "[class*='date']",
            "[class*='time']",
            ".publish-time",
            ".create-time"
        };
        
        String publishTime = SeleniumUtil.getTextByCssSelectors(driver, timeSelectors);
        return safeExtract(publishTime, "未知时间");
    }

    @Override
    protected String extractCommentCount(WebDriver driver) {
        // 小红书不支持评论数
        return "0";
    }

    @Override
    protected String extractLikeCount(WebDriver driver) {
        // 小红书不支持点赞数
        return "0";
    }
}