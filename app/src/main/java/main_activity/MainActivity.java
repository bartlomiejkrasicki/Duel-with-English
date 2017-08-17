package main_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import about_author.AuthorInformation;
import vocabulary_level_category.VocabularyCategory;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;
import vocabulary_test.VocabularyTestPreference;

//menu główne

public class MainActivity extends AppCompatActivity {

    Button vocabularyButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // wyświetla xml activity_main
        vocabularyButton = (Button) findViewById(R.id.button_vocabulary);
        context = getApplicationContext();
        configurationToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                 // menu
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

    private void configurationToolbar(){
        Toolbar mainMenuToolbar = (Toolbar) findViewById(R.id.mainMenuToolbar);
        mainMenuToolbar.setTitle(R.string.mainMenuSubtitle);
        mainMenuToolbar.setSubtitle(R.string.app_name);
        mainMenuToolbar.setLogo(R.mipmap.icon);
        setSupportActionBar(mainMenuToolbar);
    }

    public void vocabularyButtonOnClick(View view){         //klikniecie przycisku słownictwo
        Intent intent = new Intent(this, VocabularyCategory.class);
        startActivity(intent);
    }

    public void favouriteButtonOnClick(View view){          //kliknięcie przycisku ulubione
        Intent intent = new Intent(this, FavouriteList.class);
        startActivity(intent);
    }

    public void aboutAuthorButtonOnClick(View view){        //kliknięcie przycisku o autorze
        Intent intent = new Intent(this, AuthorInformation.class);
        startActivity(intent);
    }

    public void testButtonOnClick (View view) {
        Intent intent = new Intent(this, VocabularyTestPreference.class);
        startActivity(intent);
    }
}
