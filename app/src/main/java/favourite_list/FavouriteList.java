package favourite_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

public class FavouriteList extends AppCompatActivity {

    private VocabularyDatabase database;
    private Cursor cursor;
    private FavouriteListAdapter adapter;
    private ListView favouritelist;
    private Context context;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SP_ALPHABETICAL_NAME = "alphabeticalSort";
    private final String SP_FAVOURITE_LVL_NAME = "lvlOfFavourite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        initResources();
        fabInitialize();

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
            if (id == R.id.favouriteMenuSortIcon) {
                if (sharedPreferences.getBoolean(SP_ALPHABETICAL_NAME, false)) {
                    setAlphabeticalSortSP(false);
                    cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
                } else {
                    setAlphabeticalSortSP(true);
                    cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
                }
            } else if (id == R.id.menuIconSortA1) {
                setFavouriteWordsLvlSP("A1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortA2) {
                setFavouriteWordsLvlSP("A2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortB1) {
                setFavouriteWordsLvlSP("B1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortB2) {
                setFavouriteWordsLvlSP("B2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortC1) {
                setFavouriteWordsLvlSP("C1");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            } else if (id == R.id.menuIconSortC2) {
                setFavouriteWordsLvlSP("C2");
                cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
            }
            adapter.setCursor(cursor);
            adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void initResources(){
        favouritelist = (ListView) findViewById(R.id.favouriteListButton);
        favouritelist.setLongClickable(false);
        fab = (FloatingActionButton) findViewById(R.id.floatingDeleteButton);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setFavouriteWordsLvlSP("A1");
        setAlphabeticalSortSP(false);
        editor.apply();
        context = getApplicationContext();
        database = new VocabularyDatabase(context);
        cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
        if (cursor.getCount()==0) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        adapter = new FavouriteListAdapter(this, fab, cursor);
        favouritelist.setAdapter(adapter);
        favouritelist.setMultiChoiceModeListener(setMultiChoice());
    }

    private void fabInitialize(){
        fabSwitchOff();
        listItemClickReaction();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = adapter.getCursor();
                if (!(fab.isSelected())) {
                    fabSwitchOn();
                }
                else if (fab.isSelected()){
                    SparseBooleanArray booleanArray = favouritelist.getCheckedItemPositions();
                    for(int i = 0; i < adapter.getCount(); i++){
                        if (booleanArray.valueAt(i)){
                            favouritelist.getChildAt(i).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            favouritelist.getChildAt(i).setTag("0");
                            cursor.moveToPosition(i);
                            String index = String.valueOf(cursor.getInt(DatabaseColumnNames.idColumn));
                            database.updateValuesInDatabase(index, 0, getFavouriteWordsLvlSP(sharedPreferences));
                        }
                    }
                    Toast.makeText(context, "Usuwanie zakończone pomyślnie", Toast.LENGTH_SHORT).show();
                    refreshList();
                    fabSwitchOff();
                }
            }
        });
    }

    private void listItemClickReaction() {

    }

    private void fabSwitchOn(){
        fab.setSelected(true);
        favouritelist.setClickable(true);
        favouritelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.delete_list_items)));
        fab.setImageResource(R.drawable.delete_sweep);
    }

    private void fabSwitchOff(){
        fab.setSelected(false);
        favouritelist.setClickable(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_buttons_special)));
        fab.setImageResource(android.R.drawable.ic_menu_delete);
        favouritelist.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
    }

    private ListView.MultiChoiceModeListener setMultiChoice(){

        return new ListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                actionMode.setTitle("Zaznaczono " + favouritelist.getCheckedItemCount() + " elementów");
                if (favouritelist.getChildAt(i).isFocused()) {
                    favouritelist.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.delete_list_items));
                }
                refreshList();
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.favourite_menu_selection, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                if (fab.isSelected()) {
                    fabSwitchOff();
                }
            }
        };
    }

    private void refreshList(){
        cursor = database.getFavouriteValues(getFavouriteWordsLvlSP(sharedPreferences), getAlphabeticalSortSP(sharedPreferences));
        adapter.setCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private boolean getAlphabeticalSortSP(SharedPreferences sharedPreferences){
        return sharedPreferences.getBoolean(SP_ALPHABETICAL_NAME, false);
    }

    private String getFavouriteWordsLvlSP(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(SP_FAVOURITE_LVL_NAME, "");
    }

    private void setFavouriteWordsLvlSP(String lvl){
        editor.putString(SP_FAVOURITE_LVL_NAME, lvl);
        editor.apply();
    }

    private void setAlphabeticalSortSP(boolean isAlphabetical){
        editor.putBoolean(SP_ALPHABETICAL_NAME, isAlphabetical);
        editor.apply();
    }
}
