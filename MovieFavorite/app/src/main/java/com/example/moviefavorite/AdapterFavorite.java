package com.example.moviefavorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.MyViewHolder> {
    public static String POSTER_PATH = "https://image.tmdb.org/t/p/";

    private OnitemClickCallBack onitemClickCallBack;
    private Context mcontext;
    private List<ModelFilm> Listfilm;

    public void setListfilm(List<ModelFilm> listfilm) {
        this.Listfilm = listfilm;
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        mcontext = holder.itemView.getContext();
        final ModelFilm modelFilm = Listfilm.get(position);

        Glide.with(mcontext)
                .load(POSTER_PATH + "w185" + modelFilm.getPoster())
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
                bundle.putString("image", POSTER_PATH + "w500" + modelFilm.getPoster());
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


        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickCallBack.OnBtnDelete(modelFilm);
                Listfilm.remove(position);
                notifyDataSetChanged();
                Toast.makeText(mcontext, R.string.success_delete, Toast.LENGTH_LONG).show();


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
        Button btndetil;
        ImageButton btnHapus;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_listfilm);
            tvnamafilm = itemView.findViewById(R.id.tv_namafilm);
            tvdeskripsi = itemView.findViewById(R.id.tv_deskripsifilm);
            tvtglrilis = itemView.findViewById(R.id.tv_tglrilis);
            btndetil = itemView.findViewById(R.id.btn_detil);
            btnHapus = itemView.findViewById(R.id.btn_delete);

        }


    }

    public interface OnitemClickCallBack {
        void OnitemClicked(ModelFilm data);

        void OnBtnDelete(ModelFilm entity);
    }
}
