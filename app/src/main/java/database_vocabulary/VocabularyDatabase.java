package database_vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static database_vocabulary.DatabaseColumnNames.DATABASE_NAME;
import static database_vocabulary.DatabaseColumnNames.DATABASE_VERSION;

public class VocabularyDatabase extends SQLiteAssetHelper {

    public VocabularyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        public boolean updateValuesInDatabase(String id, int favouriteImageOn, final String TABLE_NAME) {          //umieszczanie danych w tablicy
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON, favouriteImageOn);
            sqLiteDatabase.update(TABLE_NAME, values, DatabaseColumnNames.COLUMN_NAME_ID + " = ?", new String[]{id});
            sqLiteDatabase.close();
            return true;
        }

    public Cursor getFavouriteValues(final String TABLE_NAME, final boolean isAlphabetical) {                           //pobieranie wszystkich danych
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON + " = " + 1 + " ORDER BY " + DatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON + " = " + 1, null);
        }
    }

    public Cursor getSpecificValues(final String TABLE_NAME) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getCategoryValues(final String TABLE_NAME) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor showVocabularyForLessons(final String TABLE_NAME, String categoryName, boolean isAlphabetical) {            //pokazywanie danych podczas nauki słownictwa
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'" + " ORDER BY " + DatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'", null);
        }
    }

    public Cursor showAllOfCategory (final String TABLE_NAME) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT DISTINCT " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " FROM " + TABLE_NAME, null);
    }
}
