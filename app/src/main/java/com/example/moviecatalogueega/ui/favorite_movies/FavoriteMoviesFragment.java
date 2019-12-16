package com.example.moviecatalogueega.ui.favorite_movies;

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

import com.example.moviecatalogueega.ui.AdapterFavorite;
import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviecatalogueega.provider.MovieProvider.CONTENT_URI;

public class FavoriteMoviesFragment extends Fragment {
    private ProgressBar progressBar;
    private ImageView imgrefresh;
    private AdapterFavorite adapterFavorite = new AdapterFavorite();
    private RecyclerView rvMovies;
    private String language = "id";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        imgrefresh = view.findViewById(R.id.ic_refresh);
        language = getString(R.string.language);
        rvMovies = view.findViewById(R.id.rvMovies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        final AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "filmdb").allowMainThreadQueries().build();

        final ViewModel_FavoriteMoviesfragment viewModel_favoriteMoviesfragment = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModel_FavoriteMoviesfragment.class);
        viewModel_favoriteMoviesfragment.setListFavorite(getContext());

        viewModel_favoriteMoviesfragment.getListFilm().observe(getActivity(), new Observer<List<ModelFilm>>() {
            @Override
            public void onChanged(List<ModelFilm> modelFilms) {
                if (modelFilms != null) {
                    ArrayList<ModelFilm> items = new ArrayList<>();
                    items.addAll(modelFilms);
                    adapterFavorite.setListfilm(items, R.layout.list_fragment_favorite);
                    rvMovies.setAdapter(adapterFavorite);
                    progressBar.setVisibility(View.GONE);
                }
                adapterFavorite.setOnitemClickCallBack(new AdapterFavorite.OnitemClickCallBack() {

                    @Override
                    public void OnitemClicked(ModelFilm data) {

                    }

                    @Override
                    public void OnBtnDelete(ModelFilm entity) {
                        getActivity().getContentResolver().delete(CONTENT_URI, String.valueOf(entity.getIdfilm()), null);
                        adapterFavorite.notifyDataSetChanged();
                    }
                });
            }
        });


    }
}