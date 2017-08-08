package test_fragments;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.inEnglish;
import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumberOfWords;


public class VocabularyTestWriteFragment extends Fragment {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private TextView wordtoGuess;
    private EditText userWordET;
    private VocabularyDatabase vocabularyDatabase;
    private int numberOfWord;
    private SharedPreferences sharedPreferences;
    private String correctAnswer;

    public VocabularyTestWriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write, container, false);
        declarationVariables(view);
        setToolbar();
        addWord();
        configureEditText();
        configureCheckButton(view);
        return view;
    }

    private void declarationVariables(View view){
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = vocabularyTest.getVocabularyDatabase(getContext());
        cursor = vocabularyTest.getCursor(getContext(), vocabularyDatabase);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        wordtoGuess = (TextView) view.findViewById(R.id.test_write_word);
        userWordET = (EditText) view.findViewById(R.id.userWriteWordET);
        numberOfWord = randomNumberOfWords[manyTestWords];
        TextView hint = (TextView) view.findViewById(R.id.test_write_hint);
        if (inEnglish[manyTestWords] == 1) {
            hint.setText(R.string.test_write_pl_hint);
        } else {
            hint.setText(R.string.test_write_en_hint);
        }
    }

    private void setToolbar(){
        cursor.moveToPosition(numberOfWord);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getInt(2));
        toolbar.setSubtitle("Postęp: " + (manyTestWords + 1) + "/" + vocabularyTest.getSPnumberOfWords(sharedPreferences));
    }

    private void addWord(){
        cursor.moveToPosition(numberOfWord);
        if (inEnglish[manyTestWords] == 1) {
            wordtoGuess.setText(cursor.getString(4));
        } else {
            wordtoGuess.setText(cursor.getString(3));
        }
    }

    private void configureEditText(){
        if (inEnglish[manyTestWords] == 1) {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(3).length())});
        } else {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(4).length())});
        }
        userWordET.setText("");
    }

    private void configureCheckButton(View view){
        Button testWriteButtonCheckEn = (Button) view.findViewById(R.id.testWriteCheckButton);
        testWriteButtonCheckEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userWordET.setFocusableInTouchMode(true);
                userWordET.requestFocus();
                userWordET.setSelected(true);
                final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                String userRealAnswer = userWordET.getText().toString();
                if (inEnglish[manyTestWords] == 1) {
                    correctAnswer = cursor.getString(3);
                } else {
                    correctAnswer = cursor.getString(4);
                }
                if (userRealAnswer.equals("")){
                    Toast.makeText(getContext(), "Pole odpowiedzi jest puste", Toast.LENGTH_SHORT).show();
                }
                else if (userRealAnswer.equals(correctAnswer)){                                                                                            //good answer
                    view.setClickable(false);
                    goodAnswerClick(view, fragmentTransaction);
                } else if (!(userRealAnswer.equals(correctAnswer))){                                                                                       //bad answer
                    view.setClickable(false);
                    badAnswerClick(view, fragmentTransaction);
                }
            }
        });
    }

    private void goodAnswerClick(final View view, final FragmentTransaction fragmentTransaction){
        manyGoodAnswer++;
        Animation animationCorrect = AnimationUtils.loadAnimation(getContext(), R.anim.correct_answer_test);
        animationCorrect.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                userWordET.setEnabled(false);
                view.setBackgroundResource(R.drawable.good_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                userWordET.setText("");
                vocabularyTest.loadNextWord(fragmentTransaction, getContext());
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationCorrect);
    }

    private void badAnswerClick(final View view, final FragmentTransaction fragmentTransaction){
        Animation animationWrong = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test);
        animationWrong.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_out);
                Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_in);
                userWordET.setEnabled(false);
                userWordET.startAnimation(animationFadeOut);
                if (inEnglish[manyTestWords] == 1) {
                    userWordET.setText(cursor.getString(3));
                } else {
                    userWordET.setText(cursor.getString(4));
                }
                userWordET.startAnimation(animationFadeIn);
                userWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view.setBackgroundResource(R.drawable.bad_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                userWordET.setText("");
                vocabularyTest.loadNextWord(fragmentTransaction, getContext());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationWrong);
    }

    @Override
    public void onDestroy() {                                                                                   //close cursor and database
        super.onDestroy();
        cursor.close();
        vocabularyDatabase.close();
    }
}
