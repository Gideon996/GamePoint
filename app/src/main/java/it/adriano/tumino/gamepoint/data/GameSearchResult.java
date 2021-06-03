package it.adriano.tumino.gamepoint.data;

public class GameSearchResult extends Information {
    private String appID;
    private String piattaforme;
    private String store;

    public GameSearchResult(String title, String imageURL, String url, String appID, String piattaforme, String store) {
        super(title, imageURL, url);
        this.appID = appID;
        this.piattaforme = piattaforme;
        this.store = store;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

}
