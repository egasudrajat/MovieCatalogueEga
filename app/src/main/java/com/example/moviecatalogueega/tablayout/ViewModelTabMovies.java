package com.example.moviecatalogueega.tablayout;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.moviecatalogueega.BuildConfig;
import com.example.moviecatalogueega.ModelFilm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewModelTabMovies extends ViewModel {

    public static String POSTER_PATH = "https://image.tmdb.org/t/p/";

    private MutableLiveData<ArrayList<ModelFilm>> ListFilm = new MutableLiveData<>();

    LiveData<ArrayList<ModelFilm>> getListFilm() {
        return ListFilm;
    }


    void dataListfilm(String lang) {

        String MOVIE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "&language=";
        AndroidNetworking.get(MOVIE_URL + lang)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        final ArrayList<ModelFilm> List = new ArrayList<>();

                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i <= results.length(); i++) {
                                JSONObject data = results.getJSONObject(i);
                                ModelFilm modelFilm = new ModelFilm();
                                modelFilm.setIdfilm(data.getInt("id"));
                                modelFilm.setNama(data.getString("title"));
                                modelFilm.setTanggal(data.getString("release_date"));
                                modelFilm.setDeskripsi(data.getString("overview"));
                                modelFilm.setPoster(data.getString("poster_path"));
                                modelFilm.setBahasa(data.getString("original_language"));
                                modelFilm.setVote(data.getString("vote_average"));
                                modelFilm.setType("movies");
                                List.add(modelFilm);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ListFilm.postValue(List);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ListFilm.postValue(null);

                    }
                });
    }
}
