package com.example.markwang.voiceghost.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.sound.SoundPlayer;
import com.example.markwang.voiceghost.sound.SoundRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFragment extends Fragment implements SoundPlayer.SoundPlayerCallback{
    private final String TAG="CustomFragment";
    private Context mContext;
    private EditText customName;
    private EditText customLattude;
    private EditText customLongitude;
    private EditText customTriggerRange;
    private EditText customRecipient;
    private EditText customTitle;
    private Button recordAudio;
    private Button playAudio;
    private Button updateData;

    private SimpleDateFormat mSimpleDateFormat;
    private SoundRecord mSoundRecord;
    private SoundPlayer mSoundPlayer;

    private boolean isRecording=false;
    private boolean isPlaying=false;

    private String fileName;

    public static CustomFragment newInstance(String str) {
        CustomFragment customFragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("test", str);
        customFragment.setArguments(bundle);
        return customFragment;
    }

    private void initLayout(View contentView) {
        customName = contentView.findViewById(R.id.customNameET);
        customLattude = contentView.findViewById(R.id.customLattudeET);
        customLongitude = contentView.findViewById(R.id.customLongitudeET);
        customTriggerRange = contentView.findViewById(R.id.customTriggerRangeET);
        customRecipient = contentView.findViewById(R.id.customRecipientET);
        customTitle = contentView.findViewById(R.id.customTitleET);
        recordAudio = contentView.findViewById(R.id.recordAudioBT);
        playAudio = contentView.findViewById(R.id.playAudioBT);
        updateData = contentView.findViewById(R.id.updateDataBT);
    }

    private void initObject() {
        mSimpleDateFormat=new SimpleDateFormat("yyyyMMdd-HHmmss");
        mSoundRecord = new SoundRecord(mContext);
        mSoundPlayer=new SoundPlayer(mContext);

        mSoundPlayer.setSoundPlayerCallback(this);
    }

    private void initListener() {
        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording=!isRecording;

                if(isRecording){
                    fileName=mSimpleDateFormat.format(new Date())+".3gp";
                    mSoundRecord.setFileName(fileName);
                    Log.d(TAG,fileName);
                    recordAudio.setText("Stop");
                }
                else{
                    recordAudio.setText("Record");
                }
                mSoundRecord.onRecord(isRecording);
            }
        });

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=!isPlaying;

                if(isPlaying){
                    mSoundPlayer.setFileName(fileName);
                    playAudio.setText("Stop");
                }else{
                    playAudio.setText("Play");
                }
                mSoundPlayer.onPlay(isPlaying);
            }
        });
    }

    private void defaultValue() {
        customName.setText("Mark");
        customLattude.setText("123");
        customLongitude.setText("456");
        customTriggerRange.setText("10");
        customRecipient.setText("Andy");
        customTitle.setText("How are you?");
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        //getArguments.getString(test);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.custom, null);
        initLayout(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initObject();
        initListener();
        defaultValue();
    }

    @Override
    public void playerCompletion() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playAudio.setText("Play");
                isPlaying = !isPlaying;
            }
        });
    }
}
