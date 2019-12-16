package com.example.moviefavorite.TabLayout;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviefavorite.AdapterFavorite;
import com.example.moviefavorite.ModelFilm;
import com.example.moviefavorite.R;

import java.util.List;

import static com.example.moviefavorite.TabLayout.TabFavoriteMoviesViewModel.CONTENT_URI;

public class TabFavoriteMovies extends Fragment {
    private ProgressBar progressBar;
    private ImageView imgrefresh;
    private AdapterFavorite adapterFavorite;
    private RecyclerView rvMovies;
    private TextView tv_noData;

    private static TabFavoriteMoviesViewModel viewModelTabMovies;

    public static TabFavoriteMovies newInstance() {
        return new TabFavoriteMovies();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelTabMovies = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(TabFavoriteMoviesViewModel.class);
        viewModelTabMovies.setListFavorite(getActivity());

        viewModelTabMovies.getListFavorite().observe(getActivity(), new Observer<List<ModelFilm>>() {
            @Override
            public void onChanged(List<ModelFilm> modelFilms) {
                if (modelFilms != null) {
                    adapterFavorite = new AdapterFavorite();
                    adapterFavorite.setListfilm(modelFilms);
                    rvMovies.setAdapter(adapterFavorite);
                    progressBar.setVisibility(View.GONE);

                    if (modelFilms.size() < 1) {
                        tv_noData.setVisibility(View.VISIBLE);
                    } else {
                        tv_noData.setVisibility(View.GONE);
                    }

                    adapterFavorite.setOnitemClickCallBack(new AdapterFavorite.OnitemClickCallBack() {
                        @Override
                        public void OnitemClicked(ModelFilm data) {
                            viewModelTabMovies.setListFavorite(getActivity());

                        }

                        @Override
                        public void OnBtnDelete(ModelFilm entity) {
                            getActivity().getContentResolver().delete(CONTENT_URI, String.valueOf(entity.getIdfilm()), null);
                        }
                    });
                }
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_favorite_movies_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        imgrefresh = view.findViewById(R.id.ic_refresh);
        rvMovies = view.findViewById(R.id.rvMovies);
        tv_noData = view.findViewById(R.id.tv_no_data);

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);


    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            viewModelTabMovies.setListFavorite(context);

        }
    }


}
