package com.mc2ads.model;



import java.util.Date;

import com.mc2ads.utils.DateTimeUtil;

public class Video
{

    public Video()
    {
        creationDate = new Date();
        creationDateStr = DateTimeUtil.formatDate(creationDate);
    }

    public Video(long article_id, String title, String thumbnail_url, Date creationDate, int pageview)
    {
        this.article_id = article_id;
        this.title = title;
        this.thumbnail_url = thumbnail_url;
        this.creationDate = creationDate;
        creationDateStr = DateTimeUtil.formatDate(creationDate);
        this.pageview = pageview;
    }

    public Video(long article_id, String title, String thumbnail_url, String creationDateStr, int pageview)
    {
        this.article_id = article_id;
        this.title = title;
        this.thumbnail_url = thumbnail_url;
        this.creationDateStr = creationDateStr;
        creationDate = DateTimeUtil.parseDateStr(creationDateStr);
        this.pageview = pageview;
    }

    public long getArticle_id()
    {
        return article_id;
    }

    public void setArticle_id(long article_id)
    {
        this.article_id = article_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getThumbnail_url()
    {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url)
    {
        this.thumbnail_url = thumbnail_url;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getCreationDateStr()
    {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr)
    {
        this.creationDateStr = creationDateStr;
    }

    public int getPageview()
    {
        return pageview;
    }

    public void setPageview(int pageview)
    {
        this.pageview = pageview;
    }

    public String toString()
    {
        System.out.println((new StringBuilder("article_id: ")).append(article_id).toString());
        System.out.println((new StringBuilder("title: ")).append(title).toString());
        System.out.println((new StringBuilder("thumbnail_url: ")).append(thumbnail_url).toString());
        System.out.println((new StringBuilder("Date: ")).append(creationDateStr).toString());
        System.out.println((new StringBuilder("pageview: ")).append(pageview).toString());
        System.out.println("-------------------------------");
        return super.toString();
    }

    long article_id;
    String title;
    String thumbnail_url;
    Date creationDate;
    String creationDateStr;
    int pageview;
}
