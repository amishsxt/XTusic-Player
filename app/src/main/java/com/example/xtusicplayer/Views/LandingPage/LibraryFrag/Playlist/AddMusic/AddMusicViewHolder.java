package com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.AddMusic;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.R;

public class AddMusicViewHolder extends RecyclerView.ViewHolder {

    TextView mTittle,mArtist;
    ImageView albumArt,addMusicBtn;
    LinearLayout musicLayout;
    boolean musicAdded;

    public AddMusicViewHolder(@NonNull View itemView) {
        super(itemView);

        mTittle=itemView.findViewById(R.id.music_tittle);
        mArtist=itemView.findViewById(R.id.artist_name);
        addMusicBtn=itemView.findViewById(R.id.add_btn);
        musicLayout=itemView.findViewById(R.id.music_layout);
        albumArt=itemView.findViewById(R.id.music_img);

    }
}
