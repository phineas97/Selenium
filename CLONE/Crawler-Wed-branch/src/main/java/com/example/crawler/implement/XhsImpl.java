package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.XHSService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class XhsImpl implements XHSService {
    String k = "";
    String maintext = "";
    String title = "";
    String publishTime = "";

    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public Info ExtractXHS(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String rawHTML = response.body().string();
        Document document = Jsoup.parse(rawHTML);

        ExtractTitle(document);
        ExtractMaintext(document);
        ExtractPublishTime(document);

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
        info.setPublishTime(publishTime);
        
        // 小红书不支持评论数和点赞数，设置为空值
        info.setCommentCount("");
        info.setLikeCount("");
        
        return info;
    }

    private void ExtractTitle (Document document) {
        title = document.title();
        if (title == null || title.isEmpty()) {
            title = "无标题";
        }
    }

    private void ExtractMaintext (Document document) {
        Element mainContent = null;
        mainContent = document.selectFirst("meta[name=description]");
        if (mainContent != null) {
            maintext = mainContent.attr("content").trim();
        } else {
            maintext = "无正文内容";
        }
        Element keyword = document.selectFirst("meta[name=keywords]");
        k = "";
        if (keyword != null) {
            k = keyword.attr("content").trim();
        } else {
            k = "无关键词";
        }
    }

    private void ExtractPublishTime(Document document) {
        try {
            // 根据提供的HTML结构：<span class="date" selected-disabled-search="" data-v-610be4fa="">编辑于 08-12 云南</span>
            Element timeElement = document.selectFirst(".date");
            if (timeElement != null) {
                publishTime = timeElement.text().trim();
            }
            
            if (publishTime == null || publishTime.isEmpty()) {
                // 备用选择器，尝试其他可能的时间元素
                String[] timeSelectors = {
                    ".bottom-container .date",
                    "[class*='date']",
                    "[class*='time']",
                    ".publish-time",
                    ".create-time"
                };
                
                for (String selector : timeSelectors) {
                    Element element = document.selectFirst(selector);
                    if (element != null && !element.text().trim().isEmpty()) {
                        publishTime = element.text().trim();
                        break;
                    }
                }
            }
            
            if (publishTime == null || publishTime.isEmpty()) {
                publishTime = "未知时间";
            }
            
        } catch (Exception e) {
            System.out.println("提取小红书发布时间失败：" + e.getMessage());
            publishTime = "未知时间";
        }
    }

}
