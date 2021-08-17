package it.adriano.tumino.gamepoint.data;

import java.util.Comparator;
import java.util.List;

public class News {
    private String title;
    private String imageURL;
    private String newsUrl;
    private String body;
    private String date;
    private String website;

    private final int comparator;

    public News(String title, String body, String imageUrl, String date, String newsUrl, String website, int comparator) {
        this.title = title;
        this.imageURL = imageUrl;
        this.newsUrl = newsUrl;
        this.body = body;
        this.date = date;
        this.website = website;
        this.comparator = comparator;
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

    public int getComparator() {
        return comparator;
    }

    public static List<News> sortNewsByDate(List<News> list) {
        list.sort(new CustomComparator());
        return list;
    }

    public static class CustomComparator implements Comparator<News> {

        @Override
        public int compare(News o1, News o2) {
            return Integer.compare(o1.getComparator(), o2.getComparator());
        }
    }
}
