package test_fragments;


import android.database.Cursor;
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

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.wefika.flowlayout.FlowLayout;

import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;
import vocabulary_test.VocabularyTest;

public class VocabularyTestJigsawWordFragment extends BaseTestFragments {

    private Cursor cursor;
    private EditText[] answerET;
    private Button[] buttonsLetters;
    private String answerWord;
    private int randomTable[], numberOfWord;
    private VocabularyDatabase dbInstance;
    private Button checkButton;

    public VocabularyTestJigsawWordFragment() {
    }

    @Override                                                   //fragment view create
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_jigsaw, container, false);
        declarationVariables(view);
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_jigsaw_word_en_hint, R.string.test_jigsaw_word_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
        addWord(view);
        configureAnswer(view);
        setButtonsClick();
        return view;
    }

    private void declarationVariables(View view){                          //declaration layout elements and variables
        numberOfWord = TestDataHelper.wordTable[TestDataHelper.currentWordNumber];
        VocabularyTest vocabularyTest = new VocabularyTest();
        dbInstance = VocabularyDatabase.getInstance(getContext());
        cursor = vocabularyTest.getCursor(dbInstance);
        checkButton = (Button) view.findViewById(R.id.jigsawButtonToCheck);
    }

    private void setProgressBar(){
        RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(TestDataHelper.currentWordNumber);
    }

    private void addWord(View view){                            //add word in english or polish
        TextView testWord = (TextView) view.findViewById(R.id.test_jigsaw_word);
        cursor.moveToPosition(numberOfWord);
        if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
            testWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        } else {
            testWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        }
    }

    private void configureAnswer(View view){                    //configure EditText table (word to write)
        if (TestDataHelper.inEnglish[TestDataHelper.currentWordNumber]) {
            answerWord = cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        } else {
            answerWord = cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        }
        shuffleTable();
        addEditTexts(view, answerWord);
        addButtons(view, answerWord);
    }

    private void addEditTexts(View view, String answerWord){        //add underlines to put word
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout answerLL = (LinearLayout) view.findViewById(R.id.answer_linear_layout_jigsaw);
        answerET = new EditText[answerWord.length()];
        for (int index = 0; index < answerWord.length(); index++) {                   //show EditText in LinearLayout
            answerET[index] = new EditText(getActivity().getApplicationContext());

            answerET[index].setTextColor(getResources().getColor(android.R.color.white));
            answerET[index].setClickable(false);
            answerET[index].setFocusable(false);
            answerET[index].setEnabled(false);
            answerET[index].setLayoutParams(layoutParams);
            answerET[index].setTag(randomTable[index]);
            if (answerWord.charAt(index)==' ') {                                      //not show edittext for spaces
                answerET[index].setVisibility(View.INVISIBLE);
                answerET[index].setTag("space");
            }

            answerET[index].setOnClickListener(new View.OnClickListener() {           //on click edit text
                @Override
                public void onClick(final View view) {
                    final int tag = (int) view.getTag();
                    final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
                    final Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);

                    animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            for (Button buttonsLetter : buttonsLetters) {
                                if(buttonsLetter.getTag().equals(tag)){
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
                                if(currentET.getTag().equals(tag)){
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
            });
            answerLL.addView(answerET[index]);
        }
    }

    private void addButtons(View view, final String answerWord){
        DisplayMetrics metrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();           //show Buttons in TableRows
        int width = metrics.widthPixels;
        FlowLayout.LayoutParams layoutButtonParams = new FlowLayout.LayoutParams((width-32)/7, FlowLayout.LayoutParams.WRAP_CONTENT);
        FlowLayout buttonsViewLL = (FlowLayout) view.findViewById(R.id.jigsawButtonsToCreateWord);
        buttonsLetters = new Button[answerWord.length()];

        for (int index = 0; index < answerWord.length(); index++){                          //add buttons and shuffle, set text
            buttonsLetters[index] = new Button(getActivity().getApplicationContext());
            buttonsLetters[index].setBackgroundResource(R.drawable.circle_button);
            buttonsLetters[index].setTextColor(getResources().getColor(android.R.color.white));
            buttonsLetters[index].setLayoutParams(layoutButtonParams);
            buttonsLetters[index].setText(String.valueOf(answerWord.charAt(randomTable[index])));
            buttonsLetters[index].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            buttonsLetters[index].setTag(randomTable[index]);
            if (answerWord.charAt(randomTable[index])==' ') {
                buttonsLetters[index].setVisibility(View.GONE);
            }

            buttonsLetters[index].setOnClickListener(new View.OnClickListener() {           //buttons on click
                @Override
                public void onClick(final View view) {
                    Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
                    final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);

                    animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setClickable(false);
                            int index = (int) view.getTag();
                            int indexEditText = 0;
                            while(!(answerET[indexEditText].getText().toString()).equals("") || answerET[indexEditText].getTag().equals("space")){
                                indexEditText++;
                            }
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
            });

            buttonsViewLL.addView(buttonsLetters[index]);
        }
    }

    private void setButtonsClick(){                                                           //check button onClick
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
                    goodAnswer(view);
                }
                else {
                    view.setClickable(false);
                    badAnswer(view);
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

    private void goodAnswer(final View view){
        disableButtons();
        disableEditText();
        TestDataHelper.manyGoodAnswer++;
        Animation animationCorrect = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test_big_button);
        view.setBackgroundResource(R.drawable.good_answer_change_color);

        animationCorrect.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (EditText completeWordET : answerET) {
                    completeWordET.setEnabled(false);
                    completeWordET.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animationCorrect);
    }

    private void badAnswer(final View view){
        disableButtons();
        disableEditText();
        final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fragment_fade_out);
        Animation animationWrong = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test_big_button);
        view.setBackgroundResource(R.drawable.bad_answer_change_color);

        animationWrong.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (int i = 0; i < answerET.length; i++) {
                    answerET[i].startAnimation(animationFadeOut);
                    answerET[i].setEnabled(false);
                    answerET[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    answerET[i].setText(String.valueOf(answerWord.charAt(i)));
                    answerET[i].startAnimation(animationFadeIn);
                }
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animationWrong);
    }

    private int[] randomTableWithoutRepeat(int [] table){         //make table of random numbers to shuffle words
        Random random = new Random();
        for (int i = table.length - 1; i > 0; i--){
            int index = random.nextInt(i+1);
            int temp = table[index];
            table[index] = table[i];
            table[i] = temp;
        }
        return table;
    }

    private int[] shuffleTable(){
        randomTable = new int[answerWord.length()];
        for (int i = 0; i < randomTable.length; i++){
            randomTable[i] = i;
        }
        randomTable = randomTableWithoutRepeat(randomTable);
        return randomTable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbInstance.close();
    }
}
