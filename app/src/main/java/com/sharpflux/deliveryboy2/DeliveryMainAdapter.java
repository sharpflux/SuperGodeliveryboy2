package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeliveryMainAdapter extends RecyclerView.Adapter<DeliveryMainAdapter.ProductViewHolder> implements OnTaskCompleted {


    private Context mCtx;
    private List<DeliveryList> deliveryList;

    private ItemClickListener clickListener;

    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();
    DeliveryList product;
    NewRequestFragment frag = null;
    public DeliveryMainAdapter(Context mCtx, List<DeliveryList> deliveryList, MediaPlayer
            mMediaPlayer, HashMap<String, MediaPlayer> mHashMap, NewRequestFragment frag) {
        this.mCtx = mCtx;
        this.deliveryList = deliveryList;

        this.mMediaPlayer = mMediaPlayer;
        this.mHashMap = mHashMap;
        this.frag=frag;
        //Setting

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.new_request, null);

        return new ProductViewHolder(view);
    }

    private static String DistanceDuration = "Not Found";
    private static String DropLocation = "Not Found";

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        String fromLat, fromLong, ToLat, ToLong, Distance, DropLocation, Duration, Mobile, DateString, TimeString, CustomerFullName;

        holder.mMediaPlayer = mMediaPlayer;
        holder.mHashMap = mHashMap;


        product = deliveryList.get(position);
        holder.pickup_location.setText(product.getPickupAddress());
        holder.txtDistance.setText(product.getDistance());
        holder.txtDuration.setText(product.getTDuration());
        holder.cardview_rupees.setText(product.getTotalCharges());

        holder.fromLat = product.getFromLat();
        holder.fromLong = product.getFromLang();
        holder.ToLat = product.getToLat();
        holder.ToLong = product.getToLong();

        holder.Distance = product.getDistance();
        holder.Duration = product.getTDuration();
        holder.DateString = product.getPickupDate();
        holder.TimeString = product.getPickuptime();
        holder.CustomerFullName = product.getCustomerFullName();
        holder.deliveryId = product.getDeliveryId();
        holder.CustomerId = product.getCustomerId();
        holder.tvOrderId2.setText("#"+String.valueOf( product.getDeliveryId()));

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context=view.getContext();

                ServiceNoDelay mSensorService = new ServiceNoDelay(context);
                Intent mServiceIntent = new Intent(mCtx, mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    mCtx.stopService(mServiceIntent);
                }
               frag.AcceptDelivery(String.valueOf(product.getDeliveryId()),product);

            }
        });

       holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag.DECLINEORDER(String.valueOf(product.getDeliveryId()));
            }
        });


        //
        //  String url = getRequestUrl(product.getFromLat()+","+product.getFromLang(),product.getToLat()+","+product.getToLong());
        // new DistanceAndDuration(this).execute(url);
        // holder.txtDuration.setText(DistanceDuration);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mCtx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;
    }

    @Override
    public void onTaskCompleted(String... values) {
        DistanceDuration = values[0].toString();
        //txtView.setText(values[0].toString());
    }

    @Override
    public void onTaskCompletedHolder(String values, DeliveryMainAdapter.ProductViewHolder... holder) {

    }


    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

   /* class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView pickup_location,cardview_rupees,cardview_list_title;
        private TextView titleTextView;
        private ItemClickListener clickListener;

        public ProductViewHolder(View itemView) {
            super(itemView);


            pickup_location = itemView.findViewById(R.id.pickup_location);
            cardview_rupees=itemView.findViewById(R.id.cardview_rupees);
            cardview_list_title=itemView.findViewById(R.id.cardview_list_title);
        }
    }*/

    private int listItemLayout;
    private ArrayList<ProductViewHolder> itemList;

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        TextView pickup_location, txtDuration, txtDistance, cardview_rupees,tvOrderId2;
        String fromLat, fromLong, ToLat, ToLong, Distance, DropLocation, Duration, Mobile, DateString, TimeString, CustomerFullName;
        TextView accept, decline;

        MediaPlayer mMediaPlayer;
        private static int INTERVAL_DECLINE = 50000;
        private HashMap<String, MediaPlayer> mHashMap;


        int deliveryId, CustomerId;
        private ItemClickListener clickListener;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvOrderId2=itemView.findViewById(R.id.tvOrderId2);
            pickup_location = itemView.findViewById(R.id.pickup_location);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            cardview_rupees = itemView.findViewById(R.id.cardview_rupees);
            accept = itemView.findViewById(R.id.btnAccept2);
            decline = itemView.findViewById(R.id.btnDecline2);


            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            Context context = view.getContext();

            if (mHashMap != null && mHashMap.size() > 0) {
                Log.e("TAG", "stopRingtone: mHashMap.size() " + mHashMap.size());
                if (mHashMap.get("6666") != null && mHashMap.get("6666").isPlaying()) {
                    mHashMap.get("6666").stop();
                    mHashMap.remove("6666");
                }
            }



            /*Log.d("onclick", "onClick " + getLayoutPosition() + " " + pickup_location.getText());

            Intent intent=new Intent();

            intent =  new Intent(context, MapsActivity.class);
            intent.putExtra("pickup_location", pickup_location.getText());
            intent.putExtra("DropLocation", DropLocation);
            intent.putExtra("fromLat",  fromLat);
            intent.putExtra("fromLong",fromLong);

            intent.putExtra("DeliveryId",deliveryId);

            intent.putExtra("ToLat",ToLat);
            intent.putExtra("ToLong",ToLong);
            intent.putExtra("Mobile",Mobile);
            intent.putExtra("Distance",txtDistance.getText());
            intent.putExtra("Duration", txtDuration.getText());
            intent.putExtra("Date",DateString);
            intent.putExtra("Time", TimeString);
            intent.putExtra("CustomerFullName", CustomerFullName);
            intent.putExtra("CustomerId",String.valueOf(CustomerId));
            intent.putExtra("TotalCharges", cardview_rupees.getText());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/
           /* switch (getLayoutPosition()){
                case 0:

                    intent =  new Intent(context, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }*/
        }

    }

    private String getRequestUrl(String fromLatLong, String toLatLong) {
        //Value of origin
      /*  String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;*/


        // String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        //     String str_dest = "destination=" + dest.latitude+","+dest.longitude;

        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";

        String key = "key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi80";
        //Build the full param
        //  String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode+"&" +key;
        //Output format
        String output = "json";
        //Create url to request
        // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        String[] latlong = fromLatLong.split(",");

        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);
        String[] latlong2 = toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);


        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong[0] + "," + latlong[1] + "&destination=" + latlong2[0] + "," + latlong2[1] + "&travelmode=driving&sensor=false&key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi8";

        return url;
    }


    private void DECLINEORDER(String OrderId) {

        final String deliveryidobj;
        final String customerIdobj;
        String  deliveryid = OrderId;

        User user=   SharedPrefManager.getInstance(mCtx).getUser();
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


                                ServiceNoDelay mSensorService = new ServiceNoDelay(mCtx);
                                Intent mServiceIntent = new Intent(mCtx, mSensorService.getClass());
                                if (!isMyServiceRunning(mSensorService.getClass())) {
                                    mCtx.startService(mServiceIntent);
                                }
                                Fragment fragment = new HomeFragment();
                                FragmentManager fm = ((AppCompatActivity)mCtx).getSupportFragmentManager();;
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.frame, fragment);
                                transaction.commit();

                                Toast.makeText(mCtx, "DELIVERY DECLINE", Toast.LENGTH_SHORT).show();

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

        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
    }
    private void AcceptDelivery(String OrderId) {

        final String deliveryidobj;
        final String customerIdobj;
        String  deliveryid = OrderId;

        User user=   SharedPrefManager.getInstance(mCtx).getUser();
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
                                intent =  new Intent(mCtx, MapsActivity.class);
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
                                mCtx.startActivity(intent);
                                Toast.makeText(mCtx, "DELIVERY ACCEPTED", Toast.LENGTH_SHORT).show();

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

        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

}