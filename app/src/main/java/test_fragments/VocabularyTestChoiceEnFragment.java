package test_fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumberOfWords;

public class VocabularyTestChoiceEnFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private String answerText;
    private Button[] guessButtons = new Button[8];
    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private VocabularyDatabase vocabularyDatabase;
    private SharedPreferences sharedPreferences;
    private int goodAnswer;
    private int numberOfWord;

    public VocabularyTestChoiceEnFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocabulary_test_choice_en_to_pl_fragment, container, false);
        declarationVariables(view);
        setToolbar();
        addWords(view);
        return view;
    }

    private void declarationVariables(View view){                          //declaration layout elements and variables
        numberOfWord = randomNumberOfWords[manyTestWords];
        guessButtons[0] = (Button) view.findViewById(R.id.testChoiceEnOption1);
        guessButtons[1] = (Button) view.findViewById(R.id.testChoiceEnOption2);
        guessButtons[2] = (Button) view.findViewById(R.id.testChoiceEnOption3);
        guessButtons[3] = (Button) view.findViewById(R.id.testChoiceEnOption4);
        guessButtons[4] = (Button) view.findViewById(R.id.testChoiceEnOption5);
        guessButtons[5] = (Button) view.findViewById(R.id.testChoiceEnOption6);
        guessButtons[6] = (Button) view.findViewById(R.id.testChoiceEnOption7);
        guessButtons[7] = (Button) view.findViewById(R.id.testChoiceEnOption8);
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = new VocabularyDatabase(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        cursor = vocabularyDatabase.getGroupValues(vocabularyTest.getSPlevelOfLanguage(sharedPreferences));
    }

    private void setToolbar(){
        cursor.moveToPosition(numberOfWord);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getInt(2));
        toolbar.setSubtitle("Postęp: " + (manyTestWords + 1) + "/" + vocabularyTest.getSPnumberOfWords(sharedPreferences));
    }

    private void addWords(View view){                                       //add words and implement onClickListener to Buttons
        TextView guessWord = (TextView) view.findViewById(R.id.testWordChoice);
        int [] shuffleNumberButtonTable = new int[guessButtons.length];
        shuffleNumberButtonTable = vocabularyTest.setRandomTableNumber(shuffleNumberButtonTable.length);    //add shuffle number button table

        cursor.moveToPosition(numberOfWord);                                //add good answer to first button
        guessWord.setText(cursor.getString(4));
        goodAnswer = shuffleNumberButtonTable[0];
        guessButtons[shuffleNumberButtonTable[0]].setText(cursor.getString(3));
        guessButtons[shuffleNumberButtonTable[0]].setOnClickListener(this);
        answerText = cursor.getString(3);
        final int idWord = cursor.getInt(0);                                //id of first word

        int category = cursor.getInt(2);                                    //set cursor to category
        cursor = vocabularyDatabase.getSpecificValues(vocabularyTest.getSPlevelOfLanguage(sharedPreferences), category);        //change cursor to category words


//        cursor.moveToFirst();
        final int index = searchId(cursor, idWord);
        int[] tableToShuffleWord = vocabularyTest.setRandomTableNumber(cursor.getCount(), index);

        for (int j = 1, i = 0; j < guessButtons.length; i++, j++) {        //add onClick to buttons, and add text
            guessButtons[i].setOnClickListener(this);
            do {
                cursor.moveToPosition(tableToShuffleWord[i]);
                guessButtons[shuffleNumberButtonTable[j]].setText(cursor.getString(3));
            } while (false);
        }
    }

    private int searchId(Cursor cursor, int id){                                                                                //search index of good answer word in database
        int index = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (cursor.getInt(0) == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        vocabularyDatabase.close();
    }

    @Override
    public void onClick(View view) {                                                                                        //onClickListener to all buttons
        for (Button currentButton : guessButtons) {
            currentButton.setClickable(false);
        }
        final Button thisButton = (Button) view;
        String buttonText = thisButton.getText().toString();

        if (buttonText.equals(answerText)) {
           setGoodAnswer(thisButton);

        } else {
           setBadAnswer(thisButton);
        }
    }

    private void setGoodAnswer(final Button thisButton){                                                                    //good answer handling
        manyGoodAnswer++;
        thisButton.setBackgroundResource(R.drawable.good_answer_change_color);
        Animation trueAnswer = AnimationUtils.loadAnimation(getContext(), R.anim.correct_answer_test);
        startAnimation(trueAnswer, thisButton, true);
    }

    private void setBadAnswer(final Button thisButton){                                                                     //bad answer handling
        thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
        guessButtons[goodAnswer].setBackgroundResource(R.drawable.good_answer_change_color);
        Animation falseAnswer = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test);
        startAnimation(falseAnswer, thisButton, false);
    }

    private void startAnimation(final Animation answerAnimation, final Button thisButton, final boolean isTrue) {           //animation when user click button
        answerAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                if (!isTrue){
                    ((TransitionDrawable) guessButtons[goodAnswer].getBackground()).startTransition(300);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                vocabularyTest.loadNextWord(fragmentTransaction, getContext());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        thisButton.startAnimation(answerAnimation);
    }

}
