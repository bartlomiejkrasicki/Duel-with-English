package lessons_vocabulary_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import database_vocabulary.CategoryDatabaseColumnNames;
import pl.flanelowapopijava.duel_with_english.R;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursorCategory;
    private TextView categoryTextView;
    private ImageView categoryProgressImageView;

    public CategoryListAdapter(Context context) {
        this.context = context;
    }

    public Cursor getCursorCategory() {
        return cursorCategory;
    }

    public void setCursorCategory(Cursor cursorCategory) {
        this.cursorCategory = cursorCategory;
    }

    @Override
    public int getCount() {
        return cursorCategory.getCount();
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
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.category_list_item, null);
        }
        declareVariables(view);
        if(cursorCategory.moveToPosition(i)){
            categoryTextView.setText(cursorCategory.getString(CategoryDatabaseColumnNames.categoryColumn));
            int resultMode = setCategoryProgressImageView(cursorCategory.getInt(CategoryDatabaseColumnNames.testResultNumber));
            if (resultMode != 0){
                categoryProgressImageView.setImageDrawable(view.getResources().getDrawable(resultMode));
            }

        }
        return view;
    }

    private void declareVariables(View view){
        categoryTextView = (TextView) view.findViewById(R.id.categoryListTV);
        categoryProgressImageView = (ImageView) view.findViewById(R.id.categoryListProgressIcon);
    }

    private int setCategoryProgressImageView(int mode) {
        if (mode == 1){
            return R.drawable.ic_star_bronze;
        } else if (mode == 2) {
            return R.drawable.ic_star_silver;
        } else if (mode == 3) {
            return R.drawable.ic_star_gold;
        } else {
            return 0;
        }
    }
}
