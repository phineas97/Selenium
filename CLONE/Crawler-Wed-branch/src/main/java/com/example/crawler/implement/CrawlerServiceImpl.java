package com.example.crawler.implement;

import com.example.crawler.Config.CrawlerConfig;
import com.example.crawler.Config.Info;
import com.example.crawler.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerServiceImpl implements CrawlerService {
    
    private final CrawlerConfig crawlerConfig;
    private final XHSService xhsService;
    private final ToutiaoService toutiaoService;
    private final BaijiahaoService baijiahaoService;
    private final WechatService wechatService;
    
    // 平台服务映射
    private Map<String, PlatformExtractor> platformExtractors;
    
    @jakarta.annotation.PostConstruct
    public void init() {
        platformExtractors = Map.of(
            "XHS", url -> xhsService.ExtractXHS(url),
            "TOUTIAO", url -> toutiaoService.ExtractToutiao(url),
            "BAIJIAHAO", url -> baijiahaoService.ExtractBaijiahao(url),
            "WECHAT", url -> wechatService.ExtractWechat(url)
        );
    }
    
    @Override
    public Info extractContent(String url) throws IOException {
        if (!isUrlSupported(url)) {
            log.warn("不支持的URL: {}", url);
            throw new IllegalArgumentException("不支持的URL: " + url);
        }
        
        String platformType = getPlatformType(url);
        PlatformExtractor extractor = platformExtractors.get(platformType);
        
        if (extractor == null) {
            log.error("找不到平台 {} 的提取器", platformType);
            throw new IllegalStateException("找不到平台提取器: " + platformType);
        }
        
        try {
            Info info = extractor.extract(url);
            log.info("成功提取内容 - 平台: {}, 标题: {}", platformType, info.getTitle());
            return info;
        } catch (Exception e) {
            log.error("提取内容失败 - 平台: {}, URL: {}, 错误: {}", platformType, url, e.getMessage());
            throw new IOException("内容提取失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isUrlSupported(String url) {
        try {
            String host = new URL(url).getHost();
            return crawlerConfig.isSupportedHost(host);
        } catch (Exception e) {
            log.warn("URL格式错误: {}", url);
            return false;
        }
    }
    
    @Override
    public String getPlatformType(String url) {
        try {
            String host = new URL(url).getHost();
            return crawlerConfig.getPlatformType(host);
        } catch (Exception e) {
            log.warn("无法解析URL: {}", url);
            return null;
        }
    }
    
    /**
     * 平台提取器函数式接口
     */
    @FunctionalInterface
    private interface PlatformExtractor {
        Info extract(String url) throws IOException;
    }
}
