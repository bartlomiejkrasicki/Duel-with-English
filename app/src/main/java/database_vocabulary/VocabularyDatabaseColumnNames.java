package database_vocabulary;

import android.provider.BaseColumns;

public interface VocabularyDatabaseColumnNames extends BaseColumns {

    String DATABASE_NAME = "vocabularydb.db";
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_LANGUAGELVL = "languagelvl";
    String COLUMN_NAME_PLWORD = "plword";
    String COLUMN_NAME_ENGWORD = "enword";
    String COLUMN_NAME_ISFAVOURITE = "isfavourite";
    String COLUMN_NAME_CATEGORY = "category";
    String TABLE_NAME_VOCABULARY = "Vocabulary";
    String TABLE_NAME_CATEGORY = "Category";

    int DATABASE_OLDVERSION = 1;
    int DATABASE_VERSION = 2;
    int idColumn = 0;
    int lvlColumn = 1;
    int categoryColumn = 2;
    int enwordColumn = 3;
    int plwordColumn = 4;
    int isfavouriteColumn = 5;
}
