package com.example.markwang.voiceghost.component;

import java.util.ArrayList;

public class SoundPositionManager {

    private final static SoundPositionManager mInstance = new SoundPositionManager();

    private  Callback mCallback;
    public static SoundPositionManager getInstance() {
        return mInstance;
    }

    private ArrayList<VoiceGhostInfo> mVoiceGhostArrayList;

    public void initilized() {
        mVoiceGhostArrayList = new ArrayList<>();
    }

    public void setCallback(Callback callback){
        mCallback=callback;
    }

    public void putVoiceGhost(VoiceGhostInfo voiceGhostInfo) {
        mVoiceGhostArrayList.add(voiceGhostInfo);
    }

    public void putVoiceGhost(ArrayList<VoiceGhostInfo> voiceGhostArrayList){
        removeArrayListAll();
        mVoiceGhostArrayList.addAll(voiceGhostArrayList);
        mCallback.onDataUpdate(mVoiceGhostArrayList);
    }

    private void removeArrayListAll() {
        mVoiceGhostArrayList.clear();
    }

    public interface Callback{
        void onDataUpdate(ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList);
    }
}
