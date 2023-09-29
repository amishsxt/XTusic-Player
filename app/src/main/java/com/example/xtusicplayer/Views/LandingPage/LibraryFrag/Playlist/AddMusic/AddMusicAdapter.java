package com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.AddMusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddMusicAdapter extends RecyclerView.Adapter<AddMusicViewHolder> {

    private Context context;
    private List<MusicEntity> musicList;
    private OnItemClickListener clickListener;

    public AddMusicAdapter(Context context, List<MusicEntity> musicList, OnItemClickListener clickListener) {
        this.context = context;
        this.musicList = musicList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AddMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddMusicViewHolder(LayoutInflater.from(context).inflate(R.layout.add_song_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddMusicViewHolder holder, int position) {
        holder.mTittle.setText(musicList.get(position).getTittle());
        holder.mArtist.setText(musicList.get(position).getArtist());
        setLocalPicture(holder,musicList.get(position).getAlbumArtUrl());


        holder.musicAdded=true;
        holder.musicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.musicAdded){
                    holder.musicAdded=false;
                    holder.addMusicBtn.setImageResource(R.drawable.done);
                    clickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    // Interface to define the click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void setLocalPicture(AddMusicViewHolder holder, String uri){

        Picasso.get()
                .load(uri)
                .error(R.drawable.music_img)
                .into(holder.albumArt);

    }
}
