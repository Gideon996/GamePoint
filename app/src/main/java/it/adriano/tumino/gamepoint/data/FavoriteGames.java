package it.adriano.tumino.gamepoint.data;

public class FavoriteGames extends Information {
    private final String store;

    public FavoriteGames(String title,  String imageURL, String gameURL, String store) {
        super(title, imageURL, gameURL);
        this.store = store;

    }

    public String getStore() {
        return store;
    }
}
