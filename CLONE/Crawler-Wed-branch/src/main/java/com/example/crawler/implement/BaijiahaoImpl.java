package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.BaijiahaoService;
import com.example.crawler.utils.SeleniumUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class BaijiahaoImpl implements BaijiahaoService {

    @Override
    public Info ExtractBaijiahao(String url) throws IOException {
        WebDriver driver = null;
        String title = "";
        String maintext = "";
        String k = "";
        String publishTime = "";
        String commentCount = "";
        String likeCount = "";
        
        try {
            driver = SeleniumUtil.createDriver();
            SeleniumUtil.loadPage(driver, url);

            // 提取标题
            title = SeleniumUtil.getTextByCssSelector(driver, ".sKHSJ");
            if (title.isEmpty()) {
                title = driver.getTitle();
            }
            if (title.isEmpty()) {
                title = "无标题";
            }

            // 提取正文
            maintext = SeleniumUtil.getMultipleTextByCssSelector(driver, ".bjh-p");
            if (maintext.isEmpty()) {
                String[] contentSelectors = {".content", "[class*='content']", ".article-content", ".text-content"};
                maintext = SeleniumUtil.getTextByCssSelectors(driver, contentSelectors);
            }
            if (maintext.isEmpty()) {
                maintext = "无正文内容";
            }

            // 提取关键词
            k = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
            if (k.isEmpty()) {
                k = "无关键词";
            }

            // 提取发布时间
            publishTime = SeleniumUtil.getTextByCssSelector(driver, "._2sjh9[data-testid='updatetime']");
            if (publishTime.isEmpty()) {
                String[] timeSelectors = {"._2sjh9", "[data-testid='updatetime']", ".publish-time", ".time", "[class*='time']", "[class*='date']"};
                publishTime = SeleniumUtil.getTextByCssSelectors(driver, timeSelectors);
            }
            if (publishTime.isEmpty()) {
                publishTime = "未知时间";
            }

            // 提取点赞数和评论数
            String interactText = SeleniumUtil.getMultipleTextByCssSelector(driver, ".interact-desc");
            String[] lines = interactText.split("\n");
            if (lines.length >= 2) {
                likeCount = SeleniumUtil.extractNumbersFromText(lines[0]);
                commentCount = SeleniumUtil.extractNumbersFromText(lines[1]);
            } else {
                likeCount = "0";
                commentCount = "0";
            }

        } catch (Exception e) {
            System.out.println("提取百家号内容时发生错误：" + e.getMessage());
            title = "提取失败";
            maintext = "无法获取正文内容，错误：" + e.getMessage();
            k = "无关键词";
            publishTime = "未知时间";
            commentCount = "0";
            likeCount = "0";
        } finally {
            SeleniumUtil.closeDriver(driver);
        }

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
        info.setPublishTime(publishTime);
        info.setCommentCount(commentCount);
        info.setLikeCount(likeCount);
        
        return info;
    }
}
