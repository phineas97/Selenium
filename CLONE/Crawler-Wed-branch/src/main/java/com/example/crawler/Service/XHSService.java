package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 小红书内容提取服务接口
 */
public interface XHSService extends PlatformExtractorService {
    
    /**
     * 提取小红书内容
     * @param url 小红书URL
     * @return 提取的内容信息
     * @throws IOException 网络或解析异常
     * @deprecated 使用 extractContent(String url) 替代
     */
    @Deprecated
    Info ExtractXHS(String url) throws IOException;
}

