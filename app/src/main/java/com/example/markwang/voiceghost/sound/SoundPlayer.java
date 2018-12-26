package com.example.markwang.voiceghost.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class SoundPlayer {
    private final String TAG = "SoundPlayer";
    private Context mContext;
    private String mFilePathAndName = null;

    private MediaPlayer mMediaPlayer;
    private Callback mCallback;

    public SoundPlayer(Context context) {
        mContext = context;
        initilized();
        initListener();
    }

    private void initilized() {
        mMediaPlayer = new MediaPlayer();
    }

    private void initListener() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
                mCallback.playerCompletion();
            }
        });
    }

    public void setFilePathAndName(String filePathAndName) {
        mFilePathAndName = filePathAndName;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void onPlay(boolean isStart) {
        if (isStart) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        try {
            mMediaPlayer.setDataSource(mFilePathAndName);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "MediaPlayer prepare failed");
        }
    }

    private void stopPlaying() {
        mMediaPlayer.reset();

    }

    public void onDestory() {
        mMediaPlayer = null;
    }

    public interface Callback {
        void playerCompletion();
    }
}
