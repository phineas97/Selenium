package com.example.crawler.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.HashMap;

@Data
@Configuration
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {
    
    // ChromeDriver配置
    private String chromeDriverPath = "D:\\Drivers\\chromedriver-win64\\chromedriver.exe";
    private int pageLoadTimeout = 30;
    private int implicitWait = 10;
    
    // 支持的平台配置
    private Map<String, String> supportedHosts = new HashMap<>();
    
    public CrawlerConfig() {
        // 初始化支持的主机
        supportedHosts.put("www.xiaohongshu.com", "XHS");
        supportedHosts.put("www.toutiao.com", "TOUTIAO");
        supportedHosts.put("mbd.baidu.com", "BAIJIAHAO");
        supportedHosts.put("mp.weixin.qq.com", "WECHAT");
    }
    
    public boolean isSupportedHost(String host) {
        return supportedHosts.containsKey(host);
    }
    
    public String getPlatformType(String host) {
        return supportedHosts.get(host);
    }
}
