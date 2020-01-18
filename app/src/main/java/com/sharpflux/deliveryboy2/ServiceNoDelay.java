package com.sharpflux.deliveryboy2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceNoDelay extends Service {
    public int counter = 0;
    Context context;
    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    static final int NOTIFICATION_ID = 543;
    public int deliveryId;
    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String,MediaPlayer> mHashMap = new HashMap();
    final static String MY_ACTION = "MY_ACTION";

    public ServiceNoDelay(Context applicationContext) {
        super();
        context = applicationContext;
        Log.i("HERE", "here service created!");
    }

    public ServiceNoDelay() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("ac.in.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 3000, 3000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                loadProducts(String.valueOf(counter++));
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void loadProducts(String Counter) {
        User user=   SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String  customerId = String.valueOf(user.getId());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELIVERIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //  mProgressDialog.dismiss();
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Intent intent = new Intent("custom-event-name");
                            intent.putExtra("List", array.toString());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                           Toast.makeText(getApplicationContext(), "*", Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerId", customerId);
                params.put("Password", "password");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}



/*
public class ServiceNoDelay extends Service {

    public int counter = 0;
    Context context;

    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    static final int NOTIFICATION_ID = 543;
    public int deliveryId;



    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String,MediaPlayer> mHashMap = new HashMap();

    final static String MY_ACTION = "MY_ACTION";



    public ServiceNoDelay(Context applicationContext) {
        super();
        context = applicationContext;

    }

    public ServiceNoDelay() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // startTimer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();

        }

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("com.sharpflux.delivery");
        sendBroadcast(broadcastIntent);
        stoptimertask();
        cancelNotification();
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

    public void cancelNotification() {

        String ns = NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  "+ (counter++));
                loadProducts();
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }



    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELIVERIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //  mProgressDialog.dismiss();
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            Intent intent = new Intent();
                            intent.setAction(MY_ACTION);
                            intent.putExtra("List",array.toString());
                            sendBroadcast(intent);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerId", "123");
                params.put("Password", "password");
                return params;
            }
        };
        ;

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);


    }


}*/
