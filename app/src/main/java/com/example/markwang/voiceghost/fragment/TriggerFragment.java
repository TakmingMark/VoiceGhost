package com.example.markwang.voiceghost.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.markwang.voiceghost.MainActivity;
import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;

import java.util.Map;

public class TriggerFragment extends DialogFragment {
    private final String TAG = "TriggerFragment";
    private Map.Entry<Float, VoiceGhostInfo> mTriggerPoint;

    private TextView triggerName;
    private TextView triggerLatitude;
    private TextView triggerLongitude;
    private TextView triggerRange;
    private TextView triggerRecipient;
    private TextView triggerTitle;

    public void setTriggerPoint(Map.Entry<Float, VoiceGhostInfo> triggerPoint) {
        mTriggerPoint = triggerPoint;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.trigger, null);
        triggerName = view.findViewById(R.id.triggerName);
        triggerLatitude = view.findViewById(R.id.triggerLatitude);
        triggerLongitude = view.findViewById(R.id.triggerLongitude);
        triggerRange = view.findViewById(R.id.triggerRange);
        triggerRecipient = view.findViewById(R.id.triggerRecipient);
        triggerTitle = view.findViewById(R.id.triggerTitle);

        VoiceGhostInfo voiceGhostInfo = mTriggerPoint.getValue();
        triggerName.setText(voiceGhostInfo.creator);
        triggerLatitude.setText(voiceGhostInfo.lattude);
        triggerLongitude.setText(voiceGhostInfo.longitude);
        triggerRange.setText(voiceGhostInfo.triggerRange);
        triggerRecipient.setText(voiceGhostInfo.recipient);
        triggerTitle.setText(voiceGhostInfo.title);

        String fragmentMessage = String.format(getResources().getString(R.string.dialog_name), mTriggerPoint.getKey());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(fragmentMessage)
                .setView(view)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
