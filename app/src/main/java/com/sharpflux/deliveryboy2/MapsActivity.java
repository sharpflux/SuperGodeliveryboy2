package com.sharpflux.deliveryboy2;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnTaskCompleted {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    private TextView acceptRequest, btnCancelDelivery, buttonAcceptRequest;
    private Bundle bundle1;
    private ImageView arrowback;
    ArrayList<LatLng> listPoints;
    private String customerId;
    private int deliveryid;
    TextView txtEstimateTime, txtDistance, txtPickupLocation, txtDropLocation, cardview_rupees;
    User user;
    String fromLat = "", fromLong = "", ToLat = "", ToLong = "";
    LinearLayout callCustLlyt, arrivedLlyt, cancelLlyt, pickedLlyt, lr_call, lr_drop;
    ImageView navigationIv, navigationDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_delivery_test);

        //arrivedLlyt = findViewById(R.id.arrivedLlyt);
        cancelLlyt = findViewById(R.id.cancelLlyt);
        navigationIv = findViewById(R.id.navigationIv);
        navigationDrop = findViewById(R.id.navigationDrop);

        pickedLlyt = findViewById(R.id.pickedLlyt);
        lr_call = findViewById(R.id.lr_call);
        lr_drop = findViewById(R.id.lr_drop);


        txtEstimateTime = findViewById(R.id.txtEstimateTime);
        txtDistance = findViewById(R.id.txtDistance);
        txtPickupLocation = findViewById(R.id.txtPickupLocation);
        txtDropLocation = findViewById(R.id.txtDropLocation);
        cardview_rupees = findViewById(R.id.cardview_rupees);


        //buttonAcceptRequest = findViewById(R.id.buttonAcceptRequest);
        // btnCancelDelivery = findViewById(R.id.btnCancelDelivery);

        //arrowback = findViewById(R.id.arrow_back_img);
        // lr_nav = findViewById(R.id.lr_nav);

        user = SharedPrefManager.getInstance(this).getUser();

        /*arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backin = new Intent(MapsActivity.this, NavActivity.class);
                startActivity(backin);
            }
        });


*/

        bundle1 = getIntent().getExtras();
        if(bundle1!=null){
            txtEstimateTime.setText(bundle1.getString("Duration"));
            txtDistance.setText(bundle1.getString("Distance"));
            txtPickupLocation.setText(bundle1.getString("pickup_location"));
            txtDropLocation.setText(bundle1.getString("deliveryAddress"));
            cardview_rupees.setText(bundle1.getString("TotalCharges"));
            if(bundle1.getString("DeliveryStatus").equals("3")){
                lr_drop.setVisibility(View.GONE);
                pickedLlyt.setVisibility(View.VISIBLE);
            }
            if(bundle1.getString("DeliveryStatus").equals("8")){
                lr_drop.setVisibility(View.VISIBLE);
                pickedLlyt.setVisibility(View.GONE);
            }


        }


        lr_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DeliveryList customer = SharedPrefManager.getInstance(MapsActivity.this).getCustomer();

                bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String phone = bundle1.getString("Mobile");

                    //CustomerName.setText(bundle.getString("Mobile"));
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }

            }
        });

        navigationDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate();
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("google.navigation:q=" + ToLat + "," + ToLong + "&mode=d"));
                navigation.setPackage("com.google.android.apps.maps");
                startActivityForResult(navigation, 1234);


            }
        });


        navigationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate();
               /* final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + fromLat + "," + fromLong + "&daddr=" + ToLat + "," + ToLong));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
*/
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("google.navigation:q=" + fromLat + "," + fromLong + "&mode=d"));
                navigation.setPackage("com.google.android.apps.maps");
                startActivityForResult(navigation, 1234);

            }
        });
        lr_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle1 = getIntent().getExtras();
                if (bundle1 != null) {

                    Intent callin = new Intent(MapsActivity.this, SmSOtpActivity.class);
                    callin.putExtra("DeliveryId", bundle1.getInt("DeliveryId"));
                    callin.putExtra("Mobile", bundle1.getString("Mobile"));
                    startActivity(callin);

                }
            }
        });

        cancelLlyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setCancelable(false);
                builder.setTitle("CANCEL THE REQUEST ??");

                builder.setMessage("Are you sure you want to cancel this order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        CANCRELDELIVERYSTATUS("6");

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        /*lr_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + 18.5204 + "," + 73.8567 + "&daddr=" + 19.0760 + "," + 72.8777));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });*/
        pickedLlyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                builder1.setCancelable(true);
                builder1.setTitle("PARCEL PICKED !");

                builder1.setMessage("Are you sure to Picked Parcel ?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        UPDATEDELIVERYSTATUS("8");
                        dialog1.cancel();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog1.cancel();
                    }
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();


       // Notification("38");

    }

    public void UPDATEDELIVERYSTATUS(String DeliveryStatusId) {
        final String deliveryidobj;
        final String customerIdobj;
        bundle1 = getIntent().getExtras();

        if (bundle1 != null) {
            deliveryid = bundle1.getInt("DeliveryId");
        }
        customerId = String.valueOf(user.getId());
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
                               /* ServiceNoDelay mSensorService = new ServiceNoDelay(getApplicationContext());
                                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                                if (!isMyServiceRunning(mSensorService.getClass())) {
                                    getApplicationContext().stopService(mServiceIntent);
                                }*/
                                    /*Intent intent =new Intent(getApplicationContext(),NavActivity.class);
                                    startActivity(intent);*/


                                    if(DeliveryStatusId=="8")
                                    {
                                        lr_drop.setVisibility(View.VISIBLE);
                                        pickedLlyt.setVisibility(View.GONE);


                                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                        builder.setCancelable(false);
                                        builder.setTitle("PARCEL");

                                        builder.setMessage("You picked parcel sucessfully !!");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              dialog.cancel();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                pickedLlyt.setClickable(false);
                                // Toast.makeText(getApplicationContext(), "DELIVERY CANCELLED", Toast.LENGTH_SHORT).show();

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
                params.put("DeliveryId", String.valueOf(deliveryid));
                params.put("CustomerId", customerId);
                params.put("vehicleType", DeliveryStatusId);//DELIVERY STATUS ID
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void CANCRELDELIVERYSTATUS(String DeliveryStatusId) {
        final String deliveryidobj;
        final String customerIdobj;
        bundle1 = getIntent().getExtras();

        if (bundle1 != null) {
            deliveryid = bundle1.getInt("DeliveryId");
        }
        customerId = String.valueOf(user.getId());
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
                               /* ServiceNoDelay mSensorService = new ServiceNoDelay(getApplicationContext());
                                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                                if (!isMyServiceRunning(mSensorService.getClass())) {
                                    getApplicationContext().stopService(mServiceIntent);
                                }*/
                                Intent intent = new Intent(getApplicationContext(), NavActivity.class);
                                startActivity(intent);




                                // Toast.makeText(getApplicationContext(), "DELIVERY CANCELLED", Toast.LENGTH_SHORT).show();

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
                params.put("DeliveryId", String.valueOf(deliveryid));
                params.put("CustomerId", customerId);
                params.put("vehicleType", DeliveryStatusId);//DELIVERY STATUS ID
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);


            return;
        }
        mMap.setMyLocationEnabled(true);


        //   Intent intent = getIntent();


        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {

            fromLat = bundle1.getString("fromLat");
            fromLong = bundle1.getString("fromLong");
            ToLat = bundle1.getString("ToLat");
            ToLong = bundle1.getString("ToLong");

            txtEstimateTime.setText(bundle1.getString("Duration"));
            txtDistance.setText(bundle1.getString("Distance"));
            txtPickupLocation.setText(bundle1.getString("pickup_location"));
            txtDropLocation.setText(bundle1.getString("deliveryAddress"));
            cardview_rupees.setText(bundle1.getString("TotalCharges"));

        }


        String url = getRequestUrl(fromLat + "," + fromLong, ToLat + "," + ToLong);
        new DistanceAndDuration(this).execute(url);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);

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

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);

        String[] latlong2 = toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);
        //TextView txtEstimateKm=(TextView) findViewById(R.id.txtEstimateKm);

        // Double distanceKm=distance (Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]), Double.parseDouble(latlong2[0]), Double.parseDouble(latlong2[1]), "K");
        // DecimalFormat precision = new DecimalFormat("0.00");
        //  txtEstimateKm.setText(precision.format(distanceKm)+"Km" );

        markerOptions.position(location2);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


        mMap.addMarker(markerOptions);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong[0] + "," + latlong[1] + "&destination=" + latlong2[0] + "," + latlong2[1] + "&travelmode=driving&sensor=false&key=AIzaSyD3lPCpXWKTSMLC4wCL4rXmatN3f9M4lt4";

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));


        return url;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public TextView GetDistanceText() {
        TextView txtView = (TextView) findViewById(R.id.txtEstimateTime);
        return txtView;
    }

    @Override
    public void onTaskCompleted(String... values) {
        //TextView txtView = (TextView)findViewById(R.id.txtEstimateKm);
        // txtView.setText(values[0].toString());
    }

    @Override
    public void onTaskCompletedHolder(String values, DeliveryMainAdapter.ProductViewHolder... holder) {

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }


    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;


            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }


                polylineOptions.addAll(points);
                polylineOptions.width(8);
                polylineOptions.color(Color.BLACK);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                dialog.cancel();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /////////////
   /* private void validate() {

        final String deliveryidobj;
        final String customerIdobj;
        bundle = getIntent().getExtras();

        if (bundle != null) {
            deliveryid = bundle.getInt("DeliveryId");
        }

        customerId = String.valueOf(user.getId());
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

                                Bundle bundle = getIntent().getExtras();
                                if (bundle != null) {


                                    Intent intent = new Intent(MapsActivity.this, LocationMonitoringService.class);
                                    intent.putExtra("DeliveryId", bundle.getInt("DeliveryId"));
                                    startService(intent);

                                    //Notification(bundle.getString("CustomerId"));

                                    Intent callin = new Intent(MapsActivity.this, CallCustomerActivity.class);
                                    callin.putExtra("Duration", bundle.getString("Duration"));
                                    callin.putExtra("Distance", bundle.getString("Distance"));
                                    callin.putExtra("pickup_location", bundle.getString("pickup_location"));
                                    callin.putExtra("TotalCharges", bundle.getString("TotalCharges"));
                                    callin.putExtra("Date", bundle.getString("Date"));
                                    callin.putExtra("Time", bundle.getString("Time"));
                                    callin.putExtra("Mobile", bundle.getString("Mobile"));
                                    callin.putExtra("CustomerFullName", bundle.getString("CustomerFullName"));
                                    callin.putExtra("DeliveryId", bundle.getInt("DeliveryId"));
                                    callin.putExtra("fromLat", bundle.getString("fromLat"));
                                    callin.putExtra("fromLong", bundle.getString("fromLong"));
                                    callin.putExtra("ToLat", bundle.getString("ToLat"));
                                    callin.putExtra("ToLong", bundle.getString("ToLong"));
                                    startActivity(callin);


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
                params.put("vehicleType", "3");//statusid
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }*/

    public void Notification(String CustomerId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SENNOTIFICATIONTOCUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerFullName", user.getUsername());
                params.put("CustomerId", CustomerId);
                return params;

            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
