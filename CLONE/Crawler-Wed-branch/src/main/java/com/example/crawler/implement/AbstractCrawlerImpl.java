package com.example.crawler.implement;

import com.example.crawler.Config.CrawlerConfig;
import com.example.crawler.Config.Info;
import com.example.crawler.utils.SeleniumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 抽象爬虫实现基类
 * 提取公共的爬虫逻辑，减少代码重复
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCrawlerImpl {
    
    @Autowired
    protected CrawlerConfig crawlerConfig;
    
    /**
     * 提取内容的模板方法
     * @param url 目标URL
     * @return 提取的内容信息
     * @throws IOException 网络异常
     */
    public final Info extractContent(String url) throws IOException {
        WebDriver driver = null;
        try {
            log.info("开始提取内容，平台: {}, URL: {}", getPlatformName(), url);
            
            driver = createWebDriver();
            loadPage(driver, url);
            
            String title = extractTitle(driver);
            String maintext = extractMaintext(driver);
            String keywords = extractKeywords(driver);
            String publishTime = extractPublishTime(driver);
            String commentCount = extractCommentCount(driver);
            String likeCount = extractLikeCount(driver);
            
            Info info = Info.builder()
                    .title(title)
                    .maintext(maintext)
                    .keywords(keywords)
                    .publishTime(publishTime)
                    .commentCount(commentCount)
                    .likeCount(likeCount)
                    .platform(getPlatformName())
                    .url(url)
                    .build();
            
            log.info("成功提取内容，平台: {}, 标题: {}", getPlatformName(), title);
            return info;
            
        } catch (Exception e) {
            log.error("提取内容失败，平台: {}, URL: {}, 错误: {}", getPlatformName(), url, e.getMessage());
            return createErrorInfo(url, e);
        } finally {
            closeWebDriver(driver);
        }
    }
    
    /**
     * 创建WebDriver实例
     */
    protected WebDriver createWebDriver() {
        return SeleniumUtil.createDriver(
            crawlerConfig.getChromeDriverPath(),
            crawlerConfig.getPageLoadTimeout(),
            crawlerConfig.getImplicitWait()
        );
    }
    
    /**
     * 加载页面
     */
    protected void loadPage(WebDriver driver, String url) throws InterruptedException {
        SeleniumUtil.loadPage(driver, url);
    }
    
    /**
     * 关闭WebDriver
     */
    protected void closeWebDriver(WebDriver driver) {
        SeleniumUtil.closeDriver(driver);
    }
    
    /**
     * 创建错误信息对象
     */
    protected Info createErrorInfo(String url, Exception e) {
        return Info.builder()
                .title("提取失败")
                .maintext("无法获取正文内容，错误：" + e.getMessage())
                .keywords("无关键词")
                .publishTime("未知时间")
                .commentCount("0")
                .likeCount("0")
                .platform(getPlatformName())
                .url(url)
                .build();
    }
    
    /**
     * 安全提取文本，如果为空则返回默认值
     */
    protected String safeExtract(String value, String defaultValue) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }
    
    // 抽象方法，由子类实现具体的提取逻辑
    
    /**
     * 获取平台名称
     */
    protected abstract String getPlatformName();
    
    /**
     * 提取标题
     */
    protected abstract String extractTitle(WebDriver driver);
    
    /**
     * 提取正文内容
     */
    protected abstract String extractMaintext(WebDriver driver);
    
    /**
     * 提取关键词
     */
    protected abstract String extractKeywords(WebDriver driver);
    
    /**
     * 提取发布时间
     */
    protected abstract String extractPublishTime(WebDriver driver);
    
    /**
     * 提取评论数
     */
    protected abstract String extractCommentCount(WebDriver driver);
    
    /**
     * 提取点赞数
     */
    protected abstract String extractLikeCount(WebDriver driver);
}
