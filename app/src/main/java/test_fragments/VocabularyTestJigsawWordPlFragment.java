package test_fragments;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.wefika.flowlayout.FlowLayout;

import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.randomNumber;

public class VocabularyTestJigsawWordPlFragment extends Fragment {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private TextView testWordEn;
    private EditText[] answerET;
    private Button[] buttonsLetters;
    private String answerWord;
    private int randomTable[];
    private VocabularyDatabase vocabularyDatabase;

    public VocabularyTestJigsawWordPlFragment() {
        // Required empty public constructor
    }


    @Override                                                   //fragment view create
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_jigsaw_word_pl, container, false);
        vocabularyTest = new VocabularyTest();
        vocabularyDatabase = vocabularyTest.getVocabularyDatabase(getContext());
        cursor = vocabularyTest.getCursor(getContext(), vocabularyDatabase);
        addWord(view);
        configureAnswer(view);
        Button checkButton = (Button) view.findViewById(R.id.jigsawPlButtonToCheck);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String answerFinalForm = "";
                for (EditText anAnswerET : answerET) {
                    answerFinalForm += anAnswerET.getText();
                }
                if (answerFinalForm.equals(answerWord)) {
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
                            Activity activity = getActivity();
                            vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(animationCorrect);
                } else {
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
                            Activity activity = getActivity();
                            vocabularyTest.loadNextWord(fragmentTransaction, getContext(), vocabularyDatabase, cursor, activity);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(animationWrong);
                }
            }
        });
        return view;
    }

    private void addWord(View view){                            //add word in English. User have to know it in Polish
        testWordEn = (TextView) view.findViewById(R.id.test_jigsaw_word_pl);
        int randomNumber = randomNumber(cursor.getCount());
        cursor.moveToFirst();
        cursor.moveToPosition(randomNumber);
        testWordEn.setText(cursor.getString(4));
    }

    private void configureAnswer(View view){                    //configure EditText table (word to write)
        answerWord = cursor.getString(3);
        shuffleTable();
        addEditTexts(view, answerWord);
        addButtons(view, answerWord);
    }

    private void addEditTexts(View view, String answerWord){        //add underlines to put word

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout answerLL = (LinearLayout) view.findViewById(R.id.answer_linear_layout_jigsaw_pl);
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
            if (answerWord.charAt(index)==' ') {
                answerET[index].setVisibility(View.INVISIBLE);
            }

            answerET[index].setOnClickListener(new View.OnClickListener() {
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
                    animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

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
        FlowLayout buttonsViewLL = (FlowLayout) view.findViewById(R.id.jigsawPlButtonsToCreateWord);
        buttonsLetters = new Button[answerWord.length()];

        for (int index = 0; index < answerWord.length(); index++){

            buttonsLetters[index] = new Button(getContext());
            buttonsLetters[index].setLayoutParams(layoutButtonParams);
            buttonsLetters[index].setText(String.valueOf(answerWord.charAt(randomTable[index])));
            buttonsLetters[index].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            buttonsLetters[index].setTag(randomTable[index]);

            if (answerWord.charAt(randomTable[index])==' ') {
                buttonsLetters[index].setVisibility(View.GONE);
            }

            buttonsLetters[index].setOnClickListener(new View.OnClickListener() {
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
}
