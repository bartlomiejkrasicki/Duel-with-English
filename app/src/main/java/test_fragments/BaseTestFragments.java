package test_fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;

public class BaseTestFragments extends android.support.v4.app.Fragment{

    public BaseTestFragments() {

    }

    protected void replaceFragment(FragmentTransaction fragmentTransaction){                 //show next fragment after answer
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        switch (TestDataHelper.getRandomNumber(3)){
            case 0:{
                VocabularyTestChoiceFragment choiceFragment = new VocabularyTestChoiceFragment();
                fragmentTransaction.replace(R.id.testFragment, choiceFragment).commit();
                break;
            }
            case 1:{
                VocabularyTestWriteFragment writeFragment = new VocabularyTestWriteFragment();
                fragmentTransaction.replace(R.id.testFragment, writeFragment).commit();
                break;
            }
            case 2:{
                VocabularyTestJigsawWordFragment jigsawWordFragment = new VocabularyTestJigsawWordFragment();
                fragmentTransaction.replace(R.id.testFragment, jigsawWordFragment).commit();
                break;
            }
            default:{
                Toast.makeText(getContext(), "Błąd wczytywania testu. Uruchom go jeszcze raz :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void loadNextWord(FragmentTransaction fragmentTransaction, Activity activity){
        TestDataHelper.currentWordNumber++;
        TestDataHelper.inEnglishSetRandom();
        if(TestDataHelper.currentWordNumber == TestDataHelper.amountOfWords){
            showEndTestAlertDialog(activity);
        } else {
            replaceFragment(fragmentTransaction);
        }
    }

    private MaterialStyledDialog.Builder createEndDialogFromLesson(){
        MaterialStyledDialog.Builder exitAppDialog = new MaterialStyledDialog.Builder(getContext())
                .withDialogAnimation(true)
                .withDivider(true)
                .setHeaderColor(R.color.colorAccent)
                .setNegativeText("Powtórz")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveResultsAndRecreate();
                    }
                })
                .setPositiveText("Zakończ")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveResults();
                    }
                });
        if(setTestResultIcon()==0){
            exitAppDialog.setStyle(Style.HEADER_WITH_TITLE);
            exitAppDialog.setTitle("Spróbuj ponownie");
            exitAppDialog.setDescription(String.format("Zdobyłeś %s %% prawidłowych odpowiedzi. Zdobądź minimum 50%% aby otrzymać osiągnięcie", TestDataHelper.calculatePercentage()));
        } else {
            exitAppDialog.setStyle(Style.HEADER_WITH_ICON);
            exitAppDialog.setIcon(setTestResultIcon());
            exitAppDialog.setDescription(String.format("Gratulacje, %s %% prawidłowych odpowiedzi", TestDataHelper.calculatePercentage()));
        }
        return exitAppDialog;
    }

    private MaterialStyledDialog.Builder createEndDialogNoLesson(final Activity activity){
        return new MaterialStyledDialog.Builder(activity)
                .withDialogAnimation(true)
                .withDivider(true)
                .setHeaderColor(R.color.colorAccent)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setTitle("Gratulacje")
                .setDescription("Odpowiedziałeś poprawnie na " + TestDataHelper.manyGoodAnswer + " z " + TestDataHelper.amountOfWords + " pytań, co stanowi " + TestDataHelper.calculatePercentage() + "% wszystkich odpowiedzi.")
                .setNegativeText("Powtórz")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TestDataHelper.cleanVariablesAfterRecreate();
                        activity.recreate();
                    }
                })
                .setPositiveText("Zakończ")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TestDataHelper.cleanVariablesAfterFinish();
                        activity.finish();
                    }
                });
    }

    private void showEndTestAlertDialog(Activity activity){
        if (TestDataHelper.isTestFromLesson){
            createEndDialogFromLesson().show();
        } else {
          createEndDialogNoLesson(activity).show();
        }
    }

    private int setTestResultIcon() {
        int progressPercent = (int) TestDataHelper.calculatePercentage();
        if (progressPercent >= 50 && progressPercent < 75 ){
            return R.drawable.ic_crown_bronze_icon;
        } else if (progressPercent >= 75 && progressPercent < 90){
            return R.drawable.ic_crown_silver_icon;
        } else if (progressPercent >= 90){
            return R.drawable.ic_crown_gold_icon;
        } else {
            return 0;
        }
    }

    private void saveResults() {
        saveTestResultsToDB();
        TestDataHelper.cleanVariablesAfterFinish();
        getActivity().finish();
    }

    private void saveResultsAndRecreate() {
        saveTestResultsToDB();
        TestDataHelper.cleanVariablesAfterRecreate();
        getActivity().recreate();
    }

    private void saveTestResultsToDB(){
        VocabularyDatabase dbInstance = VocabularyDatabase.getInstance(getContext());
        dbInstance.saveTestResult(getIconResultNumber(TestDataHelper.calculatePercentage()), TestDataHelper.lvlOfLanguage, TestDataHelper.categoryName);
    }

    private int getIconResultNumber(double testResult){
        if (testResult >= 50 && testResult < 75 ){
            return 1;
        } else if (testResult >= 75 && testResult < 90){
            return 2;
        } else if (testResult >= 90){
            return 3;
        } else {
            return 0;
        }
    }

}
