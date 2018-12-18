package com.example.markwang.voiceghost;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserFragment extends Fragment {
    private Context mContext;
    public static UserFragment newInstance(String str){
        UserFragment userFragment=new UserFragment();
        Bundle bundle=new Bundle();
        bundle.putString("test",str);
        userFragment.setArguments(bundle);
        return  userFragment;
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
        View contentView = inflater.inflate(R.layout.user, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
    }
}
