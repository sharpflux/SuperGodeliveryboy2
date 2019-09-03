package com.sharpflux.deliveryboy2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SmSOtpActivity extends AppCompatActivity implements TextWatcher {
    Bundle bundle;
    private EditText editText_one, editText_two, editText_three, editText_four;
    private String customerId;
    private int deliveryid;
    private Button btnValidate;
    AlertDialog.Builder builder;
    String Mobile="";
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_completed);


        editText_one = findViewById(R.id.pin_first_edittext);
        editText_two = findViewById(R.id.pin_second_edittext);
        editText_three = findViewById(R.id.pin_third_edittext);
        editText_four = findViewById(R.id.pin_forth_edittext);

        btnValidate = findViewById(R.id.btnValidate);

        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);
        SendOTP();
        builder = new AlertDialog.Builder(this);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_one.getText().toString())) {
                    editText_one.setError("Please enter ");
                    editText_one.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(editText_two.getText().toString())) {
                    editText_two.setError("Please enter");
                    editText_two.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(editText_three.getText().toString())) {
                    editText_three.setError("Please enter");
                    editText_three.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(editText_four.getText().toString())) {
                    editText_four.setError("Please enter");
                    editText_four.requestFocus();
                    return;
                }
                String EnteredOTP=editText_one.getText().toString()+editText_two.getText().toString()+editText_three.getText().toString()+editText_four.getText().toString();
                if(otp!=null) {
                    if (otp.equals(EnteredOTP)) {
                        validate();
                    }

                    else {
                        builder.setMessage("OTP is not valid")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Invalid OTP");
                        alert.show();
                    }
                }

                else {
                    builder.setMessage("OTP not yet generated")
                            .setCancelable(false)

                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Invalid OTP");
                    alert.show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {

            if (editText_one.length() == 1) {
                editText_two.requestFocus();
            }

            if (editText_two.length() == 1) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 1) {
                editText_four.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (editText_four.length() == 0) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 0) {
                editText_two.requestFocus();
            }
            if (editText_two.length() == 0) {
                editText_one.requestFocus();
            }
        }
    }//oc



    private void SendOTP() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            Mobile = bundle.getString("Mobile");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                otp=obj.getString("OTP");
                            }
                            else{
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", Mobile);
                params.put("OTPType", "DELIVERED");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void validate() {

        final String deliveryidobj;
        final String customerIdobj;
        bundle = getIntent().getExtras();

        if (bundle != null) {
            deliveryid = bundle.getInt("DeliveryId");
        }

        User user = SharedPrefManager.getInstance(this).getUser();

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

                                Intent intent = new Intent(SmSOtpActivity.this, LocationMonitoringService.class);
                                stopService(intent);


                                final Dialog dialog = new Dialog(SmSOtpActivity.this);
                                dialog.setContentView(R.layout.custom_dialog);
                                dialog.show();
                                ImageView declineButton = (ImageView) dialog.findViewById(R.id.declineButton);
                                declineButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SmSOtpActivity.this,NavActivity.class);
                                        startActivity(intent);
                                    }
                                });

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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DeliveryId", deliveryidobj);
                params.put("CustomerId", customerIdobj);
                params.put("vehicleType", "3");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

  /*  @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit without completing delivery?");
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
        alert.show();
    }
*/
}
