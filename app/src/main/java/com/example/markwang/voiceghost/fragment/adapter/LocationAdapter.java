package com.example.markwang.voiceghost.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private Context mContext;
    private ArrayList<VoiceGhostInfo> mDataSet;

    public static class LocationViewHolder extends  RecyclerView.ViewHolder{
        public TextView mCreator;
        public TextView mLattude;
        public TextView mLongitude;
        public TextView mTriggerRange;
        public TextView mRecipient;
        public TextView mTitle;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public LocationAdapter(Context context,ArrayList<VoiceGhostInfo> dataSet){
        mContext=context;
        mDataSet=dataSet;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View locationContentRecyclerView=LayoutInflater.from(mContext).inflate(R.layout.location_content_recycler_view,parent,false);

        LocationViewHolder holder=new LocationViewHolder(locationContentRecyclerView);

        holder.mCreator =(TextView)locationContentRecyclerView.findViewById(R.id.creator);
        holder.mLattude =locationContentRecyclerView.findViewById(R.id.lattude);
        holder.mLongitude =locationContentRecyclerView.findViewById(R.id.longitude);
        holder.mTriggerRange =locationContentRecyclerView.findViewById(R.id.triggerRange);
        holder.mRecipient =locationContentRecyclerView.findViewById(R.id.recipient);
        holder.mTitle =locationContentRecyclerView.findViewById(R.id.title);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, int position) {
        VoiceGhostInfo voiceGhostInfo=mDataSet.get(position);
        locationViewHolder.mCreator.setText(voiceGhostInfo.creator);
        locationViewHolder.mLattude.setText(voiceGhostInfo.lattude);
        locationViewHolder.mLongitude.setText(voiceGhostInfo.longitude);
        locationViewHolder.mTriggerRange.setText(voiceGhostInfo.triggerRange);
        locationViewHolder.mRecipient.setText(voiceGhostInfo.recipient);
        locationViewHolder.mTitle.setText(voiceGhostInfo.title);

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
