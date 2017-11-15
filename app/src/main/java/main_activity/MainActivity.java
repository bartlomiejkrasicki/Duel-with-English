package main_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import about_author.AuthorInformation;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;
import tenses.TensesList;
import vocabulary_level_category.VocabularyCategory;
import vocabulary_test.VocabularyTestPreference;

//menu główne

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurationToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("TAG", "tat" +item.getItemId());
        switch (id){
            case R.id.mainMenuSettings:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

    private void configurationToolbar(){
        Toolbar mainMenuToolbar = (Toolbar) findViewById(R.id.mainMenuToolbar);
        mainMenuToolbar.setTitle(R.string.mainMenuSubtitle);
        mainMenuToolbar.setSubtitle(R.string.app_name);
        mainMenuToolbar.setLogo(R.mipmap.icon);
        setSupportActionBar(mainMenuToolbar);
    }

    public void vocabularyButtonOnClick(View view){
        intent = new Intent(this, VocabularyCategory.class);
        startActivity(intent);
    }

    public void favouriteButtonOnClick(View view){
        intent = new Intent(this, FavouriteList.class);
        startActivity(intent);
    }

    public void aboutAuthorButtonOnClick(View view){        
        intent = new Intent(this, AuthorInformation.class);
        startActivity(intent);
    }

    public void testButtonOnClick (View view) {
        intent = new Intent(this, VocabularyTestPreference.class);
        startActivity(intent);
    }

    public void grammarButtonOnClick(View view) {
        intent = new Intent(this, TensesList.class);
        startActivity(intent);
    }
}
