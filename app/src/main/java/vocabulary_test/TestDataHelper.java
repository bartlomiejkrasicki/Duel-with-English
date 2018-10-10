package vocabulary_test;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

public class TestDataHelper {

    public static int manyGoodAnswer = 0, currentWordNumber = 0;
    public static boolean inEnglish, isTestFromLesson = true;
    public static int amountOfWords = 0, amountOfButtons = 0;
    public static String lvlOfLanguage = "", categoryName = "";
    public static ArrayList<Integer> wordTable;

    public static double calculatePercentage(){
        return Math.round((manyGoodAnswer * 100)/amountOfWords);
    }

    public static ArrayList<Integer> prepareWordRandomTable (){
        ArrayList<Integer> numberList = new ArrayList<>();
        for (int i = 0; i< amountOfWords; i++) {
            numberList.add(i);
        }
        Collections.shuffle(numberList);
        return numberList;
    }

    public static void setToolbarHeader(Cursor cursor, Activity currentActivity){
        cursor.moveToPosition(currentWordNumber);
        Toolbar toolbar = (Toolbar) currentActivity.findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getString(VocabularyDatabaseColumnNames.categoryColumn));
        toolbar.setSubtitle("Postęp: " + (currentWordNumber + 1) + "/" + amountOfWords);
    }

    public static void setTestHint(int enHint, int plHint, Activity currentActivity) {
        TextView hintText = (TextView) currentActivity.findViewById(R.id.testHint);
        if (inEnglish) {
            hintText.setText(enHint);
        } else {
            hintText.setText(plHint);
        }
    }

    public static void cleanVariablesAfterFinish(){
        manyGoodAnswer = 0;
        currentWordNumber = 0;
        categoryName = "";
        amountOfWords = 0;
        lvlOfLanguage = "";
        wordTable.clear();
    }

    public static void cleanVariablesAfterRecreate(){
        manyGoodAnswer = 0;
        currentWordNumber = 0;
        wordTable.clear();
    }

    public static void setProgressBar(Activity currentActivity){
        RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) currentActivity.findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(TestDataHelper.currentWordNumber);
    }

    public static void inEnglishSetRandom() {
        Random random = new Random();
        inEnglish = random.nextBoolean();
    }

    public static int getRandomNumber(int i){
        Random random = new Random();
        return random.nextInt(i);
    }

    public static void prepareView(Cursor cursor, Activity activity){
        TestDataHelper.setToolbarHeader(cursor, activity);
        TestDataHelper.setTestHint(R.string.test_write_en_hint, R.string.test_write_pl_hint, activity);
        TestDataHelper.setProgressBar(activity);
    }
}
