package com.sharpflux.deliveryboy2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegisterActivity extends AppCompatActivity {

    EditText txtFullName, txtEmail, txtMobileNo,txtPassword;
    // RadioGroup radioGroupGender;
    ProgressBar progressBar;

    TextView loginagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtFullName=findViewById(R.id.txtFullName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtMobileNo = (EditText) findViewById(R.id.txtMobileNo);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        loginagain = findViewById(R.id.buttonLoginagain);


        loginagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(CustomerRegisterActivity.this,LoginActivity.class);
                startActivity(in);

            }
        });

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });


    }

    private void registerUser() {
        final String username = txtFullName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String mob =txtMobileNo.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        //final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations

        if (TextUtils.isEmpty(username)) {
            txtFullName.setError("Please enter username");
            txtFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Please enter your email");
            txtEmail.requestFocus();
            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Enter a valid email");
            txtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mob)) {
            txtPassword.setError("Please enter your mobile number");
            txtPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Enter a password");
            txtPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

                                User user = new User(
                                        obj.getInt("CustomerId"),
                                        obj.getString("CustomerFullName"),
                                        obj.getString("Email"),
                                        obj.getString("MobileNo")

                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();
                                getOtp();
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
                params.put("CustomerFullName", username);
                params.put("Email", email);
                params.put("MobileNo", mob);
                params.put("Password", password);
                //params.put("gender", gender);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getOtp() {
        //first getting the values
        final String mobNo =  txtMobileNo.getText().toString();

        if (!CommonUtils.isValidPhone(mobNo)) {
            txtMobileNo.setError("Invalid Mobile number");
            return;
        }
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Intent in = new Intent(CustomerRegisterActivity.this,
                                        OtpRegisterActivity.class);
                                in.putExtra("otp",obj.getString("OTP"));
                                startActivity(in);
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
                params.put("OTPMobileNo", mobNo);
                params.put("OTPType", "OTP");

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
