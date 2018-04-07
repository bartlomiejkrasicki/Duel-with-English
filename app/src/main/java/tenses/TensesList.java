package tenses;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

public class TensesList extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public final static String PLTOENTRANSLATESP = "pltoentranslate";
    private VocabularyDatabase vocabularyDatabase;
    private SearchHistAdapter searchHistAdapter;
    private Cursor cursor;
    private int currentDbID = 0;
    private ListView searchListView;
    private CardView wordCardView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses_list);
        createTranslateSP();
        addSearchField();
        wordCardView = (CardView) findViewById(R.id.translateCardView);
        vocabularyDatabase = new VocabularyDatabase(getApplicationContext());
        searchHistAdapter = new SearchHistAdapter(vocabularyDatabase.getAllValues(), getApplicationContext());
        searchListView = (ListView) findViewById(R.id.searchHintList);
        searchListView.setAdapter(searchHistAdapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                wordCardViewVisible();
                fillCardViewData(i);
                animation();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchVocabularyMenuItem).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(InputMethodManager.HIDE_NOT_ALWAYS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery(s,false);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchHistAdapter.setCursor(vocabularyDatabase.getValuesToSearch(sharedPreferences.getBoolean(PLTOENTRANSLATESP, true), s));
                searchHistAdapter.notifyDataSetChanged();
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchListView.setVisibility(View.VISIBLE);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.changeTranslateLanguageIS) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean(PLTOENTRANSLATESP,false)){
                editor.putBoolean(PLTOENTRANSLATESP, false);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star_silver));
            }
            else {
                editor.putBoolean(PLTOENTRANSLATESP, true);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star_gold));
            }
            editor.apply();
            searchHistAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSearchField() {

    }

    private void fillCardViewData(int position) {
        VocabularyDatabase vocabularyDatabase = new VocabularyDatabase(getApplicationContext());
        int id = searchHistAdapter.getDBItemId(position);
        currentDbID = id;
        cursor = vocabularyDatabase.getRowFromId(id);
        cursor.moveToFirst();
        boolean isPlToEn = sharedPreferences.getBoolean(PLTOENTRANSLATESP, true);
        TextView mainSearchWord = (TextView) findViewById(R.id.searchCardViewMainWord);
        TextView secondarySearchWord = (TextView) findViewById(R.id.searchCardViewSecondaryWord);
        if (isPlToEn){
            mainSearchWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
            secondarySearchWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
        } else {
            mainSearchWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
            secondarySearchWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        }
        ImageView searchWordFavouriteStar = (ImageView) findViewById(R.id.searchCardViewFavouriteStar);
        if (cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 1) {
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
        } else {
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
        }
    }

    private void createTranslateSP() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PLTOENTRANSLATESP, true);
        editor.apply();
    }

    private void wordCardViewVisible() {
        wordCardView.setVisibility(View.VISIBLE);
    }

    public void favouriteStarOnClick(View view) {
        String index = String.valueOf(cursor.getInt(DatabaseColumnNames.idColumn));
        ImageView searchWordFavouriteStar = (ImageView) findViewById(R.id.searchCardViewFavouriteStar);
        if (cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 0) {
            vocabularyDatabase.updateValuesInDatabase(index, 1);
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
            Toast.makeText(getApplicationContext(), "Słowo zostało dodane do listy ulubionych", Toast.LENGTH_SHORT).show();
        } else if (cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 1) {
            vocabularyDatabase.updateValuesInDatabase(index, 0);
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
            Toast.makeText(getApplicationContext(), "Słowo zostało usunięte z listy ulubionych", Toast.LENGTH_SHORT).show();
        }
        refreshCursor();
    }

    private void refreshCursor() {
        cursor = vocabularyDatabase.getRowFromId(currentDbID);
        cursor.moveToFirst();
    }

    private void animation() {

        final Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fragment_fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fragment_fade_out);

        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wordCardView.startAnimation(animationFadeIn);
                searchListView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        searchListView.startAnimation(animationFadeOut);
    }
}