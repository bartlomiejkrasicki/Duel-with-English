package lessons_vocabulary_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;


public class LessonsVocabularyListAdapter extends BaseAdapter {

    private boolean isStarVisible = false;
    private Context context;
    private Cursor cursor;
    private VocabularyDatabase vocabularyDatabase;
    private ImageButton favouriteStar;
    private TextView polishWord;
    private TextView englishWord;
    private String levelLanguage = "", categoryName = "";
    private boolean isAlphabeticalSort = false;

    LessonsVocabularyListAdapter(Context context) {
        this.context = context;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public boolean isStarVisible() {
        return isStarVisible;
    }

    public void setStarVisible(boolean starVisible) {
        isStarVisible = starVisible;
    }

    public String getLevelLanguage() {
        return levelLanguage;
    }

    public void setLevelLanguage(String levelLanguage) {
        this.levelLanguage = levelLanguage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isAlphabeticalSort() {
        return isAlphabeticalSort;
    }

    public void setAlphabeticalSort(boolean alphabeticalSort) {
        isAlphabeticalSort = alphabeticalSort;
    }

    public VocabularyDatabase getVocabularyDatabase() {
        return vocabularyDatabase;
    }

    @Override
    public void notifyDataSetChanged() {
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

        if(cursor.moveToPosition(i)) {                                                  //add values to listView
            polishWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
            englishWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
            int addFavOrNot = cursor.getInt(DatabaseColumnNames.isfavouriteColumn);
            if (addFavOrNot == 1) {
                favouriteStar.setImageResource(android.R.drawable.star_big_on);
            } else
                favouriteStar.setImageResource(android.R.drawable.star_big_off);
        }
        if (isStarVisible()){                                     //star button is visible or not
            favouriteStar.setVisibility(View.VISIBLE);
        }
        else {
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
                    vocabularyDatabase.updateValuesInDatabase(index, 1);
                    favouriteStar.setImageResource(android.R.drawable.star_big_on);
                }
                else if(cursor.getInt(DatabaseColumnNames.isfavouriteColumn) == 1){
                    vocabularyDatabase.updateValuesInDatabase(index, 0);
                    favouriteStar.setImageResource(android.R.drawable.star_big_off);
                }
                setCursor(vocabularyDatabase.showVocabularyForLessons(getLevelLanguage(), getCategoryName(), isAlphabeticalSort()));
                notifyDataSetChanged();
            }
        });
    }
}
