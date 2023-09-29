package com.example.xtusicplayer.Model.Room.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.xtusicplayer.Model.MusicRepo.TypeListConverter;
import com.example.xtusicplayer.Model.Room.DAOs.MusicDao;
import com.example.xtusicplayer.Model.Room.DAOs.PlaylistDao;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;

@Database(entities = {MusicEntity.class, PlaylistEntity.class}, version = 1)
@TypeConverters(TypeListConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String dbName = "XTusicPlayerDatabase";
    private static AppDatabase instance;

    public static synchronized AppDatabase getDB(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract MusicDao musicDao();
    public abstract PlaylistDao playlistDao();

}
