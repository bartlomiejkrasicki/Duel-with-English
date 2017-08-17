package database_vocabulary;

public class VocabularyDatabaseAssetHelper2 {

//    private VocabularyDatabaseAssetHelper assetHelper;
//    private SQLiteDatabase database;
//
//    public VocabularyDatabaseAssetHelper2(Context context) {
//        this.assetHelper = new VocabularyDatabaseAssetHelper(context);
//    }
//
//    public void close() {
//        if (database != null) {
//            this.database.close();
//        }
//    }
//
//    public boolean updateValuesInDatabase(String id, int favouriteImageOn, final String TABLE_NAME) {          //umieszczanie danych w tablicy
//        SQLiteDatabase sqLiteDatabase = assetHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseColumnNames.COLUMN_NAME_FAVOURITE_IMAGE_ON, favouriteImageOn);
//        sqLiteDatabase.update(TABLE_NAME, values, DatabaseColumnNames._ID + " = ?", new String[]{id});
//        sqLiteDatabase.close();
//        return true;
//    }
//
//    public Cursor getFavouriteValues(final String TABLE_NAME) {                           //pobieranie wszystkich danych
//        SQLiteDatabase sqLiteDatabase = assetHelper.getReadableDatabase();
//        final int addedToFavourite = 1;
//        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//    }
//
//    public Cursor getSpecificValues(int i, int i1, final String TABLE_NAME) {
//        SQLiteDatabase sqLiteDatabase = assetHelper.getReadableDatabase();
//        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//    }
//
//    public Cursor getGroupValues(int i, final String TABLE_NAME) {
//        SQLiteDatabase sqLiteDatabase = assetHelper.getReadableDatabase();
//        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//    }
//
//    public void showVocabularyForLessons(Cursor cursor, TextView plword, TextView engword, ImageView favouriteImageStar, int position) {            //pokazywanie danych podczas nauki słownictwa
//        if (cursor.moveToPosition(position)) {
//            plword.setText(cursor.getString(3));
//            engword.setText(cursor.getString(4));
//            int addFavOrNot = cursor.getInt(5);
//            if (addFavOrNot == 1) {
//                favouriteImageStar.setImageResource(android.R.drawable.star_big_on);
//            } else
//                favouriteImageStar.setImageResource(android.R.drawable.star_big_off);
//        }
//    }
//
//    public void showVocabularyForFavourite(Cursor cursor, TextView plword, TextView engword, int position) {            //pokazywanie danych dla listy ulubionych
//        if (cursor.moveToPosition(position)) {
//            plword.setText(cursor.getString(3));
//            engword.setText(cursor.getString(4));
//        }
//        cursor.close();
//    }
//
//    public Cursor showAllCategories() {
//        SQLiteDatabase sqLiteDatabase = assetHelper.getReadableDatabase();
//        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseColumnNames.COLUMN_CATEGORY, null);
//    }
//

}