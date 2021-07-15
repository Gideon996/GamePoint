package it.adriano.tumino.gamepoint.data.storegame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.Game;

public abstract class StoreGame extends Game implements Parcelable {

    private String description;
    private String releaseData;
    private String store;
    private ArrayList<String> screenshotsUrl;

    public StoreGame() {
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(description);
        out.writeString(releaseData);
        out.writeString(store);
        out.writeStringList(screenshotsUrl);
    }

    protected StoreGame(Parcel in) { //devono essere in ordine di come li scrivo
        super(in);
        description = in.readString();
        releaseData = in.readString();
        store = in.readString();
        screenshotsUrl = in.createStringArrayList();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public ArrayList<String> getScreenshotsUrl() {
        return screenshotsUrl;
    }

    public void setScreenshotsUrl(ArrayList<String> screenshotsUrl) {
        this.screenshotsUrl = screenshotsUrl;
    }
}
