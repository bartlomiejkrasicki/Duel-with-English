package tenses;

import android.widget.Filter;

public class ValueFilterSearch extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if (charSequence != null && charSequence.length() > 0){

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

    }
}
