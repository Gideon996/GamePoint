package it.adriano.tumino.gamepoint.database;

import org.jetbrains.annotations.NotNull;

public class DBUtils {

    public static final String ID = "_id";
    public static final String TITLE = "Title";
    public static final String IMAGE_URL = "Image_Url";
    public static final String STORE = "Store";
    public static final String URL = "URL";
    public static final String APPID = "AppID";
    public static final String PRICE = "Price";

    public static final String LAST_RESEARCH_TABLE_TITLE = "UltimeRicercheTable";

    private static final String SELECT_FROM = "SELECT  * FROM %s";
    private static final String SELECT_FROM_NAME_AND_STORE = " where %s = '%s' AND %s = '%s'";

    public static String generateLastResearcTable() {
        String query = "";
        query += "CREATE TABLE " + LAST_RESEARCH_TABLE_TITLE;
        query += " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        query += TITLE + " " + "TEXT" + ",";
        query += IMAGE_URL + " " + "TEXT" + ",";
        query += STORE + " " + "TEXT" + ",";
        query += APPID + " " + "TEXT" + ",";
        query += PRICE + " " + "TEXT" + ",";
        query += URL + " " + "TEXT" + ")";
        return query;
    }

    @NotNull
    public static String getSelectionQuery(String tableName) {
        return String.format(SELECT_FROM, tableName);
    }

    @NotNull
    public static String getSelectionWithNameAndStoreQuery(String tableName, String title, String store) {
        String query = getSelectionQuery(tableName);
        query += String.format(SELECT_FROM_NAME_AND_STORE, TITLE, title, STORE, store);
        return query;
    }

}
