package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 平台内容提取器基础接口
 * 所有平台的提取服务都应该实现此接口
 */
public interface PlatformExtractorService {
    
    /**
     * 提取指定URL的内容
     * @param url 目标URL
     * @return 提取的内容信息
     * @throws IOException 网络或解析异常
     */
    Info extractContent(String url) throws IOException;
    
    /**
     * 获取平台名称
     * @return 平台名称
     */
    String getPlatformName();
    
    /**
     * 检查URL是否被此平台支持
     * @param url 目标URL
     * @return 是否支持
     */
    default boolean isUrlSupported(String url) {
        return url != null && !url.trim().isEmpty();
    }
}
