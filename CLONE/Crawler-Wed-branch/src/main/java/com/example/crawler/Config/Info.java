package com.example.crawler.Config;

import lombok.Data;

@Data
public class Info {
    public String Title;
    public String Maintext;
    public String Keywords;
    public String PublishTime;    // 发布时间
    public String CommentCount;   // 评论数
    public String LikeCount;      // 点赞数
}
