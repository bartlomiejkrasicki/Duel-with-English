package test_fragments;

import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class VocabularyTestChoiceFragment extends BaseTestFragments implements View.OnClickListener{

    private String answerText, categoryName;
    private Button[] guessButtons;
    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private int goodAnswer, numberOfWord, amountOfButtons, amountOfWords;

    public VocabularyTestChoiceFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocabulary_test_choice_fragment, container, false);
        amountOfButtons = this.getArguments().getInt("amountOfButtons");
        amountOfWords = this.getArguments().getInt("wordsAmount");
        try {
            categoryName = this.getArguments().getString("testCategory");
        } catch (NullPointerException nullPointerException){

        }
        declarationVariables(view);
        setToolbar();
        setProgressBar();
        addWords(view);
        return view;
    }

    private void declarationVariables(View view){                          //declaration layout elements and variables
        numberOfWord = TestDataHelper.wordTable[TestDataHelper.manyTestWords];
        dbInstance = VocabularyDatabase.getInstance(getActivity().getApplicationContext());
        guessButtons = new Button[amountOfButtons];
        buttonsDeclaration(view);
        if (categoryName != null) {
            cursor = dbInstance.getCategoryValues(categoryName, TestDataHelper.lvlOfLanguage);
        } else {
            cursor = dbInstance.getAllValues();
        }
        TextView testHint = (TextView) view.findViewById(R.id.testChoiceHint);
        if (TestDataHelper.inEnglish[TestDataHelper.manyTestWords]) {
            testHint.setText(R.string.test_choice_en_hint);
        } else {
            testHint.setText(R.string.test_choice_pl_hint);
        }
    }

    private void buttonsDeclaration(View view){
        switch (amountOfButtons){
            case 2:{
                guessButtons[0] = (Button) view.findViewById(R.id.testChoice1);
                guessButtons[1] = (Button) view.findViewById(R.id.testChoice2);
                LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.testChoiceButtonsLL);
                buttonLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 4:{
                guessButtons[0] = (Button) view.findViewById(R.id.testChoice1);
                guessButtons[1] = (Button) view.findViewById(R.id.testChoice2);
                guessButtons[2] = (Button) view.findViewById(R.id.testChoice3);
                guessButtons[3] = (Button) view.findViewById(R.id.testChoice4);
                LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.testChoiceButtonsLL);
                buttonLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 6:{
                guessButtons[0] = (Button) view.findViewById(R.id.testChoiceOption1);
                guessButtons[1] = (Button) view.findViewById(R.id.testChoiceOption2);
                guessButtons[2] = (Button) view.findViewById(R.id.testChoiceOption3);
                guessButtons[3] = (Button) view.findViewById(R.id.testChoiceOption4);
                guessButtons[4] = (Button) view.findViewById(R.id.testChoiceOption5);
                guessButtons[5] = (Button) view.findViewById(R.id.testChoiceOption6);
                TableLayout tableLayout = (TableLayout) view.findViewById(R.id.testChoiceButtonsTable);
                tableLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 8:{
                guessButtons[0] = (Button) view.findViewById(R.id.testChoiceOption1);
                guessButtons[1] = (Button) view.findViewById(R.id.testChoiceOption2);
                guessButtons[2] = (Button) view.findViewById(R.id.testChoiceOption3);
                guessButtons[3] = (Button) view.findViewById(R.id.testChoiceOption4);
                guessButtons[4] = (Button) view.findViewById(R.id.testChoiceOption5);
                guessButtons[5] = (Button) view.findViewById(R.id.testChoiceOption6);
                guessButtons[6] = (Button) view.findViewById(R.id.testChoiceOption7);
                guessButtons[7] = (Button) view.findViewById(R.id.testChoiceOption8);
                TableLayout tableLayout = (TableLayout) view.findViewById(R.id.testChoiceButtonsTable);
                tableLayout.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (Button guessButton : guessButtons){
            guessButton.setVisibility(View.VISIBLE);
        }
    }

    private void setToolbar(){
        cursor.moveToPosition(numberOfWord);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.testVocabularyToolbar);
        toolbar.setTitle("Kategoria: " + cursor.getString(VocabularyDatabaseColumnNames.categoryColumn));
        toolbar.setSubtitle("Postęp: " + (TestDataHelper.manyTestWords + 1) + "/" + amountOfWords);
    }

    private void setProgressBar(){
        RoundCornerProgressBar testProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(TestDataHelper.manyTestWords);
    }

    private void addWords(View view){                                       //add words and implement onClickListener to Buttons
        TextView guessWord = (TextView) view.findViewById(R.id.testWordChoice);
        int [] shuffleNumberButtonTable = new int[guessButtons.length];
        shuffleNumberButtonTable = setRandomTableNumber(shuffleNumberButtonTable.length);    //add shuffle number button table

        cursor.moveToPosition(numberOfWord);                                //add good answer to first button
        if (TestDataHelper.inEnglish[TestDataHelper.manyTestWords]) {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
            guessButtons[shuffleNumberButtonTable[0]].setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        } else {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
            guessButtons[shuffleNumberButtonTable[0]].setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        }
        goodAnswer = shuffleNumberButtonTable[0];
        guessButtons[shuffleNumberButtonTable[0]].setOnClickListener(this);
        if (TestDataHelper.inEnglish[TestDataHelper.manyTestWords]) {
            answerText = cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        } else {
            answerText = cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        }

        final int idWord = cursor.getInt(VocabularyDatabaseColumnNames.idColumn);                                //id of first word

        final String category = cursor.getString(VocabularyDatabaseColumnNames.categoryColumn);                                    //set cursor to category
        cursor = dbInstance.getCategoryValues(category, TestDataHelper.lvlOfLanguage);        //change cursor to category words

        final int index = searchId(cursor, idWord);
        int[] tableToShuffleWord = setRandomTableNumber(cursor.getCount(), index, amountOfButtons);

        for (int j = 1, i = 0; j < guessButtons.length; i++, j++) {        //add onClick to buttons, and add text
            guessButtons[i].setOnClickListener(this);
            do {
                cursor.moveToPosition(tableToShuffleWord[i]);
                if (TestDataHelper.inEnglish[TestDataHelper.manyTestWords]) {
                    guessButtons[shuffleNumberButtonTable[j]].setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
                } else {
                    guessButtons[shuffleNumberButtonTable[j]].setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
                }
            } while (false);
        }
    }

    private int searchId(Cursor cursor, int id){                                                                                //search index of good answer word in database
        int index = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (cursor.getInt(0) == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbInstance.close();
    }

    @Override
    public void onClick(View view) {                                                                                        //onClickListener to all buttons
        for (Button currentButton : guessButtons) {
            currentButton.setClickable(false);
        }
        final Button thisButton = (Button) view;
        String buttonText = thisButton.getText().toString();

        if (buttonText.equalsIgnoreCase(answerText)) {
           setGoodAnswer(thisButton);

        } else {
           setBadAnswer(thisButton);
        }
    }

    private void setGoodAnswer(final Button thisButton){                                                                    //good answer handling
        TestDataHelper.manyGoodAnswer++;
        thisButton.setBackgroundResource(R.drawable.good_answer_change_color);
        Animation trueAnswer;
        if (amountOfButtons > 5) {
            trueAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test);
        } else {
            trueAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test_big_button);
        }
        startAnimation(trueAnswer, thisButton, true);
    }

    private void setBadAnswer(final Button thisButton){                                                                     //bad answer handling
        thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
        guessButtons[goodAnswer].setBackgroundResource(R.drawable.good_answer_change_color);
        Animation falseAnswer;
        if (amountOfButtons > 5) {
            falseAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test);
        } else {
            falseAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test_big_button);
        }
        startAnimation(falseAnswer, thisButton, false);
    }

    private void startAnimation(final Animation answerAnimation, final Button thisButton, final boolean isTrue) {           //animation when user click button
        answerAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                if (!isTrue){
                    ((TransitionDrawable) guessButtons[goodAnswer].getBackground()).startTransition(300);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        thisButton.startAnimation(answerAnimation);
    }

    private int [] setRandomTableNumber(int maxRangeNumber, int index, int buttonsAmount){          //shuffle numbers and add to table of int, class check reply words
        int [] tableRandomNumbers = new int[buttonsAmount];
        Random random = new Random();
        boolean numberIsntOther;
        for(int i = 0; i < tableRandomNumbers.length; i++){
            if(i==0){                                                                                       //add first value
                do {
                    tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                } while (tableRandomNumbers[i] == index);
            }
            else {                                                                                          //add other values
                do {
                    do {
                        tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                    } while (tableRandomNumbers[i] == index);
                    numberIsntOther = false;
                    for(int j = 0; j < i; j++){
                        if(tableRandomNumbers[j]==tableRandomNumbers[i]){
                            numberIsntOther = true;
                        }
                    }
                } while (numberIsntOther);
            }
        }
        return tableRandomNumbers;
    }

    private int [] setRandomTableNumber(int maxRangeNumber){          //shuffle numbers and add to table of int
        int [] tableRandomNumbers = new int[maxRangeNumber];
        Random random = new Random();
        boolean numberIsOther;
        for(int i = 0; i < tableRandomNumbers.length; i++){
            if(i==0){
                tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
            }
            else {
                do {
                    numberIsOther = true;
                    tableRandomNumbers[i] = random.nextInt(maxRangeNumber);
                    for(int j = 0; j < i; j++){
                        if(tableRandomNumbers[j]==tableRandomNumbers[i]){
                            numberIsOther = true;
                            break;
                        } else {
                            numberIsOther = false;
                        }
                    }
                } while (numberIsOther);
            }
        }
        return tableRandomNumbers;
    }
}
