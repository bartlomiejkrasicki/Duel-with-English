package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;

public class LessonsVocabularyListAdapter extends BaseAdapter{

    private Context context;
    private Cursor cursor;
    private VocabularyDatabase database;
    private ImageButton favouriteStar;

    public LessonsVocabularyListAdapter(Context context, VocabularyDatabase database, Cursor cursor) {
        this.context = context;
        this.database = database;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return  i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.lv_vocabulary_lessons_item, null);
        } else {
            favouriteStar = (ImageButton) view.findViewById(R.id.favourite_star_lv);
            if (favouriteStar.getVisibility() == View.VISIBLE) {
                favouriteStar.setVisibility(View.GONE);
            } else {
                favouriteStar.setVisibility(View.VISIBLE);
            }
        }
        TextView polishWord = (TextView) view.findViewById(R.id.plWord);
        TextView englishWord = (TextView) view.findViewById(R.id.engWord);
        VocabularyDatabase database = new VocabularyDatabase(view.getContext());
        cursor = database.getValues();
        database.showVocabulary(cursor, polishWord, englishWord, favouriteStar);


        return view;
    }
}
