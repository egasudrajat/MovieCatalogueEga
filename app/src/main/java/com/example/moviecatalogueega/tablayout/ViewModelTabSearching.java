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

public class ViewModelTabSearching extends ViewModel {


    private MutableLiveData<ArrayList<ModelFilm>> ListFilm = new MutableLiveData<>();

    LiveData<ArrayList<ModelFilm>> getListFilm() {
        return ListFilm;
    }


    void dataListFilm(final String type, String lang, String search_key) {

        String URL_SEARCH = "https://api.themoviedb.org/3/search/" + type + "?api_key=" + BuildConfig.API_KEY + "&language=" + lang + "&query=";
        AndroidNetworking.get(URL_SEARCH + search_key)
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
                                modelFilm.setDeskripsi(data.getString("overview"));
                                modelFilm.setPoster(data.getString("poster_path"));
                                modelFilm.setBahasa(data.getString("original_language"));
                                modelFilm.setVote(data.getString("vote_average"));

                                if (type.equals("movie")) {
                                    modelFilm.setNama(data.getString("title"));
                                    modelFilm.setTanggal(data.getString("release_date"));
                                    modelFilm.setType("movies");
                                } else {
                                    modelFilm.setNama(data.getString("name"));
                                    modelFilm.setTanggal(data.getString("first_air_date"));
                                    modelFilm.setType("tvshow");
                                }

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
