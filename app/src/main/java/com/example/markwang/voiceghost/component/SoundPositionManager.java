package com.example.markwang.voiceghost.component;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SoundPositionManager extends BaseManager{
    private final String TAG="SoundPositionManager";
    private static SoundPositionManager mInstance = new SoundPositionManager();

    private  Callback mCallback;
    public static SoundPositionManager getInstance() {
        if(mInstance==null)
            mInstance=new SoundPositionManager();
        return mInstance;
    }

    private ArrayList<VoiceGhostInfo> mVoiceGhostArrayList;

    public void initialized() {
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
    public Map.Entry<Float,VoiceGhostInfo> getTriggerPoint(LatLng userLatLng){
        return calculateTriggerDistance(userLatLng);
    }

    public Map.Entry<Float,VoiceGhostInfo> calculateTriggerDistance(LatLng userLatLng){
        TreeMap<Float,VoiceGhostInfo> sortDistanceMap=new TreeMap();
        for(VoiceGhostInfo voiceGhostInfo:mVoiceGhostArrayList){
            LatLng coutomLatLng=new LatLng(Double.valueOf(voiceGhostInfo.lattude),Double.valueOf(voiceGhostInfo.longitude));
            float[] calculateDistanceResult=new float[1];
            Location.distanceBetween(userLatLng.latitude,userLatLng.longitude,coutomLatLng.latitude,coutomLatLng.longitude,calculateDistanceResult);
            sortDistanceMap.put(calculateDistanceResult[0],voiceGhostInfo);
        }
        for(Map.Entry<Float,VoiceGhostInfo>entry:sortDistanceMap.entrySet()){
            Log.d(TAG,"distance:"+entry.getKey());
            return entry;
        }
        return  null;
    }

    private void removeArrayListAll() {
        mVoiceGhostArrayList.clear();
    }

    @Override
    public void onDestory() {
        mInstance=null;
    }

    public interface Callback{
        void onDataUpdate(ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList);
    }
}
