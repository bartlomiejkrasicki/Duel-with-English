package vocabulary_test;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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

    public static int manyGoodAnswer = 0, manyTestWords = 0, randomNumberOfWords[];
    public static boolean inEnglish[], isTestFromLesson;
    private Cursor cursor;
    private VocabularyDatabase vocabularyDatabase;
    public static int amountOfWords, amountOfButtons;
    public static String lvlOfLanguage = "", categoryName = "";
    private RoundCornerProgressBar testProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        setDataFromIntent();
        declarationVariables();
        showFirstFragment();
    }

    private void setDataFromIntent(){
        amountOfWords = getIntent().getIntExtra("wordsAmount", 5);
        amountOfButtons = getIntent().getIntExtra("amountOfButtons", 6);
        lvlOfLanguage = getIntent().getStringExtra("lvlOfLanguage");
        categoryName = getIntent().getStringExtra("testCategory");
        isTestFromLesson = getIntent().getBooleanExtra("isTestFromLesson", false);
    }

    private void declarationVariables() {
        vocabularyDatabase = getVocabularyDatabase(getApplicationContext());
        if (categoryName != null) {                                             // do przerobienia
            cursor = vocabularyDatabase.getCategoryValues(categoryName, lvlOfLanguage);
        } else {
            cursor = vocabularyDatabase.getAllValues();
        }
        if (cursor.getCount() < amountOfWords){
            amountOfWords = cursor.getCount();
        }
        if (cursor.getCount() < amountOfButtons){
            amountOfButtons = cursor.getCount() - cursor.getCount()%2;
        }
        randomNumberOfWords = new int[amountOfWords];
        randomNumberOfWords = randomWordWithoutReply(randomNumberOfWords);
        setIsEnglishTable();
        setToolbar();
        setProgressBar();
    }

    private void showFirstFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        Bundle bundle = new Bundle();
        bundle.putInt("wordsAmount", amountOfWords);
        bundle.putInt("amountOfButtons", amountOfButtons);
        bundle.putString("lvlOfLanguage" , lvlOfLanguage);
        if (categoryName != null) {
            bundle.putString("testCategory", categoryName);
        }
        switch (randomNumber(3)){
            case 0:{
                VocabularyTestChoiceFragment choiceFragment = new VocabularyTestChoiceFragment();
                choiceFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.testFragment, choiceFragment).commit();
                break;
            }
            case 1:{
                VocabularyTestWriteFragment writeFragment = new VocabularyTestWriteFragment();
                writeFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.testFragment, writeFragment).commit();
                break;
            }
            case 2:{
                VocabularyTestJigsawWordFragment jigsawWordFragment = new VocabularyTestJigsawWordFragment();
                jigsawWordFragment.setArguments(bundle);
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

    public static boolean randomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }

    private void setIsEnglishTable(){
        inEnglish = new boolean[amountOfWords];
        for (int i = 0; i < inEnglish.length; i++){
            inEnglish[i] = randomBoolean();
        }
    }

    public double calculatePercentage(){
        return Math.round((VocabularyTest.manyGoodAnswer * 100)/VocabularyTest.amountOfWords);
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
        testProgressBar.setMax(amountOfWords);
        testProgressBar.setProgress(0);
    }

    public VocabularyDatabase getVocabularyDatabase(Context context){
        return new VocabularyDatabase(context);
    }

    public Cursor getCursor(VocabularyDatabase vocabularyDatabase) {
        if (categoryName != null) {
            return vocabularyDatabase.getCategoryValues(categoryName, lvlOfLanguage);
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

    private int [] randomWordWithoutReply(int [] randomWordsWithoutReply){                //words to create test
        Random random = new Random();
        boolean numberIsOther;
        for(int i = 0; i < randomWordsWithoutReply.length; i++){
            if(i==0){
                randomWordsWithoutReply[i] = random.nextInt(cursor.getCount());
            }
            else {
                do {
                    numberIsOther = true;
                    randomWordsWithoutReply[i] = random.nextInt(cursor.getCount());
                    for(int j = 0; j < i; j++){
                        if(randomWordsWithoutReply[j]==randomWordsWithoutReply[i]){
                            numberIsOther = true;
                            break;
                        } else {
                           numberIsOther = false;
                        }
                    }
                } while (numberIsOther);
            }
        }
    cursor.close();
    return randomWordsWithoutReply;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manyGoodAnswer = 0;
        manyTestWords = 0;
        cursor.close();
        vocabularyDatabase.close();
    }

    @Override
    public void recreate() {
        super.recreate();
        testProgressBar.setProgress(0);
        manyGoodAnswer = 0;
        manyTestWords = 0;
        cursor.close();
        vocabularyDatabase.close();
    }

    public static int getIconResultNumber(double testResult){
        if (testResult >= 65 && testResult < 75 ){
            return 1;
        } else if (testResult >= 75 && testResult < 90){
            return 2;
        } else if (testResult >= 90){
            return 3;
        } else {
            return 0;
        }
    }
}
