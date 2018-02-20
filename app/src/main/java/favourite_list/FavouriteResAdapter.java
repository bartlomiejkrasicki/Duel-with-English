package favourite_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

class FavouriteResAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context context;
    private ListView favouriteList;
    private VocabularyDatabase database;
    private boolean isEnabledDeleteMode = false;
    private boolean isAlphabeticalSort = false;
    private String lvlOfFavouriteWords = "A1";
    private boolean allFavouritesWords = true;

    public FavouriteResAdapter(Context context){
        this.context = context;
    }

    public VocabularyDatabase getDatabase() {
        return database;
    }

    public void setDatabase(VocabularyDatabase database) {
        this.database = database;
    }

    public String getLvlOfFavouriteWords() {
        return lvlOfFavouriteWords;
    }

    public void setLvlOfFavouriteWords(String lvlOfFavouriteWords) {
        this.lvlOfFavouriteWords = lvlOfFavouriteWords;
    }

    public boolean isEnabledDeleteMode() {
        return isEnabledDeleteMode;
    }

    public void setEnabledDeleteMode(boolean enabledDeleteMode) {
        isEnabledDeleteMode = enabledDeleteMode;
    }

    public void setShowAllFavouritesWords(boolean allFavouritesWords){
        this.allFavouritesWords = allFavouritesWords;
    }

    public boolean getShowAllFavouritesWords(){
        return allFavouritesWords;
    }

    public boolean isAlphabeticalSort() {
        return isAlphabeticalSort;
    }

    public void setAlphabeticalSort(boolean alphabeticalSort) {
        isAlphabeticalSort = alphabeticalSort;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
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
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null) {                                                             //add view if is empty
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.favourite_listview_item, null);
        }
        TextView engWord = (TextView) view.findViewById(R.id.engFavWord);
        TextView plWord = (TextView) view.findViewById(R.id.plFavWord);
        cursor.moveToPosition(i);
        engWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        plWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));

        onItemClickDelete(view);

        return view;
    }

    private void onItemClickDelete(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isEnabledDeleteMode()) {
                    try {
                        view.setClickable(false);
                        View listView = (View) view.getParent();
                        favouriteList = (ListView) listView;
                        cursor.moveToPosition(favouriteList.getPositionForView(view));
                        String index = String.valueOf(cursor.getInt(DatabaseColumnNames.idColumn));
                        database = new VocabularyDatabase(context);
                        database.updateValuesInDatabase(index, 0);
                        if (allFavouritesWords){
                            setCursor(database.getAllFavouriteValues(isAlphabeticalSort()));
                        } else {
                            setCursor(database.getFavouriteValues(getLvlOfFavouriteWords(), isAlphabeticalSort()));
                        }
                        notifyDataSetChanged();
                    } catch (NullPointerException exception) {
                        Toast.makeText(context, "Lista jest pusta" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
