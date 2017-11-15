package favourite_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

import static pl.flanelowapopijava.duel_with_english.R.id.favouriteListView;

public class FavouriteList extends AppCompatActivity {

    private VocabularyDatabase database;
    private Cursor cursor;
    private FavouriteResAdapted adapter;
    private ListView favouritelist;
    private Context context;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SP_ALPHABETICAL_NAME = "alphabeticalSort";
    private final String SP_FAVOURITE_LVL_NAME = "lvlOfFavourite";

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        setToolbar();
        initResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cursor.close();
        database.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.favouriteMenuSortIcon: {
                if (adapter.isAlphabeticalSort()) {
                    adapter.setAlphabeticalSort(false);
                } else {
                    adapter.setAlphabeticalSort(true);
                }
                break;
            }
            case R.id.favBarDeleteIcon: {
                if (adapter.getCursor().getCount()==0){
                    Toast.makeText(context, "Nie ma elementów do usunięcia", Toast.LENGTH_SHORT).show();
                }
                else {
                    toolbar = (Toolbar) findViewById(R.id.favouriteListToolbar);
                    if (adapter.isEnabledDeleteMode()) {
                        adapter.setEnabledDeleteMode(false);
                        favouritelist.setClickable(false);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Log.d("ONCliCK", "" + adapter.isEnabledDeleteMode());
                    } else {
                        adapter.setEnabledDeleteMode(true);
                        favouritelist.setClickable(true);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.delete_list_items));
                        Log.d("ONCliCK", "" + adapter.isEnabledDeleteMode());
                    }
                }
                break;
            }
        }
            if (id == R.id.menuIconSortA1) {
                setFavouriteWordsLvlSP("A1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort());
            } else if (id == R.id.menuIconSortA2) {
                setFavouriteWordsLvlSP("A2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort());
            } else if (id == R.id.menuIconSortB1) {
                setFavouriteWordsLvlSP("B1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort());
            } else if (id == R.id.menuIconSortB2) {
                setFavouriteWordsLvlSP("B2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort());
            } else if (id == R.id.menuIconSortC1) {
                setFavouriteWordsLvlSP("C1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortC2) {
                setFavouriteWordsLvlSP("C2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            }
        refreshList();
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.favouriteListToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initResources(){
        favouritelist = (ListView) findViewById(favouriteListView);
        favouritelist.setClickable(false);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setFavouriteWordsLvlSP("A1");
        setAlphabeticalSortSP(false);
        editor.apply();
        context = getApplicationContext();
        database = new VocabularyDatabase(context);
        adapter = new FavouriteResAdapted(context);
        cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort());
        adapter = new FavouriteResAdapted(cursor, context);
        favouritelist.setAdapter(adapter);
    }

    public void refreshList(){
        adapter.setCursor(database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), adapter.isAlphabeticalSort()));
        adapter.notifyDataSetChanged();
    }

    public boolean getAlphabeticalSortSP(SharedPreferences sharedPreferences){
        return sharedPreferences.getBoolean(SP_ALPHABETICAL_NAME, false);
    }

    public String getFavouriteWordsLvlSP(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(SP_FAVOURITE_LVL_NAME, "");
    }

    public void setFavouriteWordsLvlSP(String lvl){
        editor.putString(SP_FAVOURITE_LVL_NAME, lvl);
        editor.apply();
    }

    public void setAlphabeticalSortSP(boolean isAlphabetical){
        editor.putBoolean(SP_ALPHABETICAL_NAME, isAlphabetical);
        editor.apply();
    }
}
