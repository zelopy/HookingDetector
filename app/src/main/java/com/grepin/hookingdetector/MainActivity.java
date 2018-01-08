package com.grepin.hookingdetector;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.grepin.hookingdetector.Utils.SecureTool;
import com.grepin.hookingdetector.services.HookingDetectService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    SecureTool mSecureTool;

    /** layout */
    // 앱 목록 확인
    Button btn_app_list;
    // 후킹 여부 수동 확인
    Button btn_find_hook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSecureTool = new SecureTool();

        layout_init();

        Intent intent = new Intent(getApplicationContext(), HookingDetectService.class);
        startService(intent);
    }


    private void layout_init() {
        btn_app_list = findViewById(R.id.btn_app_list);
        btn_find_hook = findViewById(R.id.btn_find_hook);

        btn_app_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ApplicationInfo> appList = mSecureTool.getAllAppList(getApplicationContext());

                // TODO : TEST
                for(ApplicationInfo appInfo : appList) {
                    Log.w(TAG, ">>> " + appInfo.processName);
                }
            }
        });
        btn_find_hook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hookCheck = mSecureTool.checkNativeMethod(getApplicationContext());

                // TODO : TEST
                Log.w(TAG, "Hooked? " + hookCheck);
            }
        });
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
