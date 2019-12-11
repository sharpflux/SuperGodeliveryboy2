package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewRequestFragment extends Fragment {
    List<DeliveryList> productList;
    RecyclerView recyclerView;
    Bundle bundle;
    JSONArray array;
    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();

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

        if (extras != null) {
            recyclerView = view.findViewById(R.id.rec_newRequest);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.ring_tone_final);
            mHashMap.put(String.valueOf("9999"), mMediaPlayer);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();

            try {
                array = new JSONArray(extras.getString("List").toString());
                for (int i = 0; i < array.length(); i++) {

                    //getting product object from json array
                    JSONObject product = array.getJSONObject(i);


                    Vibrator vib = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(200);


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
                    DeliveryMainAdapter adapter = new DeliveryMainAdapter(getContext(), productList,mMediaPlayer,mHashMap);
                    recyclerView.setAdapter(adapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }


}
