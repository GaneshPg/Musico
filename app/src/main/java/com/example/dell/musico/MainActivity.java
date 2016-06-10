package com.example.dell.musico;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView mPlayBtn, mNextBtn, mPrevBtn, mAlbumArt;
    private TextView mSongInfo;
    private List<String> mSongFiles;
    private MediaPlayer mMediaPlayer;
    private int mSongIndex;

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaPlayer = new MediaPlayer();
        mSongFiles = new ArrayList<>();

        getMusicFiles(Environment.getExternalStorageDirectory());

        mSongIndex = mSongFiles.isEmpty()? -1:0;

        mPlayBtn = (ImageView) findViewById(R.id.playPauseButton);
        mNextBtn = (ImageView) findViewById(R.id.nextButton);
        mPrevBtn = (ImageView) findViewById(R.id.prevButton);
        mAlbumArt = (ImageView) findViewById(R.id.albumArt);

        mSongInfo = (TextView) findViewById(R.id.songInfo);

        mPlayBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPrevBtn.setOnClickListener(this);

        if(mSongFiles.isEmpty()) Toast.makeText(getApplicationContext(),"No audio files found!",Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(),mSongFiles.size()+" audio files found!",Toast.LENGTH_LONG).show();
            try {
                mMediaPlayer.setDataSource(mSongFiles.get(mSongIndex));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.d(TAG,"MediaPlayer error " + e.getMessage());
                Toast.makeText(getApplicationContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
            }
            showSongInfo();
        }
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
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(mSongFiles.get(mSongIndex));

        String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        if(title == null){
            String filename = mSongFiles.get(mSongIndex);
            title = filename.substring(filename.lastIndexOf(File.separator)+1,filename.lastIndexOf("."));
        }

        title += "\n";

        if(artist == null)
            artist = "";
        else
            artist += "\t";

        if(album == null)
            album = "";

        String infoText = title + artist + album;
        mSongInfo.setText(infoText);

        byte[] bytearray = metadataRetriever.getEmbeddedPicture();

        try{
            Bitmap albumArt = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
            mAlbumArt.setImageBitmap(albumArt);
        }catch (Exception e){
            mAlbumArt.setImageResource(R.drawable.play);
        }
    }

    public void playPause(){
        if(mSongFiles.isEmpty()){
            Toast.makeText(getApplicationContext(),"No files to play!",Toast.LENGTH_LONG).show();
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

    public void nextSong(){

        if(mSongFiles.isEmpty()){
            Toast.makeText(getApplicationContext(),"No files to play!",Toast.LENGTH_LONG).show();
            return;
        }

        mSongIndex = (mSongIndex + 1) % mSongFiles.size();

        boolean isPlaying = mMediaPlayer.isPlaying();

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mSongFiles.get(mSongIndex));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG,"MediaPlayer error " + e.getMessage());
            Toast.makeText(getApplicationContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
            return;
        }

        showSongInfo();
        if(isPlaying) mMediaPlayer.start();
    }

    public void prevSong(){

        if(mSongFiles.isEmpty()){
            Toast.makeText(getApplicationContext(),"No files to play!",Toast.LENGTH_LONG).show();
            return;
        }

        if(mSongIndex == 0)
            mSongIndex = mSongFiles.size();

        mSongIndex--;

        boolean isPlaying = mMediaPlayer.isPlaying();

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mSongFiles.get(mSongIndex));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG,"MediaPlayer error " + e.getMessage());
            Toast.makeText(getApplicationContext(),"Some error occurred. Try again!",Toast.LENGTH_LONG).show();
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
                else if (file.isFile() && file.getName().endsWith(".mp3") || file.getName().endsWith(".wav"))
                    mSongFiles.add(file.getPath());
            }
        }
    }
}
