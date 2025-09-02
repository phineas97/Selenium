package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 统一的爬虫服务接口
 */
public interface CrawlerService {
    
    /**
     * 根据URL自动识别平台并提取内容
     * @param url 目标URL
     * @return 提取的内容信息
     * @throws IOException 网络异常
     */
    Info extractContent(String url) throws IOException;
    
    /**
     * 检查URL是否被支持
     * @param url 目标URL
     * @return 是否支持
     */
    boolean isUrlSupported(String url);
    
    /**
     * 获取URL对应的平台类型
     * @param url 目标URL
     * @return 平台类型
     */
    String getPlatformType(String url);
}
