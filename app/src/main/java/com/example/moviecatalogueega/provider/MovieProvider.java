package com.example.moviecatalogueega.provider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;

import com.example.moviecatalogueega.Database.AppDatabase;
import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.helper.MappingHelper;
import com.example.moviecatalogueega.widget.ImageBannerWidget;

import static com.example.moviecatalogueega.ModelFilm.TABLE_NAME;

public class MovieProvider extends ContentProvider {
    private AppDatabase db;
    private static final int MOVIE = 1;
    private static final int MOVIE_GET_BY_ID = 2;
    public static final String AUTHORITY = "com.example.moviecatalogueega";
    private static final String SCHEME = "content";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_GET_BY_ID);
    }

    public MovieProvider() {
    }

    @Override
    public boolean onCreate() {
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "filmdb").allowMainThreadQueries().build();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                if (selection.equals("all")) {
                    cursor = db.filmDao().getAll();
                } else {
                    cursor = db.filmDao().findByTypeC(selection);
                }
                break;
            case MOVIE_GET_BY_ID:
                cursor = db.filmDao().findByIdC(Integer.valueOf(uri.getLastPathSegment()));
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        if (sUriMatcher.match(uri) == MOVIE) {
            deleted = db.filmDao().deleteBySelection(selection);
        } else {
            deleted = 0;
        }
        Log.d("TAG", "delete: ");
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        updateWidget();
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        if (sUriMatcher.match(uri) == MOVIE) {
            added = db.filmDao().insertAll(MappingHelper.contenValuesToList(values));
            Log.d("TAG", "insert:okk ");
        } else {
            added = 0;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        updateWidget();
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == MOVIE_GET_BY_ID) {
            updated = db.filmDao().update(MappingHelper.contenValuesToList(values));
        } else {
            updated = 0;
        }
        updateWidget();
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetId = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), ImageBannerWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
    }


}
