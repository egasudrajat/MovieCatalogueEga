package com.example.moviecatalogueega.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.moviecatalogueega.ModelFilm;

@Database(entities = ModelFilm.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FilmDao filmDao();
}
