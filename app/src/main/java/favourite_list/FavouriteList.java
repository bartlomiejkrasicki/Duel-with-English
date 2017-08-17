package favourite_list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

public class FavouriteList extends AppCompatActivity {

    private VocabularyDatabase database;
    private Cursor cursor;
    private FavouriteListAdapter adapter;
    private Snackbar infoSelectedItems;
    static int selected = 0;
    private ListView favouritelist;
    private Context context;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        initResources();
        favouritelist.setAdapter(adapter);
        fabInitialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    private void fabInitialize(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(fab.isSelected())) {
                    fab.setSelected(true);
                    fab.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.delete_list_items)));
                    fab.setImageResource(R.drawable.delete_sweep);
                    infoSelectedItems = Snackbar.make(view, "Zaznaczono " + selected + " elementów. \nAby usunąć naciśnij jeszcze raz ikonę kosza.", Snackbar.LENGTH_INDEFINITE);
                    infoSelectedItems.show();

                    listItemClickReaction();
//                            switch (fab.getTag().toString()){
//                                case "1":{
//                                    break;
//                                }
//                                case "0":{
//                                    break;
//                                }
//                                default:{
//                                    Toast.makeText(context, "Coś poszło nie tak :(", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                                }
//                            }
//                        });
                }
                else if (fab.isSelected()){
                    fab.setSelected(false);
                    infoSelectedItems.dismiss();
                    fab.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.color_buttons_special)));
                    fab.setImageResource(android.R.drawable.ic_menu_delete);
                    SparseBooleanArray booleanArray = favouritelist.getCheckedItemPositions();
                    for(int i = 0; i < adapter.getCount(); i++){
                        if (booleanArray.get(i)){
                            favouritelist.getChildAt(i).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            favouritelist.getChildAt(i).setTag(0);
                            cursor.moveToPosition(i);
                            String index = String.valueOf(cursor.getInt(0));
                            database.updateValuesInDatabase(index, 0, DatabaseColumnNames.TABLE_NAME_A1);
                        }
                    }
                    Toast.makeText(context, "Usuwanie zakończone pomyślnie", Toast.LENGTH_SHORT).show();
                    selected = 0;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void listItemClickReaction() {
        favouritelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (fab.isSelected()) {
//                    if (view.getTag() == null) {
//                        view.setTag(0);
//                    }
                if (view.getTag() == null || view.getTag().equals(0)) {
                    view.setBackgroundColor(view.getResources().getColor(R.color.delete_list_items));
                    favouritelist.setSelection(i);
                    view.setTag(1);
                    selected++;
                    favouritelist.setItemChecked(i, true);
                }
                else {
                    view.setBackgroundColor(view.getResources().getColor(android.R.color.transparent));
                    view.setSelected(false);
                    view.setTag(0);
                    selected--;
                    favouritelist.setItemChecked(i, false);
                }
                infoSelectedItems.setText("Zaznaczono " + selected + " elementów. \nAby usunąć naciśnij jeszcze raz ikonę kosza.");
                }
            }
        });
    }

    private void initResources(){
        favouritelist = (ListView) findViewById(R.id.favouriteListButton);
        fab = (FloatingActionButton) findViewById(R.id.floatingDeleteButton);
        context = getApplicationContext();
        database = new VocabularyDatabase(context);
        cursor = database.getFavouriteValues(DatabaseColumnNames.TABLE_NAME_A1);
        adapter = new FavouriteListAdapter(this, cursor, favouritelist);
    }
}
