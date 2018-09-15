package vocabulary_test;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

public class TestDataHelper {

    public static int manyGoodAnswer = 0, currentWordNumber = 0, randomNumberOfWords[];
    public static boolean inEnglish[], isTestFromLesson = true;
    public static int amountOfWords = 0, amountOfButtons = 0;
    public static String lvlOfLanguage = "", categoryName = "";
    public static Integer[] wordTable;

    public static double calculatePercentage(){
        return Math.round((manyGoodAnswer * 100)/amountOfWords);
    }

    public static void setIsEnglishTable(){
        TestDataHelper.inEnglish = new boolean[TestDataHelper.amountOfWords];
        for (int i = 0; i < TestDataHelper.inEnglish.length; i++){
            TestDataHelper.inEnglish[i] = randomBoolean();
        }
    }

    private static boolean randomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public static Integer[] prepareWordRandomTable (){
        Integer[] numberTable = new Integer[amountOfWords];
        for (int i = 0; i< amountOfWords; i++){
            numberTable[i] = i;
        }
        List<Integer> numberList = Arrays.asList(numberTable);
        Collections.shuffle(numberList);
        return numberTable;
    }

    public static void setToolbarHeader(Cursor cursor, Activity currentActivity){
    cursor.moveToPosition(currentWordNumber);
    Toolbar toolbar = (Toolbar) currentActivity.findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getString(VocabularyDatabaseColumnNames.categoryColumn));
        toolbar.setSubtitle("Postęp: " + (TestDataHelper.currentWordNumber + 1) + "/" + TestDataHelper.amountOfWords);
    }

    public static void setTestHint(int enHint, int plHint, Activity currentActivity) {
        TextView hintText = (TextView) currentActivity.findViewById(R.id.testHint);
        if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
            hintText.setText(enHint);
        } else {
            hintText.setText(plHint);
        }
    }
}
