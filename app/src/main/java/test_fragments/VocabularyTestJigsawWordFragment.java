package test_fragments;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.wefika.flowlayout.FlowLayout;

import java.util.Random;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.inEnglish;
import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumberOfWords;

public class VocabularyTestJigsawWordFragment extends Fragment {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private EditText[] answerET;
    private Button[] buttonsLetters;
    private String answerWord;
    private int randomTable[], numberOfWord;
    private VocabularyDatabase vocabularyDatabase;
    private Button checkButton;
    private SharedPreferences sharedPreferences;

    public VocabularyTestJigsawWordFragment() {
    }

    @Override                                                   //fragment view create
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_jigsaw, container, false);
        declarationVariables(view);
        setToolbar();
        setProgressBar();
        addWord(view);
        configureAnswer(view);
        setButtonsClick();
        return view;
    }

    private void declarationVariables(View view){                          //declaration layout elements and variables
        numberOfWord = randomNumberOfWords[manyTestWords];
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = new VocabularyDatabase(getContext());
        cursor = vocabularyTest.getCursor(getContext(), vocabularyDatabase);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        checkButton = (Button) view.findViewById(R.id.jigsawButtonToCheck);
        TextView hintText = (TextView) view.findViewById(R.id.test_jigsaw_hint);
        if (inEnglish[manyTestWords] == 1) {
            hintText.setText(R.string.test_jigsaw_word_en_hint);
        } else {
            hintText.setText(R.string.test_jigsaw_word_pl_hint);
        }
    }

    private void setToolbar(){
        cursor.moveToPosition(numberOfWord);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getString(DatabaseColumnNames.categoryColumn));
        toolbar.setSubtitle("Postęp: " + (manyTestWords + 1) + "/" + vocabularyTest.getSPnumberOfWords(sharedPreferences));
    }

    private void setProgressBar(){
        RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(manyTestWords);
        testProgressBar.setSecondaryProgress(manyTestWords+1);
    }

    private void addWord(View view){                            //add word in english or polish
        TextView testWord = (TextView) view.findViewById(R.id.test_jigsaw_word);
        cursor.moveToPosition(numberOfWord);
        if (inEnglish[manyTestWords] == 1) {
            testWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        } else {
            testWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
        }
    }

    private void configureAnswer(View view){                    //configure EditText table (word to write)
        if (inEnglish[manyTestWords] == 1) {
            answerWord = cursor.getString(DatabaseColumnNames.plwordColumn);
        } else {
            answerWord = cursor.getString(DatabaseColumnNames.enwordColumn);
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
            answerET[index] = new EditText(getContext());
            answerET[index].setClickable(false);
            answerET[index].setFocusable(false);
            answerET[index].setFocusableInTouchMode(false);
            answerET[index].setEnabled(false);
            answerET[index].setGravity(View.TEXT_ALIGNMENT_CENTER);
            answerET[index].setLayoutParams(layoutParams);
            answerET[index].setTag(randomTable[index]);
            if (answerWord.charAt(index)==' ') {                                      //not show edittext for spaces
                answerET[index].setVisibility(View.INVISIBLE);
            }

            answerET[index].setOnClickListener(new View.OnClickListener() {           //on click edit text
                @Override
                public void onClick(final View view) {
                    final int tag = (int) view.getTag();
                    final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_in);
                    final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_out);

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
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();           //show Buttons in TableRows
        int width = metrics.widthPixels;
        FlowLayout.LayoutParams layoutButtonParams = new FlowLayout.LayoutParams((width-32)/7, FlowLayout.LayoutParams.WRAP_CONTENT);
        FlowLayout buttonsViewLL = (FlowLayout) view.findViewById(R.id.jigsawButtonsToCreateWord);
        buttonsLetters = new Button[answerWord.length()];

        for (int index = 0; index < answerWord.length(); index++){                          //add buttons and shuffle, set text
            buttonsLetters[index] = new Button(getContext());
            buttonsLetters[index].setBackgroundResource(R.drawable.circle_button);
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
                    Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_out);
                    final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_in);

                    animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setClickable(false);
                            int index = (int) view.getTag();
                            int indexEditText = 0;
                            while(!(answerET[indexEditText].getText().toString()).equals("")){
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
                String answerFinalForm = "";
                boolean isEmpty = false;
                for (EditText anAnswerET : answerET) {
                    if (anAnswerET.getText().toString().equals("")){
                        isEmpty = true;
                        break;
                    }
                    answerFinalForm += anAnswerET.getText();
                }
                if (isEmpty){
                    Toast.makeText(getContext(), "Użyj wszystkich dostępnych liter", Toast.LENGTH_SHORT).show();
                }
                else if (answerFinalForm.equals(answerWord)) {
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
        manyGoodAnswer++;
        Animation animationCorrect = AnimationUtils.loadAnimation(getContext(), R.anim.correct_answer_test_big_button);
        view.setBackgroundResource(R.drawable.good_answer_change_color);

        animationCorrect.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (EditText completeWordET : answerET) {
                    completeWordET.setEnabled(false);
                }
                ((TransitionDrawable) view.getBackground()).startTransition(500);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                vocabularyTest.loadNextWord(fragmentTransaction, getContext());
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
        final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fragment_fade_out);
        Animation animationWrong = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_answer_test_big_button);
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
                vocabularyTest.loadNextWord(fragmentTransaction, getContext());
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
        vocabularyDatabase.close();
    }
}