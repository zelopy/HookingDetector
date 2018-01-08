package com.grepin.hookingdetector.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.grepin.hookingdetector.MainActivity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by P117842 on 2017-12-28.
 */
public class SecureTool {

    private final String TAG = SecureTool.class.getSimpleName();

    private List<ApplicationInfo> mAppList;


    /**
     * 디바이스에 설치된 앱 전체 목록 확인
     * @param context
     * @return
     */
    public List<ApplicationInfo> getAllAppList(Context context) {
        Log.w(TAG, "getAllAppList()");
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        return applicationInfoList;
    }


    /**
     * 디바이스 후킹 여부 확인.
     * 후킹 시 native method 존재를 힌트로 탐지함.
     * @param context
     * @return
     *  true : 후킹됨
     *  false : 후킹안됨
     */
    public boolean checkNativeMethod(Context context) {
        Log.w(TAG, "checkNativeMethod()");

        boolean isHooked = false;
        String msg = "";

        if(mAppList == null)
            mAppList = getAllAppList(context);

        for (ApplicationInfo applicationInfo : mAppList) {
            Set<String> classes = new HashSet<>();
            DexFile dex;
            try {
                dex = new DexFile(applicationInfo.sourceDir);
                Enumeration entries = dex.entries();
                while(entries.hasMoreElements()) {
                    String entry = entries.nextElement().toString();
                    classes.add(entry);
                }
                dex.close();
            }
            catch (IOException e) {
                msg = "후킹되었다!!!\n" + e.toString();
                isHooked = true;
                Log.e(TAG, msg);
            }
            for(String className : classes) {
                try {
                    Class clazz = MainActivity.class.forName(className);
                    for(Method method : clazz.getDeclaredMethods()) {
                        if(Modifier.isNative(method.getModifiers())) {
                            msg = "후킹되었다!!!\nNative 함수발견 :\n" + clazz.getCanonicalName() + " -> " + method.getName();
                            isHooked = true;
                            Log.wtf(TAG, msg);
                        }
                    }
                }
                catch(ClassNotFoundException e) {
//                    msg = "후킹되었다!!!\n" + e.toString();
//                    isHooked = true;
//                    Log.wtf(TAG, msg);
                }
            }
            break;
        }
        return isHooked;
    }
}
