package com.example.xtusicplayer.Views.LandingPage.LibraryFrag;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.R;

public class AllPlaylistViewHolder extends RecyclerView.ViewHolder {
    TextView pName,pTotalSongs;
    ImageView playlistArt,moreOptBtn;
    LinearLayout playlistLayout;

    public AllPlaylistViewHolder(@NonNull View itemView) {
        super(itemView);

        pName=itemView.findViewById(R.id.playlist_name);
        pTotalSongs=itemView.findViewById(R.id.total_music);
        playlistArt=itemView.findViewById(R.id.playlist_img);
        playlistLayout=itemView.findViewById(R.id.playlist_layout);
        moreOptBtn=itemView.findViewById(R.id.more_option);
    }
}
