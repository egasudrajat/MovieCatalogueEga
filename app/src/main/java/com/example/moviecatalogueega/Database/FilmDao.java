package com.example.moviecatalogueega.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviecatalogueega.ModelFilm;

import java.util.List;

@Dao
public interface FilmDao {

    @Query("SELECT * FROM film_table ORDER BY nama ASC")
    Cursor getAll();

    @Query("SELECT * FROM film_table WHERE idfilm LIKE :idfilm ")
    ModelFilm findById(int idfilm);

    @Query("SELECT * FROM film_table WHERE idfilm LIKE :idfilm ")
    Cursor findByIdC(int idfilm);

    @Query("SELECT * FROM film_table WHERE type LIKE :type ")
    List<ModelFilm> findByType(String type);

    @Query("SELECT * FROM film_table WHERE type LIKE :type ")
    Cursor findByTypeC(String type);

    @Query("DELETE FROM film_table WHERE idfilm LIKE :selection")
    int deleteBySelection(String selection);


    @Insert
    long insertAll(ModelFilm modelFilms);

    @Delete
    int delete(ModelFilm modelFilms);

    @Update
    int update(ModelFilm modelFilms);


}
