package it.adriano.tumino.gamepoint.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_DB_NAME = "Favorite Games";

    public DBHelper(@Nullable Context context) {
        super(context, DataBaseValues.FAVORITE_TABLE.getName(), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseValues.generateFavoriteTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
