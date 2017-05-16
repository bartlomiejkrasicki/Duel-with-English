package database_vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;
import android.widget.TextView;


public class VocabularyDatabase extends SQLiteOpenHelper {

    public VocabularyDatabase(Context context){
        super(context, DatabaseColumnNames.DATABASE_NAME, null, DatabaseColumnNames.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DatabaseColumnNames.TABLE_NAME
                + " (" + DatabaseColumnNames._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER + " INTEGER,"
                + DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER + " INTEGER,"
                + DatabaseColumnNames.COLUMN_NAME_PLWORD + " TEXT,"
                + DatabaseColumnNames.COLUMN_NAME_ENGWORD + " TEXT,"
                + DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {//aktualizacja danych
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseColumnNames.TABLE_NAME + "");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private void putValuesToDatabase(int columnGroup, int columnItem, String engword, String plword, int favouriteImageOn){          //umieszczanie danych w tablicy
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER, columnGroup);
        values.put(DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER, columnItem);
        values.put(DatabaseColumnNames.COLUMN_NAME_ENGWORD, engword);
        values.put(DatabaseColumnNames.COLUMN_NAME_PLWORD, plword);
        values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON, favouriteImageOn);

        sqLiteDatabase.insert(DatabaseColumnNames.TABLE_NAME, null, values);
    }

    public boolean updateValuesInDatabase(String id, int favouriteImageOn){          //umieszczanie danych w tablicy
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON, favouriteImageOn);
        sqLiteDatabase.update(DatabaseColumnNames.TABLE_NAME, values, DatabaseColumnNames._ID + " = ?", new String[] { id });
        sqLiteDatabase.close();
        return true;
    }

    public Cursor getFavouriteValues(){                           //pobieranie wszystkich danych
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        final int addedToFavourite = 1;
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON + "=" + addedToFavourite, null);
    }

    public Cursor getSpecificValues(int i, int i1){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER + "=" + i + " AND "
                + DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER + "=" + i1, null);
    }
    public Cursor getGroupValues(int i){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER + "=" + i, null);
    }

    public void showVocabularyForLessons(Cursor cursor, TextView plword, TextView engword, ImageView favouriteImageStar, int position) {            //pokazywanie danych podczas nauki słownictwa
        if(cursor.moveToPosition(position)) {
            plword.setText(cursor.getString(3));
            engword.setText(cursor.getString(4));
            int addFavOrNot = cursor.getInt(5);
            if (addFavOrNot == 1) {
                favouriteImageStar.setImageResource(android.R.drawable.star_big_on);
            } else
                favouriteImageStar.setImageResource(android.R.drawable.star_big_off);
        }
    }

    public void showVocabularyForFavourite(Cursor cursor, TextView plword, TextView engword, int position) {            //pokazywanie danych dla listy ulubionych
        if(cursor.moveToPosition(position)) {
            plword.setText(cursor.getString(3));
            engword.setText(cursor.getString(4));
        }
        cursor.close();
    }

    public void initData(){                            //inicjalizacja danych
        putValuesToDatabase(0, 0, "red", "czerwony", 0);
        putValuesToDatabase(0, 0, "blue", "niebieski", 0);
        putValuesToDatabase(0, 0, "brown", "brązowy", 0);
        putValuesToDatabase(0, 0, "white", "biały", 0);
        putValuesToDatabase(0, 0, "colorful", "kolorowy", 0);
        putValuesToDatabase(0, 0, "colorless", "bezbarwny", 0);
        putValuesToDatabase(0, 0, "pink", "różowy", 0);
        putValuesToDatabase(0, 0, "yellow", "żółty", 0);
        putValuesToDatabase(0, 0, "green", "zielony", 0);
        putValuesToDatabase(0, 0, "orange", "pomarańczowy", 0);
        putValuesToDatabase(0, 1, "dance", "tańczyć", 0);
        putValuesToDatabase(0, 1, "jump", "skakać", 0);
    }
}
