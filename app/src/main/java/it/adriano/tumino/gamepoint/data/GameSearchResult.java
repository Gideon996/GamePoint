package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;
import android.os.Parcelable;

public class GameSearchResult extends Information implements Parcelable {
    private String appID;
    private String piattaforme;
    private String store;
    private String price;

    public GameSearchResult(String title, String imageURL, String url, String appID, String piattaforme, String store, String price) {
        super(title, imageURL, url);
        this.appID = appID;
        this.piattaforme = piattaforme;
        this.store = store;
        this.price = price;
    }

    protected GameSearchResult(Parcel in) {
        appID = in.readString();
        piattaforme = in.readString();
        store = in.readString();
        price = in.readString();
    }

    public static final Creator<GameSearchResult> CREATOR = new Creator<GameSearchResult>() {
        @Override
        public GameSearchResult createFromParcel(Parcel in) {
            return new GameSearchResult(in);
        }

        @Override
        public GameSearchResult[] newArray(int size) {
            return new GameSearchResult[size];
        }
    };

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getPiattaforme() {
        return piattaforme;
    }

    public String getStore() {
        return store;
    }

    public String getPrice() { return price; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appID);
        dest.writeString(piattaforme);
        dest.writeString(store);
        dest.writeString(price);
    }
}
