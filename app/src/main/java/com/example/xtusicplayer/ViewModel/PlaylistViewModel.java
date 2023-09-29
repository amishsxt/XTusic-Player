package com.example.xtusicplayer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xtusicplayer.Model.Room.DAOs.PlaylistDao;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private LiveData<List<PlaylistEntity>> playlistEntity;
    private PlaylistDao playlistDao;
    private AppDatabase appDatabase;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDB(application.getApplicationContext());
        playlistDao = appDatabase.playlistDao();
        playlistEntity = playlistDao.getAllPlaylists();
    }


    public LiveData<List<PlaylistEntity>> getPlaylistEntityList(){
        return playlistEntity;
    }

    public void deletePlaylistEntity(PlaylistEntity playlistEntity){
        playlistDao.deletePlaylist(playlistEntity);
    }

    public void updatePlaylistEntity(PlaylistEntity playlistEntity){
        playlistDao.updatePlaylist(playlistEntity);
    }
}
