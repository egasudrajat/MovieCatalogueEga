package com.example.moviecatalogueega.ui.favorite_movies;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.ModelFilm;

import java.util.List;

public class ViewModel_FavoriteMoviesfragment extends ViewModel {

    private MutableLiveData<List<ModelFilm>> ListFilm = new MutableLiveData<>();

    public LiveData<List<ModelFilm>> getListFilm() {
        return ListFilm;
    }

    public void setListFavorite(Context context) {
        List<ModelFilm> data;
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        data = db.filmDao().findByType("movies");
        ListFilm.postValue(data);
    }
}