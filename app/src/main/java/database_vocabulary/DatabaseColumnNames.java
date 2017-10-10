package database_vocabulary;

import android.provider.BaseColumns;

public interface DatabaseColumnNames extends BaseColumns {

    String DATABASE_NAME = "vocabularydb.db";
    int DATABASE_VERSION = 1;
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_PLWORD = "plword";
    String COLUMN_NAME_ENGWORD = "enword";
    String COLUMN_NAME_FAVOURITE_IMAGE_ON = "isfavourite";
    String COLUMN_NAME_CATEGORY = "category";
    String TABLE_NAME = "vocabulary_table";
    String TABLE_NAME_A1 = "A1";
    String TABLE_NAME_A2 = "A2";
    String TABLE_NAME_B1 = "B1";
    String TABLE_NAME_B2 = "B2";
    String TABLE_NAME_C1 = "C1";
    String TABLE_NAME_C2 = "C2";
    int idColumn = 0;
    int categoryColumn = 1;
    int enwordColumn = 2;
    int plwordColumn = 3;
    int isfavouriteColumn = 4;
}
