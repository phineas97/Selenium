package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 微信公众号内容提取服务接口
 */
public interface WechatService extends PlatformExtractorService {
    
    /**
     * 提取微信公众号内容
     * @param url 微信公众号URL
     * @return 提取的内容信息
     * @throws IOException 网络或解析异常
     * @deprecated 使用 extractContent(String url) 替代
     */
    @Deprecated
    Info ExtractWechat(String url) throws IOException;
}
