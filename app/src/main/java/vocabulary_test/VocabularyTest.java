package vocabulary_test;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import test_fragments.VocabularyTestChoiceFragment;
import test_fragments.VocabularyTestJigsawWordFragment;
import test_fragments.VocabularyTestWriteFragment;

public class VocabularyTest extends FragmentActivity {

    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private RoundCornerProgressBar testProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        declarationVariables();
        showFirstFragment();
    }

    private void declarationVariables() {
        if (TestDataHelper.isTestFromLesson){
            dbInstance = VocabularyDatabase.getInstance(getApplicationContext());

            if (TestDataHelper.categoryName.equals("")) {                                             // do przerobienia
                cursor = dbInstance.getAllValues();
            } else {
                cursor = dbInstance.getCategoryValues(TestDataHelper.categoryName, TestDataHelper.lvlOfLanguage);
            }

            if (TestDataHelper.amountOfButtons != 0) {
                TestDataHelper.amountOfWords = getAmountOfWords(cursor.getCount());
            }

            if (cursor.getCount() < TestDataHelper.amountOfButtons){
                TestDataHelper.amountOfButtons = cursor.getCount() - cursor.getCount()%2;
            }
        }
        TestDataHelper.currentWordNumber = 0;
        Log.d("words", TestDataHelper.amountOfWords + "");
        Log.d("buttons", TestDataHelper.amountOfButtons + "");
        Log.d("lvl", TestDataHelper.lvlOfLanguage + "");
        Log.d("category", TestDataHelper.categoryName + "");
        TestDataHelper.randomNumberOfWords = new int[TestDataHelper.amountOfWords];
        TestDataHelper.wordTable = TestDataHelper.prepareWordRandomTable();
        TestDataHelper.setIsEnglishTable();
        setToolbar();
        setProgressBar();
    }

    private void showFirstFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(3)){
            case 0:{
                VocabularyTestChoiceFragment choiceFragment = new VocabularyTestChoiceFragment();
                fragmentTransaction.add(R.id.testFragment, choiceFragment).commit();
                break;
            }
            case 1:{
                VocabularyTestWriteFragment writeFragment = new VocabularyTestWriteFragment();
                fragmentTransaction.add(R.id.testFragment, writeFragment).commit();
                break;
            }
            case 2:{
                VocabularyTestJigsawWordFragment jigsawWordFragment = new VocabularyTestJigsawWordFragment();
                fragmentTransaction.add(R.id.testFragment, jigsawWordFragment).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Błąd wczytywania testu. Uruchom go jeszcze raz :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static int randomNumber(int i){
        if(i==0){
            return 1;
        }
        else {
            Random random = new Random();
            return random.nextInt(i);
        }
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.testVocabularyToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setProgressBar(){
        testProgressBar = (RoundCornerProgressBar) findViewById(R.id.testProgressBar);
        testProgressBar.setMax(TestDataHelper.amountOfWords);
        testProgressBar.setProgress(0);
    }

    public Cursor getCursor(VocabularyDatabase vocabularyDatabase) {
        if (TestDataHelper.categoryName != null) {
            return vocabularyDatabase.getCategoryValues(TestDataHelper.categoryName, TestDataHelper.lvlOfLanguage);
        }
        else {
            return vocabularyDatabase.getAllValues();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Czy chcesz zakończyć test i utracić wszystkie postępy?");
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int getAmountOfWords(int itemsCount){
        if (itemsCount <= 5){
            return itemsCount;
        }
        else if (itemsCount <= 10 && itemsCount > 5){
            return 5;
        }
        else if (itemsCount > 10 && itemsCount <=20){
            return 10;
        }
        else {
            return 15;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TestDataHelper.manyGoodAnswer = 0;
        TestDataHelper.currentWordNumber = 0;
        cursor.close();
        dbInstance.close();
    }

    @Override
    public void recreate() {
        super.recreate();
        testProgressBar.setProgress(0);
        TestDataHelper.manyGoodAnswer = 0;
        TestDataHelper.currentWordNumber = 0;
        cursor.close();
        dbInstance.close();
    }
}
