package com.example.moviecatalogueega.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.moviecatalogueega.ModelFilm;

import java.util.ArrayList;

public class MappingHelper {

    public static ContentValues listToCOntenValues(ModelFilm modelFilm){
        ContentValues values = new ContentValues();
        values.put("idfilm", modelFilm.getIdfilm());
        values.put("type", modelFilm.getType());
        values.put("nama", modelFilm.getNama());
        values.put("bahasa", modelFilm.getBahasa());
        values.put("deskripsi", modelFilm.getDeskripsi());
        values.put("poster", modelFilm.getPoster());
        values.put("tanggal", modelFilm.getTanggal());
        values.put("vote", modelFilm.getVote());

        return values;

    }

    public static ModelFilm contenValuesToList(ContentValues contentValues){
        ModelFilm modelFilm = new ModelFilm();

        modelFilm.setIdfilm(contentValues.getAsInteger("idfilm"));
        modelFilm.setType(contentValues.getAsString("type"));
        modelFilm.setNama(contentValues.getAsString("nama"));
        modelFilm.setBahasa(contentValues.getAsString("bahasa"));
        modelFilm.setDeskripsi(contentValues.getAsString("deskripsi"));
        modelFilm.setPoster(contentValues.getAsString("poster"));
        modelFilm.setTanggal(contentValues.getAsString("tanggal"));
        modelFilm.setVote(contentValues.getAsString("vote"));

        return modelFilm;
    }

    public static ArrayList<ModelFilm> mapCursorToArrayList(Cursor cursor) {
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
