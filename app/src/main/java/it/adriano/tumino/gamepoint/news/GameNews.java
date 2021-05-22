package it.adriano.tumino.gamepoint.news;

public class GameNews {

    private String title;
    private String description;
    private String imageUrl;
    private String data;
    private String newsUrl;

    public GameNews() {

    }

    public GameNews(String title, String description, String imageUrl, String data, String newsUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.data = data;
        this.newsUrl = newsUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
