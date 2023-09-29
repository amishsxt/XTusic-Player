package com.example.xtusicplayer.Views.LandingPage.HomeFrag;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.R;

public class AllMusicViewHolder extends RecyclerView.ViewHolder {

    TextView mTittle,mArtist;
    ImageView albumArt,moreOptBtn;
    LinearLayout musicLayout;

    public AllMusicViewHolder(@NonNull View itemView) {
        super(itemView);

        mTittle=itemView.findViewById(R.id.music_tittle);
        mArtist=itemView.findViewById(R.id.artist_name);
        moreOptBtn=itemView.findViewById(R.id.more_option);
        musicLayout=itemView.findViewById(R.id.music_layout);
        albumArt=itemView.findViewById(R.id.music_img);

    }
}
