package test_fragments;

import android.animation.Animator;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class VocabularyTestChoiceFragment extends BaseTestFragments implements View.OnClickListener{

    private Button[] guessButtons;
    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private int goodAnswerButtonNumber;

    public VocabularyTestChoiceFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_choice, container, false);
        declarationVariables();
        setDataOnView();
        buttonsDeclaration(view);
        setGuessText(view);
        addWords();
        return view;
    }

    private void declarationVariables(){
        dbInstance = VocabularyDatabase.getInstance(getActivity().getApplicationContext());
        guessButtons = new Button[TestDataHelper.amountOfButtons];
        cursor = TestDataHelper.getCursor(dbInstance);
    }

    private void setDataOnView(){
        TestDataHelper.setToolbarHeader(cursor, getActivity());
        TestDataHelper.setTestHint(R.string.test_choice_en_hint, R.string.test_choice_pl_hint, getActivity());
        TestDataHelper.setProgressBar(getActivity());
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
        cursor = TestDataHelper.getCursor(dbInstance);
        cursor.moveToPosition(TestDataHelper.getCurrentWordNumber());
        if (TestDataHelper.inEnglish) {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        } else {
            guessWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        }
    }

    private void addWords(){
        cursor = TestDataHelper.getCursor(dbInstance);
        ArrayList<Integer> shuffleWordIdTable = setRandomWordList(cursor.getCount(), TestDataHelper.amountOfButtons);
        for (int i = 0; i < guessButtons.length; i++) {
            guessButtons[i].setOnClickListener(this);
            cursor.moveToPosition(shuffleWordIdTable.get(i));
            guessButtons[i].setText(getButtonText());
            if(shuffleWordIdTable.get(i) == TestDataHelper.getCurrentWordNumber()){
                goodAnswerButtonNumber = i;
            }
        }
    }

    private String getButtonText(){
        if (TestDataHelper.inEnglish) {
            return cursor.getString(VocabularyDatabaseColumnNames.plwordColumn);
        } else {
            return cursor.getString(VocabularyDatabaseColumnNames.enwordColumn);
        }
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
        Button thisButton = (Button) view;
        String buttonText = thisButton.getText().toString();
        cursor.moveToPosition(TestDataHelper.getCurrentWordNumber());
        if (buttonText.equalsIgnoreCase(getButtonText())) {
            TestDataHelper.manyGoodAnswer++;
            goodAnswerAnimation(thisButton);
        } else {
            badAnswerAnimation(thisButton);
        }
    }

    private void goodAnswerAnimation(final Button thisButton){
        thisButton.setBackgroundResource(R.drawable.good_answer_change_color);
        YoYo.with(Techniques.ZoomIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction, getActivity());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(thisButton);
    }

    private void badAnswerAnimation(final Button thisButton) {
        thisButton.setBackgroundResource(R.drawable.bad_answer_change_color);
        guessButtons[goodAnswerButtonNumber].setBackgroundResource(R.drawable.good_answer_change_color);
        YoYo.with(Techniques.Shake).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                ((TransitionDrawable) thisButton.getBackground()).startTransition(500);
                ((TransitionDrawable) guessButtons[goodAnswerButtonNumber].getBackground()).startTransition(500);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                loadNextWord(fragmentTransaction, getActivity());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(thisButton);
    }

    private ArrayList<Integer> setRandomWordList(int maxRangeNumber, int buttonsAmount){
        ArrayList<Integer> randomWordList = new ArrayList<>();
        randomWordList.add(TestDataHelper.getCurrentWordNumber());
        Random random = new Random();
        int randomNumber;
        for(; randomWordList.size() < buttonsAmount;){
            randomNumber = random.nextInt(maxRangeNumber);
            if(!(randomWordList.contains(randomNumber)) && randomNumber != TestDataHelper.getCurrentWordNumber()){
                randomWordList.add(randomNumber);
            }
        }
        Collections.shuffle(randomWordList);
        return randomWordList;
    }
}
