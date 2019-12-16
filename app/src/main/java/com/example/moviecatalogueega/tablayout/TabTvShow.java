package com.example.moviecatalogueega.tablayout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.moviecatalogueega.ActivityDetilFilm;
import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.moviecatalogueega.provider.MovieProvider.CONTENT_URI;

public class TabTvShow extends Fragment {
    private RecyclerView rvTvShow;
    private AdapterFilm adapterTvShow;
    private ProgressBar progressBar;
    private ImageView imgrefresh;
    private Uri uriWithID;

    public TabTvShow() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String language = getString(R.string.language);

        final AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        final ViewModelTabTvShow viewModelTabTvShow = new ViewModelProvider(getParentFragment(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelTabTvShow.class);
        viewModelTabTvShow.dataListFilm(getContext(), language);
        viewModelTabTvShow.getListFilm().observe(getParentFragment(), new Observer<ArrayList<ModelFilm>>() {
            @Override
            public void onChanged(ArrayList<ModelFilm> modelFilms) {
                if (modelFilms != null) {
                    progressBar.setVisibility(View.GONE);
                    adapterTvShow = new AdapterFilm();
                    adapterTvShow.setListfilm(modelFilms);
                    rvTvShow.setAdapter(adapterTvShow);
                    progressBar.setVisibility(View.GONE);
                    adapterTvShow.setOnitemClickCallBack(new AdapterFilm.OnitemClickCallBack() {
                        @Override
                        public void OnitemClicked(ModelFilm data) {
                            Intent intent = new Intent(getContext(), ActivityDetilFilm.class);
                            intent.putExtra(ActivityDetilFilm.datafilm, data);
                            getContext().startActivity(intent);

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
                    imgrefresh.setVisibility(View.GONE);
                } else {

                    progressBar.setVisibility(View.GONE);
                    imgrefresh.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBartv);
        imgrefresh = view.findViewById(R.id.ic_refreshtv);
        rvTvShow = view.findViewById(R.id.rvTvShow);
        rvTvShow.setHasFixedSize(true);
        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
