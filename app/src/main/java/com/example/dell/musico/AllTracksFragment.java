package com.example.dell.musico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL on 11-Jun-16.
 */
public class AllTracksFragment extends Fragment {

    private ListView trackList;
    private View v;

    private static final String TAG = "AllTracks Fragment";

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
        CustomAdapter customAdapter = new CustomAdapter(getContext(), PlayerFragment.getPLAYERFRAGMENT().mMetaDataList);
        trackList.setAdapter(customAdapter);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PlayerFragment.getPLAYERFRAGMENT().playSong(position);
            }
        });

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
                eltview = inflater.inflate(R.layout.playlist_element,null);

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
