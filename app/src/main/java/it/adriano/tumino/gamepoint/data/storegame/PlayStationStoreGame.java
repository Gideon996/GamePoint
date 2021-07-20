package it.adriano.tumino.gamepoint.data.storegame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PlayStationStoreGame extends StoreGame {

    private String rating;
    private String numberOfPlayers;
    private String inGamePurchases;
    private String onlinePlayMode;
    private String videoUrl;
    private String shots;
    private ArrayList<String> genres;
    private ArrayList<String> categories;
    private ArrayList<String> voiceLanguages;
    private ArrayList<String> subtitleLanguages;
    private ArrayList<String> platforms;
    private ArrayList<String> subGenreList;


    public PlayStationStoreGame() {
        setStore("PSN");
    }

    private PlayStationStoreGame(Parcel in) {
        super(in);
        rating = in.readString();
        numberOfPlayers = in.readString();
        inGamePurchases = in.readString();
        onlinePlayMode = in.readString();
        videoUrl = in.readString();
        shots = in.readString();
        genres = in.createStringArrayList();
        categories = in.createStringArrayList();
        voiceLanguages = in.createStringArrayList();
        subtitleLanguages = in.createStringArrayList();
        platforms = in.createStringArrayList();
        subGenreList = in.createStringArrayList();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(rating);
        out.writeString(numberOfPlayers);
        out.writeString(inGamePurchases);
        out.writeString(onlinePlayMode);
        out.writeString(videoUrl);
        out.writeString(shots);
        out.writeStringList(genres);
        out.writeStringList(categories);
        out.writeStringList(voiceLanguages);
        out.writeStringList(subtitleLanguages);
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getShots() {
        return shots;
    }

    public void setShots(String shots) {
        this.shots = shots;
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

    public ArrayList<String> getVoiceLanguages() {
        return voiceLanguages;
    }

    public void setVoiceLanguages(ArrayList<String> voiceLanguages) {
        this.voiceLanguages = voiceLanguages;
    }

    public ArrayList<String> getSubtitleLanguages() {
        return subtitleLanguages;
    }

    public void setSubtitleLanguages(ArrayList<String> subtitleLanguages) {
        this.subtitleLanguages = subtitleLanguages;
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
