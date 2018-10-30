package vocabulary_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pl.flanelowapopijava.duel_with_english.R;

public class VocabularyTestPreference extends FragmentActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vocabulary_test_preference);
        setToolbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.preferenceTestToolbar);
        toolbar.setTitle("Personalizuj test");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static class VocabularyTestPreferenceFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.personalize_test);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.setBackgroundColor(getResources().getColor(R.color.Background));
        }
    }

    public void startTestClick(View view){
        Intent intent = new Intent(this, VocabularyTest.class);
        TestDataHelper.amountOfButtons = Integer.valueOf(sharedPreferences.getString("amountOfButtons", ""));
        TestDataHelper.amountOfWords = Integer.valueOf(sharedPreferences.getString("wordsAmount", ""));
        TestDataHelper.lvlOfLanguage = sharedPreferences.getString("levelOfLanguage", "");
        TestDataHelper.isTestFromLesson = false;
        startActivity(intent);
    }
}
