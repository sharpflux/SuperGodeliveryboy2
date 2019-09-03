package com.sharpflux.deliveryboy2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OtpActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    Bundle bundle;
    private EditText enterOTPCodeEt;
    private TextView verifyButton;
    String otp = "", enteredOtp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyButton = findViewById(R.id.verifyButton);

        enterOTPCodeEt = findViewById(R.id.enterOTPCodeEt);

        bundle = getIntent().getExtras();
        builder = new AlertDialog.Builder(this);
        if (bundle != null) {
           otp=bundle.getString("otp");

        }
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enteredOtp = enterOTPCodeEt.getText().toString();
                    if (otp.equals(enteredOtp)) {
                        Intent in = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                        startActivity(in);

                } else

                {
                    builder.setMessage("Enter valid otp")
                            .setCancelable(false)

                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Enter valid otp");
                    alert.show();
                }
            }
            });
        }
    }

