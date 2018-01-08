package com.grepin.hookingdetector.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.grepin.hookingdetector.MainActivity;

/**
 * Created by P117842 on 2017-12-28.
 */

public class SecureReceiver extends BroadcastReceiver {

    private final String TAG = SecureReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "action : " + intent.getAction());

        // TODO : TEST

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, HookingDetectService.class);
            context.startService(i);
        }

    }
}
