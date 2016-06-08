package com.example.dell.musico;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView playBtn, nextBtn, prevBtn;
    private List<String> songFiles;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMusicFiles(Environment.getExternalStorageDirectory());
        mediaPlayer = new MediaPlayer();

        List<String> songFiles = new ArrayList<String>();

        playBtn = (ImageView) findViewById(R.id.playPauseButton);
        nextBtn = (ImageView) findViewById(R.id.nextButton);
        prevBtn = (ImageView) findViewById(R.id.prevButton);

        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playPauseButton:
                //Play or pause accordingly
                break;
            case R.id.nextButton:
                //Play next song
                break;
            case R.id.prevButton:
                //Play previous song
                break;
        }
    }

    public void getMusicFiles(File file) {
        if (file.isDirectory()) {
            for (File filename : file.listFiles()) {
                if (filename.isDirectory())
                    getMusicFiles(filename);
                else if (filename.isFile() && filename.toString().endsWith(".mp3") || filename.toString().endsWith(".wav"))
                    songFiles.add(filename.getAbsolutePath());
            }
        } else if (file.isFile() && file.toString().endsWith(".mp3") || file.toString().endsWith(".wav"))
            songFiles.add(file.getAbsolutePath());
    }
}
