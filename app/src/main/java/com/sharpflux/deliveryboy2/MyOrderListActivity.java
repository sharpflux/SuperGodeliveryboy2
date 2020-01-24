package com.sharpflux.deliveryboy2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import java.util.Map;

public class MyOrderListActivity extends AppCompatActivity {

    RecyclerView myOrdersRecyView;
    MyOrderModel order;
    MyOrdersAdapter myOrdersAdapter;
    SwipeRefreshLayout pullToRefresh;
    boolean isLoading = false;
    private ArrayList<MyOrderModel> activityListModelArrayList;

    int deliveryBoyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);

        myOrdersRecyView = findViewById(R.id.myOrdersRecyView);
        activityListModelArrayList = new ArrayList<>();
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);


        LinearLayoutManager layoutManager3 = new GridLayoutManager(MyOrderListActivity.this,1);
        myOrdersRecyView.setLayoutManager(layoutManager3);
        //myOrdersRecyView.setItemAnimator(new DefaultItemAnimator());

        setTitle("My Rides");
        MyOrderListActivity.AsyncTaskRunner runner = new MyOrderListActivity.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);


        initAdapter();
      /*  myOrdersRecyView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager3) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                isLoading = true;
                initAdapter();
                int currentSize = myOrdersAdapter.getItemCount();


                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = String.valueOf(page+1);
                runner.execute(sleepTime);

            }
        });*/


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {

                Refreshcounter = Refreshcounter + 1;
                //  activityListModelArrayList.clear();
                MyOrdersAdapter adapter = new MyOrdersAdapter(getApplicationContext(), activityListModelArrayList);
                myOrdersRecyView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "1";
                runner.execute(sleepTime);
                pullToRefresh.setRefreshing(false);

            }
        });


    }


    private void initAdapter() {
        myOrdersAdapter = new MyOrdersAdapter(getApplicationContext(), activityListModelArrayList);
        myOrdersRecyView.setAdapter(myOrdersAdapter);
    }

    private void setDynamicFragmentToTabLayout() {
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        deliveryBoyId = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_ORDER+"DeliveryBoyId="+deliveryBoyId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            activityListModelArrayList.clear();
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject userJson = obj.getJSONObject(i);

                                order = new MyOrderModel
                                        (       userJson.getString("DeliveryId"),
                                                userJson.getString("DeliveryStatus"),
                                                userJson.getString("Distance"),
                                                userJson.getString("Duration"),
                                                userJson.getString("ApiTotalCharge"),
                                                userJson.getString("pickupAddress"),
                                                userJson.getString("deliveryAddress"),
                                                userJson.getString("CompanyCommisionWithGst"),
                                                userJson.getString("DeliveryBoyCommission")

                                        );


                                activityListModelArrayList.add(order);
                                /*myOrdersAdapter = new MyOrdersAdapter(getApplicationContext(), activityListModelArrayList);
                                myOrdersRecyView.setAdapter(myOrdersAdapter);*/
                            }
                            myOrdersAdapter.notifyDataSetChanged();
                            isLoading = false;

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                setDynamicFragmentToTabLayout();
                Thread.sleep(2000);
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

                progressDialog.dismiss();

            // finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {

                progressDialog = ProgressDialog.show(MyOrderListActivity.this,
                        "Loading...",
                        "Wait for result..");


        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }
    }
}
