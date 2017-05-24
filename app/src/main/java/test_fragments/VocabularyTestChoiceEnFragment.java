package test_fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumber;

public class VocabularyTestChoiceEnFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private String answerText;
    Button[] guessButtons = new Button[8];
    VocabularyTest vocabularyTest;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocabulary_test_choice_en_to_pl_fragment, container, false);
        vocabularyTest = new VocabularyTest();
        VocabularyDatabase database = new VocabularyDatabase(getContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        addWords(vocabularyTest, database, sharedPreferences, view);
        database.close();
        return view;
    }

    private void addWords(VocabularyTest vocabularyTest, VocabularyDatabase database, SharedPreferences sharedPreferences, View view){
        TextView guessWord = (TextView) view.findViewById(R.id.testWordChoice);
        guessButtons[0] = (Button) view.findViewById(R.id.testChoiceEnOption1);
        guessButtons[1] = (Button) view.findViewById(R.id.testChoiceEnOption2);
        guessButtons[2] = (Button) view.findViewById(R.id.testChoiceEnOption3);
        guessButtons[3] = (Button) view.findViewById(R.id.testChoiceEnOption4);
        guessButtons[4] = (Button) view.findViewById(R.id.testChoiceEnOption5);
        guessButtons[5] = (Button) view.findViewById(R.id.testChoiceEnOption6);
        guessButtons[6] = (Button) view.findViewById(R.id.testChoiceEnOption7);
        guessButtons[7] = (Button) view.findViewById(R.id.testChoiceEnOption8);

        Cursor cursor = database.getGroupValues(vocabularyTest.getSPlevelOfLanguage(sharedPreferences));
        boolean[] itWasDrawn = new boolean[cursor.getCount()];
        for (int i = 0; i<itWasDrawn.length; i++){
            itWasDrawn[i] = false;
        }
        int tempRandomNumber = randomNumber(cursor.getCount());
        itWasDrawn[tempRandomNumber] = true;
        cursor.moveToPosition(tempRandomNumber);
        guessWord.setText(cursor.getString(4));
        tempRandomNumber = randomNumber(8);
        guessButtons[tempRandomNumber].setText(cursor.getString(3));
        guessButtons[tempRandomNumber].setTag(1);
        answerText = cursor.getString(3);

        for (Button guessButton : guessButtons) {
            guessButton.setOnClickListener(this);
            do {
                tempRandomNumber = randomNumber(cursor.getCount());
            } while (itWasDrawn[tempRandomNumber]);
            if (guessButton.getTag() == null || guessButton.getTag().equals(0)) {
                cursor.moveToPosition(tempRandomNumber);
                guessButton.setText(cursor.getString(3));
                guessButton.setTag(1);
                itWasDrawn[tempRandomNumber] = true;
            }
        }
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        for (Button currentButton : guessButtons){
            currentButton.setClickable(false);
        }

        final Button thisButton = (Button) view;
        String buttonText = thisButton.getText().toString();

        if (buttonText.equals(answerText)) {
            manyGoodAnswer++;
            thisButton.setBackgroundResource(R.drawable.good_answer_change_color);
            Animation trueAnswer = AnimationUtils.loadAnimation(getContext(), R.anim.correct_answer_test);
            trueAnswer.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                }

                @Override
                public void onAnimationEnd(Animation animation){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadNextWord();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            thisButton.startAnimation(trueAnswer);
        }
        else {
            thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
            Animation falseAnswer = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test);
            falseAnswer.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                }

                @Override
                public void onAnimationEnd(Animation animation){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadNextWord();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            thisButton.startAnimation(falseAnswer);
        }
    }

    private void loadNextWord(){
        manyTestWords++;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(manyTestWords == vocabularyTest.getSPnumberOfWords(sharedPreferences)){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Test został zakończony");
            alertDialogBuilder.setMessage("Odpowiedziałeś poprawnie na " + manyGoodAnswer + " z " + vocabularyTest.getSPnumberOfWords(sharedPreferences) + " pytań.");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Jeszcze raz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
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
        } else {
            replaceFragment();
        }
    }

    private void replaceFragment(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(4)){
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
            default:{
                Toast.makeText(getContext(), "Przełączanie widoku na nowy nie zadziałało :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
