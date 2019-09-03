package com.sharpflux.deliveryboy2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "supergo";
    private static final String KEY_USERNAME = "CustomerFullName";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_MOBILE = "MobileNo";

    private static final String KEY_MOBILE2 = "mobile";
    //private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "CustomerId";

    private static final String KEY_ID_delivery = "deliveryId";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_MOBILE, user.getMobile());
        //editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }


    public void customerIn(DeliveryList list) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString(KEY_MOBILE2, list.getMobile());
        //editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE, null)
        );
    }


    //this method will give the logged in user
    public DeliveryList getCustomer() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new DeliveryList(

                sharedPreferences.getString(KEY_MOBILE2, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent myIntent = new Intent(mCtx, com.sharpflux.deliveryboy2.LoginActivity.class);

        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(myIntent);

    }
}
