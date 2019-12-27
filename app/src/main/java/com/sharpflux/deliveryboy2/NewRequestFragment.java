package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class NewRequestFragment extends Fragment {
    List<DeliveryList> productList;
    RecyclerView recyclerView;
    Bundle bundle;
    JSONArray array;
    private static int INTERVAL = 3000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();
    DeliveryMainAdapter adapter;

    int counter=0;

    public NewRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /**/

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_request, container, false);
        productList = new ArrayList<DeliveryList>();
        array = new JSONArray();
        Bundle extras = getArguments();
        recyclerView = view.findViewById(R.id.rec_newRequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        counter++;
        if (extras != null) {
            try {
                array = new JSONArray(extras.getString("List").toString());
                String datapassed = extras.getString("List");
               /* if(extras.getString("counter").equals("1")) {
                    mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.ring_tone_final);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setLooping(true);

                }*/
                adapter = new DeliveryMainAdapter(getContext(), productList, mMediaPlayer, mHashMap,NewRequestFragment.this);
                recyclerView.setAdapter(adapter);


                if(datapassed!="[]" && datapassed!=null)
                {
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);

                        DeliveryList list2 = new DeliveryList(product.getString("mobile"));
                        DeliveryList list = new DeliveryList(
                                product.getInt("DeliveryId"),
                                product.getInt("CustomerId"),
                                product.getString("vehicleType"),
                                product.getString("pickupAddress"),
                                product.getString("deliveryAddress"),
                                product.getString("fromLat"),
                                product.getString("fromLang"),
                                product.getString("product"),
                                product.getString("pickupDate"),
                                product.getString("pickuptime"),
                                product.getString("cpName"),
                                product.getString("mobile"),
                                product.getString("alternatemobile"),
                                product.getString("paymenttype"),
                                product.getString("ToLat"),
                                product.getString("ToLong"),
                                product.getString("Distance"),
                                product.getString("Duration"),
                                product.getString("TotalCharges"),
                                product.getString("CustomerFullName")
                        );
                        //adding the product to product list

                        productList.add(list);
                        adapter.notifyDataSetChanged();



                        notifyDriver(product.getString("DeliveryId"));
                       /* if(extras.getString("counter").equals("1")) {
                                  notifyDriver(product.getString("DeliveryId"));
                        }*/

                    }


                }
                else {
                   // mMediaPlayer.stop();
                    stopAllRingtone();
                    Fragment fragment = new HomeFragment();
                    FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();;
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.FragmentContain2, fragment);
                    transaction.commit();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }



    private void notifyDriver(final String id) {

        try {

            mMediaPlayer =  MediaPlayer.create(getActivity(), R.raw.ring_tone_final);
            mHashMap.put(id,mMediaPlayer);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            long[] mVibratePattern = new long[]{0, 400, 200, 400};
            vib.vibrate(mVibratePattern, -1);



        } catch(Exception e) {
            e.printStackTrace();
        }


        final Timer t = new Timer();

        t.schedule(new TimerTask() {
            public void run() {

                stopRingtone("In Run method",id);
                t.cancel();
            }
        }, INTERVAL);

    }

    private void stopRingtone(String msg,String crn) {

        if(mHashMap != null && mHashMap.size() > 0 ){

            if(mHashMap.get(crn) != null && mHashMap.get(crn).isPlaying() ){
                Log.e("CS", "stopRingtone:"+msg +"MediaPlayer"+mHashMap.get(crn) );
                mHashMap.get(crn).stop();
                mHashMap.remove(crn);
                Log.e("SS", "stopRingtone: mHashMap.size() after remove **"+mHashMap.size() );


            }
        }

        Log.e("DS", "stopRingtone: after*** "+mMediaPlayer );
    }
    private void stopAllRingtone() {
        if (mHashMap != null && mHashMap.size() > 0) {
            try {
                Iterator entries = mHashMap.entrySet().iterator();
                if (entries != null) {
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        String key = (String) entry.getKey();

                        if (mHashMap.get(key) != null && mHashMap.get(key).isPlaying()) {
                            mHashMap.get(key).stop();
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mHashMap.clear();
            mHashMap = null;
        }

    }

    public void DECLINEORDER(String OrderId) {

        final String deliveryidobj;
        final String customerIdobj;
        String  deliveryid = OrderId;

        User user=   SharedPrefManager.getInstance(getContext()).getUser();
        String  customerId = String.valueOf(user.getId());
        deliveryidobj = String.valueOf(deliveryid);
        customerIdobj = customerId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressBar.setVisibility(View.GONE);

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                             /*   if( mHashMap != null &&  mHashMap.size() > 0 ){
                                    Log.e("TAG", "stopRingtone: mHashMap.size() "+mHashMap.size() );
                                    if(mHashMap.get(String.valueOf( OrderId)) != null && mHashMap.get(String.valueOf( OrderId)).isPlaying() ){
                                        mHashMap.get(String.valueOf(OrderId)).stop();
                                        mHashMap.remove(String.valueOf(OrderId));
                                        mMediaPlayer.stop();
                                    }
                                }*/

                                if( mHashMap != null &&  mHashMap.size() > 0 ){
                                    Log.e("TAG", "stopRingtone: mHashMap.size() "+mHashMap.size() );
                                    if(mHashMap.get(String.valueOf( "9999")) != null && mHashMap.get(String.valueOf( "9999")).isPlaying() ){
                                        mHashMap.get(String.valueOf( "9999")).stop();
                                        mHashMap.remove(String.valueOf( "9999"));
                                        mMediaPlayer.stop();
                                    }
                                }

                                if(getContext()!=null){
                                    ServiceNoDelay mSensorService = new ServiceNoDelay(getContext());
                                    Intent mServiceIntent = new Intent(getContext(), mSensorService.getClass());
                                    if (!isMyServiceRunning(mSensorService.getClass())) {
                                        getContext().startService(mServiceIntent);
                                    }
                                    Fragment fragment = new HomeFragment();
                                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                                    ;
                                    FragmentTransaction transaction = fm.beginTransaction();
                                    transaction.replace(R.id.frame, fragment);
                                    transaction.commit();

                                    Toast.makeText(getContext(), "DELIVERY DECLINE", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DeliveryId", deliveryidobj);
                params.put("CustomerId", customerIdobj);
                params.put("vehicleType", "7");//DECLINE
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    public void AcceptDelivery(String OrderId,DeliveryList product) {

        final String deliveryidobj;
        final String customerIdobj;
        String  deliveryid = OrderId;

        User user=   SharedPrefManager.getInstance(getContext()).getUser();
        String  customerId = String.valueOf(user.getId());
        deliveryidobj = String.valueOf(deliveryid);
        customerIdobj = customerId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressBar.setVisibility(View.GONE);

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                Intent intent=new Intent();
                                intent =  new Intent(getContext(), MapsActivity.class);
                                intent.putExtra("pickup_location", product.getPickupAddress());
                                intent.putExtra("DropLocation", product.getDeliveryAddress());
                                intent.putExtra("fromLat",  product.getFromLat());
                                intent.putExtra("fromLong",product.getFromLang());
                                intent.putExtra("DeliveryId",product.getDeliveryId());
                                intent.putExtra("ToLat",product.getToLat());
                                intent.putExtra("ToLong",product.getToLong());
                                intent.putExtra("Mobile",product.getMobile());
                                intent.putExtra("Distance",product.getDistance());
                                intent.putExtra("Duration", product.getTDuration());
                                intent.putExtra("Date",product.getPickupDate());
                                intent.putExtra("Time", product.getPickuptime());
                                intent.putExtra("CustomerFullName", product.getCustomerFullName());
                                intent.putExtra("CustomerId",product.getCustomerId());
                                intent.putExtra("TotalCharges", product.getTotalCharges());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getContext().startActivity(intent);
                                Toast.makeText(getContext(), "DELIVERY ACCEPTED", Toast.LENGTH_SHORT).show();

                            } else {
                                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DeliveryId", deliveryidobj);
                params.put("CustomerId", customerIdobj);
                params.put("vehicleType", "3");//ACCEPT
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
