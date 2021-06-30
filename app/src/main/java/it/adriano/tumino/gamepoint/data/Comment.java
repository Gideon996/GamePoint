package it.adriano.tumino.gamepoint.data;

public class Comment {
    private String autore;
    private String descrizione;
    private float rating;
    private String data;

    public Comment(){
    }

    public Comment(String autore, String descrizione, float rating, String data) {
        this.autore = autore;
        this.descrizione = descrizione;
        this.rating = rating;
        this.data = data;
    }

    public String getAutore() {
        return autore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public float getRating() {
        return rating;
    }

    public String getData() {
        return data;
    }
}
