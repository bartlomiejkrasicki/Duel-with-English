package lessons_vocabulary_list;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;

public class LessonsVocabularyList extends AppCompatActivity {

    private final String PREFERENCES_NAME = "applicationPreferences";
    private final String PREFERENCES_DATABASE_INITDATA = "dataIsInit";

    private LessonsVocabularyListAdapter adapter;
    private VocabularyDatabase database;
    private Cursor cursor;
    private SharedPreferences preferences;
    private ListView LVVocabularyLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lessons_vocabulary);
        setSupportActionBar(toolbar);

        LVVocabularyLessons = (ListView) findViewById(R.id.lessonsVocabularyListView);
        Intent intent = getIntent();
        int i = intent.getIntExtra("LIST_CHOICE_GROUP", 200);
        int i1 = intent.getIntExtra("LIST_CHOICE_ITEM", 200);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

        database = new VocabularyDatabase(getApplicationContext());
        if (!(preferences.getString(PREFERENCES_DATABASE_INITDATA, "").equals("INITED"))) {
            database.initData();
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String initDataOk = "INITED";
            preferencesEditor.putString(PREFERENCES_DATABASE_INITDATA, initDataOk);
            preferencesEditor.apply();
        }
        cursor = database.getSpecificValues(i, i1);
        adapter = new LessonsVocabularyListAdapter(this, cursor, i, i1, LVVocabularyLessons);
        LVVocabularyLessons.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vocabulary_lessons_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.addToFavouriteWordsStar){
            String tag = LVVocabularyLessons.getTag().toString();
            if(tag.equals("0")){
                LVVocabularyLessons.setTag(1);
            } else {
                LVVocabularyLessons.setTag(0);
            }
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
