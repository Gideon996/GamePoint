package it.adriano.tumino.gamepoint.database;

public enum DataBaseValues {
    ID("_id", null),
    TITLE("Title", "TEXT"),
    IMAGE_URL("Image_Url", "TEXT"),
    STORE("Store", "TEXT"),
    URL("URL", "TEXT"),
    APPID("APPID", "TEXT"),
    FAVORITE_TABLE("FavoriteTable", null),
    ULITME_RICERCHE("UltimeRicercheTable", null);

    private final String name;
    private final String type;

    DataBaseValues(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static String generateFavoriteTable() {
        String query = "";
        query += "CREATE TABLE " + DataBaseValues.FAVORITE_TABLE.getName();
        query += " ( " + DataBaseValues.ID.getName() + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        query += DataBaseValues.TITLE.getName() + " " + DataBaseValues.TITLE.getType() + ",";
        query += DataBaseValues.IMAGE_URL.getName() + " " + DataBaseValues.IMAGE_URL.getType() + ",";
        query += DataBaseValues.STORE.getName() + " " + DataBaseValues.STORE.getType() + ",";
        query += DataBaseValues.APPID.getName() + " " + DataBaseValues.APPID.getType() + ",";
        query += DataBaseValues.URL.getName() + " " + DataBaseValues.URL.getType() + ")";
        return query;
    }

    public static String generateUltimeRicercheTable(){
        String query = "";
        query += "CREATE TABLE " + DataBaseValues.ULITME_RICERCHE.getName();
        query += " ( " + DataBaseValues.ID.getName() + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        query += DataBaseValues.TITLE.getName() + " " + DataBaseValues.TITLE.getType() + ",";
        query += DataBaseValues.IMAGE_URL.getName() + " " + DataBaseValues.IMAGE_URL.getType() + ",";
        query += DataBaseValues.STORE.getName() + " " + DataBaseValues.STORE.getType() + ",";
        query += DataBaseValues.APPID.getName() + " " + DataBaseValues.APPID.getType() + ",";
        query += DataBaseValues.URL.getName() + " " + DataBaseValues.URL.getType() + ")";
        return query;
    }
}
