package it.adriano.tumino.gamepoint.processes.catchgame;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;

import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.utils.Utils;

public interface JsonParser<T extends StoreGame> {

    default T getJsonAndParsing(@NonNull String url, String TAG) {
        String json = Utils.getJsonFromUrl(url);
        if (json.isEmpty()) {
            Log.e(TAG, "Unable to open the game");
            return null;
        }

        try {
            return jsonParser(json);
        } catch (JSONException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    T jsonParser(@NonNull String json) throws JSONException;

}
