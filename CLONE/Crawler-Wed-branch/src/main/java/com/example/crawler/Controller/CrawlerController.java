package com.example.crawler.Controller;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.ToutiaoService;
import com.example.crawler.Service.XHSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;

@RestController
@CrossOrigin
public class CrawlerController {

    private final XHSService xhsservice;

    private final ToutiaoService toutiaoservice;

    public CrawlerController(XHSService xhsService,
                             ToutiaoService toutiaoService) {
        this.xhsservice = xhsService;
        this.toutiaoservice = toutiaoService;
    }

    @GetMapping("/extract")
    public Info ExtractHTMLController(@RequestParam String url)throws Exception {

        String host = UrlCheck(url);
        Info info = new Info();

        if ("www.xiaohongshu.com".equals(host)) {
            info = xhsservice.ExtractXHS(url);
        }else if("www.toutiao.com".equals(host)) {
            info = toutiaoservice.ExtractToutiao(url);
        } else {
            System.out.println("不支持的URL");
            return null;
        }
        System.out.println(info.Title+"\n"+info.Maintext+"\n"+info.Keywords);
        return info;
    }

    public String UrlCheck (String url) throws IOException {
        URL UrlCheck = new URL (url);
        String host = UrlCheck.getHost();
        if ("www.xiaohongshu.com".equals(host) || "www.toutiao.com".equals(host)) {
            return host;
        } else {
            return null;
        }
    }

}
