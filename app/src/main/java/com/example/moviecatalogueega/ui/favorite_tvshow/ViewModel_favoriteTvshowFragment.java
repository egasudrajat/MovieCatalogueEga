package com.example.moviecatalogueega.ui.favorite_tvshow;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.ModelFilm;

import java.util.List;

public class ViewModel_favoriteTvshowFragment extends ViewModel {

    private MutableLiveData<List<ModelFilm>> ListTv = new MutableLiveData<>();

    public LiveData<List<ModelFilm>> getListTv() {
        return ListTv;
    }

    public void setListTvFavorite(Context context) {
        List<ModelFilm> data;
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        data = db.filmDao().findByType("tvshow");
        ListTv.postValue(data);
    }
}