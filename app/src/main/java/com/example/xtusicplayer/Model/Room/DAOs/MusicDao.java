package com.example.xtusicplayer.Model.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;

import java.util.List;

@Dao
public interface MusicDao {
    @Query("SELECT * FROM MusicEntity ORDER BY Tittle ASC")
    LiveData<List<MusicEntity>> getAllMusic();

    @Query("SELECT * FROM MusicEntity ORDER BY Tittle DESC")
    LiveData<List<MusicEntity>> getAllMusicDesc();

    @Query("SELECT * FROM MusicEntity ORDER BY Album ASC")
    LiveData<List<MusicEntity>> getAllMusicAlbum();

    @Query("SELECT * FROM MusicEntity ORDER BY Album DESC")
    LiveData<List<MusicEntity>> getAllMusicAlbumDesc();

    @Query("SELECT * FROM MusicEntity ORDER BY Artist ASC")
    LiveData<List<MusicEntity>> getAllMusicArtist();

    @Query("SELECT * FROM MusicEntity ORDER BY Artist DESC")
    LiveData<List<MusicEntity>> getAllMusicArtistDesc();

    @Query("SELECT COUNT(Data) FROM MusicEntity")
    int countDataRow();

    @Insert
    void insert(MusicEntity music);
}
