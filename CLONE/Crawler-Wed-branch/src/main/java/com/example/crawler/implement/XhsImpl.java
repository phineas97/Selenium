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
        ExtractKeywords(document);

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
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

    private void ExtractKeywords (Document document) {
        Element keyword = document.selectFirst("meta[name=keywords]");
        k = "";
        if (keyword != null) {
            k = keyword.attr("content").trim();
        } else {
            k = "无关键词";
        }
    }

}
