package com.example.markwang.voiceghost;

import android.util.Log;

public class VoiceGhostInfo {
    private final String TAG="VoiceGhostInfo";
    public String creator;
    public String recipient;
    public String location;
    public String distanceRange;
    public String createAt;
    public String expireAt;
    public String readOnce;
    public String title;

    VoiceGhostInfo(){

    }

    public VoiceGhostInfo(String creator, String recipient, String location, String distanceRange, String createAt, String expireAt, String readOnce, String title) {
        this.creator = creator;
        this.recipient = recipient;
        this.location = location;
        this.distanceRange = distanceRange;
        this.createAt = createAt;
        this.expireAt = expireAt;
        this.readOnce = readOnce;
        this.title = title;
    }


    public String print(){
        String log="creator:"+creator+"recipient:"+recipient+"location:"+location+
                "distanceRange:"+distanceRange+"createAt:"+createAt+"expireAt:"+expireAt+"readOnce:"+readOnce+"title:"+title;
        return  log;
    }

}