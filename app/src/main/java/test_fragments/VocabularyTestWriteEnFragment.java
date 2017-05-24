package test_fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTest;

import static vocabulary_test.VocabularyTest.manyGoodAnswer;
import static vocabulary_test.VocabularyTest.manyTestWords;
import static vocabulary_test.VocabularyTest.randomNumber;



public class VocabularyTestWriteEnFragment extends Fragment {

    private VocabularyTest vocabularyTest;
    private Cursor cursor;
    private TextView wordtoGuess;
    private EditText userWordET;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_test_write_en, container, false);
        vocabularyTest = new VocabularyTest();
        wordtoGuess = (TextView) view.findViewById(R.id.test_write_en_word);
        Button testWriteButtonCheckEn = (Button) view.findViewById(R.id.testWriteEnCheckButton);
        userWordET = (EditText) view.findViewById(R.id.userWriteWordEnET);
        testWriteButtonCheckEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userWordET.getText().toString().equals(cursor.getString(3))){
                    manyGoodAnswer++;
                    loadNextWord();
                }
                else {
                    loadNextWord();
                }
            }
        });
        addWord();
        return view;
    }

    private void addWord(){
        cursor = vocabularyTest.getCursor(getContext());
        int randomNumber = randomNumber(cursor.getCount());
        cursor.moveToPosition(randomNumber);
        wordtoGuess.setText(cursor.getString(4));
    }

    private void loadNextWord(){
        manyTestWords++;
        if(manyTestWords == vocabularyTest.getSPnumberOfWords(vocabularyTest.getSharedPreferences(getContext()))){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Test został zakończony");
            alertDialogBuilder.setMessage("Odpowiedziałeś poprawnie na " + manyGoodAnswer + " z " + vocabularyTest.getSPnumberOfWords(vocabularyTest.getSharedPreferences(getContext())) + " pytań.");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Jeszcze raz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().recreate();
                }
            });
            alertDialogBuilder.setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else {
               replaceFragment();
        }
    }

    private void replaceFragment(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (randomNumber(4)){
            case 0:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestChoiceEnFragment()).commit();
                break;
            }
            case 1:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestChoicePlFragment()).commit();
                break;
            }
            case 2:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestWriteEnFragment()).commit();
                break;
            }
            case 3:{
                fragmentTransaction.replace(R.id.testFragmentId, new VocabularyTestWritePlFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(getContext(), "Przełączanie widoku na nowy nie zadziałało :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
