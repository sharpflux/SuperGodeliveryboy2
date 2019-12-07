package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DeliveryMainAdapter extends RecyclerView.Adapter<DeliveryMainAdapter.ProductViewHolder> implements OnTaskCompleted {


    private Context mCtx;
    private List<DeliveryList> deliveryList;

    private ItemClickListener clickListener;

    private static int INTERVAL = 45000;
    MediaPlayer mMediaPlayer;
    private static int INTERVAL_DECLINE = 50000;
    private HashMap<String, MediaPlayer> mHashMap = new HashMap();

    public DeliveryMainAdapter(Context mCtx, List<DeliveryList> deliveryList,MediaPlayer mMediaPlayer,HashMap<String, MediaPlayer> mHashMap ) {
        this.mCtx = mCtx;
        this.deliveryList = deliveryList;

        this.mMediaPlayer=mMediaPlayer;
        this.mHashMap=mHashMap;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.requests_activity, null);

        return new ProductViewHolder(view);
    }

    private static  String  DistanceDuration="Not Found";
    private static  String  DropLocation="Not Found";

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        String fromLat,fromLong,ToLat,ToLong,Distance,DropLocation,Duration,Mobile,DateString,TimeString,CustomerFullName;

        holder.mMediaPlayer=mMediaPlayer;
        holder.mHashMap=mHashMap;


        DeliveryList product = deliveryList.get(position);
        holder.pickup_location.setText(product.getPickupAddress());
       // holder.cardview_rupees.setText(product.getFromLat());
        holder.txtDistance.setText(product.getDistance());
        holder.txtDuration.setText(product.getTDuration());
        holder.fromLat=product.getFromLat();
        holder.fromLong=product.getFromLang();
        holder.ToLat=product.getToLat();
        holder.ToLong=product.getToLong();
        holder.DropLocation=product.getDeliveryAddress();
        holder.cardview_rupees.setText(product.getTotalCharges());
        holder.Mobile=product.getMobile();
        holder.Distance=product.getDistance();
        holder.Duration=product.getTDuration();
        holder.DateString=product.getPickupDate();
        holder.TimeString=product.getPickuptime();
        holder.CustomerFullName=product.getCustomerFullName();
        holder.deliveryId=product.getDeliveryId();
        holder.CustomerId=product.getCustomerId();
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context=view.getContext();
                holder.mMediaPlayer.stop();

              /*  if( mHashMap != null &&  mHashMap.size() > 0 ){
                    Log.e("TAG", "stopRingtone: mHashMap.size() "+mHashMap.size() );
                    if(mHashMap.get("6666") != null && mHashMap.get("6666").isPlaying() ){
                        mHashMap.get("6666").stop();
                        mHashMap.remove("6666");
                        mMediaPlayer.stop();
                    }
                }
*/

                ServiceNoDelay mSensorService = new ServiceNoDelay(context);
                Intent mServiceIntent = new Intent(context, mSensorService.getClass());
                if (isMyServiceRunning(mSensorService.getClass())) {
                    context.stopService(mServiceIntent);
                }



                Intent intent=new Intent();

                intent =  new Intent(context, MapsActivity.class);
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
                context.startActivity(intent);



            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        //
      //  String url = getRequestUrl(product.getFromLat()+","+product.getFromLang(),product.getToLat()+","+product.getToLong());
       // new DistanceAndDuration(this).execute(url);
       // holder.txtDuration.setText(DistanceDuration);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)mCtx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;
    }
    @Override
    public void onTaskCompleted(String... values) {
        DistanceDuration=values[0].toString();
        //txtView.setText(values[0].toString());
    }
    @Override
    public void  onTaskCompletedHolder(String values, DeliveryMainAdapter.ProductViewHolder...holder){

    }
    private String getRequestUrl(String fromLatLong,String toLatLong) {
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

        String key="key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi80";
        //Build the full param
        //  String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode+"&" +key;
        //Output format
        String output = "json";
        //Create url to request
        // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        String[] latlong =  fromLatLong.split(",");

        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);
         String[] latlong2 =  toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);


        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latlong[0]+","+latlong[1]+"&destination="+latlong2[0]+","+latlong2[1]+"&travelmode=driving&sensor=false&key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi8";

        return url;
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
        TextView pickup_location,txtDuration,txtDistance,cardview_rupees;
        String fromLat,fromLong,ToLat,ToLong,Distance,DropLocation,Duration,Mobile,DateString,TimeString,CustomerFullName;
        Button accept,decline;

        MediaPlayer mMediaPlayer;
        private static int INTERVAL_DECLINE = 50000;
        private HashMap<String, MediaPlayer> mHashMap;


        int deliveryId,CustomerId;
        private ItemClickListener clickListener;

        public ProductViewHolder(View itemView) {
            super(itemView);

            pickup_location = itemView.findViewById(R.id.pickup_location);
            txtDuration=itemView.findViewById(R.id.txtDuration);
            txtDistance=itemView.findViewById(R.id.txtDistance);
            cardview_rupees=itemView.findViewById(R.id.cardview_rupees);
            accept=itemView.findViewById(R.id.btn_accept);
            decline=itemView.findViewById(R.id.btn_decline);


            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View view) {

            Context context=view.getContext();

            if(mHashMap != null && mHashMap.size() > 0 ){
                Log.e("TAG", "stopRingtone: mHashMap.size() "+mHashMap.size() );
                if(mHashMap.get("6666") != null && mHashMap.get("6666").isPlaying() ){
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



}