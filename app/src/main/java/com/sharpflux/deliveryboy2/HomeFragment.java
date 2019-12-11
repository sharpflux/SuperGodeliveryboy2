package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    TextView tvTotalEarnings,txt_driver_status;
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
    Switch switch1;
    List<DeliveryList> productList;
    RecyclerView recyclerView;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        // Inflate the layout for this fragment
        switch1 = view.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);

        tvTotalEarnings =view.findViewById(R.id.tvTotalEarnings);
        txt_driver_status =view.findViewById(R.id.txt_driver_status);
        categoryList = new ArrayList<>();
       // mSocketStickyIntent = new Intent(getContext(),BackgroundService.class);

       // startStickyService();
        array = new JSONArray();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceNoDelay.MY_ACTION);
        getContext().registerReceiver(myReceiver, intentFilter);
        productList = new ArrayList<DeliveryList>();

        if (mHashMap != null && mHashMap.size() > 0) {
            mHashMap.clear();
        }


        if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();

            mMediaPlayer.reset();

            mMediaPlayer.release();

            mMediaPlayer = null;
        }


        HomeFragment.AsyncTaskRunner runner = new HomeFragment.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);

        recyclerView = view.findViewById(R.id.recylcerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            Bundle bundle = new Bundle();
            String datapassed = arg1.getStringExtra("List");


            if(!datapassed.equals("[]") && datapassed!=null) {
                //pushAppToForground();
                ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    getContext().stopService(mServiceIntent);
                }
                Fragment fragment = new NewRequestFragment();
                FragmentManager fm = getFragmentManager();
                bundle.putString("List", datapassed);
                FragmentTransaction transaction = fm.beginTransaction();
                fragment.setArguments(bundle);
                transaction.replace(R.id.FragmentContain, fragment);
                transaction.commit();

            }
        }

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
            Log.e("TAG", "callFragWithDelay:  available********");
            Intent intent = new Intent(getContext(),HomeFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        KeyguardManager myKM = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {

            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(switch1.isChecked()){
            Toast.makeText(getContext(), "Now you are Online", Toast.LENGTH_LONG).show();
            txt_driver_status.setText("ON");

            ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
            Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                getContext().startService(mServiceIntent);
            }

           /* Intent startIntent = new Intent(getContext(), ServiceNoDelay.class);
            startIntent.setAction("MyService");
           getContext().startService(startIntent);*/

          /*  Intent intent = new Intent(getContext(), BackgroundService.class);
            intent.setAction("MyService");
            getContext().bindService(intent, m_serviceConnection, Context.BIND_AUTO_CREATE);*/


        }else{
            Toast.makeText(getContext(), "Now you are Offline", Toast.LENGTH_LONG).show();
            txt_driver_status.setText("OFF");
            if (mHashMap != null && mHashMap.size() > 0) {
                mHashMap.clear();
            }


            ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
            Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
            if (isMyServiceRunning(mSensorService.getClass())) {
                getContext().stopService(mServiceIntent);
            }

        }
    }
    private void setDynamicFragmentToTabLayout() {
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        deliveryBoyId = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_ORDER_HISTORY+"DeliveryBoyId="+deliveryBoyId+"&pageIndex=1&pageSize=500&Search=",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject userJson = obj.getJSONObject(i);

                                myCategoryType = new HistoryModel
                                        (       userJson.getString("ApiDeliveryBoyEarning"),
                                                userJson.getString("Fromdates"),
                                                userJson.getString("Todates")
                                        );


                               /*categoryList.add(myCategoryType);


                                myCategoryTypeAdapter = new HistoryFragmentAdapter(getContext(), categoryList);
                                mRecyclerView.setAdapter(myCategoryTypeAdapter);*/
                                tvTotalEarnings.setText("₹ "+userJson.getString("ApiDeliveryBoyEarning"));

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
        protected void onPostExecute(String result)
        {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(),
                    "Loading...",
                    "");
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }


}