package com.example.moviecatalogueega.tablayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogueega.ActivityDetilFilm;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.DialogImage;

import java.util.ArrayList;

public class AdapterFilm extends RecyclerView.Adapter<AdapterFilm.MyViewHolder> {

    private OnitemClickCallBack onitemClickCallBack;
    private Context mcontext;
    private ArrayList<ModelFilm> Listfilm;

    public void setListfilm(ArrayList<ModelFilm> listfilm) {
        Listfilm = listfilm;
    }

    public void removeList() {
           Listfilm.clear();
    }

    public void setOnitemClickCallBack(OnitemClickCallBack onitemClickCallBack) {
        this.onitemClickCallBack = onitemClickCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_film_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        mcontext = holder.itemView.getContext();
        final ModelFilm modelFilm = Listfilm.get(position);

        Glide.with(mcontext)
                .load(ViewModelTabMovies.POSTER_PATH + "w185" + modelFilm.getPoster())
                .apply(new RequestOptions().override(170, 250))
                .into(holder.imageView);
        holder.tvnamafilm.setText(modelFilm.getNama());
        holder.tvtglrilis.setText(modelFilm.getTanggal());
        holder.tvdeskripsi.setText(modelFilm.getDeskripsi());
        holder.btndetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetilFilm.class);
                intent.putExtra(ActivityDetilFilm.datafilm, modelFilm);
                v.getContext().startActivity(intent);
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogImage dialogImage = new DialogImage();

                Bundle bundle = new Bundle();
                bundle.putString("image", ViewModelTabMovies.POSTER_PATH + "w500" + modelFilm.getPoster());
                dialogImage.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) mcontext).getSupportFragmentManager();
                dialogImage.show(fragmentManager, DialogImage.class.getSimpleName());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickCallBack.OnitemClicked(modelFilm);
            }
        });


        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickCallBack.OnFavoriteitemClicked(modelFilm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Listfilm.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvnamafilm, tvdeskripsi, tvtglrilis;
        Button btndetil, btnFavorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_listfilm);
            tvnamafilm = itemView.findViewById(R.id.tv_namafilm);
            tvdeskripsi = itemView.findViewById(R.id.tv_deskripsifilm);
            tvtglrilis = itemView.findViewById(R.id.tv_tglrilis);
            btndetil = itemView.findViewById(R.id.btn_detil);
            btnFavorite = itemView.findViewById(R.id.btn_add_favorite);

        }
    }


    public interface OnitemClickCallBack {
        void OnitemClicked(ModelFilm data);

        void OnFavoriteitemClicked(ModelFilm data);
    }
}
