package dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import database_vocabulary.DatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

public class SearchHistAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    private TextView hintCategory, hintWord;
    private SharedPreferences sharedPreferences;
    private Boolean plToEnTranslate;

    SearchHistAdapter(Cursor cursor, Context context){
        this.cursor = cursor;
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getCount() {
        if (cursor == null){
            return 0;
        } else {
            return cursor.getCount();
        }

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
        if (view == null) {                                                             //add view if is empty
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.search_hint_list_item, null);
        }
        declarationVaribles(view);

        if (cursor.moveToPosition(i)){
            hintCategory.setText(String.format("%s:", cursor.getString(2)));
            setHintWord();
        }

        return view;
    }

    private void declarationVaribles(View view){
        hintCategory = (TextView) view.findViewById(R.id.hintListCategory);
        hintWord = (TextView) view.findViewById(R.id.hintListWord);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        plToEnTranslate = sharedPreferences.getBoolean(Dictionary.PLTOENTRANSLATESP, true);
    }

    private void setHintWord() {
        if (plToEnTranslate) {
            hintWord.setText(cursor.getString(DatabaseColumnNames.plwordColumn));
        } else {
            hintWord.setText(cursor.getString(DatabaseColumnNames.enwordColumn));
        }
    }

    public int getDBItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getInt(0);
    }
}
