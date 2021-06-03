package it.adriano.tumino.gamepoint.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_DB_NAME = "Favorite Games";
    private String tableName;

    public DBHelper(@Nullable Context context, String tableName) {
        super(context, tableName, null, 1);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(tableName.equals(DataBaseValues.FAVORITE_TABLE.getName())){
            db.execSQL(DataBaseValues.generateFavoriteTable());
        }else{
            db.execSQL(DataBaseValues.generateUltimeRicercheTable());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
