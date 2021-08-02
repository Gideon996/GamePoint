package it.adriano.tumino.gamepoint.data;

public class News {
    private String title;
    private String imageURL;
    private String newsUrl;
    private String body;
    private String date;
    private String website;

    public News(String title, String body, String imageUrl, String date, String newsUrl, String website) {
        this.title = title;
        this.imageURL = imageUrl;
        this.newsUrl = newsUrl;
        this.body = body;
        this.date = date;
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
