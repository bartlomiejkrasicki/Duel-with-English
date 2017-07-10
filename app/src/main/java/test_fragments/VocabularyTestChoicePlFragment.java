package test_fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import static vocabulary_test.VocabularyTest.randomNumber;

public class VocabularyTestChoicePlFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private String answerText;
    private Button[] guessButtons = new Button[8];
    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private VocabularyDatabase vocabularyDatabase;
    private int goodAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocabulary_test_choice_pl_to_en_fragment, container, false);
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = vocabularyTest.getVocabularyDatabase(getContext());
        cursor = vocabularyTest.getCursor(getContext(), vocabularyDatabase);
        addWords(view);

        return view;
    }

    private void addWords(View view){
        TextView guessWord = (TextView) view.findViewById(R.id.testWordChoice);
        guessButtons[0] = (Button) view.findViewById(R.id.testChoicePlOption1);
        guessButtons[1] = (Button) view.findViewById(R.id.testChoicePlOption2);
        guessButtons[2] = (Button) view.findViewById(R.id.testChoicePlOption3);
        guessButtons[3] = (Button) view.findViewById(R.id.testChoicePlOption4);
        guessButtons[4] = (Button) view.findViewById(R.id.testChoicePlOption5);
        guessButtons[5] = (Button) view.findViewById(R.id.testChoicePlOption6);
        guessButtons[6] = (Button) view.findViewById(R.id.testChoicePlOption7);
        guessButtons[7] = (Button) view.findViewById(R.id.testChoicePlOption8);
        boolean[] itWasDrawn = new boolean[cursor.getCount()];
        for (int i = 0; i<itWasDrawn.length; i++){
            itWasDrawn[i] = false;
        }
        int tempRandomNumber = randomNumber(cursor.getCount());
        itWasDrawn[tempRandomNumber] = true;
        cursor.moveToPosition(tempRandomNumber);
        guessWord.setText(cursor.getString(3));
        tempRandomNumber = randomNumber(8);
        goodAnswer = tempRandomNumber;
        guessButtons[tempRandomNumber].setText(cursor.getString(4));
        guessButtons[tempRandomNumber].setTag(1);
        answerText = cursor.getString(4);

        for (Button guessButton : guessButtons) {
            guessButton.setOnClickListener(this);
            do {
                tempRandomNumber = randomNumber(cursor.getCount());
            } while (itWasDrawn[tempRandomNumber]);
            if (guessButton.getTag() == null || guessButton.getTag().equals(0)) {
                cursor.moveToPosition(tempRandomNumber);
                guessButton.setText(cursor.getString(4));
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
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    Activity activity = getActivity();
                    vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            thisButton.startAnimation(trueAnswer);
        }
        else {
            thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
            guessButtons[goodAnswer].setBackgroundResource(R.drawable.good_answer_change_color);
            Animation falseAnswer = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test);
            falseAnswer.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                    ((TransitionDrawable) guessButtons[goodAnswer].getBackground()).startTransition(300);
                }

                @Override
                public void onAnimationEnd(Animation animation){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    Activity activity = getActivity();
                    vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            thisButton.startAnimation(falseAnswer);
        }
    }
}
