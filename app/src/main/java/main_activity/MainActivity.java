package main_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import database_vocabulary.VocabularyDatabase;
import dictionary.Dictionary;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_level_category.VocabularyCategory;
import vocabulary_test.VocabularyTestPreference;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurationToolbar();
        firstLaunch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.mainMenuInfoIcon){

        } else if (id == R.id.mainMenuSettingsIcon){
            intent = new Intent(this, MainAppSettings.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void configurationToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainMenuToolbar);
        setSupportActionBar(toolbar);
    }

    public void vocabularyButtonOnClick(View view){
        intent = new Intent(this, VocabularyCategory.class);
        startActivity(intent);
    }

    public void favouriteButtonOnClick(View view){
        intent = new Intent(this, FavouriteList.class);
        startActivity(intent);
    }

    public void testButtonOnClick (View view) {
        intent = new Intent(this, VocabularyTestPreference.class);
        startActivity(intent);
    }

    public void dictionaryButtonOnClick(View view) {
        intent = new Intent(this, Dictionary.class);
        startActivity(intent);
    }

    public void exitButtonOnClick(View view) {
        exitApp();
    }

    private void firstLaunch() {
        sharedPreferences = getSharedPreferences("pl.flanelowapopijava.duel_with_english", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstLaunch", true)){
            configureDatabase();
            setFirstLaunch();
        }
    }

    public void setFirstLaunch() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstLaunch", false);
        editor.apply();
    }

    private void configureDatabase(){
        VocabularyDatabase dbInstance = VocabularyDatabase.getInstance(getApplicationContext());
        dbInstance.allCategoryFromVocabulary();
        dbInstance.close();
    }

    private void exitApp(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Czy na pewno chcesz zamknąć aplikację?");
        alertDialogBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
