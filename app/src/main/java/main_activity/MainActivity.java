package main_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

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
        exitAppDialog();
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
        exitAppDialog();
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

    private void exitAppDialog(){
        MaterialStyledDialog.Builder exitAppDialog = new MaterialStyledDialog.Builder(this)
                .withDialogAnimation(true)
                .withDivider(true)
                .setTitle("Zamknąć aplikację?")
                .setHeaderColor(R.color.colorAccent)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setNegativeText("Nie")
                .setPositiveText("Tak")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                });
        exitAppDialog.show();
    }
}
