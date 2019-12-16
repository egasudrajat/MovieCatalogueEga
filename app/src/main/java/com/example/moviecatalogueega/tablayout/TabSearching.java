package com.example.moviecatalogueega.tablayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.MainActivity;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.MappingHelper;
import com.example.moviecatalogueega.helper.SearchViewCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.moviecatalogueega.provider.MovieProvider.CONTENT_URI;

public class TabSearching extends Fragment {
    private AppDatabase db;
    private ViewModelTabSearching viewModelTabSearching;
    private AdapterFilm adapterFilm = new AdapterFilm();
    private RecyclerView rvMovies;
    private String language;
    private ProgressBar progressBar;
    private String type = "movie";
    private Uri uriWithID;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        language = getString(R.string.language);
        viewModelTabSearching = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelTabSearching.class);

        viewModelTabSearching.dataListFilm("movie", language, "Rambo: first blood");
        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        viewModelTabSearching.getListFilm().observe(getActivity(), new Observer<ArrayList<ModelFilm>>() {
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
                                Log.d("TAG", "OnFavoriteitemClicked: " + data.getIdfilm());
                                getActivity().getContentResolver().update(uriWithID, MappingHelper.listToCOntenValues(data), null, null);
                                Snackbar.make(getView(), data.getNama() + " " + getString(R.string.success_update), Snackbar.LENGTH_SHORT).show();
                            } else {
                                getActivity().getContentResolver().insert(CONTENT_URI, MappingHelper.listToCOntenValues(data));
                                Snackbar.make(getView(), data.getNama() + " " + getString(R.string.success_insert), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_searching, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        rvMovies = view.findViewById(R.id.rvSearching);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        RadioGroup radioGroup = view.findViewById(R.id.rG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdMovie:
                        type = "movie";
                        break;
                    case R.id.rdTv:
                        type = "tv";
                        break;

                }
            }
        });

        MainActivity mainActivity = new MainActivity();
        mainActivity.setSearchViewCallback(new SearchViewCallback() {
            @Override
            public void OnSubmit(String query) {
                adapterFilm.removeList();
                progressBar.setVisibility(View.VISIBLE);
                viewModelTabSearching.dataListFilm(type, language, query);

            }

        });


    }


}
