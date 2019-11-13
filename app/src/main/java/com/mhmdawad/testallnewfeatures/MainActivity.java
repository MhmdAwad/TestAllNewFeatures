package com.mhmdawad.testallnewfeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class MainActivity extends AppCompatActivity {

//    DefaultTrackSelector trackSelector;
    int HI_BITRATE = 2097152;
    int MI_BITRATE = 1048576;
    int LO_BITRATE = 524288;
    SimpleExoPlayer player;
    PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ImageView ImgFullScreen;
    private ImageView ImgVideoQuality;
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        trackSelector = new DefaultTrackSelector();
//        DefaultTrackSelector.Parameters defaultTrackParam = trackSelector.buildUponParameters()
//                .setMaxVideoSizeSd()
//                .build();
//        trackSelector.setParameters(defaultTrackParam);

//        findViewById(R.id.quality_lo).setOnClickListener(v -> {
//            DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
//                    .setMaxVideoBitrate(LO_BITRATE)
////                    .setMaxVideoSize(480,270)
//                    .setForceHighestSupportedBitrate(true)
//                    .build();
//            trackSelector.setParameters(parameters);
//            releasePlayer();
//            initializePlayer();
//        });
//
//        findViewById(R.id.quality_mi).setOnClickListener(v -> {
//            DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
//                    .setMaxVideoBitrate(MI_BITRATE)
////                    .setMaxVideoSize(960,540)
//                    .setForceHighestSupportedBitrate(true)
//                    .build();
//            trackSelector.setParameters(parameters);
//            releasePlayer();
//            initializePlayer();
//        });
//
//        findViewById(R.id.quality_hi).setOnClickListener(v -> {
//            DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
//                    .setMaxVideoBitrate(HI_BITRATE)
////                    .setMaxVideoSize(1920,1080)
//                    .setForceHighestSupportedBitrate(true)
//                    .build();
//            trackSelector.setParameters(parameters);
//            releasePlayer();
//            initializePlayer();
//        });

        ImgFullScreen =  findViewById(R.id.fullscreen_icon);
        ImgVideoQuality = findViewById(R.id.video_quality);
        playerView = findViewById(R.id.videoView);

        ImgVideoQuality.setOnClickListener(v ->{});
        ImgFullScreen.setOnClickListener(v -> {
            if (isFullScreen) {
                ImgFullScreen.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.open_fullscreen));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
                isFullScreen = false;
            } else {
                ImgFullScreen.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.close_fullscreen));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);
                isFullScreen = true;
            }

        });

    }

    private void initializePlayer() {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(trackSelector
                    .buildUponParameters()
                    .setMaxVideoSizeSd()
                    .build());

        }
        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(player);
        Uri uri = Uri.parse("http://ldjosh.menoetius.bysh.me/data/Disney/Alice%20in%20Wonderland.mkv");
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "user");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
