package expandable_lessons_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lessons_vocabulary_list.LessonsVocabularyList;
import pl.flanelowapopijava.angielski_slownictwo.R;

public class VocabularyExpandableList extends AppCompatActivity {

    //menu listy wyboru slownictwa
    private List<String> ListHeaderData;
    private HashMap<String, List<String>> ListDescriptionHash;
    private HashMap<String, List<String>> ListSubdescriptionHash;
    private List<Integer> categoriesImages;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabularylist);
        ExpandableListView EListView;
        ExpandableListAdapter EListAdapter;
        EListView = (ExpandableListView) findViewById(R.id.vocabularyList);
        initData();
        EListAdapter = new ExpandableListAdapter(this, ListHeaderData, categoriesImages, ListDescriptionHash, ListSubdescriptionHash);
        EListView.setAdapter(EListAdapter);
        EListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(VocabularyExpandableList.this, LessonsVocabularyList.class);
                intent.putExtra("LIST_CHOICE_GROUP", i);
                intent.putExtra("LIST_CHOICE_ITEM", i1);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vocabulary_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.vocabularyListClose){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {       //lists of English Vocabulary Themes
        ListHeaderData = new ArrayList<>();
        ListDescriptionHash = new HashMap<>();
        ListSubdescriptionHash = new HashMap<>();

        ListHeaderData.add("Poziom A1");    //groupText
        ListHeaderData.add("Poziom A2");
        ListHeaderData.add("Poziom B1");
        ListHeaderData.add("Poziom B2");
        ListHeaderData.add("Poziom C1");
        ListHeaderData.add("Poziom C2");

        categoriesImages = new ArrayList<>();

        categoriesImages.add(0, R.drawable.a_one_level);
        categoriesImages.add(1, R.drawable.a_two_level);
        categoriesImages.add(2, R.drawable.b_one_level);
        categoriesImages.add(3, R.drawable.b_two_level);
        categoriesImages.add(4, R.drawable.c_one_level);
        categoriesImages.add(5, R.drawable.c_two_level);




        List<String> A1Level = new ArrayList<>();      //list of Level Groups items
        A1Level.add("Szkoła");
        A1Level.add("Dom");
        A1Level.add("Rodzina");
        A1Level.add("Praca");
        A1Level.add("Kolory");
        A1Level.add("Liczby");

        List<String> A2Level = new ArrayList<>();
        A2Level.add("Szkoła");
        A2Level.add("Dom");
        A2Level.add("Rodzina");
        A2Level.add("Praca");
        A2Level.add("Kolory");
        A2Level.add("Liczby");

        List<String> B1Level = new ArrayList<>();
        B1Level.add("Szkoła");
        B1Level.add("Dom");
        B1Level.add("Rodzina");
        B1Level.add("Praca");
        B1Level.add("Kolory");
        B1Level.add("Liczby");

        List<String> B2Level = new ArrayList<>();
        B2Level.add("Szkoła");
        B2Level.add("Dom");
        B2Level.add("Rodzina");
        B2Level.add("Praca");
        B2Level.add("Kolory");
        B2Level.add("Liczby");

        List<String> C1Level = new ArrayList<>();
        C1Level.add("Szkoła");
        C1Level.add("Dom");
        C1Level.add("Rodzina");
        C1Level.add("Praca");
        C1Level.add("Kolory");
        C1Level.add("Liczby");

        List<String> C2Level = new ArrayList<>();
        C2Level.add("Szkoła");
        C2Level.add("Dom");
        C2Level.add("Rodzina");
        C2Level.add("Praca");
        C2Level.add("Kolory");
        C2Level.add("Liczby");

        ListDescriptionHash.put(ListHeaderData.get(0), A1Level);        //descriptionsText
        ListDescriptionHash.put(ListHeaderData.get(1), A2Level);
        ListDescriptionHash.put(ListHeaderData.get(2), B1Level);
        ListDescriptionHash.put(ListHeaderData.get(3), B2Level);
        ListDescriptionHash.put(ListHeaderData.get(4), C1Level);
        ListDescriptionHash.put(ListHeaderData.get(5), C2Level);

        ListSubdescriptionHash.put(ListHeaderData.get(0), A1Level);     //subdescriptionsText
        ListSubdescriptionHash.put(ListHeaderData.get(1), A2Level);
        ListSubdescriptionHash.put(ListHeaderData.get(2), B1Level);
        ListSubdescriptionHash.put(ListHeaderData.get(3), B2Level);
        ListSubdescriptionHash.put(ListHeaderData.get(4), C1Level);
        ListSubdescriptionHash.put(ListHeaderData.get(5), C2Level);
    }
}
