package com.example.xtusicplayer.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPosition = new MutableLiveData<>();

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition.setValue(currentPosition);
    }

    public LiveData<Integer> getCurrentPosition(){
        return currentPosition;
    }
}
