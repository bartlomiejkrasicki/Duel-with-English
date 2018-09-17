package test_fragments;

import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;
import vocabulary_test.VocabularyTest;

public class VocabularyTestWriteFragment extends BaseTestFragments {

    private Cursor cursor;
    private TextView wordtoGuess;
    private EditText userWordET;
    private VocabularyDatabase dbInstance;
    private String correctAnswer;

    public VocabularyTestWriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write, container, false);
        declarationVariables(view);
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_write_en_hint, R.string.test_write_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
        addWord();
        configureEditText();
        configureCheckButton(view);
        return view;
    }

    private void declarationVariables(View view){
        VocabularyTest vocabularyTest = new VocabularyTest();
        dbInstance = VocabularyDatabase.getInstance(getActivity().getApplicationContext());
        cursor = vocabularyTest.getCursor(dbInstance);
        wordtoGuess = (TextView) view.findViewById(R.id.test_write_word);
        userWordET = (EditText) view.findViewById(R.id.userWriteWordET);
    }

    private void addWord(){
        cursor.moveToPosition(TestDataHelper.wordTable[TestDataHelper.currentWordNumber]);
        if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
            wordtoGuess.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        } else {
            wordtoGuess.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        }
    }

    private void configureEditText(){
        if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn).length())});
        } else {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn).length())});
        }
        userWordET.setText("");
    }

    private void configureCheckButton(View view){
        Button testWriteButtonCheck = (Button) view.findViewById(R.id.testWriteCheckButton);
        testWriteButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userWordET.setFocusableInTouchMode(true);
                userWordET.requestFocus();
                userWordET.setSelected(true);
                final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                String userRealAnswer = userWordET.getText().toString();
                if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
                    correctAnswer = cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
                } else {
                    correctAnswer = cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
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
        TestDataHelper.manyGoodAnswer++;
//        Flubber.with().animation(Flubber.AnimationPreset.SHAKE).duration(2000).createFor(view).start();
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
                if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
                    userWordET.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
                } else {
                    userWordET.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
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
        dbInstance.close();
    }
}
