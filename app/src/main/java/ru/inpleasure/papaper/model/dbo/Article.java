package ru.inpleasure.papaper.model.dbo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ru.inpleasure.papaper.api.dto.NewsDto;

public class Article
{
    private Integer id;
    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Long publishedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public static Article createFromDto(NewsDto.ArticleDto articleDto)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Article article = new Article();
        article.setSource(articleDto.getSourceDto().getName());
        article.setAuthor(articleDto.getAuthor());
        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setUrl(articleDto.getUrl());
        article.setUrlToImage(articleDto.getUrlToImage());
        Long publishedAt = null;
        try {
            Date date = sdf.parse(articleDto.getPublishedAt());
            publishedAt = date.getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        article.setPublishedAt(publishedAt);
        return article;
    }

    public String getFormattedPublishedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(publishedAt);
        return sdf.format(calendar.getTime());
    }
}
