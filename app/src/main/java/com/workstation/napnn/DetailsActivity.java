package com.workstation.napnn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class DetailsActivity extends AppCompatActivity {

    ImageView backButton;
    VideoView video;
    TextView titleText;
    String titleDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        backButton = (ImageView)findViewById(R.id.back_button);
        video = (VideoView)findViewById(R.id.video_view);
        titleText = (TextView) findViewById(R.id.title_details);

        Intent intent=this.getIntent();
        titleDetail = intent.getStringExtra("TITLE");

        titleText.setText(titleDetail);

//        Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(video);

        Uri uri=Uri.parse("https://i.vimeocdn.com//filter//overlay?src0=https%3A%2F%2Fi.vimeocdn.com%2Fvideo%2F755896288_100x75.jpg&src1=http%3A%2F%2Ff.vimeocdn.com%2Fp%2Fimages%2Fcrawler_play.png");

        video.setMediaController(mediaController);
        video.setVideoURI(uri);
        video.setKeepScreenOn(true);
        video.requestFocus();
        video.start();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
