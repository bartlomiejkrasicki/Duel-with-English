package database_vocabulary;

public interface CategoryDatabaseColumnNames extends DatabaseColumnNames {

    String CAT_COLUMN_NAME_LANGUAGELVL = "languageLvl";
    String CAT_COLUMN_NAME_CATEGORY = "category";
    String CAT_COLUMN_NAME_TESTRESULT = "testResultNumber";

    int idColumn = 0;
    int lvlColumn = 1;
    int categoryColumn = 2;
    int testResultNumber = 3;
}
