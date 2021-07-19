package it.adriano.tumino.gamepoint.data;

public class Comment {
    private String author;
    private String authorID;
    private String description;
    private float rating;
    private String data;

    public Comment(){
    }

    public Comment(String author, String authorID, String description, float rating, String data) {
        this.author = author;
        this.authorID = authorID;
        this.description = description;
        this.rating = rating;
        this.data = data;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorID() {return authorID;}

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public String getData() {
        return data;
    }
}
