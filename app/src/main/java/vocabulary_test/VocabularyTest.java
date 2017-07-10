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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import test_fragments.VocabularyTestChoiceEnFragment;
import test_fragments.VocabularyTestChoicePlFragment;
import test_fragments.VocabularyTestJigsawWordEnFragment;
import test_fragments.VocabularyTestJigsawWordPlFragment;
import test_fragments.VocabularyTestWriteEnFragment;
import test_fragments.VocabularyTestWritePlFragment;

public class VocabularyTest extends FragmentActivity {

    public static int manyGoodAnswer = 0;
    public static int manyTestWords = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        showFirstFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manyGoodAnswer = 0;
        manyTestWords = 0;
    }

    @Override
    public void recreate() {
        super.recreate();
        manyGoodAnswer = 0;
        manyTestWords = 0;
    }

    private void showFirstFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(6)){
            case 0:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestChoiceEnFragment()).commit();
                break;
            }
            case 1:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestChoicePlFragment()).commit();
                break;
            }
            case 2:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestWriteEnFragment()).commit();
                break;
            }
            case 3:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestWritePlFragment()).commit();
                break;
            }
            case 4:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestJigsawWordEnFragment()).commit();
                break;
            }
            case 5:{
                fragmentTransaction.add(R.id.testFragmentId, new VocabularyTestJigsawWordPlFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Przełączanie widoku na nowy nie zadziałało :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void replaceFragment(FragmentTransaction fragmentTransaction){                 //show next fragment after answer
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(6)){
            case 0:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestChoiceEnFragment()).commit();
                break;
            }
            case 1:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestChoicePlFragment()).commit();
                break;
            }
            case 2:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestWriteEnFragment()).commit();
                break;
            }
            case 3:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestWritePlFragment()).commit();
                break;
            }
            case 4:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestJigsawWordEnFragment()).commit();
                break;
            }
            case 5:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestJigsawWordPlFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Przełączanie widoku na nowy nie zadziałało :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadNextWord(FragmentTransaction fragmentTransaction, Context context, VocabularyDatabase vocabularyDatabase, Cursor cursor, Activity activity){
        vocabularyDatabase.close();
        cursor.close();
        manyTestWords++;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(manyTestWords == getSPnumberOfWords(sharedPreferences)){
            endTestAlertDialog(context, activity);
        } else {
            replaceFragment(fragmentTransaction);
        }
    }

    public void endTestAlertDialog(Context context, final Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Test został zakończony");
        alertDialogBuilder.setMessage("Odpowiedziałeś poprawnie na " + manyGoodAnswer + " z " + getSPnumberOfWords(sharedPreferences) + " pytań.");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Jeszcze raz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.recreate();
            }
        });
        alertDialogBuilder.setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
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

    public VocabularyDatabase getVocabularyDatabase(Context context){
        return new VocabularyDatabase(context);
    }

    public Cursor getCursor(Context context, VocabularyDatabase vocabularyDatabase) {
        return vocabularyDatabase.getGroupValues(getSPlevelOfLanguage(getSharedPreferences(context)));
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

    public void hideKeyboard(Context context){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException exception){
            Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_SHORT).show();
        }
    }
}
