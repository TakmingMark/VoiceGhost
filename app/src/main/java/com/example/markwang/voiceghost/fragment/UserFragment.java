package com.example.markwang.voiceghost.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.markwang.voiceghost.fragment.adapter.LocationAdapter;
import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private final String TAG="UserFragment";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;


    //    If Android decides to recreate your Fragment later,
    //    it's going to call the no-argument constructor of your fragment.
    //    So overloading the constructor is not a solution.
    public static UserFragment newInstance(String str){
        UserFragment userFragment=new UserFragment();
        Bundle bundle=new Bundle();
        bundle.putString("wantStore",str);
        userFragment.setArguments(bundle);
        return  userFragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG,"onAttach");
        mContext=context;
        //getArguments.getString(test);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View contentView = inflater.inflate(R.layout.user, null);
        mRecyclerView=contentView.findViewById(R.id.locationRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger=new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManger);

        ArrayList<VoiceGhostInfo> dataSet=new ArrayList<>();
        for(int i=0;i<0;i++){
            VoiceGhostInfo voiceGhostInfo=new VoiceGhostInfo();
            voiceGhostInfo.creator="Mark";
            voiceGhostInfo.lattude="25.043066964818927";
            voiceGhostInfo.longitude="121.5646205842495";
            voiceGhostInfo.triggerRange ="10";
            voiceGhostInfo.recipient="12";
            voiceGhostInfo.title="how are you?";
            dataSet.add(voiceGhostInfo);
        }

        mAdapter=new LocationAdapter(mContext,dataSet);
        mRecyclerView.setAdapter(mAdapter);

        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }
}
