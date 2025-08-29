package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.ToutiaoService;
import com.example.crawler.utils.SeleniumUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ToutiaoImpl implements ToutiaoService {

    @Override
    public Info ExtractToutiao(String url) throws IOException {
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
            title = driver.getTitle();
            if (title.isEmpty()) {
                title = "无标题";
            }

            // 提取正文
            String[] contentSelectors = {
                "article.syl-article-base.syl-page-article.tt-article-content.syl-device-pc",
                "article.article-content",
                ".article-content",
                ".content",
                "[class*='article-content']",
                "[class*='content']"
            };
            
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
            
            if (maintext.isEmpty()) {
                maintext = "无正文内容";
            }

            // 提取关键词
            k = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='keywords']", "content");
            if (k.isEmpty()) {
                String desc = SeleniumUtil.getAttributeByCssSelector(driver, "meta[name='description']", "content");
                if (!desc.isEmpty()) {
                    k = "从描述提取: " + desc.substring(0, Math.min(desc.length(), 100));
                } else {
                    k = "无关键词";
                }
            }

            // 提取发布时间
            String[] timeSelectors = {".article-meta span", ".article-meta span:first-child", "[class*='time']", "[class*='date']", ".publish-time", ".time"};
            publishTime = SeleniumUtil.getTextByCssSelectors(driver, timeSelectors);
            if (publishTime.isEmpty()) {
                publishTime = "未知时间";
            }

            // 提取评论数
            String commentText = SeleniumUtil.getTextByCssSelector(driver, ".title span");
            if (commentText.matches("\\d+")) {
                commentCount = commentText;
            } else {
                String[] commentSelectors = {".comment-count", "[class*='comment'] span", "[aria-label*='评论']"};
                for (String selector : commentSelectors) {
                    String text = SeleniumUtil.getTextByCssSelector(driver, selector);
                    String numbers = SeleniumUtil.extractNumbersFromText(text);
                    if (!numbers.equals("0")) {
                        commentCount = numbers;
                        break;
                    }
                }
            }
            if (commentCount.isEmpty() || !commentCount.matches("\\d+")) {
                commentCount = "0";
            }

            // 提取点赞数
            String likeText = SeleniumUtil.getTextByCssSelector(driver, ".detail-like span");
            if (likeText.matches("\\d+")) {
                likeCount = likeText;
            } else {
                // 尝试通过aria-label获取
                String ariaLabel = SeleniumUtil.getAttributeByCssSelector(driver, "[aria-label*='点赞']", "aria-label");
                if (!ariaLabel.isEmpty()) {
                    likeCount = SeleniumUtil.extractNumbersFromText(ariaLabel);
                } else {
                    String[] likeSelectors = {".like-count", "[class*='like'] span", "[class*='digg'] span", ".digg-count"};
                    for (String selector : likeSelectors) {
                        String text = SeleniumUtil.getTextByCssSelector(driver, selector);
                        String numbers = SeleniumUtil.extractNumbersFromText(text);
                        if (!numbers.equals("0")) {
                            likeCount = numbers;
                            break;
                        }
                    }
                }
            }
            if (likeCount.isEmpty() || !likeCount.matches("\\d+")) {
                likeCount = "0";
            }

        } catch (Exception e) {
            System.out.println("提取今日头条内容时发生错误：" + e.getMessage());
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