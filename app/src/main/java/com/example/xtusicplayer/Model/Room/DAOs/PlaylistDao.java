package com.example.xtusicplayer.Model.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM PlaylistEntity ORDER BY Name ASC")
    LiveData<List<PlaylistEntity>> getAllPlaylists();

    @Query("SELECT * FROM PlaylistEntity ORDER BY Name DESC")
    LiveData<List<PlaylistEntity>> getAllPlaylistsDesc();

    @Insert
    void insertPlaylist(PlaylistEntity playlistEntity);

    @Delete
    void deletePlaylist(PlaylistEntity playlistEntity);

    @Update
    void updatePlaylist(PlaylistEntity playlistEntity);

    @Query("SELECT COUNT(Name) FROM PlaylistEntity")
    int countDataRow();
}
