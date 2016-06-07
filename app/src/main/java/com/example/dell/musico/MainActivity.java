package com.example.dell.musico;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView playBtn, nextBtn, prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
