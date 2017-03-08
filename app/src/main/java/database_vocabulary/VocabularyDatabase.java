package database_vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
                + " (" + DatabaseColumnNames._ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER + " INTEGER,"
                + DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER + " INTEGER,"
                + DatabaseColumnNames.COLUMN_NAME_PLWORD + " TEXT,"
                + DatabaseColumnNames.COLUMN_NAME_ENGWORD + " TEXT,"
                + DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("AKTUALIZACJA", "Aktualizacja bazy danych z wersji: " + oldVersion + " na: " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + DatabaseColumnNames.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void putValuesToDatabase(int columnGroup, int columnItem, String engword, String plword, String favouriteImage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumnNames.COLUMN_NAME_GROUP_NUMBER, columnGroup);
        values.put(DatabaseColumnNames.COLUMN_NAME_ITEM_NUMBER, columnItem);
        values.put(DatabaseColumnNames.COLUMN_NAME_ENGWORD, engword);
        values.put(DatabaseColumnNames.COLUMN_NAME_PLWORD, plword);
        values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE, favouriteImage);

        sqLiteDatabase.insert(DatabaseColumnNames.TABLE_NAME, null, values);
    }

    public SQLiteCursor getValues() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME, null);

        return cursor;
    }

    public SQLiteCursor takeValues(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) sqLiteDatabase.query(DatabaseColumnNames.TABLE_NAME, new String[]{DatabaseColumnNames._ID, DatabaseColumnNames.COLUMN_NAME_ENGWORD, DatabaseColumnNames.COLUMN_NAME_PLWORD}, null, null, null, null, DatabaseColumnNames.COLUMN_NAME_PLWORD + " DESC");
        return cursor;
    }

    public void showVocabulary(SQLiteCursor cursor){
        while(cursor.moveToNext()){
            long id = cursor.getLong(0);
            String plword = cursor.getString(3);
            String engword = cursor.getString(4);
            String favouriteImageStar = cursor.getString(5);
        }
    }
}
