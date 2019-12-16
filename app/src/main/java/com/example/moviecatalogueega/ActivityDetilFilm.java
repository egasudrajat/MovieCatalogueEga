package com.example.moviecatalogueega;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviecatalogueega.tablayout.ViewModelTabMovies;

public class ActivityDetilFilm extends AppCompatActivity {

    public static final String datafilm = "datafilm";
    ModelFilm modelFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_film);
        final ProgressBar progressBar = findViewById(R.id.progressDetil);
        TextView tvnamafilm = findViewById(R.id.tv_namadetil);
        TextView tvVote = findViewById(R.id.tv_vote);
        TextView tvBahasa = findViewById(R.id.tv_bahasa);
        TextView tvtglrilis = findViewById(R.id.tv_detilrilis);
        TextView tvdeskripsi = findViewById(R.id.tv_Deskripsidetil);
        ImageView imageViewdetil = findViewById(R.id.imgdetil);

        modelFilm = getIntent().getParcelableExtra(datafilm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.detail) + " " + modelFilm.getNama());

        tvnamafilm.setText(modelFilm.getNama());
        tvVote.setText(getResources().getString(R.string.voting) + " " + modelFilm.getVote());
        tvtglrilis.setText(String.format("%s %s", getResources().getString(R.string.rilis), modelFilm.getTanggal()));
        tvBahasa.setText(String.format("%s %s", getResources().getString(R.string.durasi), bahasa(modelFilm)));
        tvdeskripsi.setText(modelFilm.getDeskripsi());
        Glide.with(this)
                .load(ViewModelTabMovies.POSTER_PATH + "w500" + modelFilm.getPoster())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(ActivityDetilFilm.this, getResources().getString(R.string.koneksiGagal), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().override(550, 550))
                .into(imageViewdetil);
    }

    private String bahasa(ModelFilm modelFilm) {
        String bhs = modelFilm.getBahasa();
        String result;
        if (bhs.equals("en")) {
            result = "English";
        } else if (bhs.equals("id")) {
            result = "Indonesia";
        } else {
            result = "Other";
        }
        return result;
    }
}
