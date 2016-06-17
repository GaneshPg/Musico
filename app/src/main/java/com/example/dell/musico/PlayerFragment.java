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

public class PlayerFragment extends Fragment implements View.OnClickListener{

    private ImageView mPlayBtn, mNextBtn, mPrevBtn, mAlbumArt;
    private TextView mSongInfo;

    public static PlayerFragment PLAYERFRAGMENT;

    public int mSongIndex = 0;
    public ArrayList<HashMap<String,String>> mMetaDataList = new ArrayList<>();
    public MediaPlayer mMediaPlayer = new MediaPlayer();

    private static final String TAG = "Player Fragment";
    
    private View v;

    public static PlayerFragment createInstance(){
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(null);
        PLAYERFRAGMENT = fragment;
        return fragment;
    }

    public static PlayerFragment getPLAYERFRAGMENT(){
        return PLAYERFRAGMENT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_player_fragment,container,false);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });

        getMusicFiles(Environment.getExternalStorageDirectory());

        mSongIndex = mMetaDataList.isEmpty()? -1:mSongIndex;

        mPlayBtn = (ImageView) v.findViewById(R.id.playPauseButton);
        mNextBtn = (ImageView) v.findViewById(R.id.nextButton);
        mPrevBtn = (ImageView) v.findViewById(R.id.prevButton);
        mAlbumArt = (ImageView) v.findViewById(R.id.albumArt);

        mSongInfo = (TextView) v.findViewById(R.id.songInfo);

        mPlayBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPrevBtn.setOnClickListener(this);

        if(mMetaDataList.isEmpty()) Toast.makeText(this.getContext(),"No audio files found!",Toast.LENGTH_LONG).show();
        else {
            try {
                mMediaPlayer.setDataSource(mMetaDataList.get(mSongIndex).get("filepath"));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.d(TAG,"MediaPlayer error " + e.getMessage());
                Toast.makeText(this.getContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
            }
            showSongInfo();
        }
        
        return v;
    }

    public void reselect() {

        if(mMediaPlayer.isPlaying())
            mPlayBtn.setImageResource(R.drawable.pause);
        else
            mPlayBtn.setImageResource(R.drawable.play);

        showSongInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playPauseButton:
                //Play or pause accordingly
                playPause();
                break;
            case R.id.nextButton:
                //Play next song
                nextSong();
                break;
            case R.id.prevButton:
                //Play previous song
                prevSong();
                break;
        }
    }

    public void showSongInfo(){

        String title = mMetaDataList.get(mSongIndex).get("title");
        String artist = mMetaDataList.get(mSongIndex).get("artist");
        String album = mMetaDataList.get(mSongIndex).get("album");
        String duration = mMetaDataList.get(mSongIndex).get("duration");

        title += "\n";

        if(!artist.isEmpty())
            artist += "\n";

        String infoText = title + artist + album;
        mSongInfo.setText(infoText);

        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(mMetaDataList.get(mSongIndex).get("filepath"));

        try{
            byte[] bytearray = metadataRetriever.getEmbeddedPicture();
            Bitmap albumArt = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
            mAlbumArt.setImageBitmap(albumArt);
        }catch (Exception e){
            mAlbumArt.setImageResource(R.drawable.play);
        }
    }

    public void playPause(){
        if(mMetaDataList.isEmpty()){
            Toast.makeText(this.getContext(),"No files to play!",Toast.LENGTH_LONG).show();
            return;
        }

        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            mPlayBtn.setImageResource(R.drawable.play);
        }
        else {
            mMediaPlayer.start();
            mPlayBtn.setImageResource(R.drawable.pause);
        }
    }

    public void playSong(int songNumber){
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mMetaDataList.get(songNumber).get("filepath"));
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mSongIndex = songNumber;
        } catch (IOException e) {
            Log.d(TAG,"MediaPlayer error " + e.getMessage());
            Toast.makeText(getContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
        }
    }

    public void nextSong(){

        if(mMetaDataList.isEmpty()){
            Toast.makeText(this.getContext(),"No files to play!",Toast.LENGTH_LONG).show();
            return;
        }

        mSongIndex = (mSongIndex + 1) % mMetaDataList.size();

        boolean isPlaying = mMediaPlayer.isPlaying();

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mMetaDataList.get(mSongIndex).get("filepath"));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG,"MediaPlayer error " + e.getMessage());
            Toast.makeText(this.getContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
            return;
        }

        showSongInfo();
        if(isPlaying) mMediaPlayer.start();
    }

    public void prevSong(){

        if(mMetaDataList.isEmpty()){
            Toast.makeText(this.getContext(),"No files to play!",Toast.LENGTH_LONG).show();
            return;
        }

        if(mSongIndex == 0)
            mSongIndex = mMetaDataList.size();

        mSongIndex--;

        boolean isPlaying = mMediaPlayer.isPlaying();

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mMetaDataList.get(mSongIndex).get("filepath"));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG,"MediaPlayer error " + e.getMessage());
            Toast.makeText(this.getContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
            return;
        }

        showSongInfo();
        if(isPlaying) mMediaPlayer.start();
    }

    public void getMusicFiles(File dir) {

        File listofFiles[] = dir.listFiles();

        if (listofFiles != null) {
            for (File file : listofFiles) {
                if (file.isDirectory())
                    getMusicFiles(file);
                else if (file.isFile() && file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {

                    HashMap<String,String> metadata = new HashMap<>();

                    MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                    metadataRetriever.setDataSource(file.getPath());

                    String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                    if(title == null){
                        String filePath = file.getPath();
                        title = filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.lastIndexOf("."));
                    }
                    if(artist == null)
                        artist = "";
                    if(album == null)
                        album = "";
                    if(duration == null)
                        duration = "";

                    metadata.put("title",title);
                    metadata.put("artist",artist);
                    metadata.put("album",album);
                    metadata.put("duration",duration);
                    metadata.put("filepath",file.getPath());

                    mMetaDataList.add(metadata);
                }
            }
        }
    }
}
