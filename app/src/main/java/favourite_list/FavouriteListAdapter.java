package favourite_list;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;

public class FavouriteListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private ListView favouriteList;
    private VocabularyDatabase database;

    public FavouriteListAdapter(Context context, Cursor cursor, ListView favouriteList, VocabularyDatabase database) {
        this.context = context;
        this.cursor = cursor;
        this.favouriteList = favouriteList;
        this.database = database;
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
        database.showVocabularyForFavourite(cursor, plword, engword, i);

        return view;
    }
}