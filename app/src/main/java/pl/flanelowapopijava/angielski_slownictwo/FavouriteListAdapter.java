package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FavouriteListAdapter extends BaseAdapter {

    private List<String> valuesEN;
    private List<String> valuesPL;
    private Context context;
    private boolean PLOrEN;
    private SharedPreferences preferences;

    public FavouriteListAdapter(List<String> valuesEN, List<String> valuesPL, Context context, SharedPreferences preferences) {
//        this.valuesEN = valuesEN;
//        this.valuesPL = valuesPL;
        this.context = context;
        this.preferences = preferences;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
//        if (PLOrEN){
//            return valuesPL.get(i);
//        }
//        else {
//            return valuesEN.get(i);
//        }
    return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        PLOrEN = false;
//        final String enTemp = (String) getItem(i);
//        PLOrEN = true;
//        final String plTemp = (String) getItem(i);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.lv_vocabulary_lessons_item, null);
        }
//        String sharedEN = preferences.getString(enTemp, "DEFAULT");
//        String sharedPL = preferences.getString(plTemp, "DEFAULT");
        TextView enVocabularyTV = (TextView) view.findViewById(R.id.engWord);
        TextView plVocabularyTV = (TextView) view.findViewById(R.id.plWord);
        enVocabularyTV.setText("tak tak");
        plVocabularyTV.setText("tak tak");
        return view;
    }
}
