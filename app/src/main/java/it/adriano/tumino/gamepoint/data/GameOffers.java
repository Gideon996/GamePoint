package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;
import android.os.Parcelable;

public class GameOffers extends BasicGameInformation {
    private String originalPrice;
    private String discountPercentage;

    public GameOffers(String title, String imageURL, String url, String appID, String platforms, String store, String priceFinal, String originalPrice, String discountPercentage) {
        super(title, imageURL, url, appID, platforms, store, priceFinal);
        this.originalPrice = originalPrice;
        this.discountPercentage = discountPercentage;
    }

    private GameOffers(Parcel in) {
        super(in);
        originalPrice = in.readString();
        discountPercentage = in.readString();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(originalPrice);
        out.writeString(discountPercentage);
    }

    public static final Parcelable.Creator<GameOffers> CREATOR = new Parcelable.Creator<GameOffers>() {
        public GameOffers createFromParcel(Parcel in) {
            return new GameOffers(in);
        }

        public GameOffers[] newArray(int size) {
            return new GameOffers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
