package com.example.moviecatalogueega.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.MappingHelper;

import java.util.List;

import static com.example.moviecatalogueega.provider.MovieProvider.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;

    private List<ModelFilm> dataFavorite;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }


    private void getDatabase() {
        cursor = context.getContentResolver().query(CONTENT_URI, null, "all", null, null);
        dataFavorite = MappingHelper.mapCursorToArrayList(cursor);
    }

    @Override
    public void onCreate() {
        getDatabase();

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        getDatabase();
        Log.d("TAG", "onDataSetChanged: ");
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dataFavorite.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        try {

            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w342/" + dataFavorite.get(position).getPoster())
                    .submit(350, 180)
                    .get();

            rv.setImageViewBitmap(R.id.img_widget, bitmap);
            rv.setTextViewText(R.id.title_widget, dataFavorite.get(position).getNama());
            rv.setTextViewText(R.id.popularity, dataFavorite.get(position).getTanggal());
            rv.setTextViewText(R.id.deskripsi_widget, dataFavorite.get(position).getDeskripsi());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putString(ImageBannerWidget.EXTRA_ITEM, dataFavorite.get(position).getNama());
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_widget, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
