package com.example.moviefavorite.helper;

import android.database.Cursor;

import com.example.moviefavorite.ModelFilm;

import java.util.ArrayList;

public class MappingHelper {


    public static ArrayList<ModelFilm> mapCursorToArrayList2(Cursor cursor) {
        ArrayList<ModelFilm> films = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("idfilm"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"));
            String bahasa = cursor.getString(cursor.getColumnIndexOrThrow("bahasa"));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow("poster"));
            String tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));
            String vote = cursor.getString(cursor.getColumnIndexOrThrow("vote"));

            ModelFilm data = new ModelFilm();
            data.setIdfilm(id);
            data.setType(type);
            data.setNama(nama);
            data.setBahasa(bahasa);
            data.setDeskripsi(deskripsi);
            data.setPoster(poster);
            data.setTanggal(tanggal);
            data.setVote(vote);

            films.add(data);
        }

        return films;
    }
}
