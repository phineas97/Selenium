package com.example.crawler.Config;

import lombok.Data;

@Data
public class Info {
    public String Title;
    public String Maintext;
    public String Keywords;
    
    // 新增字段 - 今日头条专用
    public String PublishTime;    // 发布时间
    public String CommentCount;   // 评论数
    public String LikeCount;      // 点赞数
}
