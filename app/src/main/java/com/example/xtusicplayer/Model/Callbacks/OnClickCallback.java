package com.example.xtusicplayer.Model.Callbacks;

import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;

public interface OnClickCallback {
    public void shareData(int position, PlaylistEntity currentPlaylist);
}
