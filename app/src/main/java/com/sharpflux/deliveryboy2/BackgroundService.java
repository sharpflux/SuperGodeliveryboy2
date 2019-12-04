package com.sharpflux.deliveryboy2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    static final int NOTIFICATION_ID = 543;
    public int counter=0;
    public static boolean isServiceRunning = false;

    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    public int deliveryId;

    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String,MediaPlayer> mHashMap = new HashMap();


    @Override
    public void onCreate() {
        super.onCreate();
        startTimer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground();
        else
            startForeground(1, new Notification());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals("MyService")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForeground();
            startTimer();
            deliveryId = 9999;
        }
        else stopMyService();
        return START_STICKY;
    }

    // In case the service is deleted or crashes some how
    @Override
    public void onDestroy() {
        isServiceRunning = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    private void startForeground() {

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID);

        Notification notification;



        notification = mBuilder.setTicker(getString(R.string.app_name)).setWhen(0)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("SuperGo is running background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        //All notifications should go through NotificationChannel on Android 26 & above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }
        notificationManager.notify(NOTIFICATION_ID, notification);

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive (Context context, Intent intent)
        {
            if (intent.getAction().equals("MyService"))
            {
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(new Intent("MyService"));
            }
        }
    };

    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  "+ (counter++));
                SendLocation(String.valueOf(counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void SendLocation(final String latLong) {
        String k = latLong;

        if (deliveryId != 0) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOCATIONUPDATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                           /* mMediaPlayer =  MediaPlayer.create(getApplication(), R.raw.ring_tone_final);
                            mHashMap.put("68545",mMediaPlayer);
                            //    Log.e(TAG, "notifyDriver: *** mMediaPlayer "+mMediaPlayer );

                            Log.e("TAG", "notifyDriver: from Asset"+mMediaPlayer );
                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();*/
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("DeliveryId", String.valueOf(deliveryId));
                    params.put("LatLong", latLong.trim());
                    return params;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
        else
        {
            Log.d("Message", "Delivery Id is not found");
        }
    }

    private void stopRingtone(String DeliveryId) {

        if(mHashMap != null && mHashMap.size() > 0 ){
            Log.e("TAG", "stopRingtone: mHashMap.size() "+mHashMap.size() );
            if(mHashMap.get(DeliveryId) != null && mHashMap.get(DeliveryId).isPlaying() ){
                mHashMap.get(DeliveryId).stop();
                mHashMap.remove(DeliveryId);
            }
        }


    }

}