package it.adriano.tumino.gamepoint.processes.catchgame;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Document;

import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.utils.Utils;

public interface WebScrapping<T extends StoreGame> {

    default T getWebPageAndParsing(@NonNull String url, String TAG) {
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) {
            Log.e(TAG, "Unable to open the game");
            return null;
        }

        return scrapping(document);
    }

    T scrapping(@NonNull Document document);

}
