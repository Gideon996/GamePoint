package it.adriano.tumino.gamepoint.database;

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
        Log.i(TAG, "Inizializzazione DataBase");
        this.tableName = tableName;
        dbHelper = new DBHelper(context, tableName);
    }

    public void save(String title, String imageUrl, String store, String url, String AppID) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseValues.TITLE.getName(), title);
        contentValues.put(DataBaseValues.IMAGE_URL.getName(), imageUrl);
        contentValues.put(DataBaseValues.STORE.getName(), store);
        contentValues.put(DataBaseValues.URL.getName(), url);
        contentValues.put(DataBaseValues.APPID.getName(), AppID);

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

    public boolean deleteFromNameAndStore(String name, String store){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "select * from " + tableName + " where " + DataBaseValues.TITLE.getName() + " = '" + name + "'"
                + " AND " + DataBaseValues.STORE.getName() + " = '" + store + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex(DataBaseValues.ID.getName()));
        return delete(id);
    }

    public ArrayList<BasicGameInformation> getAll() {
        ArrayList<BasicGameInformation> list = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + tableName;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            for(int i = 0; i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                String title = cursor.getString(cursor.getColumnIndex(DataBaseValues.TITLE.getName()));
                String imageUrl = cursor.getString(cursor.getColumnIndex(DataBaseValues.IMAGE_URL.getName()));
                String gameUrl = cursor.getString(cursor.getColumnIndex(DataBaseValues.URL.getName()));
                String store = cursor.getString(cursor.getColumnIndex(DataBaseValues.STORE.getName()));
                String appID = cursor.getString(cursor.getColumnIndex(DataBaseValues.APPID.getName()));

                BasicGameInformation basicGameInformation = new BasicGameInformation(title, imageUrl, gameUrl, appID, null, store, "");
                list.add(basicGameInformation);
            }
        }
        cursor.close();
        database.close();

        return list;
    }

    public boolean checkIfElementsIsOnDataBase(String name, String store){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "select * from " + tableName + " where " + DataBaseValues.TITLE.getName() + " = '" + name + "'"
                + " AND " + DataBaseValues.STORE.getName() + " = '" + store + "'";
        Cursor cursor = db.rawQuery(query, null);
        return (cursor.getCount() > 0);
    }


}
