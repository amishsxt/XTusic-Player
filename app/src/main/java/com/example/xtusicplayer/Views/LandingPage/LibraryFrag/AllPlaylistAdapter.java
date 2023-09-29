package com.example.xtusicplayer.Views.LandingPage.LibraryFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllPlaylistAdapter extends RecyclerView.Adapter<AllPlaylistViewHolder> {

    private Context context;
    private List<PlaylistEntity> playlistList;
    private OnItemClickListener clickListener;

    public AllPlaylistAdapter(Context context, List<PlaylistEntity> playlistList, OnItemClickListener clickListener) {
        this.context = context;
        this.playlistList = playlistList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AllPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllPlaylistViewHolder(LayoutInflater.from(context).inflate(R.layout.playlist_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllPlaylistViewHolder holder, int position) {
        holder.pName.setText(playlistList.get(position).getName());
        holder.pTotalSongs.setText(String.valueOf(playlistList.get(position).getMusicIds().size()));
        holder.moreOptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.moreOptViewClick(position);
            }
        });

        holder.playlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.layoutViewClick(position);

            }
        });
//        setLocalPicture(holder,playlistList.get(position).getMusicIds().get(0).getAlbumArtUrl());

    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    // Interface to define the click listener
    public interface OnItemClickListener {
        void moreOptViewClick(int position);
        void layoutViewClick(int position);
    }


    private void setLocalPicture(AllPlaylistViewHolder holder, String uri){

        Picasso.get()
                .load(uri)
                .error(R.drawable.music_img)
                .into(holder.playlistArt);

    }
}
