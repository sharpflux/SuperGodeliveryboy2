package com.sharpflux.deliveryboy2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sharpflux.deliveryboy2.CustomerRegisterActivity;
import com.sharpflux.deliveryboy2.MapsActivity;
import com.sharpflux.deliveryboy2.SharedPrefManager;
import com.sharpflux.deliveryboy2.URLs;
import com.sharpflux.deliveryboy2.User;
import com.sharpflux.deliveryboy2.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeliveryPersonLoginActivity extends AppCompatActivity {

    EditText  editTextmobile, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MapsActivity.class));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextmobile = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerRegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String username =  editTextmobile.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextmobile.setError("Please enter your username");
            editTextmobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            //JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);
                            //if no error in response
                            if (1==1) {
                                Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < obj.length(); i++) {
                                    //getting the user from the response
                                    JSONObject userJson = obj.getJSONObject(i);

                                    User user = new User(
                                            userJson.getInt("CustomerId"),
                                            userJson.getString("CustomerFullName"),
                                            userJson.getString("Email"),
                                            userJson.getString("MobileNo")

                                    );

                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    //starting the profile activity
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));}
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
                params.put("Email", username);
                params.put("Password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
}
