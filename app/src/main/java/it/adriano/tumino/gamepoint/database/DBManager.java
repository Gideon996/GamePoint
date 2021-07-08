package it.adriano.tumino.gamepoint.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;

public class DBManager {
    public static final String TAG = "DBManager";
    private final DBHelper dbHelper;
    private final String tableName;

    public DBManager(Context context, String tableName) {
        Log.i(TAG, "Initialization " + tableName + " Database");
        this.tableName = tableName;
        dbHelper = new DBHelper(context, tableName);
    }

    public void save(String title, String imageUrl, String store, String url, String AppID) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.TITLE, title);
        contentValues.put(DBUtils.IMAGE_URL, imageUrl);
        contentValues.put(DBUtils.STORE, store);
        contentValues.put(DBUtils.URL, url);
        contentValues.put(DBUtils.APPID, AppID);

        try {
            Log.i(TAG, "Save game " + title + " in " + tableName);
            database.insert(tableName, null, contentValues);
        } catch (SQLiteException exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public boolean delete(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            if (database.delete(tableName, DBUtils.ID + "=?", new String[]{Long.toString(id)}) > 0) {
                Log.i(TAG, "Deletted game " + id + " in " + tableName);
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

    @SuppressLint("Recycle")
    public boolean deleteWithNameAndStore(String name, String store) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = DBUtils.getSelectionWithNameAndStoreQuery(tableName, name, store);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex(DBUtils.ID));
        Log.d(TAG, "Delete game " + id + " in " + tableName);
        return delete(id);
    }

    public ArrayList<BasicGameInformation> getAllElements() {
        ArrayList<BasicGameInformation> list = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selectQuery = DBUtils.getSelectionQuery(tableName);

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String title = cursor.getString(cursor.getColumnIndex(DBUtils.TITLE));
                String imageUrl = cursor.getString(cursor.getColumnIndex(DBUtils.IMAGE_URL));
                String gameUrl = cursor.getString(cursor.getColumnIndex(DBUtils.URL));
                String store = cursor.getString(cursor.getColumnIndex(DBUtils.STORE));
                String appID = cursor.getString(cursor.getColumnIndex(DBUtils.APPID));

                BasicGameInformation basicGameInformation = new BasicGameInformation(title, imageUrl, gameUrl, appID, null, store, "");
                list.add(basicGameInformation);
            }
        }
        cursor.close();
        database.close();
        Log.d(TAG, "Get all games from " + tableName);

        return list;
    }

    @SuppressLint("Recycle")
    public boolean checkIfElementsIsOnDataBase(String name, String store) {
        Log.d(TAG, "Check existing game " + name + " in " + tableName);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = DBUtils.getSelectionWithNameAndStoreQuery(tableName, name, store);
        Cursor cursor = db.rawQuery(query, null);
        return (cursor.getCount() > 0);
    }


}
