package com.example.markwang.voiceghost.component;

public class VoiceGhostInfo {
    private final String TAG="VoiceGhostInfo";
    public String creator;
    public String recipient;
    public String lattude;
    public String longitude;
    public String triggerRange;
    public String createAt;
    public String expireAt;
    public String readOnce;
    public String title;
    public String voiceAddress;

    public VoiceGhostInfo(){

    }

    public VoiceGhostInfo(String creator, String recipient, String lattude, String longitude, String triggerRange, String createAt, String expireAt, String readOnce, String title, String voiceAddress) {
        this.creator = creator;
        this.recipient = recipient;
        this.lattude = lattude;
        this.longitude = longitude;
        this.triggerRange = triggerRange;
        this.createAt = createAt;
        this.expireAt = expireAt;
        this.readOnce = readOnce;
        this.title = title;
        this.voiceAddress = voiceAddress;
    }

    public String print(){
        String log="mCreator:"+creator+"mRecipient:"+recipient+"mLattude:"+lattude+"mLongitude"+longitude+
                "mTriggerRange:"+ triggerRange +"createAt:"+createAt+"expireAt:"+expireAt+"readOnce:"+readOnce+"mTitle:"+title +"voiceAddress"+voiceAddress;
        return  log;
    }

}