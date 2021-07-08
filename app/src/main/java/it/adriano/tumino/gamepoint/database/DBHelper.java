package it.adriano.tumino.gamepoint.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private final String tableName;

    public DBHelper(@Nullable Context context, String tableName) {
        super(context, tableName, null, 1);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(tableName.equals(DBUtils.FAVORITE_TABLE_TITLE)){
            db.execSQL(DBUtils.generateFavoriteTable());
        }else{
            db.execSQL(DBUtils.generateLastResearcTable());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
