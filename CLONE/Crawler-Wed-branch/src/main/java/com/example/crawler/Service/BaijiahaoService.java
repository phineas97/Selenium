package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import java.io.IOException;

/**
 * 百家号内容提取服务接口
 */
public interface BaijiahaoService extends PlatformExtractorService {
    
    /**
     * 提取百家号内容
     * @param url 百家号URL
     * @return 提取的内容信息
     * @throws IOException 网络或解析异常
     * @deprecated 使用 extractContent(String url) 替代
     */
    @Deprecated
    Info ExtractBaijiahao(String url) throws IOException;
}
