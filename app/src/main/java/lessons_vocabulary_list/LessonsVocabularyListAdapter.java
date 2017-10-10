package lessons_vocabulary_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;


class LessonsVocabularyListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private ListView LVVocabularyLesson;
    private VocabularyDatabase vocabularyDatabase;
    private ImageButton favouriteStar;
    private TextView polishWord;
    private TextView englishWord;
    private String levelLanguage, categoryName;
    private LessonsVocabularyList lessonsVocabularyList = new LessonsVocabularyList();
    private SharedPreferences sharedPreferences;

    LessonsVocabularyListAdapter(Context context, Cursor cursor, ListView LVVocabularyLesson, String levelLanguage, String categoryName) {
        this.context = context;
        this.cursor = cursor;
        this.LVVocabularyLesson = LVVocabularyLesson;
        this.levelLanguage = levelLanguage;
        this.categoryName = categoryName;
    }

    private Cursor getCursor() {
        return cursor;
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public void notifyDataSetChanged() {
        cursor = getCursor();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {                                                             //add view if is empty
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.lv_vocabulary_lessons_item, null);
        }

        declarationVariables(view);

        int addFavOrNot;

        cursor = getCursor();

        if(cursor.moveToPosition(i)) {                                                  //add values to listView
            polishWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
            englishWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
            addFavOrNot = cursor.getInt(DatabaseColumnNames.isfavouriteColumn);
            if (addFavOrNot == 1) {
                favouriteStar.setImageResource(android.R.drawable.star_big_on);
            } else
                favouriteStar.setImageResource(android.R.drawable.star_big_off);
        }

        final String tag = LVVocabularyLesson.getTag().toString();                      //star button is visible or not
        if (tag.equals("1")){
            favouriteStar.setVisibility(View.VISIBLE);
        }
        else if (tag.equals("0")){
            favouriteStar.setVisibility(View.INVISIBLE);
        }

        favouriteStarOnClick(i);

    return view;
    }

    private void declarationVariables(View view){
        favouriteStar = (ImageButton) view.findViewById(R.id.favourite_star_lv);
        polishWord = (TextView) view.findViewById(R.id.plWord);
        englishWord = (TextView) view.findViewById(R.id.engWord);
        vocabularyDatabase = new VocabularyDatabase(context);
    }

    private void favouriteStarOnClick(final int i){
        favouriteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(i);
                String index = String.valueOf(cursor.getInt(DatabaseColumnNames.idColumn));
                if(cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 0) {
                    vocabularyDatabase.updateValuesInDatabase(index, 1, levelLanguage);
                    favouriteStar.setImageResource(android.R.drawable.star_big_on);
                }
                else if(cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 1){
                    vocabularyDatabase.updateValuesInDatabase(index, 0, levelLanguage);
                    favouriteStar.setImageResource(android.R.drawable.star_big_off);
                }
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                setCursor(vocabularyDatabase.showVocabularyForLessons(levelLanguage, categoryName, lessonsVocabularyList.getAlphabeticalSortSP(sharedPreferences)));
                notifyDataSetChanged();
            }
        });
    }
}
