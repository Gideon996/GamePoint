package it.adriano.tumino.gamepoint.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.FavoriteGames;
import it.adriano.tumino.gamepoint.data.GameSearchResult;

public class DBManager {
    public static final String TAG = "DBManager";
    private final DBHelper dbHelper;
    private final String tableName;

    public DBManager(Context context, String tableName) {
        Log.i(TAG, "Inizializzazione DataBase");
        this.tableName = tableName;
        dbHelper = new DBHelper(context, tableName);
    }

    public void save(String title, String imageUrl, String store, String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseValues.TITLE.getName(), title);
        contentValues.put(DataBaseValues.IMAGE_URL.getName(), imageUrl);
        contentValues.put(DataBaseValues.STORE.getName(), store);
        contentValues.put(DataBaseValues.URL.getName(), url);

        try {
            database.insert(tableName, null, contentValues);
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public boolean delete(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            if (database.delete(tableName, DataBaseValues.ID.getName() + "=?", new String[]{Long.toString(id)}) > 0) {
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
            cursor = database.query(tableName, null, null, null, null, null, null, null);
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
        return cursor;
    }

    public String getUrlFromName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "select * from " + tableName + " where " + DataBaseValues.TITLE.getName() + " = '" + name + "'";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        return cur.getString(cur.getColumnIndex(DataBaseValues.URL.getName()));
    }

    public ArrayList<GameSearchResult> getAll() {
        ArrayList<GameSearchResult> list = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + tableName;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(DataBaseValues.TITLE.getName()));
                String imageUrl = cursor.getString(cursor.getColumnIndex(DataBaseValues.IMAGE_URL.getName()));
                String gameUrl = cursor.getString(cursor.getColumnIndex(DataBaseValues.URL.getName()));
                String store = cursor.getString(cursor.getColumnIndex(DataBaseValues.STORE.getName()));

                GameSearchResult gameSearchResult = new GameSearchResult(title, imageUrl, gameUrl, null, null, store);
                list.add(gameSearchResult);
            }
        }
        cursor.close();
        database.close();

        return list;
    }
}
