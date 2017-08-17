package favourite_list;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.duel_with_english.R;

public class FavouriteListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private ListView favouriteList;

    public FavouriteListAdapter(Context context, Cursor cursor, ListView favouriteList) {
        this.context = context;
        this.cursor = cursor;
        this.favouriteList = favouriteList;
    }

    @Override
    public void notifyDataSetChanged() {
        VocabularyDatabase database = new VocabularyDatabase(context);
        cursor = database.getFavouriteValues(DatabaseColumnNames.TABLE_NAME_A1);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.lv_vocabulary_lessons_item, null);
        }
        TextView plword = (TextView) view.findViewById(R.id.plWord);
        TextView engword = (TextView) view.findViewById(R.id.engWord);
        VocabularyDatabase database = new VocabularyDatabase(context);

        cursor = database.getFavouriteValues(DatabaseColumnNames.TABLE_NAME_A1);
        database.showVocabularyForFavourite(cursor, plword, engword, i);

        cursor.close();
        database.close();
        return view;
    }

}