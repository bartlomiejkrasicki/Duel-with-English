package favourite_list;


import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import database_vocabulary.DatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

class FavouriteListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private FloatingActionButton floatingActionButton;

    FavouriteListAdapter(Context context, FloatingActionButton floatingActionButton, Cursor cursor) {
        this.context = context;
        this.floatingActionButton = floatingActionButton;
        this.cursor = cursor;
    }

    Cursor getCursor() {
        return cursor;
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public void notifyDataSetChanged() {
        if (cursor.getCount()==0) {
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
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

        cursor = getCursor();

        if (cursor.moveToPosition(i)) {
            plword.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
            engword.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        }

        notifyDataSetChanged();

        return view;
    }

}