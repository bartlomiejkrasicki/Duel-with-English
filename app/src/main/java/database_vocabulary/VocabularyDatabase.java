package database_vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;
import android.widget.TextView;


public class VocabularyDatabase extends SQLiteOpenHelper {


    public VocabularyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DatabaseColumnNames.DATABASE_NAME, factory, DatabaseColumnNames.DATABASE_VERSION);
    }

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

    public void putValuesToDatabase(int columnGroup, int columnItem, String engword, String plword, int favouriteImageOn){          //umieszczanie danych w tablicy
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER, columnGroup);
        values.put(DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER, columnItem);
        values.put(DatabaseColumnNames.COLUMN_NAME_ENGWORD, engword);
        values.put(DatabaseColumnNames.COLUMN_NAME_PLWORD, plword);
        values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON, favouriteImageOn);

        sqLiteDatabase.insert(DatabaseColumnNames.TABLE_NAME, null, values);
    }

    public Cursor getValues(){                           //pobieranie danych
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseColumnNames.TABLE_NAME, new String[]{DatabaseColumnNames._ID, DatabaseColumnNames.COLUMN_NAME_ENGWORD,
                DatabaseColumnNames.COLUMN_NAME_PLWORD, DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON}, null, null, null, null, null);
        return cursor;
    }

    public int getTableCount(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT (*) FROM " + DatabaseColumnNames.TABLE_NAME + ";", null);
        return cursor.getCount();
    }

    public void showVocabulary(Cursor cursor, TextView plword, TextView engword, ImageView favouriteImageStar) {            //pokazywanie danych
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            plword.setText(cursor.getString(2));
            engword.setText(cursor.getString(1));
        }
    }

    public void initData(){                            //inicjalizacja danych
        putValuesToDatabase(0, 0, "jump", "skakać", 0);
        putValuesToDatabase(0, 1, "jump", "skakać", 0);
        putValuesToDatabase(0, 2, "jump", "skakać", 0);
        putValuesToDatabase(0, 3, "jump", "skakać", 0);
        putValuesToDatabase(0, 4, "jump", "skakać", 0);
        putValuesToDatabase(0, 5, "jump", "skakać", 0);
    }
}
