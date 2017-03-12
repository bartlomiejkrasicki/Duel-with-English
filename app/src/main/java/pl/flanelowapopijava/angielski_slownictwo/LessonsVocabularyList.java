package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;

public class LessonsVocabularyList extends AppCompatActivity {

    private LessonsVocabularyListAdapter adapter;
    public static List<String> vocabularyLessonsPL;
    public static List<String> vocabularyLessonsEN;
    private VocabularyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lessons_vocabulary);
        setSupportActionBar(toolbar);

        ListView LVVocabularyLessons = (ListView) findViewById(R.id.lessonsVocabularyListView);
        Intent intent = getIntent();
        int i = intent.getIntExtra("LIST_CHOICE_GROUP", 200);
        int i1 = intent.getIntExtra("LIST_CHOICE_ITEM", 200);

        Context context = getApplicationContext();
        context.deleteDatabase(DatabaseColumnNames.DATABASE_NAME);
        database = new VocabularyDatabase(getApplicationContext());
        database.initData();
        Cursor cursor = database.getValues();
        adapter = new LessonsVocabularyListAdapter(this, database, cursor);
        LVVocabularyLessons.setAdapter(adapter);
        cursor.close();
        database.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vocabulary_lessons_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.addToFavouriteWordsStar){
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }




    private void initLessons(int i, int i1){
        vocabularyLessonsPL = new ArrayList<>();
        vocabularyLessonsEN = new ArrayList<>();
        vocabularyLessonsPL.clear();
        vocabularyLessonsPL.clear();
        switch (i){
            case 0:{
                switch (i1){
                    case 0:{
                        vocabularyLessonsPL.add("skakać");
                        vocabularyLessonsPL.add("tańczyć");
                        vocabularyLessonsPL.add("umierać");
                        vocabularyLessonsPL.add("reszta");

                        vocabularyLessonsEN.add("jump");
                        vocabularyLessonsEN.add("dance");
                        vocabularyLessonsEN.add("die");
                        vocabularyLessonsEN.add("change");
                        break;
                        }
                    default:{
                        break;
                    }
                }
                break;
            }
            case 1:{
                switch (i1){
                    case 0:{
                        vocabularyLessonsPL.add("śpiewać");
                        vocabularyLessonsPL.add("strzelać");
                        vocabularyLessonsPL.add("krzesło");
                        vocabularyLessonsPL.add("kraśc");

                        vocabularyLessonsEN.add("sing");
                        vocabularyLessonsEN.add("shoot");
                        vocabularyLessonsEN.add("chair");
                        vocabularyLessonsEN.add("steal");
                        break;
                    }
                }
                break;
            }
            default:{
                break;
            }
        }
    }
}
