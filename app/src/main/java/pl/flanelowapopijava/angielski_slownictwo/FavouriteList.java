package pl.flanelowapopijava.angielski_slownictwo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class FavouriteList extends AppCompatActivity {

    private FavouriteListAdapter adapter;
    private LessonsVocabularyList getResources;
    private List<String> vocabularyFavouriteEN;
    private List<String> vocabularyFavouritePL;
    private ListView favouriteListView;
    private SharedPreferences preferences;
    private final String WORDS = "pl.flanelowapopijava.angielski_slownictwo.favouriteWords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favouriteListView = (ListView) findViewById(R.id.favouriteList);
//        vocabularyFavouriteEN = getResources.getVocabularyFavouriteEN();
//        vocabularyFavouritePL = getResources.getVocabularyFavouritePL();
        preferences = getSharedPreferences(WORDS, MODE_PRIVATE);

        adapter = new FavouriteListAdapter(vocabularyFavouriteEN, vocabularyFavouritePL, this, preferences);
        favouriteListView.setAdapter(adapter);
    }

}
