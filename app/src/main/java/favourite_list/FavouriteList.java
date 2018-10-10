package favourite_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

import static pl.flanelowapopijava.duel_with_english.R.id.favouriteListView;

public class FavouriteList extends AppCompatActivity {

    private VocabularyDatabase dbInstance;
    private FavouriteResAdapter adapter;
    private ListView favouriteList;
    private Context context;
    private Toolbar toolbar;

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
    protected void onDestroy() {
        adapter.getCursor().close();
        dbInstance.close();
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
                if (adapter.getShowAllFavouritesWords()){
                    adapter.setCursor(dbInstance.getAllFavouriteValues(adapter.isAlphabeticalSort()));
                } else {
                    adapter.setCursor(dbInstance.getFavouriteValues(adapter.getLvlOfFavouriteWords(), adapter.isAlphabeticalSort()));
                }
                refreshList();
                break;
            }
            case R.id.favBarDeleteIcon: {
                if (adapter.getCursor().getCount()==0){
                    Toast.makeText(context, "Nie ma elementów do usunięcia", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (adapter.isEnabledDeleteMode()) {
                        adapter.setEnabledDeleteMode(false);
                        favouriteList.setClickable(false);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        adapter.setEnabledDeleteMode(true);
                        favouriteList.setClickable(true);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.redColor));
                    }
                }
                break;
            }
        }
        switch (id) {
            case R.id.menuIconSortA1: {
                setCursorToShowFavList("A1");
                break;
            }
            case R.id.menuIconSortA2: {
                setCursorToShowFavList("A2");
                break;
            }
            case R.id.menuIconSortB1: {
                setCursorToShowFavList("B1");
                break;
            }
            case R.id.menuIconSortB2: {
                setCursorToShowFavList("B2");
                break;
            }
            case R.id.menuIconSortC1: {
                setCursorToShowFavList("C1");
                break;
            }
            case R.id.menuIconSortC2: {
                setCursorToShowFavList("C2");
                break;
            }
            case R.id.menuIconShowAllFavourites:{
                adapter.setCursor(dbInstance.getAllFavouriteValues(adapter.isAlphabeticalSort()));
                adapter.setShowAllFavouritesWords(true);
                break;
            }
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
        favouriteList = (ListView) findViewById(favouriteListView);
        favouriteList.setClickable(false);
        context = getApplicationContext();
        dbInstance = VocabularyDatabase.getInstance(context);
        adapter = new FavouriteResAdapter(context);
        adapter.setCursor(dbInstance.getAllFavouriteValues(adapter.isAlphabeticalSort()));
        favouriteList.setAdapter(adapter);
    }

    private void setCursorToShowFavList(String vocabularyLvl){
        adapter.setShowAllFavouritesWords(false);
        adapter.setLvlOfFavouriteWords(vocabularyLvl);
        adapter.setCursor(dbInstance.getFavouriteValues(adapter.getLvlOfFavouriteWords(), adapter.isAlphabeticalSort()));
    }

    public void refreshList(){
        adapter.notifyDataSetChanged();
    }


}
