package com.example.xtusicplayer.Model.Room.DataEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class PlaylistEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Name")
    public String name;

    @ColumnInfo(name = "MusicIds")
    public List<MusicEntity> musicIds;


    @Ignore
    public PlaylistEntity(int id, String name, List<MusicEntity> musicIds) {
        this.id = id;
        this.name = name;
        this.musicIds = musicIds;
    }

    public PlaylistEntity(String name, List<MusicEntity> musicIds) {
        this.name = name;
        this.musicIds = musicIds;
    }

    public PlaylistEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicEntity> getMusicIds() {
        return musicIds;
    }

    public void setMusicIds(List<MusicEntity> musicIds) {
        this.musicIds = musicIds;
    }
}
