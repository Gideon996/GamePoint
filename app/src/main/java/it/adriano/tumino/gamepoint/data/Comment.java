package it.adriano.tumino.gamepoint.data;

public class Comment {
    private final String id;
    private final String autore;
    private final String descrizione;
    private final int rating;
    private final String data;

    public Comment(String id, String autore, String descrizione, int rating, String data) {
        this.id = id;
        this.autore = autore;
        this.descrizione = descrizione;
        this.rating = rating;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getAutore() {
        return autore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getRating() {
        return rating;
    }

    public String getData() {
        return data;
    }
}
