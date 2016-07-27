package com.example.dell.musico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerFragment extends Fragment {

    private ImageView mAlbumArt;
    private TextView mSongInfo;

    private static final String TAG = "Player Fragment";

    private View v;

    public static PlayerFragment createInstance(String filepath) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filepath",filepath);
        fragment.setArguments(bundle);

        /*MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(filepath);
        String info = null;
        Bitmap albumArt = null;
        info = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        byte[] bytearray = metadataRetriever.getEmbeddedPicture();
        if (bytearray != null)
            albumArt = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
        if (info == null)
            fragment.mSongInfo.setText(filepath);
        else
            fragment.mSongInfo.setText(info);
        if (albumArt != null)
            fragment.mAlbumArt.setImageBitmap(albumArt);*/

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_player_fragment, container, false);

        mAlbumArt = (ImageView) v.findViewById(R.id.albumArt);
        mSongInfo = (TextView) v.findViewById(R.id.songInfo);

        Bundle bundle = getArguments();
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(bundle.getString("filepath"));
        String filepath = bundle.getString("filepath");
        mSongInfo.setText(filepath);


        return v;
    }
}
