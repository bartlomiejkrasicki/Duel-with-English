package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LessonsVocabularyList extends AppCompatActivity {

    private LessonsVocabularyListAdapter adapter;
    private List<String> vocabularyLessonsPL;
    private List<String> vocabularyLessonsEN;
    private List<String> vocabularyFavouritePL;
    private List<String> vocabularyFavouriteEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lessons_vocabulary);
        setSupportActionBar(toolbar);


        ListView LVVocabularyLessons = (ListView) findViewById(R.id.lessonsVocabularyListView);
        vocabularyFavouritePL = new ArrayList<>();
        vocabularyFavouriteEN = new ArrayList<>();


        Intent intent = getIntent();
        int i = intent.getIntExtra("LIST_CHOICE_GROUP", 200);
        int i1 = intent.getIntExtra("LIST_CHOICE_ITEM", 200);
        initLessons(i, i1);
        adapter = new LessonsVocabularyListAdapter(this, vocabularyLessonsPL, vocabularyLessonsEN);
        LVVocabularyLessons.setAdapter(adapter);
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
