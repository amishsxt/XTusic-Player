package com.example.xtusicplayer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xtusicplayer.Model.Room.DAOs.MusicDao;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;

import java.util.List;

public class MusicViewModel extends AndroidViewModel {

    private LiveData<List<MusicEntity>> musicEntityList;
    private MusicDao musicDao;
    private AppDatabase appDatabase;

    public MusicViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDB(application.getApplicationContext());
        musicDao = appDatabase.musicDao();
        musicEntityList = musicDao.getAllMusic();
    }


    public LiveData<List<MusicEntity>> getMusicEntityList(){
        return musicEntityList;
    }

}
