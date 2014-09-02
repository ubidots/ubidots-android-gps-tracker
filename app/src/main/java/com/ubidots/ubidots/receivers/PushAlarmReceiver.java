package com.ubidots.ubidots.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubidots.ubidots.services.PushLocationService;

public class PushAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_PUSH_LOCATION_ALARM =
            "com.ubidots.ubidots.ACTION_PUSH_LOCATION_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent = new Intent(context, PushLocationService.class);
        context.startService(startIntent);
    }
}
