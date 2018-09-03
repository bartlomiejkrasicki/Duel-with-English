package database_vocabulary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static database_vocabulary.VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL;
import static database_vocabulary.VocabularyDatabaseColumnNames.DATABASE_NAME;
import static database_vocabulary.VocabularyDatabaseColumnNames.DATABASE_VERSION;
import static database_vocabulary.VocabularyDatabaseColumnNames.TABLE_NAME_CATEGORY;
import static database_vocabulary.VocabularyDatabaseColumnNames.TABLE_NAME_VOCABULARY;

public class VocabularyDatabase extends SQLiteAssetHelper {

    @SuppressLint("StaticFieldLeak")
    private static VocabularyDatabase dbInstance = null;

    private VocabularyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static VocabularyDatabase getInstance(Context context){
        if(dbInstance == null){
            dbInstance = new VocabularyDatabase(context);
        }
        return dbInstance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void allCategoryFromVocabulary() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + "," + VocabularyDatabaseColumnNames.COLUMN_NAME_CATEGORY + " FROM " + TABLE_NAME_VOCABULARY, null);
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
        values.put(VocabularyDatabaseColumnNames.COLUMN_NAME_ISFAVOURITE, favouriteImageOn);
        sqLiteDatabase.update(VocabularyDatabaseColumnNames.TABLE_NAME_VOCABULARY, values, VocabularyDatabaseColumnNames.COLUMN_NAME_ID + " = ?", new String[]{id});
        sqLiteDatabase.close();
    }

    public Cursor getRowFromId(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_ID + " = '" + id + "'",null);
    }

    public Cursor getFavouriteValues(String vocabularyLevel, boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        final int isFavouriteWord = 1;
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLevel + "' AND " + VocabularyDatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = " + isFavouriteWord + " ORDER BY " + VocabularyDatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLevel + "' AND " + VocabularyDatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = " + isFavouriteWord,  null);
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

    public Cursor getCategoryValues(final String categoryName, final String lvlLanguage) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "' AND " + COLUMN_NAME_LANGUAGELVL + " = '" + lvlLanguage + "'", null);
    }

    public Cursor getValuesToSearch(final boolean plToEnTranslate, final String textQuery) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (plToEnTranslate){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_PLWORD + " LIKE '" + textQuery + "%'", null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_ENGWORD + " LIKE '" + textQuery + "%'", null);
        }
    }

    public Cursor getAllFavouriteValues(boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical) {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = 1  ORDER BY " + VocabularyDatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_ISFAVOURITE + " = 1",null);
        }
    }

    public Cursor showVocabularyForLessons(String vocabularyLvl, String categoryName, boolean isAlphabetical) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (isAlphabetical){
            return sqLiteDatabase.rawQuery("SELECT * FROM " + VocabularyDatabaseColumnNames.TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "' AND " + VocabularyDatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'" + " ORDER BY " + VocabularyDatabaseColumnNames.COLUMN_NAME_ENGWORD, null);
        } else {
            return sqLiteDatabase.rawQuery("SELECT * FROM " + VocabularyDatabaseColumnNames.TABLE_NAME_VOCABULARY + " WHERE " + VocabularyDatabaseColumnNames.COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "' AND " + VocabularyDatabaseColumnNames.COLUMN_NAME_CATEGORY + " = '" + categoryName + "'", null);
        }
    }

    public Cursor showAllOfCategory (String vocabularyLvl) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + VocabularyDatabaseColumnNames.TABLE_NAME_CATEGORY + " WHERE " + CategoryDatabaseColumnNames.CAT_COLUMN_NAME_LANGUAGELVL + " = '" + vocabularyLvl + "'", null);
    }
}
