package com.sharpflux.deliveryboy2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.LinkedHashMap;
import java.util.Map;


public class HistoryFragment extends Fragment {

    RecyclerView mRecyclerView, recyclerview_Rate;
    GridLayoutManager mGridLayoutManager, Grid2;
    HistoryFragmentAdapter myCategoryTypeAdapter;
    RateShowAdapter myRateShowAdapter;
    ArrayList<HistoryModel> categoryList;
    HistoryModel myCategoryType;

    ArrayList<RateShowModalKeyValue> RateShowModalKeyValueList;
    RateShowModalKeyValue rateShowModalKeyValue;


    int deliveryBoyId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        mRecyclerView = view.findViewById(R.id.recyclerview_history);
        mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        recyclerview_Rate = view.findViewById(R.id.recyclerview_Rate);
        Grid2 = new GridLayoutManager(getContext(), 1);
        recyclerview_Rate.setLayoutManager(Grid2);


        categoryList = new ArrayList<>();
        RateShowModalKeyValueList = new ArrayList<>();

        HistoryFragment.AsyncTaskRunner runner = new HistoryFragment.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);

        HistoryFragment.AsyncTaskRateRunner Raterunner = new HistoryFragment.AsyncTaskRateRunner();
        Raterunner.execute("1");

        return view;
    }

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
                                        (
                                                userJson.getString("ApiDeliveryBoyEarning"),
                                                userJson.getString("Fromdates"),
                                                userJson.getString("Todates")
                                        );


                                categoryList.add(myCategoryType);


                                myCategoryTypeAdapter = new HistoryFragmentAdapter(getContext(), categoryList);
                                mRecyclerView.setAdapter(myCategoryTypeAdapter);


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

    private void RateFetch() {

        LinkedHashMap<String, String> newmap = new LinkedHashMap<String, String>();


        User user = SharedPrefManager.getInstance(getContext()).getUser();
        deliveryBoyId = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_RATESHOW + deliveryBoyId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject userJson = obj.getJSONObject(i);


                                newmap.put("Vehicle Type", userJson.getString("VehicleType"));
                                newmap.put("Day Base Fare", userJson.getString("DayBaseFare")+" Rs.");
                                newmap.put("DAY 0-5 Km", userJson.getString("Day0To5Price")+" Rs.");
                                newmap.put("Day 5-10 Km", userJson.getString("Day5To10Price")+" Rs.");
                                newmap.put("Day 10-15 Km", userJson.getString("Day10To15Price")+" Rs.");
                                newmap.put("Day Above 15 Km", userJson.getString("DayAbove15")+" Rs.");
                                newmap.put("Day Per Min Charges", userJson.getString("DayPerMinCharges")+" Rs.");

                                newmap.put("Night Base Fare", userJson.getString("NightBaseFare")+" Rs.");
                                newmap.put("Night 0-5 Km", userJson.getString("Night0To5Price")+" Rs.");
                                newmap.put("Night 10- 15 Km", userJson.getString("Night5To10Price")+" Rs.");
                                newmap.put("Night Above 15 Km", userJson.getString("NightAbove15")+" Rs.");
                                newmap.put("Night Per Min Charges", userJson.getString("NightPerMinCharges")+" Rs.");

                                newmap.put("Driver Commission", userJson.getString("DriverCommission")+" %");
                                newmap.put("Company Commission", userJson.getString("CompanyCommission")+" %");


                            }

                            for (LinkedHashMap.Entry<String, String> entry : newmap.entrySet()) {
                                System.out.println("Key = " + entry.getKey() +
                                        ", Value = " + entry.getValue());

                                rateShowModalKeyValue = new RateShowModalKeyValue
                                        (entry.getKey(),
                                                entry.getValue()
                                        );


                                RateShowModalKeyValueList.add(rateShowModalKeyValue);

                           }
                            myRateShowAdapter = new RateShowAdapter(getContext(), RateShowModalKeyValueList);
                            recyclerview_Rate.setAdapter(myRateShowAdapter);


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
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
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

    private class AsyncTaskRateRunner extends AsyncTask<String, String, String> {

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
                RateFetch();
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
