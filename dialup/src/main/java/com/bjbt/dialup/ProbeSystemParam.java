package com.bjbt.dialup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;


class ProbeSystemParam {

    protected static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    protected static String getDeviceModel() {
        return Build.MODEL;
    }

    protected static String getDeviceBrand() {
        return Build.BRAND;
    }

    protected static String getOperatingSystem() {
        String s = Build.MANUFACTURER.toLowerCase();
        if (s.contains("huawei")) {
            return "EMUI";
        } else if (s.contains("meizu")) {
            return "FLYME";
        } else if (s.contains("xiaomi")) {
            return "MIUI";
        } else if (s.contains("oppo")) {
            return "Color OS";
        } else if (s.contains("vivo")) {
            return "Funtouch OS";
        } else if (s.contains("samsung")) {
            return "Bada";
        } else if (s.contains("smartisan")) {
            return "Smartisan OS";
        } else if (s.contains("lenovo")) {
            return "VIBE UI";
        } else {
            return "OTHER";
        }
    }

    protected static String getVersionCode(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String getOperators(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = null;
        String IMSI = tm.getSimOperator();
        if (IMSI == null || IMSI.equals("")) {
            return null;
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            operator = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            operator = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            operator = "中国电信";
        }
        return operator;
    }

    public static String getNetFlag(Context context) {

        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "2";
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "1";
            } else {
                return "网络未连接";
            }
        }
        return "网络未连接";
    }

    protected static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageManager.getApplicationLabel(packageInfo.applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "获取APP Name失败";
    }

    protected static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    protected static void systemParameter(Context context) {
        File systemFile = new File(ProbeInitializer.getContext().getFilesDir(), ProbeConstant.BUSINESS_CODE_SYSTEM);
        if (!systemFile.exists()) {
            ProbeUtils.cacheDate(getDeviceBrand() + "," + getDeviceModel() + "," + getSystemVersion() + "," + getOperatingSystem() + "," + getVersionCode(context) + "," + getOperators(context), ProbeConstant.BUSINESS_CODE_SYSTEM);
        }
    }
}
