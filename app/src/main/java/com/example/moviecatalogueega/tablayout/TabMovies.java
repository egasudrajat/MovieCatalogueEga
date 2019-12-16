package com.example.moviecatalogueega.tablayout;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.androidnetworking.AndroidNetworking;
import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.moviecatalogueega.provider.MovieProvider.CONTENT_URI;


public class TabMovies extends Fragment {
    private ProgressBar progressBar;
    private ImageView imgrefresh;
    private AdapterFilm adapterFilm;
    private RecyclerView rvMovies;
    private String language;
    private Uri uriWithID;


    public TabMovies() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        language = getString(R.string.language);

        AndroidNetworking.initialize(getActivity());
        final ViewModelTabMovies viewModelTabMovies = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelTabMovies.class);
        final ViewModelTabTvShow viewModelTaTvbMovies = new ViewModelProvider(getParentFragment(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelTabTvShow.class);
        viewModelTabMovies.dataListfilm(language);

        viewModelTabMovies.getListFilm().observe(getActivity(), new Observer<ArrayList<ModelFilm>>() {
            @Override
            public void onChanged(ArrayList<ModelFilm> modelFilms) {
                if (modelFilms != null) {
                    adapterFilm = new AdapterFilm();
                    adapterFilm.setListfilm(modelFilms);
                    rvMovies.setAdapter(adapterFilm);
                    progressBar.setVisibility(View.GONE);
                    adapterFilm.setOnitemClickCallBack(new AdapterFilm.OnitemClickCallBack() {
                        @Override
                        public void OnitemClicked(ModelFilm data) {
                        }

                        @Override
                        public void OnFavoriteitemClicked(ModelFilm data) {

                            if (db.filmDao().findById(data.getIdfilm()) != null) {
                                uriWithID = Uri.parse(CONTENT_URI + "/" + data.getIdfilm());
                                getActivity().getContentResolver().update(uriWithID, MappingHelper.listToCOntenValues(data), null, null);
                                Snackbar.make(getView(), data.getNama() + " " + getString(R.string.success_update), Snackbar.LENGTH_SHORT).show();
                            } else {
                                getActivity().getContentResolver().insert(CONTENT_URI, MappingHelper.listToCOntenValues(data));
                                Snackbar.make(getView(), data.getNama() + " " + getString(R.string.success_insert), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(getView(), getResources().getString(R.string.koneksiGagal), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getResources().getString(R.string.cobaLagi), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewModelTabMovies.dataListfilm(language);
                                    viewModelTaTvbMovies.dataListFilm(getContext(), language);
                                    imgrefresh.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);

                                }
                            }).show();
                    progressBar.setVisibility(View.GONE);
                    imgrefresh.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        imgrefresh = view.findViewById(R.id.ic_refresh);
        rvMovies = view.findViewById(R.id.rvMovies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));


    }


}
