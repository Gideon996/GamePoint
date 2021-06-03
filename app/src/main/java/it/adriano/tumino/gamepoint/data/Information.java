package it.adriano.tumino.gamepoint.data;

public abstract class Information {
    private String title;
    private String imageURL;
    private String url;

    public Information(){ }

    public Information(String title, String imageURL, String url) {
        this.title = title;
        this.imageURL = imageURL;
        this.url = url;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getImageURL() { return imageURL; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }
}
