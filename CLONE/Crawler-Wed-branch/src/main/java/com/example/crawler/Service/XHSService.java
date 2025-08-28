package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public interface XHSService {

    Info ExtractXHS(String url) throws IOException;

//    Info ExtractToutiao(String url) throws IOException;
//    private final OkHttpClient okHttpClient= new OkHttpClient();
//    private Element mainContent = null;
//    private String maintext = null;
//    private String k = null;
//    private Element keyword = null;
//
//    public String ExtractXHS() throws Exception {
//        Request request = new Request.Builder()
//                .url("https://www.xiaohongshu.com/discovery/item/6889822c00000000230330f1?app_platform=ios&app_version=8.69&share_from_user_hidden=true&xsec_source=app_share&type=normal&xsec_token=CBaD7naznx2xd0q4hl8zdxQPe8GPdkm9qo7NhHfcpJZ9s=&author_share=1&xhsshare=CopyLink&shareRedId=ODtHNUdHNEE2NzUyOTgwNjY4OTc9Pkg5&apptime=1753961349&share_id=2735308753644d05ac025aff823ce133")
//                .build();
//
//        Response response = okHttpClient.newCall(request).execute();
//
//        String RawHTML = response.body().string();
//
//        Document document = Jsoup.parse(RawHTML);
//
//        String title = document.title();
//
//        URL url = new URL("https://www.xiaohongshu.com/discovery/item/6889822c00000000230330f1?app_platform=ios&app_version=8.69&share_from_user_hidden=true&xsec_source=app_share&type=normal&xsec_token=CBaD7naznx2xd0q4hl8zdxQPe8GPdkm9qo7NhHfcpJZ9s=&author_share=1&xhsshare=CopyLink&shareRedId=ODtHNUdHNEE2NzUyOTgwNjY4OTc9Pkg5&apptime=1753961349&share_id=2735308753644d05ac025aff823ce133");
//        String host = url.getHost();
//
//        if (host == "www.xiaohongshu.com") {
//            mainContent = document.selectFirst("span.note-text > span");
//            maintext = "";
//
//            if (mainContent != null) {
//                maintext = mainContent.text().trim();
//            } else {
//                maintext = "无正文内容";
//            }
//            keyword = document.selectFirst("meta[name=keywords]");
//            k = "";
//            if (keyword != null) {
//                k = keyword.attr("content").trim();
//            } else {
//                k = "无关键词";
//            }
//        } else if (host=="www.toutiao.com") {
//            mainContent = document.selectFirst("span.note-text > span");
//            maintext = "";
//
//            if (mainContent != null) {
//                maintext = mainContent.text().trim();
//            } else {
//                maintext = "无正文内容";
//            }
//            keyword = document.selectFirst("meta[name=keywords]");
//            k = "";
//            if (keyword != null) {
//                k = keyword.attr("content").trim();
//            } else {
//                k = "无关键词";
//            }
//        }
//        return String.format("\n\n标题：%s\n\n正文：\n%s\n\n关键词：%s\n", title.isEmpty() ? "无标题" : title, maintext, k);
//    }

}

