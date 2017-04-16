package vocabulary_test;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pl.flanelowapopijava.angielski_slownictwo.R;

public class VocabularyTestPreference extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vocabulary_test_preference);
        setToolbar();
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.preferenceTestToolbar);
        toolbar.setLogo(R.drawable.a_one_level);
        toolbar.setTitle("Personalizuj test");
        toolbar.setTitleMarginStart(10);
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

    }
}
