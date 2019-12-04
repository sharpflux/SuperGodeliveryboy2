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

    private RecyclerView myOrdersRecyView;
    MyOrderModel order;
    MyOrderAdapter myOrdersAdapter;
    private ArrayList<MyOrderModel> activityListModelArrayList;

    int deliveryBoyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);

        myOrdersRecyView = findViewById(R.id.myOrdersRecyView);
        activityListModelArrayList = new ArrayList<>();

        LinearLayoutManager layoutManager3 = new GridLayoutManager(MyOrderListActivity.this,1);
        myOrdersRecyView.setLayoutManager(layoutManager3);
        //myOrdersRecyView.setItemAnimator(new DefaultItemAnimator());

        setTitle("My Order");
        MyOrderListActivity.AsyncTaskRunner runner = new MyOrderListActivity.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);
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
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject userJson = obj.getJSONObject(i);

                                order = new MyOrderModel
                                        (       userJson.getString("DeliveryId"),
                                                userJson.getString("DeliveryStatus"),
                                                userJson.getString("Distance"),
                                                userJson.getString("Duration"),
                                                userJson.getString("ApiTotalCharge"),
                                                userJson.getString("pickupAddress"),
                                                userJson.getString("deliveryAddress")
                                        );


                                activityListModelArrayList.add(order);


                                myOrdersAdapter = new MyOrderAdapter(getApplicationContext(), activityListModelArrayList);
                                myOrdersRecyView.setAdapter(myOrdersAdapter);


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
