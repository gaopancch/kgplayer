package com.kuaige.kgplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kuaige.player.listener.VideoListener;
import com.kuaige.player.player.VideoPlayer;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity implements VideoListener {
    public static String TAG = "gaopankuaigeplayer";
    private VideoPlayer videoPlayer;
    private long currentPosition = 59000;
    private long maxPosition = 0;
    private Button play,pause;
    private Handler progressHanler;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"MainActivity onCreate ");
        videoPlayer = findViewById(R.id.video);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        progressBar = findViewById(R.id.progress_bar);
        progressHanler = new Handler();
        videoPlayer.setVideoListener(this);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.pause();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initPlayer();
    }

    private void initPlayer(){
        videoPlayer.setPath("https://cdn.letv-cdn.com/2018/12/05/JOCeEEUuoteFrjCg/playlist.m3u8");
        try {
            videoPlayer.load();
        } catch (IOException e) {
            Toast.makeText(this,"播放失败",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentPosition = videoPlayer.getCurrentPosition();
        videoPlayer.release();
        progressHanler.removeCallbacks(progressRunnable);
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        Log.i(TAG,"onBufferingUpdate: "+i);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Log.i(TAG,"onCompletion");
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.i(TAG,"onPrepared");
        videoPlayer.start();
        maxPosition = videoPlayer.getDuration();
        videoPlayer.seekTo(currentPosition);
        progressHanler.postDelayed(progressRunnable,1000);
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            currentPosition = videoPlayer.getCurrentPosition();
            int progress = (int) ((videoPlayer.getCurrentPosition()/maxPosition) * 10000) ;
            Log.i(TAG,"MainActivity currentPosition: "+currentPosition + " maxPosition:"+maxPosition);
            Log.i(TAG,"MainActivity progress: "+progress );
            progress += 50;
            progressBar.setProgress(progress);
            progressHanler.postDelayed(this,1000);
        }
    };

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        Log.i(TAG,"onSeekComplete");
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        Log.i(TAG,"onVideoSizeChanged");
    }


}
