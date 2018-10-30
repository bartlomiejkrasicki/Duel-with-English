package test_fragments;

import android.animation.Animator;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class VocabularyTestWriteFragment extends BaseTestFragments {

    private Cursor cursor;
    private TextView wordToGuess;
    private EditText userWordET;
    private VocabularyDatabase dbInstance;

    public VocabularyTestWriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write, container, false);
        declarationVariables(view);
        addWord();
        configureEditText();
        configureCheckButton(view);
        return view;
    }

    private void declarationVariables(View view){
        dbInstance = VocabularyDatabase.getInstance(getActivity());
        cursor = TestDataHelper.getCursor(dbInstance);
        wordToGuess = (TextView) view.findViewById(R.id.test_write_word);
        userWordET = (EditText) view.findViewById(R.id.userWriteWordET);
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_write_en_hint, R.string.test_write_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
    }

    private void addWord(){
        cursor.moveToPosition(TestDataHelper.wordTable.get(TestDataHelper.currentWordNumber));
        if (TestDataHelper.inEnglish) {
            wordToGuess.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        } else {
            wordToGuess.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        }
    }

    private void configureEditText(){
        if (TestDataHelper.inEnglish) {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn).length())});
        } else {
            userWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn).length())});
        }
        userWordET.setText("");
        userWordET.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
    }

    private void configureCheckButton(View view){
        Button testWriteButtonCheck = (Button) view.findViewById(R.id.testWriteCheckButton);
        testWriteButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userWordET.setFocusableInTouchMode(true);
                userWordET.requestFocus();
                userWordET.setSelected(true);
                String userRealAnswer = userWordET.getText().toString();
                if (userRealAnswer.equalsIgnoreCase("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Pole odpowiedzi jest puste", Toast.LENGTH_SHORT).show();
                }
                else if (userRealAnswer.equalsIgnoreCase(getCorrectAnswer())){                                                                                            //good answer
                    view.setClickable(false);
                    userWordET.setEnabled(false);
                    correctAnswerOnClick(view);
                } else if (!(userRealAnswer.equalsIgnoreCase(getCorrectAnswer()))){                                                                                       //bad answer
                    view.setClickable(false);
                    userWordET.setEnabled(false);
                    incorrectAnswerOnClick(view);
                }
            }
        });
    }

    private void correctAnswerOnClick(final View view){
        TestDataHelper.manyGoodAnswer++;
        YoYo.with(Techniques.ZoomIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fragment_fade_out);
                Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fragment_fade_in);
                userWordET.setEnabled(false);
                userWordET.startAnimation(animationFadeOut);
                userWordET.setText(getCorrectAnswer());
                userWordET.setTextColor(Color.WHITE);
                userWordET.startAnimation(animationFadeIn);
                userWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view.setBackgroundResource(R.drawable.good_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                userWordET.setText("");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction, getActivity());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(view);
    }

    private void incorrectAnswerOnClick(final View view){
        YoYo.with(Techniques.Shake).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fragment_fade_out);
                Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fragment_fade_in);
                userWordET.setEnabled(false);
                userWordET.startAnimation(animationFadeOut);
                userWordET.setText(getCorrectAnswer());
                userWordET.setTextColor(Color.WHITE);
                userWordET.startAnimation(animationFadeIn);
                userWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view.setBackgroundResource(R.drawable.bad_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                userWordET.setText("");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction, getActivity());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(view);
    }

    private String getCorrectAnswer(){
        if (TestDataHelper.inEnglish) {
            return cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        } else {
            return cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbInstance.close();
    }
}
