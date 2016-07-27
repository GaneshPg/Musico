package com.example.dell.musico;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ImageView mPlayBtn;
    public int mSongIndex = 0;
    public ArrayList<HashMap<String, String>> mMetaDataList = new ArrayList<>();
    public MediaPlayer mMediaPlayer = new MediaPlayer();

    private boolean firstRun;

    final private String TAG = "MAIN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstRun = true;

        getMusicFiles(Environment.getExternalStorageDirectory());

        mPlayBtn = (ImageView) findViewById(R.id.playPauseButton);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Play-Pause
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //Play next song
            }
        });

        if (mMetaDataList.isEmpty())
            Toast.makeText(getApplicationContext(), "No audio files found!", Toast.LENGTH_LONG).show();
        else {
            try {
                mMediaPlayer.setDataSource(mMetaDataList.get(mSongIndex).get("filepath"));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.d(TAG, "MediaPlayer error " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred. Try again!", Toast.LENGTH_LONG).show();
            }
            //Set view to second fragment, ie, current song
        }

        /*

        public void reselect() {

        if(mMediaPlayer.isPlaying())
            mPlayBtn.setImageResource(R.drawable.pause);
        else
            mPlayBtn.setImageResource(R.drawable.play);

        showSongInfo();
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

         */

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        final CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(customPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int nextSongIndex, prevSongIndex;
                nextSongIndex = (mSongIndex + 1) % mMetaDataList.size();
                prevSongIndex = mSongIndex - 1;
                if (prevSongIndex == -1)
                    prevSongIndex = mMetaDataList.size() - 1;
                if (firstRun) {
                    firstRun = false;
                } else {
                    switch (position) {
                        case 0: //Previous song
                            //Handle previous song change
                            mViewPager.removeViewAt(2);
                            mViewPager.addView(mViewPager.getChildAt(1), 2);
                            mViewPager.removeViewAt(1);
                            mViewPager.addView(mViewPager.getChildAt(0), 1);
                            mViewPager.removeViewAt(0);
                            mViewPager.addView(PlayerFragment.createInstance(mMetaDataList.get(prevSongIndex).get("filepath")).getView(), 0);
                            mSongIndex = prevSongIndex;
                            mViewPager.setCurrentItem(1);
                            break;
                        case 1: //Not possible.. Current song
                            break;
                        case 2: //Next song
                            //Handle next song change
                            mViewPager.removeViewAt(0);
                            mViewPager.addView(mViewPager.getChildAt(1), 0);
                            mViewPager.removeViewAt(1);
                            mViewPager.addView(mViewPager.getChildAt(2), 1);
                            mViewPager.removeViewAt(2);
                            mViewPager.addView(PlayerFragment.createInstance(mMetaDataList.get(nextSongIndex).get("filepath")).getView(), 2);
                            mSongIndex = nextSongIndex;
                            mViewPager.setCurrentItem(1);
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(1);
    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {
        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position - position of the fragment in the swipe view
         */
        @Override
        public Fragment getItem(int position) {
            PlayerFragment fragment;
            int nextSongIndex, prevSongIndex;

            if (!mMetaDataList.isEmpty()) {
                nextSongIndex = (mSongIndex + 1) % mMetaDataList.size();
                prevSongIndex = mSongIndex - 1;
                if (prevSongIndex == -1)
                    prevSongIndex = mMetaDataList.size() - 1;
                switch (position) {
                    case 0: //Previous song fragment
                        fragment = PlayerFragment.createInstance(mMetaDataList.get(prevSongIndex).get("filepath"));
                        break;
                    case 1: //Current song fragment
                        fragment = PlayerFragment.createInstance(mMetaDataList.get(mSongIndex).get("filepath"));
                        break;
                    case 2: //Next song fragment
                        fragment = PlayerFragment.createInstance(mMetaDataList.get(nextSongIndex).get("filepath"));
                        break;
                    default: //Not possible
                        fragment = null;
                }
            } else {
                //Handle no music files
                fragment = null;
            }
            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 3;
        }
    }

    public void getMusicFiles(File dir) {

        File listofFiles[] = dir.listFiles();

        if (listofFiles != null) {
            for (File file : listofFiles) {
                if (file.isDirectory())
                    getMusicFiles(file);
                else if (file.isFile() && file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {

                    HashMap<String, String> metadata = new HashMap<>();

                    MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                    metadataRetriever.setDataSource(file.getPath());

                    String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                    if (title == null) {
                        String filePath = file.getPath();
                        title = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf("."));
                    }
                    if (artist == null)
                        artist = "";
                    if (album == null)
                        album = "";
                    if (duration == null)
                        duration = "";

                    metadata.put("title", title);
                    metadata.put("artist", artist);
                    metadata.put("album", album);
                    metadata.put("duration", duration);
                    metadata.put("filepath", file.getPath());

                    mMetaDataList.add(metadata);
                }
            }
        }
    }
}
