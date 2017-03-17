package lessons_vocabulary_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import database_vocabulary.VocabularyDatabase;
import pl.flanelowapopijava.angielski_slownictwo.R;


class LessonsVocabularyListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private int group_number;
    private int item_number;
    private ListView LVVocabularyLesson;


    LessonsVocabularyListAdapter(Context context, Cursor cursor, int group_number, int item_number, ListView LVVocabularyLesson) {
        this.context = context;
        this.cursor = cursor;
        this.group_number = group_number;
        this.item_number = item_number;
        this.LVVocabularyLesson = LVVocabularyLesson;
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
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.lv_vocabulary_lessons_item, null);
        }
        final ImageButton favouriteStar = (ImageButton) view.findViewById(R.id.favourite_star_lv);
        TextView polishWord = (TextView) view.findViewById(R.id.plWord);
        TextView englishWord = (TextView) view.findViewById(R.id.engWord);
        VocabularyDatabase database = new VocabularyDatabase(view.getContext());

        cursor = database.getSpecificValues(group_number, item_number);
        database.showVocabularyForLessons(cursor, polishWord, englishWord, favouriteStar, i);

        String tag = LVVocabularyLesson.getTag().toString();
        if (tag.equals("1")){
            favouriteStar.setVisibility(View.VISIBLE);
        }
        else if (tag.equals("0")){
            favouriteStar.setVisibility(View.INVISIBLE);
        }

        favouriteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VocabularyDatabase database = new VocabularyDatabase(view.getContext());
                cursor.moveToPosition(i);
                String index = String.valueOf(cursor.getInt(0));
                int isFavourite = cursor.getInt(5);
                if (isFavourite == 0) {
                    database.updateValuesInDatabase(index, 1);
                    favouriteStar.setImageResource(android.R.drawable.star_big_on);

                }
                else {
                   database.updateValuesInDatabase(index, 0);
                   favouriteStar.setImageResource(android.R.drawable.star_big_off);
                }
                notifyDataSetChanged();
            }
        });
    return view;
    }
}
