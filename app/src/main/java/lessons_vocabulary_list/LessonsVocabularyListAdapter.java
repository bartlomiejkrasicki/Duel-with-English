package lessons_vocabulary_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;


public class LessonsVocabularyListAdapter extends BaseAdapter {

    private boolean favouriteIconVisible = false;
    private Context context;
    private Cursor cursor;
    private VocabularyDatabase dbInstance;
    private ImageButton favouriteIcon;
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

    boolean isFavouriteIconVisible() {
        return favouriteIconVisible;
    }

    void setFavouriteIconVisible(boolean favouriteIconVisible) {
        this.favouriteIconVisible = favouriteIconVisible;
    }

    String getLevelLanguage() {
        return levelLanguage;
    }

    void setLevelLanguage(String levelLanguage) {
        this.levelLanguage = levelLanguage;
    }

    String getCategoryName() {
        return categoryName;
    }

    void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    boolean isAlphabeticalSort() {
        return isAlphabeticalSort;
    }

    void setAlphabeticalSort(boolean alphabeticalSort) {
        isAlphabeticalSort = alphabeticalSort;
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
            polishWord.setText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn));
            englishWord.setText(cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
            int addFavOrNot = cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn);
            if (addFavOrNot == 1) {
                favouriteIcon.setImageResource(R.drawable.ic_favouriteiconon);
            } else
                favouriteIcon.setImageResource(R.drawable.ic_favouriteiconoff);
        }
        if (isFavouriteIconVisible()){                                     //star button is visible or not
            favouriteIcon.setVisibility(View.VISIBLE);
        }
        else {
            favouriteIcon.setVisibility(View.INVISIBLE);
        }
        favouriteStarOnClick(i);
    return view;
    }

    private void declarationVariables(View view){
        favouriteIcon = (ImageButton) view.findViewById(R.id.favourite_star_lv);
        polishWord = (TextView) view.findViewById(R.id.plWord);
        englishWord = (TextView) view.findViewById(R.id.engWord);
        dbInstance = VocabularyDatabase.getInstance(context);
    }

    private void favouriteStarOnClick(final int i){
        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(i);
                String index = String.valueOf(cursor.getInt(VocabularyDatabaseColumnNames.idColumn));
                if(cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn) == 0) {
                    dbInstance.updateValuesInDatabase(index, 1);
                    favouriteIcon.setImageResource(R.drawable.ic_favouriteiconon);
                }
                else if(cursor.getInt(VocabularyDatabaseColumnNames.isfavouriteColumn) == 1){
                    dbInstance.updateValuesInDatabase(index, 0);
                    favouriteIcon.setImageResource(R.drawable.ic_favouriteiconoff);
                }
                setCursor(dbInstance.showVocabularyForLessons(getLevelLanguage(), getCategoryName(), isAlphabeticalSort()));
                notifyDataSetChanged();
            }
        });
    }
}
