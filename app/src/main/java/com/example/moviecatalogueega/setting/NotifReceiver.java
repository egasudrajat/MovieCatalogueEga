package com.example.moviecatalogueega.setting;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.moviecatalogueega.BuildConfig;
import com.example.moviecatalogueega.MainActivity;
import com.example.moviecatalogueega.ModelFilm;
import com.example.moviecatalogueega.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotifReceiver extends BroadcastReceiver {
    public final static int ID_REPEATING_REMINDER = 101;
    public final static int ID_REPEATING_RELEASE = 102;
    private static final String TYPE = "type";

    private static final int NOTIFICATION_ID_REMINDER = 1;
    private static final int NOTIFICATION_ID_RELEASE = 2;
    private static final String CHANNEL_ID = "channel_01";
    private static final String CHANNEL_NAME = "mychannel";

    ArrayList<ModelFilm> listRelease = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(TYPE, 0);

        if (type == ID_REPEATING_REMINDER) {
            String title = context.getString(R.string.daily_reminder);
            String message = context.getString(R.string.daily_reminder_message);
            showNotification(context, title, message, NOTIFICATION_ID_REMINDER);
        } else {
            LoadDataAsync load = new LoadDataAsync(context);
            load.execute();

        }
    }


    public void setRepeatingAlarm(Context context, int id_repeat, int type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotifReceiver.class);
        intent.putExtra(TYPE, type);

        Calendar calendar = Calendar.getInstance();
        if (id_repeat == ID_REPEATING_REMINDER) {
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_repeat, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, R.string.toast_repeat_alarm, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, int id_repeat) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotifReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_repeat, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, R.string.cancel_alarm, Toast.LENGTH_SHORT).show();
    }


    private void showNotification(Context context, String title, String msg, int notif_id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.InboxStyle iStyle = new NotificationCompat.InboxStyle();

        if (title.equals(context.getString(R.string.daily_reminder))) {
            iStyle.addLine(context.getString(R.string.daily_reminder_message));
            iStyle.setSummaryText(context.getString(R.string.daily_reminder));

        } else {
            iStyle.addLine(context.getString(R.string.tglrilis) + " : " + listRelease.get(0).getTanggal());
            for (int i = 0; i < listRelease.size(); i++) {
                iStyle.addLine(listRelease.get(i).getNama() + " ,..");
            }
            iStyle.setSummaryText(listRelease.size() + " " + context.getString(R.string.summary_text));
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(iStyle)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notification.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(notif_id, notification.build());
        }
    }

    private void getDataRelease(final Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String today = dateFormat.format(new Date());

        String MOVIE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;
        AndroidNetworking.get(MOVIE_URL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i <= results.length(); i++) {
                                JSONObject data = results.getJSONObject(i);
                                ModelFilm modelFilm = new ModelFilm();
                                modelFilm.setIdfilm(data.getInt("id"));
                                modelFilm.setNama(data.getString("title"));
                                modelFilm.setTanggal(data.getString("release_date"));
                                modelFilm.setDeskripsi(data.getString("popularity"));
                                listRelease.add(modelFilm);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String title = context.getString(R.string.release_reminder_title);
                        String message = context.getString(R.string.release_reminder_message);
                        showNotification(context, title, message, NOTIFICATION_ID_RELEASE);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private class LoadDataAsync extends AsyncTask<Void, Void, Void> {
        private Context context;

        LoadDataAsync(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getDataRelease(context);
            return null;
        }
    }
}
