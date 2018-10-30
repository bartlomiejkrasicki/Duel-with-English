package test_fragments;


import android.animation.Animator;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class VocabularyTestJigsawWordFragment extends BaseTestFragments {

    private Cursor cursor;
    private EditText[] answerET;
    private Button[] buttonsLetters;
    private String answerWord;
    private ArrayList<Integer> randomTable;
    private VocabularyDatabase dbInstance;
    private Button checkButton;

    public VocabularyTestJigsawWordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_jigsaw, container, false);
        declarationVariables(view);
        addWord(view);
        setViewComponentsValues(view);
        setButtonsClick();
        return view;
    }

    private void declarationVariables(View view){
        dbInstance = VocabularyDatabase.getInstance(getContext());
        cursor = TestDataHelper.getCursor(dbInstance);
        checkButton = (Button) view.findViewById(R.id.jigsawButtonToCheck);
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_jigsaw_word_en_hint, R.string.test_jigsaw_word_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
    }

    private void addWord(View view){
        TextView testWord = (TextView) view.findViewById(R.id.test_jigsaw_word);
        cursor.moveToPosition(TestDataHelper.wordTable.get(TestDataHelper.currentWordNumber));
        if (TestDataHelper.inEnglish) {
            testWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        } else {
            testWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        }
    }

    private void setViewComponentsValues(View view){
        setAnswerWord();
        randomTable = getShuffleTable(answerWord.length());
        addEditTexts(view, answerWord);
        addButtons(view, answerWord);
    }

    private void setAnswerWord(){
        if (TestDataHelper.inEnglish) {
            answerWord = cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        } else {
            answerWord = cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        }
    }

    private void addEditTexts(View view, String answerWord){
        LinearLayout answerLL = (LinearLayout) view.findViewById(R.id.answer_linear_layout_jigsaw);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        answerET = new EditText[answerWord.length()];
        for (int index = 0; index < answerWord.length(); index++) {
            setCharEditTextView(index, layoutParams);
            answerET[index].setTag(randomTable.get(index));
            hideIfEditTextSpace(index);
            answerET[index].setOnClickListener(answerOnClick);
            answerLL.addView(answerET[index]);
        }
    }

    private void hideIfEditTextSpace(int index){
        if (answerWord.charAt(index) == ' ') {
            answerET[index].setVisibility(View.INVISIBLE);
            answerET[index].setTag("space");
        }
    }

    private void setCharEditTextView(int index, ViewGroup.LayoutParams layoutParams){
        answerET[index] = new EditText(getActivity());
        answerET[index].setTextColor(getResources().getColor(android.R.color.white));
        answerET[index].setClickable(false);
        answerET[index].setFocusable(false);
        answerET[index].setEnabled(false);
        answerET[index].getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        answerET[index].setLayoutParams(layoutParams);
    }

    private View.OnClickListener answerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
            animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                int tag = (int) view.getTag();
                Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
                @Override
                public void onAnimationStart(Animation animation) {
                    for (Button buttonsLetter : buttonsLetters) {
                        if (buttonsLetter.getTag().equals(tag)) {
                            buttonsLetter.startAnimation(animationFadeIn);
                            buttonsLetter.setClickable(true);
                            view.setClickable(false);
                            view.setEnabled(false);
                            buttonsLetter.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (EditText currentET : answerET) {
                        if (currentET.getTag().equals(tag)) {
                            currentET.setText("");
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(animationFadeOut);
        }
    };

    private void addButtons(View view, final String answerWord){
        DisplayMetrics metrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();           //show Buttons in TableRows
        FlowLayout.LayoutParams layoutButtonParams = new FlowLayout.LayoutParams((metrics.widthPixels-32)/7, FlowLayout.LayoutParams.WRAP_CONTENT);
        FlowLayout buttonsViewLL = (FlowLayout) view.findViewById(R.id.jigsawButtonsToCreateWord);
        buttonsLetters = new Button[answerWord.length()];
        for (int index = 0; index < answerWord.length(); index++){
            hideButtonsIfSpace(randomTable.get(index));
            setButtonsView(index, layoutButtonParams);
            buttonsLetters[index].setOnClickListener(buttonsOnClick);
            buttonsViewLL.addView(buttonsLetters[index]);
        }
    }

    private void hideButtonsIfSpace(int index){
        if (answerWord.charAt(randomTable.get(index))==' ') {
            buttonsLetters[index].setVisibility(View.GONE);
        }
    }

    private void setButtonsView(int index, FlowLayout.LayoutParams layoutButtonParams){
        buttonsLetters[index] = new Button(getActivity().getApplicationContext());
        buttonsLetters[index].setBackgroundResource(R.drawable.circle_button);
        buttonsLetters[index].setTextColor(getResources().getColor(android.R.color.white));
        buttonsLetters[index].setLayoutParams(layoutButtonParams);
        buttonsLetters[index].setText(String.valueOf(answerWord.charAt(randomTable.get(index))));
        buttonsLetters[index].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        buttonsLetters[index].setTag(randomTable.get(index));
    }

    private View.OnClickListener buttonsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
                Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
                animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setClickable(false);
                        int index = (int) view.getTag();
                        int indexEditText = 0;
                        while(!(answerET[indexEditText].getText().toString()).equals("") || answerET[indexEditText].getTag().equals("space")){
                            indexEditText++;
                        }
                        Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
                        answerET[indexEditText].startAnimation(animationFadeIn);
                        answerET[indexEditText].setText(String.valueOf(answerWord.charAt(index)));
                        answerET[indexEditText].setTag(index);
                        answerET[indexEditText].setClickable(true);
                        answerET[indexEditText].setEnabled(true);
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(animationFadeOut);
        }
    };

    private void setButtonsClick(){
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                StringBuilder answerFinalForm = new StringBuilder();
                for (EditText anAnswerET : answerET) {
                    if (anAnswerET.getTag().equals("space"))
                        answerFinalForm.append(" ");
                    else
                        answerFinalForm.append(anAnswerET.getText());

                }
                if (answerFinalForm.toString().equalsIgnoreCase(answerWord)) {
                    view.setClickable(false);
                    correctAnswerOnClick(view);
                }
                else {
                    view.setClickable(false);
                    incorrectAnswerOnClick(view);
                }
            }
        });
    }

    private void disableButtons(){
        for (Button button : buttonsLetters) {
            button.setEnabled(false);
            button.setClickable(false);
        }
    }

    private void disableEditText(){
        for (EditText anAnswerET : answerET) {
            anAnswerET.setEnabled(false);
        }
    }

    private void correctAnswerOnClick(final View view){
        disableButtons();
        disableEditText();
        TestDataHelper.manyGoodAnswer++;
        YoYo.with(Techniques.ZoomIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                for (EditText completeWordET : answerET) {
                    completeWordET.setEnabled(false);
                    completeWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                view.setBackgroundResource(R.drawable.good_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
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
        disableButtons();
        disableEditText();

        YoYo.with(Techniques.Shake).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
                Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
                for (int i = 0; i < answerET.length; i++) {
                    answerET[i].startAnimation(animationFadeOut);
                    answerET[i].setEnabled(false);
                    answerET[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    answerET[i].setText(String.valueOf(answerWord.charAt(i)));
                    answerET[i].startAnimation(animationFadeIn);
                }
                view.setBackgroundResource(R.drawable.bad_answer_change_color);
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
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

    private ArrayList<Integer> getShuffleTable(int length){
        ArrayList<Integer> randomWordList = new ArrayList<>();
        for(int i = 0; i < length; i++){
            randomWordList.add(i);
        }
        Collections.shuffle(randomWordList);
        return randomWordList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbInstance.close();
    }
}
