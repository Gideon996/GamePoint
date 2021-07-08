package it.adriano.tumino.gamepoint.data.storegame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PlayStationStoreGame extends StoreGame {
    private String rating;
    private String numberOfPlayers;
    private String inGamePurchases;
    private String onlinePlayMode;
    private ArrayList<String> genres;
    private ArrayList<String> categories;
    private ArrayList<String> voiceLaunguage;
    private ArrayList<String> subtitleLanguage;
    private ArrayList<String> platforms;
    private ArrayList<String> subGenreList;


    public PlayStationStoreGame() {
    }

    private PlayStationStoreGame(Parcel in) {
        super(in);
        rating = in.readString();
        numberOfPlayers = in.readString();
        inGamePurchases = in.readString();
        onlinePlayMode = in.readString();
        genres = in.createStringArrayList();
        categories = in.createStringArrayList();
        voiceLaunguage = in.createStringArrayList();
        subtitleLanguage = in.createStringArrayList();
        platforms = in.createStringArrayList();
        subGenreList = in.createStringArrayList();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(rating);
        out.writeString(numberOfPlayers);
        out.writeString(inGamePurchases);
        out.writeString(onlinePlayMode);
        out.writeStringList(genres);
        out.writeStringList(categories);
        out.writeStringList(voiceLaunguage);
        out.writeStringList(subtitleLanguage);
        out.writeStringList(platforms);
        out.writeStringList(subGenreList);
    }

    public static final Parcelable.Creator<PlayStationStoreGame> CREATOR = new Parcelable.Creator<PlayStationStoreGame>() {
        public PlayStationStoreGame createFromParcel(Parcel in) {
            return new PlayStationStoreGame(in);
        }

        public PlayStationStoreGame[] newArray(int size) {
            return new PlayStationStoreGame[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getInGamePurchases() {
        return inGamePurchases;
    }

    public void setInGamePurchases(String inGamePurchases) {
        this.inGamePurchases = inGamePurchases;
    }

    public String getOnlinePlayMode() {
        return onlinePlayMode;
    }

    public void setOnlinePlayMode(String onlinePlayMode) {
        this.onlinePlayMode = onlinePlayMode;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getVoiceLaunguage() {
        return voiceLaunguage;
    }

    public void setVoiceLaunguage(ArrayList<String> voiceLaunguage) {
        this.voiceLaunguage = voiceLaunguage;
    }

    public ArrayList<String> getSubtitleLanguage() {
        return subtitleLanguage;
    }

    public void setSubtitleLanguage(ArrayList<String> subtitleLanguage) {
        this.subtitleLanguage = subtitleLanguage;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = platforms;
    }

    public ArrayList<String> getSubGenreList() {
        return subGenreList;
    }

    public void setSubGenreList(ArrayList<String> subGenreList) {
        this.subGenreList = subGenreList;
    }
}
