package com.example.crawler.Config;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    private String title;
    private String maintext;
    private String keywords;
    private String publishTime;    // 发布时间
    private String commentCount;   // 评论数
    private String likeCount;      // 点赞数
    private String platform;       // 来源平台
    private String url;            // 原始URL
    
    // 为了保持向后兼容性，保留旧的public字段访问器
    public String getTitle() { return title; }
    public String getMaintext() { return maintext; }
    public String getKeywords() { return keywords; }
    public String getPublishTime() { return publishTime; }
    public String getCommentCount() { return commentCount; }
    public String getLikeCount() { return likeCount; }
    
    // 兼容旧代码的公共字段
    public String Title;
    public String Maintext;
    public String Keywords;
    public String PublishTime;
    public String CommentCount;
    public String LikeCount;
    
    // 同步新旧字段
    public void setTitle(String title) {
        this.title = title;
        this.Title = title;
    }
    
    public void setMaintext(String maintext) {
        this.maintext = maintext;
        this.Maintext = maintext;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
        this.Keywords = keywords;
    }
    
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
        this.PublishTime = publishTime;
    }
    
    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
        this.CommentCount = commentCount;
    }
    
    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
        this.LikeCount = likeCount;
    }
}
