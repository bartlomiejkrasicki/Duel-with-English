package lessons_vocabulary_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import database_vocabulary.VocabularyDatabase;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;

public class LessonsVocabularyList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VocabularyDatabase vocabularyDatabase;
    private DrawerLayout drawer;
    private String categoryName, levelLanguage;
    private Toolbar toolbar;
    private ListView vocabularyList;
    private LessonsVocabularyListAdapter adapterVocabulary;
    private Cursor cursorVocabulary;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SP_ALPHABETICAL_NAME = "alphabeticalSort";
    private boolean isFirstListClick = true;

    private void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private void setlevelLanguage(String levelLanguage) {
        this.levelLanguage = levelLanguage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);
        initVariables();
        setToolbar();
        setDrawer();
    }

    private void initVariables(){
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setAlphabeticalSortSP(false);
        editor.apply();
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.vocabularyListToolbar);
        setlevelLanguage(getIntent().getStringExtra("levelLanguage"));
        toolbar.setTitle(levelLanguage);
        setSupportActionBar(toolbar);
    }

    private void setDrawer(){                                                                       // set navigation drawer (hamburger menu)
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setCategoryList();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        drawer.openDrawer(navigationView);
    }

    private void setCategoryList(){                                                                 // set category list in nav drawer
        ListView categoryList = (ListView) findViewById(R.id.categoryListLessonDrawer);
        vocabularyDatabase = new VocabularyDatabase(getApplicationContext());
        Cursor cursorCategory = vocabularyDatabase.showAllOfCategory(levelLanguage);

        final ArrayList<String> categoryListValues = new ArrayList<>();                             //add categories to list
        while (cursorCategory.moveToNext()){
            categoryListValues.add(cursorCategory.getString(0));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categoryListValues);    //add categories to listview
        categoryList.setAdapter(arrayAdapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isFirstListClick){
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                isFirstListClick = false;
                }
                drawer.closeDrawer(GravityCompat.START);
                setCategoryName(categoryListValues.get(i));
                toolbar.setSubtitle(categoryName);
                setVocabularyList();
            }
        });
    }

    private void setVocabularyList(){                                                               //set list of words lesson --> adapter to listview
        vocabularyList = (ListView) findViewById(R.id.vocabularyListView);
        cursorVocabulary = vocabularyDatabase.showVocabularyForLessons(levelLanguage, categoryName, getAlphabeticalSortSP(sharedPreferences));
        adapterVocabulary = new LessonsVocabularyListAdapter(getApplicationContext(), cursorVocabulary, vocabularyList, levelLanguage, categoryName);
        vocabularyList.setAdapter(adapterVocabulary);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lessons_vocabulary_list2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addToFavouriteWordsStar) {
            String tag = vocabularyList.getTag().toString();
            if (tag.equals("0")) {
                vocabularyList.setTag(1);
            } else {
                vocabularyList.setTag(0);
            }
            adapterVocabulary.notifyDataSetChanged();
        } else if (id == R.id.goToFavouriteFromMenu) {
            Intent intent = new Intent(this, FavouriteList.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.LessonMenuSortIcon) {
            if (sharedPreferences.getBoolean(SP_ALPHABETICAL_NAME, false)) {
                setAlphabeticalSortSP(false);
                cursorVocabulary = vocabularyDatabase.showVocabularyForLessons(levelLanguage, categoryName, getAlphabeticalSortSP(sharedPreferences));
                adapterVocabulary.setCursor(cursorVocabulary);
            } else {
                setAlphabeticalSortSP(true);
                cursorVocabulary = vocabularyDatabase.showVocabularyForLessons(levelLanguage, categoryName, getAlphabeticalSortSP(sharedPreferences));
                adapterVocabulary.setCursor(cursorVocabulary);
            }
            adapterVocabulary.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setAlphabeticalSortSP(boolean isAlphabetical){
        editor.putBoolean(SP_ALPHABETICAL_NAME, isAlphabetical);
        editor.apply();
    }

    public boolean getAlphabeticalSortSP(SharedPreferences sharedPreferences){
        return sharedPreferences.getBoolean(SP_ALPHABETICAL_NAME, false);
    }

//    public void favStarOnClick(View view) {
//                VocabularyDatabase vocabularyDatabase = new VocabularyDatabase(context);
//                cursor.moveToPosition(i);
//                String index = String.valueOf(cursor.getInt(DatabaseColumnNames.idColumn));
//                if(cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 0) {
//                    vocabularyDatabase.updateValuesInDatabase(index, 1, levelLanguage);
//                    favouriteStar.setImageResource(android.R.drawable.star_big_on);
//                }
//                else if(cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 1){
//                    vocabularyDatabase.updateValuesInDatabase(index, 0, levelLanguage);
//                    favouriteStar.setImageResource(android.R.drawable.star_big_off);
//                }
//                notifyDataSetChanged();
//            }
//        });
//    }
}
