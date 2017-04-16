package main_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import about_author.AuthorInformation;
import expandable_lessons_list.VocabularyExpandableList;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.angielski_slownictwo.R;
import vocabulary_test.VocabularyTestPreference;

//menu główne

public class MainActivity extends AppCompatActivity {

    Button vocabularyButton;
    Context context;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // wyświetla xml activity_main
        vocabularyButton = (Button) findViewById(R.id.button_vocabulary);
        context = getApplicationContext();
        configurationNavDrawer();
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
            case 16908332:{
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurationToolbar(){
        Toolbar mainMenuToolbar = (Toolbar) findViewById(R.id.mainMenuToolbar);
        mainMenuToolbar.setTitle(R.string.mainMenuSubtitle);
        mainMenuToolbar.setSubtitle(R.string.app_name);
        mainMenuToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainMenuToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
        }
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configurationNavDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMainMenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationMainMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

    }

    public void vocabularyButtonOnClick(View view){         //klikniecie przycisku słownictwo
        Intent intent = new Intent(this, VocabularyExpandableList.class);
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
