package com.example.xtusicplayer.Model.MusicRepo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.xtusicplayer.Model.Callbacks.MusicFetchCallback;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;

import java.io.File;

public class MusicHelper {
    private AppDatabase appDatabase;

    public void getAllMP3Files(Context context, MusicFetchCallback musicFetchCallback) {

        appDatabase = AppDatabase.getDB(context.getApplicationContext());
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        Cursor cursor = contentResolver.query(uri, projection, selection, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumArtUrl = getAlbumArtUri(albumId).toString();

                // Handle cases where attributes might be null or empty
                if (artist == null || artist.isEmpty()) {
                    artist = "Unknown Artist";
                }
                if (album == null || album.isEmpty()) {
                    album = "Unknown Album";
                }
                if (albumArtUrl == null || albumArtUrl.isEmpty()){
                    albumArtUrl = "default_album_art_url";
                }

                //setting music attributes
                MusicEntity music = new MusicEntity();
                music.setData(data);
                music.setTittle(title);
                music.setArtist(artist);
                music.setAlbum(album);
                music.setDuration(duration);
                music.setAlbumArtUrl(albumArtUrl);

                if(new File(music.getData()).exists()){
                    //Inserting data to roomDB
                    appDatabase.musicDao().insert(music);
                }
            }
            cursor.close();
            if(appDatabase.musicDao().countDataRow() == 0){
                musicFetchCallback.onMusicFetchFailed("No music found");
            }
            else {
                musicFetchCallback.onMusicFetchComplete();
            }
        }else {
            musicFetchCallback.onMusicFetchFailed("Failed!");
        }
    }

    private static Uri getAlbumArtUri(long albumId) {
        return Uri.parse("content://media/external/audio/albumart/" + albumId);
    }
}
