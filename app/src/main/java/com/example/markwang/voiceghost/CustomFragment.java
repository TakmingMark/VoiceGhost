package com.example.markwang.voiceghost;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomFragment extends Fragment {
    private Context mContext;
    public static CustomFragment newInstance(String str){
        CustomFragment customFragment=new CustomFragment();
        Bundle bundle=new Bundle();
        bundle.putString("test",str);
        customFragment.setArguments(bundle);
        return  customFragment;
    }

    @Override
    public void onAttach(Context context) {
        mContext=context;
        //getArguments.getString(test);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.custom, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
    }
}
