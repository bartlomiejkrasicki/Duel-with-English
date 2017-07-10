package test_fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.randomNumber;

public class VocabularyTestWritePlFragment extends Fragment {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private TextView wordtoGuess;
    private EditText userWordAnswerPlET;
    private VocabularyDatabase vocabularyDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write_pl, container, false);
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = vocabularyTest.getVocabularyDatabase(getContext());
        cursor = vocabularyTest.getCursor(getContext(), vocabularyDatabase);
        wordtoGuess = (TextView) view.findViewById(R.id.test_write_pl_word);
        userWordAnswerPlET = (EditText) view.findViewById(R.id.userWriteWordPlET);
        Button testWriteButtonCheckEn = (Button) view.findViewById(R.id.testWritePlCheckButton);
        testWriteButtonCheckEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Activity activity = getActivity();
                InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Animation animationCorrect = AnimationUtils.loadAnimation(getContext(), R.anim.correct_answer_test);
                Animation animationWrong = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test);
                final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (userWordAnswerPlET.getText().toString().equals(cursor.getString(4))) {
                    manyGoodAnswer++;
                    animationCorrect.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setBackgroundResource(R.drawable.good_answer_change_color);
                            ((TransitionDrawable) view.getBackground()).startTransition(500);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(animationCorrect);
                }
                animationWrong.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setBackgroundResource(R.drawable.bad_answer_change_color);
                        ((TransitionDrawable) view.getBackground()).startTransition(500);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.setAnimation(animationWrong);
            }
        });
        addWord(view);
        return view;
    }

    private void addWord(View view) {
        int randomNumber = randomNumber(cursor.getCount());
        cursor.moveToPosition(randomNumber);
        wordtoGuess.setText(cursor.getString(3));
    }
}

