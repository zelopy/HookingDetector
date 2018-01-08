package com.grepin.hookingdetector.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.grepin.hookingdetector.R;
import com.grepin.hookingdetector.Utils.SecureTool;

/**
 * Created by P117842 on 2017-12-28.
 * 후킹 탐지를 서비스로 동작하면서 실시간 검사함.
 */
public class HookingDetectService extends Service {

    private final String TAG = HookingDetectService.class.getSimpleName();

    SecureTool mScureTool;


    @Override
    public void onCreate() {
        super.onCreate();

        mScureTool = new SecureTool();

        ServiceThread thread = new ServiceThread();
        thread.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand() " + intent.getAction());

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Service called onDestroy()");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class ServiceThread extends Thread {
        public boolean running = true;

        @Override
        public void run() {
            while(running) {
                try {
                    // 5초마다 후킹 탐지 시도.
                    Thread.sleep(5000);
//                    Log.w(TAG, "after 5 sec.");

                    if(mScureTool.checkNativeMethod(getApplicationContext())) {
//                        Log.w(TAG, "Hooking detected!!!");
                        showHookingDetectedMessage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                }
            }
        }
    }


    /**
     * 후킹 탐지 경고 메시지 출력.
     */
    private void showHookingDetectedMessage() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.msg_hooking_detected), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
