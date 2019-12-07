package com.sharpflux.deliveryboy2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceNoDelay.class));

    }
}
