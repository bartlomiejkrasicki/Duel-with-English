package test_fragments;

import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class VocabularyTestChoiceFragment extends BaseTestFragments implements View.OnClickListener{

    private String answerText;
    private Button[] guessButtons;
    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private int goodAnswer;

    public VocabularyTestChoiceFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_choice, container, false);
        declarationVariables();
        setDataOnView();
        buttonsDeclaration(view);
        setGuessText(view);
        addWords(view);
        return view;
    }

    private void declarationVariables(){
        dbInstance = VocabularyDatabase.getInstance(getActivity().getApplicationContext());
        guessButtons = new Button[TestDataHelper.amountOfButtons];
        cursor = setCursor();
    }

    private void setDataOnView(){
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_choice_en_hint, R.string.test_choice_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
    }

    private Cursor setCursor(){
        if (TestDataHelper.isTestFromLesson) {
            return dbInstance.getCategoryValues(TestDataHelper.categoryName, TestDataHelper.lvlOfLanguage);
        } else {
            return dbInstance.getAllValues();
        }
    }

    private void buttonsDeclaration(View view){
        for(int i = 0; i < TestDataHelper.amountOfButtons; i++){
            String buttonId = getButtonIdString(TestDataHelper.amountOfButtons, i);
            int resourceButtonId = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
            guessButtons[i] = ((Button) view.findViewById(resourceButtonId));
        }
        setButtonsVisible(TestDataHelper.amountOfButtons, view);
    }

    private void setButtonsVisible(int amountOfButtons, View view){
        if (amountOfButtons <= 4){
            LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.testChoiceButtonsLL);
            buttonLayout.setVisibility(View.VISIBLE);
        } else {
            TableLayout tableLayout = (TableLayout) view.findViewById(R.id.testChoiceButtonsTable);
            tableLayout.setVisibility(View.VISIBLE);
        }
        for (Button guessButton : guessButtons){
            guessButton.setVisibility(View.VISIBLE);
        }
    }

    private String getButtonIdString(int amountOfButtons, int iterator){
        if (amountOfButtons <= 4){
            return "testChoice" + (iterator+1);
        }
        else {
            return "testChoiceOption" + (iterator+1);
        }
    }

    private void setGuessText(View view){
        TextView guessWord = (TextView) view.findViewById(R.id.testWordChoice);
        cursor.moveToPosition(TestDataHelper.wordTable.get(TestDataHelper.currentWordNumber));
        if (TestDataHelper.inEnglish) {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        } else {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        }
    }

    private void addWords(View view){
        ArrayList<Integer> shuffleNumberButtonTable = setRandomTableNumber(guessButtons.length);


        goodAnswer = shuffleNumberButtonTable.get(0);
        if (TestDataHelper.inEnglish) {
            answerText = cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        } else {
            answerText = cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        }

        final int idWord = cursor.getInt(VocabularyDatabaseColumnNames.idColumn);

        final String category = cursor.getString(VocabularyDatabaseColumnNames.categoryColumn);
        cursor = dbInstance.getCategoryValues(category, TestDataHelper.lvlOfLanguage);

        final int index = searchId(cursor, idWord);
        ArrayList<Integer> randomWordList = setRandomWordList(cursor.getCount(), index, TestDataHelper.amountOfButtons);

        for (int j = 0, i = 0; j < guessButtons.length; i++, j++) {
            guessButtons[i].setOnClickListener(this);
            do {
                cursor.moveToPosition(randomWordList.get(i));
                if (TestDataHelper.inEnglish) {
                    guessButtons[shuffleNumberButtonTable.get(j)].setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
                } else {
                    guessButtons[shuffleNumberButtonTable.get(j)].setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
                }
            } while (false);
        }
    }

    private int searchId(Cursor cursor, int id){
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
    public void onClick(View view) {
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

    private void setGoodAnswer(final Button thisButton){
        TestDataHelper.manyGoodAnswer++;
        thisButton.setBackgroundResource(R.drawable.good_answer_change_color);
        Animation trueAnswer;
        if (TestDataHelper.amountOfButtons > 5) {
            trueAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test);
        } else {
            trueAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.correct_answer_test_big_button);
        }
        startAnimation(trueAnswer, thisButton, true);
    }

    private void setBadAnswer(final Button thisButton){
        thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
        guessButtons[goodAnswer].setBackgroundResource(R.drawable.good_answer_change_color);
        Animation falseAnswer;
        if (TestDataHelper.amountOfButtons > 5) {
            falseAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test);
        } else {
            falseAnswer = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.wrong_answer_test_big_button);
        }
        startAnimation(falseAnswer, thisButton, false);
    }

    private void startAnimation(final Animation answerAnimation, final Button thisButton, final boolean isTrue) {
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
                loadNextWord(fragmentTransaction, getActivity());
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        thisButton.startAnimation(answerAnimation);
    }

    private ArrayList<Integer> setRandomWordList(int maxRangeNumber, int index, int buttonsAmount){
        ArrayList<Integer> randomWordList = new ArrayList<>();
        randomWordList.add(index);
        Random random = new Random();
        int randomNumber;
        for(; randomWordList.size() < buttonsAmount;){
            randomNumber = random.nextInt(maxRangeNumber);
            if(!(randomWordList.contains(randomNumber)) && randomNumber != index){
                randomWordList.add(randomNumber);
            }
        }
        Collections.shuffle(randomWordList);
        return randomWordList;
    }

    private ArrayList<Integer> setRandomTableNumber(int maxLength){
        ArrayList<Integer> randomWordList = new ArrayList<>();
        for(int i = 0; i < maxLength; i++){
            randomWordList.add(i);
        }
        Collections.shuffle(randomWordList);
        return randomWordList;
    }
}
