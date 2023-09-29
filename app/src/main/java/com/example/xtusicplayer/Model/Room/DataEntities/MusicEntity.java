package com.example.xtusicplayer.Model.Room.DataEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MusicEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="Data")
    public String data;

    @ColumnInfo(name="Tittle")
    public String tittle;

    @ColumnInfo(name="Artist")
    public String artist;

    @ColumnInfo(name="Album")
    public String album;

    @ColumnInfo(name="AlbumArtUrl")
    public String albumArtUrl;

    @ColumnInfo(name="Duration")
    public long duration;

    @Ignore
    public MusicEntity(int id, String data, String tittle, String artist, String album, String albumArtUrl, long duration) {
        this.id = id;
        this.data = data;
        this.tittle = tittle;
        this.artist = artist;
        this.album = album;
        this.albumArtUrl = albumArtUrl;
        this.duration = duration;
    }

    public MusicEntity(String data, String tittle, String artist, String album, String albumArtUrl, long duration) {
        this.data = data;
        this.tittle = tittle;
        this.artist = artist;
        this.album = album;
        this.albumArtUrl = albumArtUrl;
        this.duration = duration;
    }

    public MusicEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }

    public void setAlbumArtUrl(String albumArtUrl) {
        this.albumArtUrl = albumArtUrl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
