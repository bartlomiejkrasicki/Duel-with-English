package tenses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

public class TensesList extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public final static String PLTOENTRANSLATESP = "pltoentranslate";
    private VocabularyDatabase vocabularyDatabase;
    private SearchHistAdapter searchHistAdapter;
    private android.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses_list);
        createTranslateSP();
        addSearchField();
        vocabularyDatabase = new VocabularyDatabase(getApplicationContext());
        searchHistAdapter = new SearchHistAdapter(getApplicationContext());
        ListView searchListView = (ListView) findViewById(R.id.searchHintList);
        searchListView.setAdapter(searchHistAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        searchView = (android.widget.SearchView) menu.findItem(R.id.searchVocabularyMenuItem).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    private void addSearchField(){
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String queryText) {
//                searchHistAdapter.setCursor(vocabularyDatabase.getValuesToSearch(DatabaseColumnNames.COLUMN_NAME_PLWORD, queryText));
//                searchHistAdapter.notifyDataSetChanged();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String queryText) {
//                searchHistAdapter.setCursor(vocabularyDatabase.getValuesToSearch(DatabaseColumnNames.COLUMN_NAME_PLWORD, queryText));
//                searchHistAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });
    }

    private void createTranslateSP(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PLTOENTRANSLATESP, true);
        editor.apply();
    }
}
