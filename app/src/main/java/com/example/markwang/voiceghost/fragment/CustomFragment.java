package com.example.markwang.voiceghost.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.markwang.voiceghost.R;

public class CustomFragment extends Fragment {
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

    public static CustomFragment newInstance(String str) {
        CustomFragment customFragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("test", str);
        customFragment.setArguments(bundle);
        return customFragment;
    }

    private void initLayout(View contentView){
        customName = contentView.findViewById(R.id.customNameET);
        customLattude = contentView.findViewById(R.id.customLattudeET);
        customLongitude = contentView.findViewById(R.id.customLongitudeET);
        customTriggerRange = contentView.findViewById(R.id.customTriggerRangeET);
        customRecipient = contentView.findViewById(R.id.customRecipientET);
        customTitle=contentView.findViewById(R.id.customTitleET);
        recordAudio = contentView.findViewById(R.id.recordAudioBT);
        playAudio = contentView.findViewById(R.id.playAudioBT);
        updateData = contentView.findViewById(R.id.updateDataBT);
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
        defaultValue();
    }
}
