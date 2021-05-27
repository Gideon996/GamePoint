package it.adriano.tumino.gamepoint.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBManager {
    public static final String TAG = "DBManager";
    private final DBHelper dbHelper;

    public DBManager(Context context) {
        Log.i(TAG, "Inizializzazione DataBase");
        dbHelper = new DBHelper(context);
    }

    public void save(String title, String imageUrl, String store, String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseValues.TITLE.getName(), title);
        contentValues.put(DataBaseValues.IMAGE_URL.getName(), imageUrl);
        contentValues.put(DataBaseValues.STORE.getName(), store);
        contentValues.put(DataBaseValues.URL.getName(), url);

        try {
            database.insert(DataBaseValues.FAVORITE_TABLE.getName(), null, contentValues);
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public boolean delete(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            if (database.delete(DataBaseValues.FAVORITE_TABLE.getName(), DataBaseValues.ID.getName() + "=?", new String[]{Long.toString(id)}) > 0) {
                return true;
            }
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
        }
        return false;
    }

    public Cursor query() {
        Cursor cursor;
        try {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            cursor = database.query(DataBaseValues.FAVORITE_TABLE.getName(), null, null, null, null, null, null, null);
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
        return cursor;
    }

    public String getUrlFromName(String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "select * from " + DataBaseValues.FAVORITE_TABLE.getName() + " where "+ DataBaseValues.TITLE.getName() + " = '" + name + "'";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        return cur.getString(cur.getColumnIndex(DataBaseValues.URL.getName()));
    }

}
