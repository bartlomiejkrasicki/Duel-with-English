package database_vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static database_vocabulary.DatabaseColumnNames.DATABASE_NAME;
import static database_vocabulary.DatabaseColumnNames.DATABASE_VERSION;
import static database_vocabulary.DatabaseColumnNames.TABLE_NAME_CATEGORY;
import static database_vocabulary.DatabaseColumnNames.TABLE_NAME_VOCABULARY;

public class VocabularyDatabase extends SQLiteAssetHelper {

    public VocabularyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void allCategoryFromVocabulary() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + DatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + "," + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " FROM " + TABLE_NAME_VOCABULARY, null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put(CategoryDatabaseColumnNames.CAT_COLUMN_NAME_LANGUAGELVL, cursor.getString(0));
                values.put(CategoryDatabaseColumnNames.CAT_COLUMN_NAME_CATEGORY, cursor.getString(1));
                sqLiteDatabase.insert(TABLE_NAME_CATEGORY, null, values);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public void updateValuesInDatabase(String id, int favouriteImageOn) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumnNames.COLUMN_NAME_ISFAVOURITE, favouriteImageOn);
        sqLiteDatabase.update(DatabaseColumnNames.TABLE_NAME_VOCABULARY, values, DatabaseColumnNames.COLUMN_NAME_ID + " = ?", new String[]{id});
        sqLiteDatabase.close();
    }

    public Cursor getFavouriteValues(String vocabularyLevel, boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        final int isFavouriteWord = 1;
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLevel + "' AND " + DatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = " + isFavouriteWord + " ORDER BY " + DatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLevel + "' AND " + DatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = " + isFavouriteWord,  null);
        }
    }

    public Cursor getAllValues() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY, null);
    }

    public void saveTestResult(int result, String lvlLanguage, String categoryName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, testResultNumber FROM Category WHERE category = '" + categoryName + "' AND languageLvl = '" + lvlLanguage + "'", null);
        cursor.moveToFirst();
        int id = cursor.getInt(CategoryDatabaseColumnNames.idColumn);
        if (isBetterResultTest(result, cursor.getInt(1))) {
            sqLiteDatabase.execSQL("UPDATE " + TABLE_NAME_CATEGORY + " SET " + CategoryDatabaseColumnNames.CAT_COLUMN_NAME_TESTRESULT + " = " + result + " WHERE " + CategoryDatabaseColumnNames.COLUMN_NAME_ID + " = " + id);
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    private boolean isBetterResultTest(int newResult, int oldResult){
        return newResult > oldResult;
    }

    public Cursor getCategoryValues(final String categoryName) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'", null);
    }

    public Cursor getAllFavouriteValues(boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical) {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = 1  ORDER BY " + DatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = 1",null);
        }
    }

    public Cursor showVocabularyForLessons(String vocabularyLvl, String categoryName, boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "' AND " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'" + " ORDER BY " + DatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME_VOCABULARY + " WHERE " + DatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "' AND " + DatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'", null);
        }
    }

    public Cursor showAllOfCategory (String vocabularyLvl) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.TABLE_NAME_CATEGORY + " WHERE " + CategoryDatabaseColumnNames.CAT_COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "'", null);
    }
}
