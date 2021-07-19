package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;

import java.util.Objects;

public class BasicGameInformation extends Game{

    private String url;
    private String appID;
    private String platforms;
    private String store;

    public BasicGameInformation(){
    }

    public BasicGameInformation(String title, String imageURL, String url, String appID, String platforms, String store, String price) {
        super.setTitle(title);
        super.setImageHeaderURL(imageURL);
        super.setPrice(price);
        this.url = url;
        this.appID = appID;
        this.platforms = platforms;
        this.store = store;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(url);
        out.writeString(appID);
        out.writeString(platforms);
        out.writeString(store);
    }

    protected BasicGameInformation(Parcel in) {
        super(in);
        url = in.readString();
        appID = in.readString();
        platforms = in.readString();
        store = in.readString();
    }

    public static final Creator<BasicGameInformation> CREATOR = new Creator<BasicGameInformation>() {
        @Override
        public BasicGameInformation createFromParcel(Parcel in) {
            return new BasicGameInformation(in);
        }

        @Override
        public BasicGameInformation[] newArray(int size) {
            return new BasicGameInformation[size];
        }
    };

    public String getAppID() {
        return appID;
    }

    public String getPlatforms() {
        return platforms;
    }

    public String getStore() {
        return store;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicGameInformation that = (BasicGameInformation) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, appID, platforms, store);
    }
}
