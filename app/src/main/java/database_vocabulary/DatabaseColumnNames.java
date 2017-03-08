package database_vocabulary;

import android.provider.BaseColumns;

public interface DatabaseColumnNames extends BaseColumns {

    String DATABASE_NAME = "vocabulary_database.db";
    int DATABASE_VERSION = 1;
    String COLUMN_NAME_PLWORD = "plword";
    String COLUMN_NAME_ENGWORD = "engword";
    String COLUMN_NAME_FAVOURITE_IMAGE_ON = "favouriteimage";
    String TABLE_NAME = "vocabulary_table";
    String COLUMN_NAME_GROUP_NUMBER = "group_number";
    String COLUMN_NAME_ITEM_NUMBER = "item_number";
}
