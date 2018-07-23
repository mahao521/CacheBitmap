package com.bitmap.database.bean;

/**
 * Created by Administrator on 2018/7/5.
 */

public class MainInfo {

    private int id;
    private String articlePic;
    private String articleTitle;
    private String articleContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticlePic() {
        return articlePic;
    }

    public void setArticlePic(String articlePic) {
        this.articlePic = articlePic;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}
