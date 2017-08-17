package vocabulary_test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.Random;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import test_fragments.VocabularyTestChoiceFragment;
import test_fragments.VocabularyTestJigsawWordFragment;
import test_fragments.VocabularyTestWriteFragment;

public class VocabularyTest extends FragmentActivity {

    public static int manyGoodAnswer = 0;
    public static int manyTestWords = 0;
    public static int randomNumberOfWords[], inEnglish[];
    private RoundCornerProgressBar testProgressBar;
    private Cursor cursor;
    private VocabularyDatabase vocabularyDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        declarationVariables();
        showFirstFragment();
    }

    private void declarationVariables() {
        vocabularyDatabase = getVocabularyDatabase(getApplicationContext());
        cursor = getCursor(getApplicationContext(), vocabularyDatabase);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        randomNumberOfWords = new int[getSPnumberOfWords(sharedPreferences)];
        randomNumberOfWords = randomWordWithoutReply(randomNumberOfWords, cursor);
        inEnglish = new int[getSPnumberOfWords(sharedPreferences)];
        for (int i = 0; i < inEnglish.length; i++){
            inEnglish[i] = randomNumber(2);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.testVocabularyToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        testProgressBar = (RoundCornerProgressBar) findViewById(R.id.testProgressBar);
        testProgressBar.setMax(getSPnumberOfWords(sharedPreferences));
        testProgressBar.setProgress(3);
    }

    private void showFirstFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(3)){
            case 0:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestChoiceFragment()).commit();
                break;
            }
            case 1:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestWriteFragment()).commit();
                break;
            }
            case 2:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestJigsawWordFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Błąd wczytywania testu. Uruchom go jeszcze raz :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void replaceFragment(FragmentTransaction fragmentTransaction){                 //show next fragment after answer
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(3)){
            case 0:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestChoiceFragment()).commit();
                break;
            }
            case 1:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestWriteFragment()).commit();
                break;
            }
            case 2:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestJigsawWordFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Błąd wczytywania testu. Uruchom go jeszcze raz :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadNextWord(FragmentTransaction fragmentTransaction, Context context){
        manyTestWords++;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(manyTestWords == getSPnumberOfWords(sharedPreferences)){
            endTestAlertDialog(context);
        } else {
            replaceFragment(fragmentTransaction);
        }
    }

    public void endTestAlertDialog(final Context context){
        testProgressBar.setProgress(testProgressBar.getMax());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Test został zakończony");
        alertDialogBuilder.setMessage("Odpowiedziałeś poprawnie na " + manyGoodAnswer + " z " + getSPnumberOfWords(sharedPreferences) + " pytań.");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Jeszcze raz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity activity = (VocabularyTest) context;
                activity.recreate();
            }
        });
        alertDialogBuilder.setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity activity1 = (VocabularyTest) context;
                activity1.finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    public int getSPnumberOfWords(SharedPreferences sharedPreferences) {
        return Integer.parseInt(sharedPreferences.getString("numberOfWords", ""));
    }

    public int getSPlevelOfLanguage(SharedPreferences sharedPreferences){
        return Integer.parseInt(sharedPreferences.getString("levelOfLanguage", ""));
    }

    public int getSPamountOfButtons(SharedPreferences sharedPreferences){
        return Integer.parseInt(sharedPreferences.getString("amountOfButtons", ""));
    }

    public VocabularyDatabase getVocabularyDatabase(Context context){
        return new VocabularyDatabase(context);
    }

    public Cursor getCursor(Context context, VocabularyDatabase vocabularyDatabase) {
        return vocabularyDatabase.getGroupValues(getSPlevelOfLanguage(getSharedPreferences(context)), DatabaseColumnNames.TABLE_NAME_A1);
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
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

    public int [] randomWordWithoutReply(int [] randomWordsWithoutReply, Cursor cursor){                //words to create test
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


    public int [] setRandomTableNumber(int maxRangeNumber){          //shuffle numbers and add to table of int
        int [] tableRandomNumbers = new int[maxRangeNumber];
        Random random = new Random();
        boolean numberIsOther;
        for(int i = 0; i < tableRandomNumbers.length; i++){
            if(i==0){
                tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
            }
            else {
                do {
                    numberIsOther = true;
                    tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                    for(int j = 0; j < i; j++){
                        if(tableRandomNumbers[j]==tableRandomNumbers[i]){
                            numberIsOther = true;
                            break;
                        } else {
                            numberIsOther = false;
                        }
                    }
                } while (numberIsOther);
            }
        }
        return tableRandomNumbers;
    }

    public int [] setRandomTableNumber(int maxRangeNumber, int index){          //shuffle numbers and add to table of int, class check reply words
        int [] tableRandomNumbers = new int[7];
        Random random = new Random();
        boolean numberIsntOther;
        for(int i = 0; i < tableRandomNumbers.length; i++){
            if(i==0){                                                                                       //add first value
                do {
                    tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                } while (tableRandomNumbers[i] == index);
            }
            else {                                                                                          //add other values
                do {
                    do {
                        tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                    } while (tableRandomNumbers[i] == index);
                    numberIsntOther = false;
                    for(int j = 0; j < i; j++){
                        if(tableRandomNumbers[j]==tableRandomNumbers[i]){
                            numberIsntOther = true;
                        }
                    }
                } while (numberIsntOther);
            }
        }
        return tableRandomNumbers;
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
        manyGoodAnswer = 0;
        manyTestWords = 0;
        cursor.close();
        vocabularyDatabase.close();

    }
}
