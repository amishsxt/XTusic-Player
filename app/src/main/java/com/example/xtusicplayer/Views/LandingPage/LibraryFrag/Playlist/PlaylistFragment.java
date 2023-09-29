package com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Callbacks.OnClickCallback;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;
import com.example.xtusicplayer.R;
import com.example.xtusicplayer.ViewModel.MusicViewModel;
import com.example.xtusicplayer.ViewModel.PlaylistViewModel;
import com.example.xtusicplayer.Views.LandingPage.HomeFrag.AllMusicAdapter;
import com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.AddMusic.AddMusicActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistFragment extends Fragment {

    private ImageView backBtn,playlistCoverImage;
    private TextView playlistName,headerPlaylistName,musicCount;
    private CardView addSongs;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private OnClickCallback onClickCallback;
    private AllMusicAdapter allMusicAdapter;
    private MusicEntity currentMusic;
    private MusicViewModel musicViewModel;
    private PlaylistViewModel playlistViewModel;
    private List<MusicEntity> musicEntityList;
    private PlaylistEntity playlistEntity;
    private AppDatabase appDatabase;
    private int position;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public PlaylistFragment(int position) {
        // Required empty public constructor
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        //casting views
        backBtn=view.findViewById(R.id.back_btn);
        playlistName=view.findViewById(R.id.playlist_name);
        headerPlaylistName=view.findViewById(R.id.playlist_name_header);
        musicCount=view.findViewById(R.id.music_count);
        addSongs=view.findViewById(R.id.add_music_btn);
        playlistCoverImage=view.findViewById(R.id.playlist_cover_img);
        recyclerView=view.findViewById(R.id.recyler_view);
        progressBar=view.findViewById(R.id.progress_bar);

        //vertical Recycler Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        //setting up database
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        playlistViewModel.getPlaylistEntityList().observe(getViewLifecycleOwner(), new Observer<List<PlaylistEntity>>() {
            @Override
            public void onChanged(List<PlaylistEntity> playlistEntities) {
                // Fetch songs from roomDB
                playlistEntity = playlistEntities.get(position);
                musicEntityList = playlistEntity.getMusicIds();

                //setting up
                playlistName.setText(playlistEntity.getName());
                headerPlaylistName.setText(playlistEntity.getName());

                //setLocalPicture(playlistEntity.get);
                musicCount.setText(String.valueOf(playlistEntity.getMusicIds().size()) + " songs");

                //setting adapter
                setAdapter();
            }
        });

        //backBtn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //backPressed
                requireActivity().onBackPressed();
            }
        });

        //addSongs
        addSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddMusicActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });

        return view;
    }

    private void setAdapter(){
        showProgressBar();
        allMusicAdapter = new AllMusicAdapter(getContext(), musicEntityList, new AllMusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onClickCallback = (OnClickCallback) getActivity();
                onClickCallback.shareData(position, playlistEntity);
            }
        });

        recyclerView.setAdapter(allMusicAdapter);
        hideProgressBar();
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setLocalPicture(String uri){

        Picasso.get()
                .load(uri)
                .error(R.drawable.playlist_img)
                .into(playlistCoverImage);

    }

}