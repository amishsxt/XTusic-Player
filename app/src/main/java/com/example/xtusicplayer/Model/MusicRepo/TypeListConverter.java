package com.example.xtusicplayer.Model.MusicRepo;

import androidx.room.TypeConverter;

import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class TypeListConverter implements Serializable {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<MusicEntity> fromString(String value) {
        Type listType = new TypeToken<List<MusicEntity>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MusicEntity> list) {
        return gson.toJson(list);
    }
}
