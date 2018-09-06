package lessons_vocabulary_list;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import database_vocabulary.CategoryDatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.TestDataHelper;
import vocabulary_test.VocabularyTest;

public class LessonsVocabularyList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VocabularyDatabase dbInstance;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private LessonsVocabularyListAdapter adapterVocabulary;
    private CategoryListAdapter categoryListAdapter;
    private boolean isFirstListClick = true;

    public boolean isFirstListClick() {
        return isFirstListClick;
    }

    public void setFalseFirstListClick() {
        isFirstListClick = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);
        categoryListAdapter = new CategoryListAdapter(getApplicationContext());
        adapterVocabulary = new LessonsVocabularyListAdapter(getApplicationContext());
        adapterVocabulary.setLevelLanguage(getIntent().getStringExtra("levelLanguage"));
        adapterVocabulary.setCategoryName(getIntent().getStringExtra("categoryName"));
        setToolbar();
        setDrawer();
    }

    @Override
    protected void onResume() {
        categoryListAdapter.setCursorCategory(dbInstance.showAllOfCategory(adapterVocabulary.getLevelLanguage()));
        categoryListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        dbInstance.close();
        super.onDestroy();
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.vocabularyListToolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle(adapterVocabulary.getLevelLanguage());
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
        dbInstance = VocabularyDatabase.getInstance(getApplicationContext());
        categoryListAdapter.setCursorCategory(dbInstance.showAllOfCategory(adapterVocabulary.getLevelLanguage()));
        categoryList.setAdapter(categoryListAdapter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isFirstListClick()){
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                setFalseFirstListClick();
                }
                drawer.closeDrawer(GravityCompat.START);
                Cursor tempCategoryCursor = categoryListAdapter.getCursorCategory();
                tempCategoryCursor.moveToPosition(i);
                adapterVocabulary.setCategoryName(tempCategoryCursor.getString(CategoryDatabaseColumnNames.categoryColumn));

                toolbar.setTitle(adapterVocabulary.getCategoryName());
                setVocabularyList();
            }
        });
    }

    private void setVocabularyList(){                                                               //set list of words lesson --> adapter to listview
        ListView vocabularyList = (ListView) findViewById(R.id.vocabularyListView);
        adapterVocabulary.setCursor(dbInstance.showVocabularyForLessons(adapterVocabulary.getLevelLanguage(), adapterVocabulary.getCategoryName(), adapterVocabulary.isAlphabeticalSort()));
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
        getMenuInflater().inflate(R.menu.lessons_vocabulary_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addToFavouriteWordsStar) {
            if (adapterVocabulary.isStarVisible()) {
                adapterVocabulary.setStarVisible(false);
            } else {
                adapterVocabulary.setStarVisible(true);
            }
            adapterVocabulary.setCursor(dbInstance.showVocabularyForLessons(adapterVocabulary.getLevelLanguage(), adapterVocabulary.getCategoryName(), adapterVocabulary.isAlphabeticalSort()));
            adapterVocabulary.notifyDataSetChanged();
        } else if (id == R.id.goToFavouriteFromMenu) {
            Intent intent = new Intent(this, FavouriteList.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.LessonMenuSortIcon) {
            if (adapterVocabulary.isAlphabeticalSort()) {
                adapterVocabulary.setAlphabeticalSort(false);
            } else {
                adapterVocabulary.setAlphabeticalSort(true);
            }
            adapterVocabulary.setCursor(dbInstance.showVocabularyForLessons(adapterVocabulary.getLevelLanguage(), adapterVocabulary.getCategoryName(), adapterVocabulary.isAlphabeticalSort()));
            adapterVocabulary.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startTestFromLessonOnClick(View view) {
        TestDataHelper.amountOfWords = adapterVocabulary.getCount()-2;
        TestDataHelper.amountOfButtons = 6;
        TestDataHelper.lvlOfLanguage = adapterVocabulary.getLevelLanguage();
        TestDataHelper.categoryName = adapterVocabulary.getCategoryName();
        TestDataHelper.isTestFromLesson = true;

        Log.d("dupa", "" + TestDataHelper.amountOfWords + "" + TestDataHelper.amountOfButtons + "" + TestDataHelper.lvlOfLanguage + "" + TestDataHelper.categoryName + "" + TestDataHelper.isTestFromLesson);

        Intent intent = new Intent(this, VocabularyTest.class);
        startActivity(intent);
    }
}
