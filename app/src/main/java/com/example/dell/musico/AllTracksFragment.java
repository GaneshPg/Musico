package com.example.dell.musico;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

/**
 * Created by DELL on 11-Jun-16.
 */
public class AllTracksFragment extends Fragment {

    private ListView trackList;
    private View v;

    public static AllTracksFragment createInstance() {
        AllTracksFragment fragment = new AllTracksFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_all_tracks_fragment, container, false);

        trackList = (ListView) v.findViewById(R.id.tracksList);

        /*String[] listElements = new String[]{
                "Hello",
                "Hi",
                "What's up?",
                "Bye"
        };

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,
                android.R.id.text1,listElements);

        trackList.setAdapter(stringArrayAdapter);*/

        CustomAdapter customAdapter = new CustomAdapter(getContext(), PlayerFragment.METADATALIST);

        trackList.setAdapter(customAdapter);

        for(HashMap<String ,String> h : PlayerFragment.METADATALIST)
            System.out.println(h.get("title"));

        return v;
    }

    public class CustomAdapter extends BaseAdapter {

        ArrayList<HashMap<String, String>> mData;
        Context mContext;
        private LayoutInflater inflater;

        public CustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {
            mData = data;
            mContext = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getViewTypeCount() {

            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View eltview = view;
            if (view == null) {
                eltview = inflater.inflate(R.layout.playlist_element, null);

                ImageView thumbnail = (ImageView) eltview.findViewById(R.id.thumbnail);
                TextView title = (TextView) eltview.findViewById(R.id.elementTitle);
                TextView artist = (TextView) eltview.findViewById(R.id.elementArtist);

                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                metadataRetriever.setDataSource(mData.get(i).get("filepath"));
                byte[] bytearray = metadataRetriever.getEmbeddedPicture();

                try {
                    Bitmap albumArt = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                    thumbnail.setImageBitmap(albumArt);
                } catch (Exception e) {
                    thumbnail.setImageResource(R.drawable.play);
                }

                title.setText(mData.get(i).get("title"));

                String artistString = "Artist : ";
                if (mData.get(i).get("artist").isEmpty())
                    artistString += "Unknown";
                else
                    artistString += mData.get(i).get("artist");

                artist.setText(artistString);

            }

            return eltview;
        }
    }
}
