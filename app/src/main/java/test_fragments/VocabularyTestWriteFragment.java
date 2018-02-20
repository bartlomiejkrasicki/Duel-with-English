package test_fragments;


import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
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

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.inEnglish;
import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumberOfWords;


public class VocabularyTestWriteFragment extends BaseTestFragments {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private TextView wordtoGuess;
    private EditText userWordET;
    private VocabularyDatabase vocabularyDatabase;
    private int numberOfWord, amountOfWords;
    private String correctAnswer;

    public VocabularyTestWriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write, container, false);
        amountOfWords = getArguments().getInt("wordsAmount");
        declarationVariables(view);
        setToolbar();
        setProgressBar();
        addWord();
        configureEditText();
        configureCheckButton(view);
        return view;
    }

    private void declarationVariables(View view){
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = vocabularyTest.getVocabularyDatabase(getActivity().getApplicationContext());
        cursor = vocabularyTest.getCursor(vocabularyDatabase);
        wordtoGuess = (TextView) view.findViewById(R.id.test_write_word);
        userWordET = (EditText) view.findViewById(R.id.userWriteWordET);
        numberOfWord = randomNumberOfWords[manyTestWords];
        declareTextHint(view);
    }

    private void declareTextHint(View view){
        TextView hint = (TextView) view.findViewById(R.id.test_write_hint);
        if (inEnglish[manyTestWords]) {
            hint.setText(R.string.test_write_pl_hint);
        } else {
            hint.setText(R.string.test_write_en_hint);
        }
    }

    private void setToolbar(){
        cursor.moveToPosition(numberOfWord);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getString(DatabaseColumnNames.categoryColumn));
        toolbar.setSubtitle("Postęp: " + (manyTestWords + 1) + "/" + amountOfWords);
    }

    private void setProgressBar(){
        RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(manyTestWords);
    }

    private void addWord(){
        cursor.moveToPosition(numberOfWord);
        if (inEnglish[manyTestWords]) {
            wordtoGuess.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
        } else {
            wordtoGuess.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        }
    }

    private void configureEditText(){
        if (inEnglish[manyTestWords]) {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(DatabaseColumnNames.enwordColumn).length())});
        } else {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(DatabaseColumnNames.plwordColumn).length())});
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
                if (inEnglish[manyTestWords]) {
                    correctAnswer = cursor.getString(DatabaseColumnNames.enwordColumn);
                } else {
                    correctAnswer = cursor.getString(DatabaseColumnNames.plwordColumn);
                }
                if (userRealAnswer.equalsIgnoreCase("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pole odpowiedzi jest puste", Toast.LENGTH_SHORT).show();
                }
                else if (userRealAnswer.equalsIgnoreCase(correctAnswer)){                                                                                            //good answer
                    view.setClickable(false);
                    goodAnswerClick(view, fragmentTransaction);
                } else if (!(userRealAnswer.equalsIgnoreCase(correctAnswer))){                                                                                       //bad answer
                    view.setClickable(false);
                    badAnswerClick(view, fragmentTransaction);
                }
            }
        });
    }

    private void goodAnswerClick(final View view, final FragmentTransaction fragmentTransaction){
        manyGoodAnswer++;
        Animation animationCorrect = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test_big_button);
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

                loadNextWord(fragmentTransaction);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationCorrect);
    }

    private void badAnswerClick(final View view, final FragmentTransaction fragmentTransaction){
        Animation animationWrong = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test_big_button);
        animationWrong.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
                Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
                userWordET.setEnabled(false);
                userWordET.startAnimation(animationFadeOut);
                if (inEnglish[manyTestWords]) {
                    userWordET.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
                } else {
                    userWordET.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
                }
                userWordET.startAnimation(animationFadeIn);
                userWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view.setBackgroundResource(R.drawable.bad_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                userWordET.setText("");
                loadNextWord(fragmentTransaction);
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
