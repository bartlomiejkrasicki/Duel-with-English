package test_fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.VocabularyTest;

public class BaseTestFragments extends android.support.v4.app.Fragment{

    private VocabularyTest vocabularyTest = new VocabularyTest();

    protected void replaceFragment(FragmentTransaction fragmentTransaction){                 //show next fragment after answer
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        Bundle bundle = new Bundle();
        bundle.putInt("wordsAmount", VocabularyTest.amountOfWords);
        bundle.putInt("amountOfButtons", VocabularyTest.amountOfButtons);
        bundle.putString("lvlOfLanguage" , VocabularyTest.lvlOfLanguage);
        if (VocabularyTest.categoryName != null) {
            bundle.putString("testCategory", VocabularyTest.categoryName);
        }
        switch (VocabularyTest.randomNumber(3)){
            case 0:{
                VocabularyTestChoiceFragment choiceFragment = new VocabularyTestChoiceFragment();
                choiceFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.testFragment, choiceFragment).commit();
                break;
            }
            case 1:{
                VocabularyTestWriteFragment writeFragment = new VocabularyTestWriteFragment();
                writeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.testFragment, writeFragment).commit();
                break;
            }
            case 2:{
                VocabularyTestJigsawWordFragment jigsawWordFragment = new VocabularyTestJigsawWordFragment();
                jigsawWordFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.testFragment, jigsawWordFragment).commit();
                break;
            }
            default:{
                Toast.makeText(getActivity(), "Błąd wczytywania testu. Uruchom go jeszcze raz :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void loadNextWord(FragmentTransaction fragmentTransaction){
        VocabularyTest.manyTestWords++;
        if(VocabularyTest.manyTestWords == VocabularyTest.amountOfWords){
            endTestAlertDialog();
        } else {
            replaceFragment(fragmentTransaction);
        }
    }

    private void endTestAlertDialog(){
        final RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.testProgressBar);
        if (VocabularyTest.isTestFromLesson){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final View view = getActivity().getLayoutInflater().inflate(R.layout.test_from_lesson_custom_dialog, null);
            setTestResultIcon(view);
            TextView testResultPercent = (TextView) view.findViewById(R.id.testResultPercentDialog);
            testResultPercent.setText(String.format("%s %%", vocabularyTest.calculatePercentage()));
            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveResultsTest();
                    VocabularyTest.manyGoodAnswer = 0;
                    VocabularyTest.manyTestWords = 0;
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("Powtórz test", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    testProgressBar.setProgress(0);
                    saveAndReplayTest();
                    VocabularyTest.manyGoodAnswer = 0;
                    VocabularyTest.manyTestWords = 0;
                    getActivity().recreate();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            testProgressBar.setProgress(VocabularyTest.manyTestWords);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Test został zakończony");
            alertDialogBuilder.setMessage("Odpowiedziałeś poprawnie na " + VocabularyTest.manyGoodAnswer + " z " + VocabularyTest.amountOfWords + " pytań, co stanowi " + vocabularyTest.calculatePercentage() + "% wszystkich odpowiedzi.");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Jeszcze raz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    testProgressBar.setProgress(0);
                    VocabularyTest.manyGoodAnswer = 0;
                    VocabularyTest.manyTestWords = 0;
                    getActivity().recreate();
                }
            });
            alertDialogBuilder.setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void setTestResultIcon(View view) {
        int progressPercent = (int) vocabularyTest.calculatePercentage();
        ImageView endTestIcon = (ImageView) view.findViewById(R.id.endTestIcon);
        if (progressPercent >= 65 && progressPercent < 75 ){
            endTestIcon.setImageDrawable(getActivity().getDrawable(R.drawable.ic_crown_bronze_icon));
        } else if (progressPercent >= 75 && progressPercent < 90){
            endTestIcon.setImageDrawable(getActivity().getDrawable(R.drawable.ic_crown_silver_icon));
        } else if (progressPercent >= 90){
            endTestIcon.setImageDrawable(getActivity().getDrawable(R.drawable.ic_crown_gold_icon));
        } else {
            endTestIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void saveResultsTest() {
        saveTestResults();
        getActivity().finish();
    }

    private void saveAndReplayTest() {
        saveTestResults();
        getActivity().recreate();
    }

    private void saveTestResults(){
        VocabularyDatabase vocabularyDatabase = new VocabularyDatabase(getContext());
        vocabularyDatabase.saveTestResult(getIconResultNumber(vocabularyTest.calculatePercentage()), VocabularyTest.lvlOfLanguage, VocabularyTest.categoryName);
    }

    private int getIconResultNumber(double testResult){
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
