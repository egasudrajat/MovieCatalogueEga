package com.example.moviefavorite.TabLayout;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviefavorite.ModelFilm;
import com.example.moviefavorite.helper.MappingHelper;

import java.util.List;

public class TabFavoriteMoviesViewModel extends ViewModel {
    public static final String AUTHORITY = "com.example.moviecatalogueega";
    private static final String SCHEME = "content";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("film_table")
            .build();

    public LiveData<List<ModelFilm>> getListFavorite() {
        return ListFavorite;
    }

    private MutableLiveData<List<ModelFilm>> ListFavorite = new MutableLiveData<>();

    public void setListFavorite(Context context){
        List<ModelFilm> data;
        Cursor datacur = context.getContentResolver().query(CONTENT_URI,null,"movies",null,null);
        data = MappingHelper.mapCursorToArrayList2(datacur);
        ListFavorite.postValue(data);
    }
}
