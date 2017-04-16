package expandable_lessons_list;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pl.flanelowapopijava.angielski_slownictwo.R;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private List<Integer> categoriesImages;
    private HashMap<String, List<String>> descriptionHashMap;
    private HashMap<String, List<String>> subdescriptionHashMap;
    private boolean getDescOrSubdesc;

    public ExpandableListAdapter(Context context, List<String> listHeader, List<Integer> categoriesImages, HashMap<String, List<String>> descriptionHashMap, HashMap<String, List<String>> subdescriptionHashMap) {
        this.context = context;
        this.listHeader = listHeader;
        this.categoriesImages = categoriesImages;
        this.descriptionHashMap = descriptionHashMap;
        this.subdescriptionHashMap = subdescriptionHashMap;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return descriptionHashMap.get(listHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        if (getDescOrSubdesc) {
            return descriptionHashMap.get(listHeader.get(i)).get(i1);
        } else
            return subdescriptionHashMap.get(listHeader.get(i)).get(i1);  //i - groupItem, i1 - ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.el_vocabulary_group, null);
        }
        TextView headerEL = (TextView) view.findViewById(R.id.headerEL);
        headerEL.setTypeface(null, Typeface.BOLD);
        headerEL.setText(headerTitle);
        ImageView levelImage = (ImageView) view.findViewById(R.id.iconELGroup);
        if (categoriesImages.size() > i) {
            levelImage.setImageResource(categoriesImages.get(i));
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        getDescOrSubdesc = true;
        final String descriptionTitle = (String) getChild(i, i1);
        getDescOrSubdesc = false;
        final String subdescriptionTitle = (String) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.el_vocabulary_item, null);
        }
        TextView ELdescription = (TextView) view.findViewById(R.id.ELdescription);
        TextView ELsubdescription = (TextView) view.findViewById(R.id.ELSubdescription);
        ELdescription.setTypeface(null, Typeface.BOLD);
        ELdescription.setText(descriptionTitle);
        ELsubdescription.setText(subdescriptionTitle);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

