package com.example.markwang.voiceghost.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class SoundRecord {
    private final String TAG="SoundRecord";
    private Context mContext;
    private String mFilePath = null;
    private String mFileName=null;
    private MediaRecorder mMediaRecorder;


    public SoundRecord(Context context){
        mContext=context;
        initilized();
    }

    private void initilized(){

        mFilePath=mContext.getExternalCacheDir().getAbsolutePath();
        Log.d(TAG,"mFilePath:"+mFilePath);
    }

    public void setFileName(String fileName){
        mFileName=fileName;
    }

    public void onRecord(boolean isStart) {
        if(isStart)
            startRecording();
        else
            stopRecording();
    }

    private void startRecording(){
        mMediaRecorder=new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(mFilePath+"/"+mFileName);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mMediaRecorder.start();
    }

    private void stopRecording() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }
}
