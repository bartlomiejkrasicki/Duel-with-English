package dictionary;

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

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

public class Dictionary extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public final static String PLTOENTRANSLATESP = "pltoentranslate";
    private VocabularyDatabase dbInstance;
    private SearchHistAdapter searchHistAdapter;
    private Cursor cursor;
    private int currentDbID = 0;
    private ListView searchListView;
    private CardView wordCardView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        createTranslateSP();
        declareVariables();
        searchListView.setOnItemClickListener(searchListViewOnClick);
    }

    private void declareVariables() {
        wordCardView = (CardView) findViewById(R.id.translateCardView);
        dbInstance = VocabularyDatabase.getInstance(getApplicationContext());
        searchHistAdapter = new SearchHistAdapter(dbInstance.getAllValues(), getApplicationContext());
        searchListView = (ListView) findViewById(R.id.searchHintList);
        searchListView.setAdapter(searchHistAdapter);
    }

    private AdapterView.OnItemClickListener searchListViewOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.setFocusable(false);
            searchView.clearFocus();
            wordCardViewVisible();
            fillCardViewData(i);
            animation();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchViewDeclaration(menu, searchManager);
        searchViewSetOnQuery();
        searchViewVisibleConfigure();
        return true;
    }

    private void searchViewDeclaration(Menu menu, SearchManager searchManager) {
        searchView = (SearchView) menu.findItem(R.id.searchVocabularyMenuItem).getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchImageView = (ImageView) searchView.findViewById(searchImgId);
        searchImageView.setImageResource(R.drawable.ic_magnifier_icon);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
    }

    private void searchViewSetOnQuery() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery(s,false);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.matches(".*[!-@]") || s.matches(".*[!-@].*")){
                    s = s.substring(0, s.length()-1);
                    searchView.setQuery(s.toLowerCase(),false);
                } else {
                    searchHistAdapter.setCursor(dbInstance.getValuesToSearch(sharedPreferences.getBoolean(PLTOENTRANSLATESP, true), s));
                    searchHistAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    private void searchViewVisibleConfigure() {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchListView.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchListView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    searchListView.setVisibility(View.VISIBLE);
                } else {
                    searchListView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.changeTranslateLanguageIS) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean(PLTOENTRANSLATESP,false)){
                editor.putBoolean(PLTOENTRANSLATESP, false);
                item.setIcon(getResources().getDrawable(R.drawable.ic_en_to_pl_icon));
            }
            else {
                editor.putBoolean(PLTOENTRANSLATESP, true);
                item.setIcon(getResources().getDrawable(R.drawable.ic_pl_to_en_icon));
            }
            editor.apply();
            searchHistAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillCardViewData(int position) {
        int id = searchHistAdapter.getDBItemId(position);
        currentDbID = id;
        cursor = dbInstance.getRowFromId(id);
        cursor.moveToFirst();
        boolean isPlToEn = sharedPreferences.getBoolean(PLTOENTRANSLATESP, true);
        TextView mainSearchWord = (TextView) findViewById(R.id.searchCardViewMainWord);
        TextView secondarySearchWord = (TextView) findViewById(R.id.searchCardViewSecondaryWord);
        if (isPlToEn){
            mainSearchWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
            secondarySearchWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
        } else {
            mainSearchWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
            secondarySearchWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        }
        ImageView searchWordFavouriteStar = (ImageView) findViewById(R.id.searchCardViewFavouriteStar);
        if (cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn) == 1) {
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_favouriteiconon));
        } else {
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_favouriteiconoff));
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
        String index = String.valueOf(cursor.getInt(VocabularyDatabaseColumnNames.idColumn));
        ImageView searchWordFavouriteStar = (ImageView) findViewById(R.id.searchCardViewFavouriteStar);
        if (cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn) == 0) {
            dbInstance.updateValuesInDatabase(index, 1);
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_favouriteiconon));
            Toast.makeText(getApplicationContext(), "Słowo zostało dodane do listy ulubionych", Toast.LENGTH_SHORT).show();
        } else if (cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn) == 1) {
            dbInstance.updateValuesInDatabase(index, 0);
            searchWordFavouriteStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_favouriteiconoff));
            Toast.makeText(getApplicationContext(), "Słowo zostało usunięte z listy ulubionych", Toast.LENGTH_SHORT).show();
        }
        refreshCursor();
    }

    private void refreshCursor() {
        cursor = dbInstance.getRowFromId(currentDbID);
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