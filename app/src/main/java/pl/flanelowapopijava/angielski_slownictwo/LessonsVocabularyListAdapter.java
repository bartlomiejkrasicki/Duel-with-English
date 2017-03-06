package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class LessonsVocabularyListAdapter extends BaseAdapter{

    private Context context;
    private List<String> valuesPL;
    private List<String> valuesEN;
    private boolean ENorPL;
    private ImageButton favouriteStar;

    public LessonsVocabularyListAdapter(Context context, List<String> valuesPL, List<String> valuesEN) {
        this.context = context;
        this.valuesPL = valuesPL;
        this.valuesEN = valuesEN;
    }

    @Override
    public int getCount() {
        return valuesEN.size();
    }

    @Override
    public Object getItem(int i) {
        if (ENorPL) {
            return valuesEN.get(i);
        }
        else {
            return valuesPL.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        ENorPL = true;
        String engWord = (String) getItem(i);
        ENorPL = false;
        String plWord = (String) getItem(i);
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
        polishWord.setText(engWord);
        englishWord.setText(plWord);
        return view;
    }
}
