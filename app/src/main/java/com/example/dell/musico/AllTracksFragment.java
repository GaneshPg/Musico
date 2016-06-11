package com.example.dell.musico;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by DELL on 11-Jun-16.
 */
public class AllTracksFragment extends Fragment {

    private ListView trackList;

    private View v;

    public static AllTracksFragment createInstance(){
        AllTracksFragment fragment = new AllTracksFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_all_tracks_fragment,container,false);

        trackList = (ListView) v.findViewById(R.id.tracksList);

        String[] listElements = new String[]{
                "Hello",
                "Hi",
                "What's up?",
                "Bye"
        };

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,
                android.R.id.text1,listElements);

        trackList.setAdapter(stringArrayAdapter);

        return v;
    }
}
