package it.adriano.tumino.gamepoint.data;

public class FavoriteGames extends Information {
    private String store;

    public FavoriteGames(){}

    public FavoriteGames(String title,  String imageURL, String gameURL, String store) {
        super(title, imageURL, gameURL);
        this.store = store;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store){this.store = store;}
}
