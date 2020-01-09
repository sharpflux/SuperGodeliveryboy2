package com.sharpflux.deliveryboy2;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener {

    SwipeRefreshLayout pullToRefresh;
    int deliveryBoyId = 0;

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://api.supergo.in/api/DeliveryRequests/DeliveryRequestsall";

    //a list to store all the products
    List<DeliveryList> productList;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressBar spinner;
    ProgressDialog mProgressDialog;

    private DeliveryMainAdapter mAdapter;
    TextView navBarName, navMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    String subs = "";

                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            subs = "Sucess";
                        }

                        // Toast.makeText(MapsActivity.this, subs, Toast.LENGTH_SHORT).show();
                    }
                });*/
       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( NavActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e("Token",mToken);
                //  Toast.makeText(getApplicationContext(), "Token : " + mToken, Toast.LENGTH_LONG).show();
            }
        });*/

        // builder.append("<?xml version=\"1.0\" ?>");
        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(NavActivity.this);
        mProgressDialog.setIndeterminate(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Loading Data..");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are loading your data...");


        Intent startIntent = new Intent(getApplicationContext(), BackgroundService.class);
        startIntent.setAction("MyService");
        startService(startIntent);

       // switch1 = findViewById(R.id.switch1);
      //  switch1.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);



        setContentView(R.layout.activity_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ////////////////
        //getting the recyclerview from xml
        /*recyclerView = findViewById(R.id.recylcerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        //initializing the productlist
        productList = new ArrayList<DeliveryList>();


        View header = navigationView.getHeaderView(0);

        //customerName=findViewById(R.id.textViewCustomerName);
        navBarName = header.findViewById(R.id.navheaderName);
        navMobileNumber = header.findViewById(R.id.navheaderMobile);

        User user = SharedPrefManager.getInstance(this).getUser();

        // customerName.setText("Hey "+user.getUsername()+"!");
        navBarName.setText("Hey " + user.getUsername() + "!");
        navMobileNumber.setText("+91" + user.getMobile());

        deliveryBoyId = user.getId();

    /*    pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);




        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {

                Refreshcounter = Refreshcounter + 1;
                productList.clear();
                *//*DeliveryMainAdapter adapter = new DeliveryMainAdapter(getApplicationContext(), productList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "1";
                runner.execute(sleepTime);
                pullToRefresh.setRefreshing(false);
                GetOngoingDelivery();*//*
            }
        });
*/
        GetOngoingDelivery();

        //  mAdapter.setClickListener(this);

        //this method will fetch and parse json
        //to display it in recyclerview
        // loadProducts();


        //   spinner = (ProgressBar)findViewById(R.id.progressBar1);


/*        AsyncTaskRunner runner = new AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);*/

        ViewPager vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("MyNotificaiton", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("MyNotificaiton")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "SuccessFul";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }

                        //     Toast.makeText(NavActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }


   /* public void startService(View v) {
       String input= "Hey User";
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        stopService(serviceIntent);
    }*/



    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new HistoryFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                //
                //Your tab titles
                //
                case 0:return "Active Trips";
                case 1:return "Details";

                default:return null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
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
        alert.show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public  void  StopBackGround()
    {
        ServiceNoDelay mSensorService = new ServiceNoDelay(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
        if (isMyServiceRunning(mSensorService.getClass())) {
            getApplicationContext().stopService(mServiceIntent);
        }
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            /*Fragment fragment = null;
            fragment = new HomeFragment();
            displaySelectedFragment(fragment);*/

        } else if (id == R.id.nav_order) {

            StopBackGround();
             Intent oin = new Intent(NavActivity.this,MyOrderListActivity.class);
             startActivity(oin);
/*
            Fragment fragment = null;
            fragment = new OrderFragment();
            displaySelectedFragment(fragment);*/

        } else if (id == R.id.nav_offers) {
           /* Fragment fragment = null;
            fragment = new OffersFragment();
            displaySelectedFragment(fragment);*/

        } else if (id == R.id.nav_contactus) {

            //Open URL on click of Visit Us
            StopBackGround();
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NavigationDrawerConstants.SITE_URL));
            startActivity(urlIntent);

        } else if (id == R.id.nav_help) {

            //Open URL on click of Visit Us
            StopBackGround();
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NavigationDrawerConstants.SITE_URL));
            startActivity(urlIntent);

        } else if (id == R.id.nav_share) {

            //Display Share Via dialogue
            StopBackGround();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(NavigationDrawerConstants.SHARE_TEXT_TYPE);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, NavigationDrawerConstants.SHARE_TITLE);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, NavigationDrawerConstants.SHARE_MESSAGE);
            startActivity(Intent.createChooser(sharingIntent, NavigationDrawerConstants.SHARE_VIA));


        } else if (id == R.id.nav_rate) {
            appLink();

        } else if (id == R.id.nav_logout) {
            //when the user presses logout button
            //calling the logout method

            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void appLink() {

       /* String market_uri ="https://play.google.com/store/apps/details?id=com.supergo.customer";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(market_uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                .FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);*/

        // final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.supergo.customer")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.supergo.customer")));
        }
    }

    @Override
    public void onClick(View view, int position) {
        final DeliveryList city = productList.get(position);
        Intent i = new Intent(this, MapsActivity.class);
        /*i.putExtra("city", city.name);
        i.putExtra("desc", city.description);
        i.putExtra("image", city.imageName);
        Log.i("hello", city.name);*/
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }


    private void GetOngoingDelivery() {

        int deliveryBoyIds=deliveryBoyId;
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

                                Intent intent = new Intent(NavActivity.this, LocationMonitoringService.class);
                                intent.putExtra("DeliveryId", obj.getInt("DeliveryId"));
                                startService(intent);

                                Intent callin = new Intent(NavActivity.this, CallCustomerActivity.class);
                                callin.putExtra("Duration", obj.getString("Duration"));
                                callin.putExtra("Distance", obj.getString("Distance"));
                                callin.putExtra("pickup_location", obj.getString("pickupAddress"));
                                callin.putExtra("TotalCharges", "00.00");
                                callin.putExtra("Date", obj.getString("pickupDate"));
                                callin.putExtra("Time", obj.getString("pickuptime"));
                                callin.putExtra("Mobile", obj.getString("mobile"));
                                callin.putExtra("CustomerFullName", obj.getString("CustomerFullName"));
                                callin.putExtra("DeliveryId", obj.getInt("DeliveryId"));
                                callin.putExtra("fromLat", obj.getString("fromLat"));
                                callin.putExtra("fromLong", obj.getString("fromLang"));
                                callin.putExtra("ToLat", obj.getString("ToLat"));
                                callin.putExtra("ToLong", obj.getString("ToLong"));
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
        Volley.newRequestQueue(this).add(stringRequest);


    }

/*    private void loadProducts() {

        *//*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * *//*
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELIVERIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                          //  mProgressDialog.dismiss();
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
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

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).customerIn(list2);

                            }
                            //creating adapter object and setting it to recyclerview
                            DeliveryMainAdapter adapter = new DeliveryMainAdapter(getApplicationContext(), productList);
                            recyclerView.setAdapter(adapter);
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


    }*/

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


        String[] latlong2 = toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong[0] + "," + latlong[1] + "&destination=" + latlong2[0] + "," + latlong2[1] + "&travelmode=driving&sensor=false&key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi8";

        return url;
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;
                //loadProducts();
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
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();
            // finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
//            mProgressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

           // mProgressDialog.setProgress(92);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }
}
