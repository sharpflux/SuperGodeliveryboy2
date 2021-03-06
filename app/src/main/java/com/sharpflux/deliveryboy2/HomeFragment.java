package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment{

    TextView tvTotalEarnings, txt_driver_status;
    int deliveryBoyId = 0;

    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    HistoryFragmentAdapter myCategoryTypeAdapter;
    ArrayList<HistoryModel> categoryList;
    HistoryModel myCategoryType;
    Intent startIntent;
    Intent serviceIntent1;
    BackgroundService m_service;
    boolean isBound = false;
    private Intent mSocketStickyIntent;
    MyReceiver myReceiver;

    JSONArray array;
    Switch switch2;
    List<DeliveryList> productList;
    RecyclerView recyclerView;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();
LinearLayout ClickableCheck;
    private boolean isReceiverRegistered = false;
    String status;
    //SharedPreferences preferences;
    DriverStatus myDatabase;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        // Inflate the layout for this fragment
        switch2 = view.findViewById(R.id.switchDriverDuty2);


        tvTotalEarnings = view.findViewById(R.id.tvTotalEarnings);
        txt_driver_status = view.findViewById(R.id.txt_driver_status);
        ClickableCheck=view.findViewById(R.id.ClickableCheck);
        categoryList = new ArrayList<>();
        // mSocketStickyIntent = new Intent(getContext(),BackgroundService.class);

        // startStickyService();
        array = new JSONArray();
        myReceiver = new MyReceiver();
     /*   IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceNoDelay.MY_ACTION);*/
        isReceiverRegistered = true;

        GetOngoingDelivery();

        myDatabase = new DriverStatus(getContext());
        String res = myDatabase.GetLastStatus();
        txt_driver_status.setText(res);

        ClickableCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();
            }
        });

        if(res.contains("ON")){
            switch2.setChecked(true);
            ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
            Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                getContext().startService(mServiceIntent);
            }
        }
        else {
            switch2.setChecked(false);
            if (getContext() != null) {
                ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    getContext().stopService(mServiceIntent);
                }
            }
        }

        if (getContext() != null) {
            LocationMonitoringService mSensorService = new LocationMonitoringService();
            Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
            if (isMyServiceRunning(mSensorService.getClass())) {
                getContext().stopService(mServiceIntent);
            }
        }


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("custom-event-name"));

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled


                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Now you are Online", Toast.LENGTH_LONG).show();
                       txt_driver_status.setText("ON");
                        myDatabase.StatusInsert("ON");
                        switch2.setChecked(true);
                /*if (myDatabase.GetLastStatus().equals("0"))
                        myDatabase.StatusInsert("ON");
                 else if(myDatabase.GetLastStatus().equals("OFF"))
                    myDatabase.UpdateStatus("OFF");*/

                        ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                        Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            getContext().startService(mServiceIntent);
                        }
                    }



                } else {
                    // The toggle is disabled



                    Toast.makeText(getContext(), "Now you are Offline", Toast.LENGTH_LONG).show();
                    txt_driver_status.setText("OFF");
                    switch2.setChecked(false);
                    myDatabase.StatusInsert("OFF");
           /* if (myDatabase.GetLastStatus().equals("0"))
                myDatabase.StatusInsert("OFF");
            else if(myDatabase.GetLastStatus().equals("ON"))
                myDatabase.UpdateStatus("OFF");
*/

                    if (getContext() != null) {
                        ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                        Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                        if (isMyServiceRunning(mSensorService.getClass())) {
                            getContext().stopService(mServiceIntent);
                        }
                    }
                }

            }
        });



     /*  txt_driver_status.setText(res);
       if(res.equals("ON")){
           switch1.setEnabled(true);
       }
        switch1.setEnabled(false);
*/



       /* if (res.contains("0")) {
            myDatabase.UpdateStatus("OFF");
        }

        if (res.equals("ON")) {
            txt_driver_status.setText("ON");
            switch1.setChecked(true);
            Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
        } else {
            txt_driver_status.setText("OFF");
            switch1.setChecked(false);
            Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
        }*/



   /*     LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver,
                new IntentFilter(ServiceNoDelay.MY_ACTION));*/



      /*  preferences = this.getActivity().getSharedPreferences("DriverDutyStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(status, txt_driver_status.toString());
        editor.commit();*/


        productList = new ArrayList<DeliveryList>();

        if (mHashMap != null && mHashMap.size() > 0) {
            mHashMap.clear();
        }


        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();

            mMediaPlayer.reset();

            mMediaPlayer.release();

            mMediaPlayer = null;
        }

        /*Cursor myCursor = myDatabase.getAllData();
        while (myCursor.moveToNext()) {
            String DriverState = myCursor.getString(0);
            txt_driver_status.setText(DriverState);

        }
*/

        AsyncTaskRunner runner = new AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);

        recyclerView = view.findViewById(R.id.recylcerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        int counter = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            counter++;
            Bundle bundle = new Bundle();
            String datapassed = intent.getStringExtra("List");
            String Visible =getVisibleFragment();
            if(!datapassed.equals("[]")) {
                String visibleFrag = Visible();
                Fragment fragment = new NewRequestFragment();
                FragmentManager fm = getFragmentManager();
                if (fm != null) {
                    bundle.putString("List", datapassed);
                    bundle.putString("counter", String.valueOf(counter));
                    FragmentTransaction transaction = fm.beginTransaction();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.frame, fragment, "NewRequest");
                    //transaction.commitAllowingStateLoss();
                    transaction.commit();
                    return;
                }
            }
           else {
                Fragment fragment = new HomeFragment();
                FragmentManager fm = getFragmentManager();
                if (fm != null) {
                  FragmentTransaction transaction = fm.beginTransaction();
                  transaction.detach(fragment);
                   transaction.replace(R.id.frame, fragment);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

        }
    };


    private class MyReceiver extends BroadcastReceiver {

        int counter = 0;

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            Bundle bundle = new Bundle();
            String datapassed = arg1.getStringExtra("List");

            counter++;

            //pushAppToForground();
               /* ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    getContext().stopService(mServiceIntent);
                }*/
            if (arg1.getAction().equalsIgnoreCase(ServiceNoDelay.MY_ACTION)) {
                String visibleFrag = getVisibleFragment();
                Log.e("Visible", "socketresponse: " + visibleFrag);
                if (!visibleFrag.equals("NewRequestFragment")) {
                    Fragment fragment = new NewRequestFragment();
                    FragmentManager fm = getFragmentManager();
                    if (fm != null) {
                        Log.e("Visible", "PUSHING : " + visibleFrag);

                        bundle.putString("List", datapassed);
                        bundle.putString("counter", String.valueOf(counter));
                        FragmentTransaction transaction = fm.beginTransaction();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.frame, fragment);
                        transaction.commitAllowingStateLoss();
                        return;
                    }

                }


            }


        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    public String Visible() {
        if (getContext() != null) {

            Fragment page = ((AppCompatActivity) getContext()).getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_pages + ":" + 0);
            return page.getTag();
        }
        return " ";
    }


    public String getVisibleFragment() {

        if (getContext() != null) {
            FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible())
                        return fragment.getTag();
                }
            }
        }
        return " ";
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;
    }

    private void pushAppToForground() {
        if (!MyApplication.isActivityVisible()) {

            if (getContext() != null) {
                KeyguardManager myKM = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
                //if (myKM.inKeyguardRestrictedInputMode()) {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                // }
                Log.e("TAG", "callFragWithDelay:  available********");
                Intent intent = new Intent(getContext(), NavActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

    }


    /*public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (switch1.isChecked()) {

            if (getContext() != null) {
                Toast.makeText(getContext(), "Now you are Online", Toast.LENGTH_LONG).show();
                txt_driver_status.setText("ON");
                myDatabase.StatusInsert("ON");
                switch1.setChecked(true);
                *//*if (myDatabase.GetLastStatus().equals("0"))
                        myDatabase.StatusInsert("ON");
                 else if(myDatabase.GetLastStatus().equals("OFF"))
                    myDatabase.UpdateStatus("OFF");*//*

                ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                if (!isMyServiceRunning(mSensorService.getClass())) {
                    getContext().startService(mServiceIntent);
                }
            }
           *//* Intent startIntent = new Intent(getContext(), ServiceNoDelay.class);
            startIntent.setAction("MyService");
           getContext().startService(startIntent);*//*

          *//*  Intent intent = new Intent(getContext(), BackgroundService.class);
            intent.setAction("MyService");
            getContext().bindService(intent, m_serviceConnection, Context.BIND_AUTO_CREATE);*//*


        } else {
            Toast.makeText(getContext(), "Now you are Offline", Toast.LENGTH_LONG).show();
            txt_driver_status.setText("OFF");
            switch1.setChecked(false);
            myDatabase.StatusInsert("OFF");
           *//* if (myDatabase.GetLastStatus().equals("0"))
                myDatabase.StatusInsert("OFF");
            else if(myDatabase.GetLastStatus().equals("ON"))
                myDatabase.UpdateStatus("OFF");
*//*

            if (getContext() != null) {
                ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    getContext().stopService(mServiceIntent);
                }
            }
        }
    }*/

    private void setDynamicFragmentToTabLayout() {
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        deliveryBoyId = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_ORDER_HISTORY + "DeliveryBoyId=" + deliveryBoyId + "&pageIndex=1&pageSize=500&Search=",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject userJson = obj.getJSONObject(i);

                                myCategoryType = new HistoryModel
                                        (userJson.getString("ApiDeliveryBoyEarning"),
                                                userJson.getString("Fromdates"),
                                                userJson.getString("Todates")
                                        );


                               /*categoryList.add(myCategoryType);


                                myCategoryTypeAdapter = new HistoryFragmentAdapter(getContext(), categoryList);
                                mRecyclerView.setAdapter(myCategoryTypeAdapter);*/
                                tvTotalEarnings.setText("₹ " + userJson.getString("ApiDeliveryBoyEarning"));


                            }

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
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {

               /* setDynamicFragmentToTabLayout();
                Thread.sleep(100);

                resp = "Slept for " + params[0] + " seconds";*/

                int time = Integer.parseInt(params[0]) * 1000;
                setDynamicFragmentToTabLayout();
                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            if ((progressDialog != null) && progressDialog.isShowing()) { progressDialog.dismiss();}
        }

        @Override
        protected void onPreExecute() {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(getContext(),
                        "Loading...",
                        "");
            }
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");


        if (isReceiverRegistered) {
            try {
                IntentFilter filter = new IntentFilter();
                getActivity().registerReceiver(myReceiver, filter);
                isReceiverRegistered = true;// set it back to false.
            } catch (Exception e) {
                // already unregistered
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        if (isReceiverRegistered) {
            try {
                getContext().unregisterReceiver(myReceiver);
                isReceiverRegistered = false;// set it back to false.
            } catch (Exception e) {
                // already unregistered
            }
        }
        super.onPause();
    }

    private void GetOngoingDelivery() {
        int deliveryBoyIds=deliveryBoyId;
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        deliveryBoyIds = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GETONGOINGDELIVERY + deliveryBoyId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject obj = array.getJSONObject(i);

                                Intent intent = new Intent(getContext(), LocationMonitoringService.class);
                                intent.putExtra("DeliveryId", obj.getInt("DeliveryId"));
                                getContext().startService(intent);

                                Intent callin = new Intent(getContext(), MapsActivity.class);
                                callin.putExtra("Duration", obj.getString("Duration"));
                                callin.putExtra("Distance", obj.getString("Distance"));
                                callin.putExtra("pickup_location", obj.getString("pickupAddress"));
                                callin.putExtra("deliveryAddress", obj.getString("deliveryAddress"));
                                callin.putExtra("TotalCharges", obj.getString("TotalCharges"));
                                callin.putExtra("Date", obj.getString("pickupDate"));
                                callin.putExtra("Time", obj.getString("pickuptime"));
                                callin.putExtra("Mobile", obj.getString("mobile"));
                                callin.putExtra("CustomerFullName", obj.getString("CustomerFullName"));
                                callin.putExtra("CustomerId",obj.getString("CustomerId"));
                                callin.putExtra("DeliveryId", obj.getInt("DeliveryId"));
                                callin.putExtra("fromLat", obj.getString("fromLat"));
                                callin.putExtra("fromLong", obj.getString("fromLang"));
                                callin.putExtra("ToLat", obj.getString("ToLat"));
                                callin.putExtra("ToLong", obj.getString("ToLong"));
                                callin.putExtra("DeliveryStatus", obj.getString("DeliveryStatus"));
                                startActivity(callin);
                                return;
                            }

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
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }
}