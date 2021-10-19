package com.pagalbeta.cartoonvideos;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.pagalbeta.cartoonvideos.dao.VideoDao;
import com.pagalbeta.cartoonvideos.entity.Video;

import org.jetbrains.annotations.NotNull;


@Database(
        entities = {Video.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {
    @NotNull
    public abstract VideoDao pigeonDao();
}
