package test_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.flanelowapopijava.angielski_slownictwo.R;

public class VocabularyTestJigsawWordEnFragment extends Fragment {


    public VocabularyTestJigsawWordEnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vocabulary_test_jigsaw_word_en, container, false);
    }

}
