package com.example.crawler.Controller;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CrawlerController {

    private final CrawlerService crawlerService;

    /**
     * 提取网页内容
     * @param url 目标URL
     * @return 提取的内容信息
     */
    @GetMapping("/extract")
    public ResponseEntity<Info> extractContent(@RequestParam String url) {
        try {
            if (url == null || url.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Info info = crawlerService.extractContent(url.trim());
            return ResponseEntity.ok(info);
            
        } catch (IllegalArgumentException e) {
            log.warn("不支持的URL: {}", url);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("提取内容失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 检查URL是否被支持
     * @param url 目标URL
     * @return 支持状态
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkUrlSupport(@RequestParam String url) {
        try {
            boolean supported = crawlerService.isUrlSupported(url);
            return ResponseEntity.ok(supported);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * 获取支持的平台列表
     * @return 支持的平台列表
     */
    @GetMapping("/platforms")
    public ResponseEntity<List<String>> getSupportedPlatforms() {
        List<String> platforms = List.of(
            "小红书 (www.xiaohongshu.com)",
            "今日头条 (www.toutiao.com)", 
            "百家号 (mbd.baidu.com)",
            "微信公众号 (mp.weixin.qq.com)"
        );
        return ResponseEntity.ok(platforms);
    }
}
