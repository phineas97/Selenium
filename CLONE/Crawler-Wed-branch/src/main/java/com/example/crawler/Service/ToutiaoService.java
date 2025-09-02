package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 今日头条内容提取服务接口
 */
public interface ToutiaoService extends PlatformExtractorService {
    
    /**
     * 提取今日头条内容
     * @param url 今日头条URL
     * @return 提取的内容信息
     * @throws IOException 网络或解析异常
     * @deprecated 使用 extractContent(String url) 替代
     */
    @Deprecated
    Info ExtractToutiao(String url) throws IOException;
}
