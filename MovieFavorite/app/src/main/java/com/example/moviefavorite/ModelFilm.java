package com.example.moviefavorite;

import android.os.Parcel;
import android.os.Parcelable;


public class ModelFilm implements Parcelable {


    private int idfilm;
    private String type;
    private String nama;
    private String bahasa;
    private String deskripsi;
    private String poster;
    private String tanggal;
    private String vote;


    public int getIdfilm() {
        return idfilm;
    }

    public void setIdfilm(int idfilm) {
        this.idfilm = idfilm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getBahasa() {
        return bahasa;
    }

    public void setBahasa(String durasi) {
        this.bahasa = durasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public ModelFilm() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idfilm);
        dest.writeString(this.type);
        dest.writeString(this.nama);
        dest.writeString(this.bahasa);
        dest.writeString(this.deskripsi);
        dest.writeString(this.poster);
        dest.writeString(this.tanggal);
        dest.writeString(this.vote);
    }

    protected ModelFilm(Parcel in) {
        this.idfilm = in.readInt();
        this.type = in.readString();
        this.nama = in.readString();
        this.bahasa = in.readString();
        this.deskripsi = in.readString();
        this.poster = in.readString();
        this.tanggal = in.readString();
        this.vote = in.readString();
    }

    public static final Creator<ModelFilm> CREATOR = new Creator<ModelFilm>() {
        @Override
        public ModelFilm createFromParcel(Parcel source) {
            return new ModelFilm(source);
        }

        @Override
        public ModelFilm[] newArray(int size) {
            return new ModelFilm[size];
        }
    };
}
