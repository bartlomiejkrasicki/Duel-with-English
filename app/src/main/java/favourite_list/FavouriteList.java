package favourite_list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;

public class FavouriteList extends AppCompatActivity {

    private VocabularyDatabase database;
    private Cursor cursor;
    private FavouriteListAdapter adapter;
    private ListView favouritelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        database = new VocabularyDatabase(getApplicationContext());
        cursor = database.getFavouriteValues();
        favouritelist = (ListView) findViewById(R.id.favouriteList);
        adapter = new FavouriteListAdapter(this, cursor, favouritelist, database);
        favouritelist.setAdapter(adapter);
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
}
