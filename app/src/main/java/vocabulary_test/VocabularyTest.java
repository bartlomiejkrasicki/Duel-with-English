package vocabulary_test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;
import test_fragments.VocabularyTestJigsawWordFragment;
import test_fragments.VocabularyTestWriteFragment;

public class VocabularyTest extends FragmentActivity {

    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private RoundCornerProgressBar testProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setToolbarOnClick();
        declarationVariables();
        prepareProgressBar();
        showFirstFragment();
    }

    private void setToolbarOnClick() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.testVocabularyToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEndTestDialog().show();
            }
        });
    }

    private void declarationVariables() {
        dbInstance = VocabularyDatabase.getInstance(getApplicationContext());
        if (TestDataHelper.isTestFromLesson && !TestDataHelper.categoryName.equals("")) {
            cursor = dbInstance.getCategoryValues(TestDataHelper.categoryName, TestDataHelper.lvlOfLanguage);
            TestDataHelper.amountOfButtons = 6;
        } else {
            cursor = dbInstance.getAllLvlValues(TestDataHelper.lvlOfLanguage);
        }

        if (cursor.getCount() < TestDataHelper.amountOfButtons) {
            TestDataHelper.amountOfButtons = cursor.getCount() - cursor.getCount() % 2;
        }
        TestDataHelper.wordTable = TestDataHelper.prepareWordRandomTable();
        TestDataHelper.inEnglishSetRandom();
        Log.d("tag",TestDataHelper.isTestFromLesson + "");
    }

    private void showFirstFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fragment_fade_in, R.anim.anim_fragment_fade_out);
        Object firstFragmentInstance = getFirstFragmentObject(getRandomFirstFragmentNumber());
        fragmentTransaction.add(R.id.testFragment, (Fragment) firstFragmentInstance).commit();
    }

    private int getRandomFirstFragmentNumber(){
        if(cursor.getCount() < 2)
            return TestDataHelper.getRandomNumber(3);
        else
            return TestDataHelper.getRandomNumber(2);
    }

    private Object getFirstFragmentObject(int firstFragmentNumber) {
        switch (firstFragmentNumber) {
            case 0: {
                return new VocabularyTestWriteFragment();
            }
            case 1: {
                return new VocabularyTestJigsawWordFragment();
            }
            case 2: {
                return new VocabularyTestWriteFragment();
            }
            default: {
                Toast.makeText(this, "Błąd wczytywania testu. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    private void prepareProgressBar(){
        testProgressBar = (RoundCornerProgressBar) findViewById(R.id.testProgressBar);
        testProgressBar.setMax(TestDataHelper.amountOfWords);
        testProgressBar.setProgress(0);
    }

    private MaterialStyledDialog.Builder getEndTestDialog(){
        return new MaterialStyledDialog.Builder(this)
                .setTitle("Zakończyć test?")
                .setDescription("Spowoduje to utratę bieżących postępów.")
                .withDialogAnimation(true)
                .withDivider(true)
                .setPositiveText("Tak")
                .setNegativeText("Nie")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderColor(R.color.colorAccent)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TestDataHelper.cleanVariablesAfterFinish();
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        getEndTestDialog().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbInstance.close();
    }

    @Override
    public void recreate() {
        super.recreate();
        testProgressBar.setProgress(0);
        cursor.close();
        dbInstance.close();
    }
}
