package com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.AddMusic;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;
import com.example.xtusicplayer.R;
import com.example.xtusicplayer.ViewModel.MusicViewModel;
import com.example.xtusicplayer.ViewModel.PlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddMusicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ImageView backBtn;
    private AppDatabase appDatabase;
    private PlaylistViewModel playlistViewModel;
    private MusicViewModel musicViewModel;
    private List<MusicEntity> musicList,addMusicList,playlistList;
    private List<PlaylistEntity> playlistEntityList;
    private PlaylistEntity playlistEntity;
    private AddMusicAdapter addMusicAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        //casting views
        backBtn=findViewById(R.id.back_btn);
        searchView=findViewById(R.id.search_bar);
        recyclerView=findViewById(R.id.recyler_view);
        progressBar=findViewById(R.id.progress_bar);

        //setting up database
        appDatabase = AppDatabase.getDB(this);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);

        //vertical Recycler Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        showProgressBar();

        position = getIntent().getIntExtra("position",0);

        //fetching playlist data
        playlistViewModel.getPlaylistEntityList().observe(this, new Observer<List<PlaylistEntity>>() {
            @Override
            public void onChanged(List<PlaylistEntity> playlistEntities) {
                playlistEntityList = playlistEntities;
                playlistEntity = playlistEntityList.get(position);
                playlistList = new ArrayList<>(playlistEntity.getMusicIds());
            }
        });

        musicViewModel.getMusicEntityList().observe(this, new Observer<List<MusicEntity>>() {
            @Override
            public void onChanged(List<MusicEntity> musicEntities) {
                musicList = musicEntities;
                addMusicList = musicEntities;
                MusicEntity cMusic;

                for(int i=0; i<playlistList.size()-1;i++){
                    cMusic = playlistList.get(i);
                    addMusicList.remove(cMusic);
                }

                //setting adapter
                setAdapter();
            }
        });

        //backBtn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistEntity.setMusicIds(playlistList);
                appDatabase.playlistDao().updatePlaylist(playlistEntity);

                Toast.makeText(AddMusicActivity.this, "added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setAdapter(){
        if(addMusicList!=null){
            showProgressBar();
            addMusicAdapter = new AddMusicAdapter(this, addMusicList, new AddMusicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
//                    playlistList.add(addMusicList.get(position));
//                    playlistEntity.setMusicIds(playlistList);

                    addToPlaylist(addMusicList.get(position));
                }
            });

            recyclerView.setAdapter(addMusicAdapter);
            hideProgressBar();
        }

    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void addToPlaylist(MusicEntity music){
        playlistList.add(music);
        playlistEntity.setMusicIds(playlistList);

        addMusicList.remove(music);
        addMusicAdapter.notifyDataSetChanged();

    }
}