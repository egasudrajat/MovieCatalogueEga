package com.example.moviecatalogueega.helper;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogueega.R;


public class DialogImage extends DialogFragment {


    public DialogImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageView imgx = view.findViewById(R.id.imgx);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String mybundle = bundle.getString("image");
            Glide.with(this)
                    .load(mybundle)
                    .apply(new RequestOptions()).override(550, 550)
                    .into(imgx);
        }

    }
}
