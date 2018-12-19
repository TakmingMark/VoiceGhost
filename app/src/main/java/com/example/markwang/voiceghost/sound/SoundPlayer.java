package com.example.markwang.voiceghost.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class SoundPlayer {
    private final String TAG="SoundPlayer";
    private Context mContext;
    private String mFilePath = null;
    private String mFileName=null;
    private MediaPlayer mMediaPlayer;
    private SoundPlayerCallback mSoundPlayerCallback;

    SoundPlayer(Context context){
        mContext=context;
        initilized();
        initListener();
    }

    private void initilized(){
        mFilePath=mContext.getExternalCacheDir().getAbsolutePath();
        mMediaPlayer=new MediaPlayer();
    }

    private void initListener(){
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mSoundPlayerCallback.playerCompletion();
            }
        });
    }

    public void setFileName(String fileName){
        mFileName=fileName;
    }

    public void setSoundPlayerCallback(SoundPlayerCallback soundPlayerCallback){
        mSoundPlayerCallback=soundPlayerCallback;
    }

    public void onPlay(boolean isStart){
        if(isStart){
            startPlaying();
        }else{
            stopPlaying();
        }
    }

    private void startPlaying(){
        try{
            mMediaPlayer.setDataSource(mFilePath+"/"+mFileName);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (IOException e){
            Log.d(TAG,"MediaPlayer prepare failed");
        }
    }

    private void stopPlaying(){
        mMediaPlayer.release();
    }

    public void onDestory(){
        mMediaPlayer=null;
    }

    public interface SoundPlayerCallback{
        void playerCompletion();
    }
}
